package com.northcoders.pigliotech_frontend.utils;

import static com.northcoders.pigliotech_frontend.utils.BackendAvailabilityInterceptor.PING_RETRY_DELAY_MS;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.northcoders.pigliotech_frontend.PiglioTechApp;
import com.northcoders.pigliotech_frontend.ui.activities.BackendColdStartActivity;
import com.northcoders.pigliotech_frontend.network.RetrofitInstance;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackendStatusManager {
    private static final String TAG = "BackendStatusManager";
    private static final String LOG_PREFIX = "[BackendStatus] ";
    private static final String LOG_FORMAT = "%s%s - %s";
    private static BackendStatusManager instance;
    private static final long BACKEND_TIMEOUT = TimeUnit.MINUTES.toMillis(30); // 30 minutes timeout
    private static final long STATUS_CACHE_DURATION = TimeUnit.MINUTES.toMillis(5); // 5 minutes cache
    private static final long AUTO_CHECK_INTERVAL = 5000; // Check every 5 seconds when cold start screen is shown
    private static final long NETWORK_CHECK_DELAY = 5000; // 5 seconds between network checks
    private static final long QUICK_CHECK_TIMEOUT = 10000; // 10 seconds timeout for quick check
    private static final long MIN_STATUS_CHANGE_INTERVAL = 2000; // Minimum 2 seconds between status changes
    private static final long DEBOUNCE_INTERVAL = 1000; // 1 second debounce for status checks
    public static final String ACTION_BACKEND_ONLINE = "com.northcoders.pigliotech_frontend.BACKEND_ONLINE";
    private static final int MAX_RETRIES = 10;
    private static final long RETRY_DELAY_MS = 60000; // 60 seconds between retries
    private long nextRetryTime = 0;
    private boolean isHandlingError = false;
    private boolean hasReachedMaxRetries = false;
    private final Object errorLock = new Object();
    private boolean isInterceptorCheck = false;

    public enum BackendStatus {
        ONLINE,
        OFFLINE,
        STARTING,
        UNKNOWN,
        NO_INTERNET
    }

    private volatile BackendStatus currentStatus = BackendStatus.UNKNOWN;
    private final AtomicBoolean isChecking = new AtomicBoolean(false);
    private final Object pingLock = new Object();
    private final Object lock = new Object();
    private final Queue<Runnable> pendingRequests = new ConcurrentLinkedQueue<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private long lastSuccessfulCheck = 0;
    private long startTime = 0;
    private String pendingAction = null;
    private Call<Void> currentPingCall = null;
    private int retryCount = 0;
    private boolean isColdStartScreenShown = false;
    private long lastPingTime = 0;
    private Runnable autoCheckRunnable;
    private boolean isInitialCheckDone = false;
    private boolean isInternetCheckDone = false;
    private boolean isNetworkCheckInProgress = false;
    private long lastNetworkCheckTime = 0;
    private long quickCheckStartTime = 0;
    private boolean isQuickCheckInProgress = false;
    private long coldStartStartTime = 0;
    private long lastStatusChangeTime = 0;
    private long lastDebounceTime = 0;

    private BackendStatusManager() {
        autoCheckRunnable = () -> {
            if (isColdStartScreenShown) {
                long currentTime = System.currentTimeMillis();
                if (!isChecking.get() && (currentTime >= nextRetryTime || retryCount == 0)) {
                    Log.d(TAG, "Auto-check triggered");
                    startBackendCheck(false);
                }
                handler.postDelayed(autoCheckRunnable, AUTO_CHECK_INTERVAL);
            }
        };
    }

    public static synchronized BackendStatusManager getInstance() {
        if (instance == null) {
            instance = new BackendStatusManager();
        }
        return instance;
    }

    public static synchronized BackendStatusManager getInstance(Context context) {
        return getInstance();
    }

    public void executeRequest(Runnable request, String actionDescription) {
        if (currentStatus == BackendStatus.ONLINE) {
            request.run();
            return;
        }

        pendingAction = actionDescription;
        pendingRequests.offer(request);
        startBackendCheck();
    }

    public boolean isBackendAvailable() {
        synchronized (lock) {
            if (currentStatus == BackendStatus.ONLINE) {
                long cacheAge = System.currentTimeMillis() - lastSuccessfulCheck;
                boolean isValid = cacheAge < STATUS_CACHE_DURATION;
                Log.d(TAG, "Backend available check: " + isValid + " (Status: " + currentStatus + ", Cache age: "
                        + cacheAge + "ms)");
                return isValid;
            }
            Log.d(TAG, "Backend available check: false (Status: " + currentStatus + ")");
            return false;
        }
    }

    public void startBackendCheck() {
        startBackendCheck(false);
    }

    public void startBackendCheck(boolean fromInterceptor) {
        // Implement debouncing
        long now = System.currentTimeMillis();
        if (now - lastDebounceTime < DEBOUNCE_INTERVAL) {
            logDebug("Debouncing backend check request");
            return;
        }
        lastDebounceTime = now;

        // If we're already checking, don't start another check
        if (!isChecking.compareAndSet(false, true)) {
            logDebug("Backend check already in progress, request will be queued");
            return;
        }

        // Set a timeout to reset the isChecking flag if the check takes too long
        handler.postDelayed(() -> {
            if (isChecking.get()) {
                Log.w(TAG, "Backend check timed out, resetting isChecking flag");
                isChecking.set(false);
                handlePingComplete();
            }
        }, QUICK_CHECK_TIMEOUT * 2);

        synchronized (pingLock) {
            logInfo("Starting backend status check" + (fromInterceptor ? " (from interceptor)" : ""));

            // Check if we've already reached max retries
            if (hasReachedMaxRetries) {
                logWarning("Max retries already reached, not starting new check");
                isChecking.set(false);
                return;
            }

            // Check if we need to wait longer before the next retry
            long currentTime = System.currentTimeMillis();
            if (retryCount > 0 && currentTime < nextRetryTime && !fromInterceptor) {
                long remainingDelay = nextRetryTime - currentTime;
                logInfo(String.format("Waiting %d ms before next retry (attempt %d/%d) - Next retry at: %s",
                        remainingDelay, retryCount, MAX_RETRIES,
                        new java.text.SimpleDateFormat("HH:mm:ss.SSS").format(new java.util.Date(nextRetryTime))));
                handler.postDelayed(() -> startBackendCheck(false), remainingDelay);
                isChecking.set(false);
                return;
            }

            // Only reset startTime if this is a new check (not a retry)
            if (retryCount == 0) {
                startTime = System.currentTimeMillis();
                nextRetryTime = startTime;
                hasReachedMaxRetries = false;
            }

            // Check if we've already reached max retries
            if (retryCount >= MAX_RETRIES) {
                logWarning("Max retries already reached, not starting new check");
                isChecking.set(false);
                hasReachedMaxRetries = true;
                return;
            }

            currentStatus = BackendStatus.STARTING;
            isInternetCheckDone = false;
            isInterceptorCheck = fromInterceptor;

            // First, do a quick check to see if the backend is already online
            // This prevents showing the cold start screen unnecessarily
            if (!isInitialCheckDone) {
                isInitialCheckDone = true;
                quickBackendCheck();
            } else {
                showColdStartScreen();
                // Instead of calling checkBackendStatus() which would create a loop,
                performBackendPing(fromInterceptor);
            }
        }
    }

    private void quickBackendCheck() {
        if (isQuickCheckInProgress) {
            logDebug("Quick check already in progress, skipping");
            return;
        }

        isQuickCheckInProgress = true;
        quickCheckStartTime = System.currentTimeMillis();

        // Set a timeout for the quick check
        handler.postDelayed(() -> {
            if (isQuickCheckInProgress) {
                Log.w(TAG, "Quick check timed out, showing cold start screen");
                isQuickCheckInProgress = false;
                showColdStartScreen();
                checkBackendStatus();
            }
        }, QUICK_CHECK_TIMEOUT);

        try {
            logInfo("Performing quick backend check");
            Call<Void> quickPingCall = RetrofitInstance.getPingService().pingBackend();
            quickPingCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    long responseTimeMs = System.currentTimeMillis() - quickCheckStartTime;
                    isQuickCheckInProgress = false;
                    if (response.isSuccessful()) {
                        logInfo(String.format("Quick ping successful with code: %d - Response time: %d ms",
                                response.code(), responseTimeMs));
                        handleSuccess();
                    } else {
                        logWarning(String.format("Quick ping failed with code: %d - Response time: %d ms",
                                response.code(), responseTimeMs));
                        showColdStartScreen();
                        checkBackendStatus();
                    }
                    handlePingComplete();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    long responseTimeMs = System.currentTimeMillis() - quickCheckStartTime;
                    isQuickCheckInProgress = false;
                    logWarning(String.format("Quick ping failed: %s - Response time: %d ms",
                            t.getMessage(), responseTimeMs));
                    showColdStartScreen();
                    checkBackendStatus();
                    handlePingComplete();
                }
            });
        } catch (Exception e) {
            isQuickCheckInProgress = false;
            logWarning("Error executing quick ping: " + e.getMessage());
            showColdStartScreen();
            checkBackendStatus();
            handlePingComplete();
        }
    }

    private void performBackendPing(boolean isInterceptorCheck) {
        long elapsedTimeMs = System.currentTimeMillis() - startTime;
        long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMs);
        long elapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMs) % 60;

        if (!isInterceptorCheck) {
            logInfo(String.format("Pinging backend (attempt %d/%d) - User wait time: %d min %d sec",
                    retryCount + 1, MAX_RETRIES, elapsedMinutes, elapsedSeconds));
        }

        try {
            if (currentPingCall != null && !currentPingCall.isCanceled()) {
                currentPingCall.cancel();
            }

            lastPingTime = System.currentTimeMillis();
            currentPingCall = RetrofitInstance.getPingService().pingBackend();
            currentPingCall.enqueue(createPingCallback(elapsedMinutes, elapsedSeconds, isInterceptorCheck));
        } catch (Exception e) {
            logWarning("Error executing ping: " + e.getMessage());
            handleError();
            handlePingComplete();
        }
    }

    private Callback<Void> createPingCallback(long elapsedMinutes, long elapsedSeconds, boolean isInterceptorCheck) {
        return new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                long responseTimeMs = System.currentTimeMillis() - lastPingTime;
                if (response.isSuccessful()) {
                    if (!isInterceptorCheck) {
                        logInfo(String.format(
                                "Ping successful with code: %d - Response time: %d ms - User wait time: %d min %d sec",
                                response.code(), responseTimeMs, elapsedMinutes, elapsedSeconds));
                    }
                    handleSuccess();
                } else {
                    if (!isInterceptorCheck) {
                        logWarning(String.format(
                                "Ping failed with code: %d - Response time: %d ms - User wait time: %d min %d sec",
                                response.code(), responseTimeMs, elapsedMinutes, elapsedSeconds));
                    }
                    handleError();
                }
                handlePingComplete();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                long responseTimeMs = System.currentTimeMillis() - lastPingTime;
                if (t instanceof IOException && t.getMessage() != null && t.getMessage().contains("Canceled")) {
                    logDebug("Ping canceled - Response time: " + responseTimeMs + " ms");
                } else {
                    logWarning(
                            String.format("Ping failed: %s - Response time: %d ms - User wait time: %d min %d sec",
                                    t.getMessage(), responseTimeMs, elapsedMinutes, elapsedSeconds));
                }
                handleError();
                handlePingComplete();
            }
        };
    }

    private void handlePingComplete() {
        synchronized (pingLock) {
            isChecking.set(false);
            pingLock.notifyAll();
        }
    }

    public void checkBackendStatus() {
        synchronized (lock) {
            if (isChecking.get() || isNetworkCheckInProgress) {
                logDebug(isChecking.get() ? "Backend check already in progress, skipping" 
                        : "Network check in progress, skipping backend check");
                return;
            }

            long currentTime = System.currentTimeMillis();
            if (retryCount > 0 && currentTime < nextRetryTime) {
                long remainingDelay = nextRetryTime - currentTime;
                logDebug(String.format("Waiting %d ms before next retry (attempt %d/%d)",
                        remainingDelay, retryCount, MAX_RETRIES));
                return;
            }

            isChecking.set(true);
            performBackendPing(false);
        }
    }

    private void handleSuccess() {
        synchronized (lock) {
            long now = System.currentTimeMillis();

            // Prevent rapid status changes
            if (now - lastStatusChangeTime < MIN_STATUS_CHANGE_INTERVAL) {
                logDebug("Ignoring rapid status change to ONLINE");
                return;
            }

            lastStatusChangeTime = now;
            lastSuccessfulCheck = now;

            // Update status to ONLINE if it's different
            if (currentStatus != BackendStatus.ONLINE) {
                currentStatus = BackendStatus.ONLINE;
                logInfo("Backend is now ONLINE");
            }

            // Only reset retry count if this was a new check (not a retry)
            if (retryCount == 0) {
                hasReachedMaxRetries = false;
            }

            hideColdStartScreen();
            processPendingRequests();
        }
    }

    private void processPendingRequests() {
        Runnable request;
        while ((request = pendingRequests.poll()) != null) {
            try {
                request.run();
            } catch (Exception e) {
                Log.e(TAG, "Error processing pending request", e);
            }
        }
        pendingAction = null;
    }

    public void handleError() {
        synchronized (errorLock) {
            // Prevent multiple simultaneous error handling
            if (isHandlingError) {
                logDebug("Already handling an error, skipping duplicate error handling");
                return;
            }

            isHandlingError = true;

            try {
                // Check if we've already reached max retries
                if (hasReachedMaxRetries) {
                    logWarning("Max retries already reached, skipping error handling");
                    return;
                }

                // Only increment retry count if this wasn't from the interceptor
                if (!isInterceptorCheck) {
                    retryCount++;
                }

                // Check internet connectivity on the first failure
                if (!isInternetCheckDone && retryCount == 1) {
                    isInternetCheckDone = true;
                    checkInternetConnectivity();
                    return;
                }

                if (retryCount >= MAX_RETRIES) {
                    logWarning("Max retries reached, giving up");
                    currentStatus = BackendStatus.OFFLINE;
                    isChecking.set(false);
                    hasReachedMaxRetries = true;
                    showColdStartScreen();
                    return;
                } else {
                    long elapsedTimeMs = System.currentTimeMillis() - startTime;
                    long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMs);
                    long elapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMs) % 60;

                    // Only log if this is not from the interceptor
                    if (!isInterceptorCheck) {
                        logWarning(String.format("Ping failed (attempt %d/%d) - Elapsed time: %d min %d sec",
                                retryCount, MAX_RETRIES, elapsedMinutes, elapsedSeconds));
                    }

                    // Set the next retry time to be exactly RETRY_DELAY_MS from now
                    nextRetryTime = System.currentTimeMillis() + RETRY_DELAY_MS;

                    // Only log if this is not from the interceptor
                    if (!isInterceptorCheck) {
                        // Make sure we're not already checking before scheduling a retry
                        if (!isChecking.get()) {
                            handler.postDelayed(() -> {
                                logInfo("Executing scheduled retry");
                                startBackendCheck(false);
                            }, RETRY_DELAY_MS);
                        }
                    }
                }
            } finally {
                isHandlingError = false;
            }
        }
    }

    private void checkInternetConnectivity() {
        // Prevent multiple network checks from running simultaneously
        if (isNetworkCheckInProgress) {
            logDebug("Network check already in progress, skipping");
            return;
        }

        // Prevent too frequent network checks
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastNetworkCheckTime < NETWORK_CHECK_DELAY) {
            logDebug("Network check too soon, skipping");
            return;
        }

        isNetworkCheckInProgress = true;
        lastNetworkCheckTime = currentTime;

        logInfo("Checking internet connectivity");

        // Check using modern connectivity API
        ConnectivityManager connectivityManager = (ConnectivityManager) PiglioTechApp.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            logWarning("No active network detected");
            showNoInternetMessage();
            isNetworkCheckInProgress = false;
            return;
        }

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        boolean hasInternetCapability = capabilities != null && 
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || 
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));

        if (!hasInternetCapability) {
            logWarning("No internet capability detected");
            showNoInternetMessage();
            isNetworkCheckInProgress = false;
            return;
        }

        // If we have a connection according to ConnectivityManager, continue with the ping
        doPingToGoogleForConnectivityCheck();
    }

    private void doPingToGoogleForConnectivityCheck() {
        try {
            logInfo("Pinging Google to verify internet connectivity");
            Call<Void> googlePingCall = RetrofitInstance.getGooglePingService().pingGoogle();
            googlePingCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    long responseTimeMs = System.currentTimeMillis() - lastNetworkCheckTime;
                    isNetworkCheckInProgress = false;
                    if (response.isSuccessful()) {
                        logInfo(String.format("Google ping successful - Response time: %d ms", responseTimeMs));
                        // Internet is working, continue with backend check
                        handler.postDelayed(() -> startBackendCheck(false), 1000);
                    } else {
                        logWarning(String.format("Google ping failed with code: %d - Response time: %d ms",
                                response.code(), responseTimeMs));
                        showNoInternetMessage();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    long responseTimeMs = System.currentTimeMillis() - lastNetworkCheckTime;
                    isNetworkCheckInProgress = false;
                    logWarning(String.format("Google ping failed: %s - Response time: %d ms",
                            t.getMessage(), responseTimeMs));

                    // Check if it's a DNS resolution error
                    if (t instanceof UnknownHostException) {
                        logWarning("DNS resolution error, showing no internet message");
                        showNoInternetMessage();
                    } else {
                        // For other network errors, try again after a delay
                        handler.postDelayed(() -> startBackendCheck(false), 5000);
                    }
                }
            });
        } catch (Exception e) {
            isNetworkCheckInProgress = false;
            logWarning("Error executing Google ping: " + e.getMessage());
            showNoInternetMessage();
        }
    }

    private void showNoInternetMessage() {
        handler.post(() -> {
            try {
                Toast.makeText(PiglioTechApp.getContext(),
                        "No internet connection detected. Please check your connection and try again.",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e(TAG, "Error showing toast: " + e.getMessage());
            }

            // Reset the retry count so we can try again when internet is back
            retryCount = 0;
            isChecking.set(false);
            currentStatus = BackendStatus.NO_INTERNET;
        });
    }

    public void handleTimeout() {
        synchronized (lock) {
            currentStatus = BackendStatus.OFFLINE;
            isChecking.set(false);
            hasReachedMaxRetries = true;
            showColdStartScreen();
            // Clear pending requests on timeout
            pendingRequests.clear();
            pendingAction = null;
        }
    }

    private void showColdStartScreen() {
        if (!isColdStartScreenShown) {
            isColdStartScreenShown = true;
            coldStartStartTime = System.currentTimeMillis();
            handler.post(() -> {
                try {
                    logInfo("Showing cold start screen");
                    Intent intent = new Intent(PiglioTechApp.getContext(), BackendColdStartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PiglioTechApp.getContext().startActivity(intent);

                    // Start auto-checking for backend availability
                    handler.postDelayed(autoCheckRunnable, AUTO_CHECK_INTERVAL);
                } catch (Exception e) {
                    Log.e(TAG, "Error showing cold start screen: " + e.getMessage());
                    isColdStartScreenShown = false;
                }
            });
        }
    }

    private void hideColdStartScreen() {
        if (isColdStartScreenShown) {
            isColdStartScreenShown = false;
            handler.removeCallbacks(autoCheckRunnable);

            // Send a broadcast to notify the activity to finish
            handler.post(() -> {
                try {
                    logInfo("Sending broadcast to close cold start screen");
                    Intent intent = new Intent(ACTION_BACKEND_ONLINE);
                    intent.setPackage(PiglioTechApp.getContext().getPackageName());
                    PiglioTechApp.getContext().sendBroadcast(intent);

                    // Send a second broadcast after a longer delay to ensure it's received
                    // and to give the app time to complete its loading process
                    handler.postDelayed(() -> {
                        try {
                            logInfo("Sending delayed broadcast to close cold start screen");
                            Intent delayedIntent = new Intent(ACTION_BACKEND_ONLINE);
                            delayedIntent.setPackage(PiglioTechApp.getContext().getPackageName());
                            PiglioTechApp.getContext().sendBroadcast(delayedIntent);
                        } catch (Exception e) {
                            Log.e(TAG, "Error sending delayed broadcast: " + e.getMessage());
                        }
                    }, 5000); // Increased from 1000ms to 5000ms (5 seconds)
                } catch (Exception e) {
                    Log.e(TAG, "Error sending broadcast: " + e.getMessage());
                }
            });
        }
    }

    public long getElapsedTime() {
        if (isColdStartScreenShown) {
            return System.currentTimeMillis() - coldStartStartTime;
        }
        return System.currentTimeMillis() - startTime;
    }

    private void logInfo(String message) {
        Log.i(TAG, String.format(LOG_FORMAT, LOG_PREFIX, getStatusInfo(), message));
    }

    private void logDebug(String message) {
        Log.d(TAG, String.format(LOG_FORMAT, LOG_PREFIX, getStatusInfo(), message));
    }

    private void logWarning(String message) {
        Log.w(TAG, String.format(LOG_FORMAT, LOG_PREFIX, getStatusInfo(), message));
    }

    private String getStatusInfo() {
        long elapsedTimeMs = System.currentTimeMillis() - startTime;
        return String.format("[Status: %s, Retry: %d/%d, Elapsed: %ds]",
                currentStatus,
                retryCount,
                MAX_RETRIES,
                TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMs));
    }
}
