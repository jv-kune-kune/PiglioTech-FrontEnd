package com.northcoders.pigliotech_frontend.utils;

import android.util.Log;
import androidx.annotation.NonNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

public class BackendAvailabilityInterceptor implements Interceptor {
    private static final String TAG = "BackendInterceptor";
    private static final int MAX_PING_RETRIES = 30; // 30 attempts
    static final long PING_RETRY_DELAY_MS = 3000; // 3 seconds base delay
    private static final long MAX_RETRY_DELAY_MS = 30000; // 30 seconds max delay
    private static final double BACKOFF_MULTIPLIER = 1.5; // Exponential backoff multiplier

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d(TAG, "Intercepting request: " + chain.request().url());
        BackendStatusManager manager = BackendStatusManager.getInstance();

        // First check if backend status is cached and valid
        if (!manager.isBackendAvailable()) {
            Log.i(TAG, "Backend not available, starting status check");

            // Start ping check and wait for result
            int pingAttempts = 0;
            long currentDelay = PING_RETRY_DELAY_MS;

            while (pingAttempts < MAX_PING_RETRIES) {
                Log.d(TAG, "Ping attempt " + (pingAttempts + 1) + "/" + MAX_PING_RETRIES + " with delay: "
                        + currentDelay + "ms");

                // Start a new ping check
                manager.startBackendCheck(true);

                try {
                    // Wait with exponential backoff
                    Thread.sleep(currentDelay);

                    // Check if backend is now available
                    if (manager.isBackendAvailable()) {
                        Log.i(TAG, "Backend is now online");
                        break;
                    }

                    // Calculate next delay with exponential backoff
                    currentDelay = Math.min((long) (currentDelay * BACKOFF_MULTIPLIER), MAX_RETRY_DELAY_MS);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Log.e(TAG, "Ping wait interrupted", e);
                    throw new IOException("Backend check interrupted");
                }

                pingAttempts++;
                if (pingAttempts == MAX_PING_RETRIES) {
                    Log.e(TAG, "Max ping retries reached, backend still unavailable");
                    throw new IOException("Backend is not available after " + MAX_PING_RETRIES + " attempts");
                }
            }
        }

        // At this point backend should be online, proceed with original request
        Log.d(TAG, "Proceeding with request: " + chain.request().url());
        return chain.proceed(chain.request());
    }
}