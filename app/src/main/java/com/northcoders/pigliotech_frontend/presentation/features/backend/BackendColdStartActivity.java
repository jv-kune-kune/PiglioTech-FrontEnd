package com.northcoders.pigliotech_frontend.presentation.features.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.utils.BackendStatusManager;
import java.util.concurrent.TimeUnit;
import java.util.Locale;

public class BackendColdStartActivity extends AppCompatActivity {
    private static final String TAG = "BackendColdStartActivity";
    private static final String LOG_PREFIX = "[ColdStart] ";
    private static final String LOG_FORMAT = "%s%s - %s";
    private static final String BACKEND_ONLINE_ACTION = "com.northcoders.pigliotech_frontend.BACKEND_ONLINE";
    private static final long UPDATE_INTERVAL = 1000; // Update every second
    private static final long ESTIMATED_COLD_START_TIME = 300000; // 5 minutes in milliseconds

    private TextView elapsedTimeTextView;
    private TextView progressText;
    private ProgressBar progressBar;
    private Handler handler;
    private Runnable updateElapsedTimeRunnable;
    private BroadcastReceiver backendOnlineReceiver;
    private boolean isReceiverRegistered = false;
    private boolean isFinishing = false;
    private long startTime;

    private void logInfo(String message) {
        Log.i(TAG, String.format(Locale.US, LOG_FORMAT, LOG_PREFIX, getActivityInfo(), message));
    }

    private void logDebug(String message) {
        Log.d(TAG, String.format(Locale.US, LOG_FORMAT, LOG_PREFIX, getActivityInfo(), message));
    }

    private void logError(String message) {
        Log.e(TAG, String.format(Locale.US, LOG_FORMAT, LOG_PREFIX, getActivityInfo(), message));
    }

    private String getActivityInfo() {
        long elapsedTimeMs = System.currentTimeMillis() - startTime;
        return String.format(Locale.US, "[Elapsed: %ds, Receiver: %s]",
                TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMs),
                isReceiverRegistered ? "Registered" : "Unregistered");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backend_cold_start);

        try {
            startTime = System.currentTimeMillis();
            handler = new Handler(Looper.getMainLooper());
            elapsedTimeTextView = findViewById(R.id.elapsedTimeTextView);
            progressBar = findViewById(R.id.progressBar);
            progressText = findViewById(R.id.progressText);
            Button retryButton = findViewById(R.id.retryButton);
            TextView messageTextView = findViewById(R.id.messageTextView);

            // Set the message explaining the cold start process
            messageTextView.setText(
                    """
                            Our backend server is currently starting up. This is normal and usually takes less than 5 minutes if the server hasn't been used recently (max 10).

                            This happens because we're using a free tier on Render.com which puts our server to sleep after periods of inactivity to save resources.

                            You don't need to keep clicking 'Try Again' - the app will automatically detect when the server is back online and continue.
                            """);

            // Set up the retry button
            retryButton.setOnClickListener(v -> {
                try {
                    logInfo("Retry button clicked, starting backend check");
                    BackendStatusManager.getInstance().startBackendCheck();
                } catch (Exception e) {
                    logError("Error starting backend check: " + e.getMessage());
                }
            });

            // Set up the elapsed time update
            updateElapsedTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    updateElapsedTime();
                    updateProgressBar();
                    handler.postDelayed(this, UPDATE_INTERVAL);
                }
            };

            // Register the broadcast receiver to listen for backend online events
            registerBackendOnlineReceiver();

            // Check if backend is already online
            if (BackendStatusManager.getInstance().isBackendAvailable()) {
                logInfo("Backend is already online, finishing activity");
                if (!isFinishing) {
                    isFinishing = true;
                    finish();
                }
            }
        } catch (Exception e) {
            logError("Error in onCreate: " + e.getMessage());
        }
    }

    private void registerBackendOnlineReceiver() {
        try {
            logDebug("Registering backend online receiver");
            backendOnlineReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (BACKEND_ONLINE_ACTION.equals(intent.getAction())) {
                        logInfo("Received backend online broadcast, finishing activity");
                        if (!isFinishing) {
                            isFinishing = true;
                            finish();
                        }
                    }
                }
            };

            IntentFilter filter = new IntentFilter(BACKEND_ONLINE_ACTION);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(backendOnlineReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
            } else {
                // For older versions, use ContextCompat with RECEIVER_NOT_EXPORTED flag
                ContextCompat.registerReceiver(this, backendOnlineReceiver, filter,
                        ContextCompat.RECEIVER_NOT_EXPORTED);
            }
            isReceiverRegistered = true;
            logInfo("Backend online receiver registered successfully");
        } catch (Exception e) {
            logError("Error registering broadcast receiver: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            // Start updating the elapsed time
            handler.post(updateElapsedTimeRunnable);

            // Check if backend is already online
            if (BackendStatusManager.getInstance().isBackendAvailable()) {
                logInfo("Backend is already online, finishing activity");
                if (!isFinishing) {
                    isFinishing = true;
                    finish();
                }
            }
        } catch (Exception e) {
            logError("Error in onResume: " + e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            // Stop updating the elapsed time
            handler.removeCallbacks(updateElapsedTimeRunnable);
            logDebug("Stopped elapsed time updates");
        } catch (Exception e) {
            logError("Error in onPause: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            // Unregister the broadcast receiver
            if (backendOnlineReceiver != null && isReceiverRegistered) {
                unregisterReceiver(backendOnlineReceiver);
                isReceiverRegistered = false;
                logInfo("Backend online receiver unregistered");
            }
        } catch (Exception e) {
            logError("Error in onDestroy: " + e.getMessage());
        }
    }

    private void updateElapsedTime() {
        long elapsedTimeMs = System.currentTimeMillis() - startTime;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMs);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMs) % 60;

        String timeText = String.format(Locale.US, "Time elapsed: %d min %d sec", minutes, seconds);
        elapsedTimeTextView.setText(timeText);
    }

    private void updateProgressBar() {
        long elapsedTimeMs = System.currentTimeMillis() - startTime;
        int progress = (int) Math.min(100, (elapsedTimeMs * 100) / ESTIMATED_COLD_START_TIME);

        progressBar.setProgress(progress);
        progressText.setText(String.format(Locale.US, "%d%%", progress));

        // Change progress bar color based on progress
        if (progress > 80) {
            // Almost done - green
            progressBar.getProgressDrawable().setColorFilter(
                    ContextCompat.getColor(this, android.R.color.holo_green_dark),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (progress > 50) {
            // Halfway there - yellow
            progressBar.getProgressDrawable().setColorFilter(
                    ContextCompat.getColor(this, android.R.color.holo_orange_light),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            // Just started - red
            progressBar.getProgressDrawable().setColorFilter(
                    ContextCompat.getColor(this, android.R.color.holo_red_light),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }
}