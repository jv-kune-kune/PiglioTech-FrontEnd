package com.northcoders.pigliotech_frontend.utils;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

/**
 * Utility class for crash reporting and logging throughout the app.
 * Provides methods to log exceptions, custom error messages, and user
 * information
 * to Firebase Crashlytics for easier diagnosis of issues.
 */
public class CrashReportingHelper {
    private static final String TAG = "CrashReportingHelper";

    /**
     * Check if Crashlytics is properly initialized and log the result.
     * Call this from your main activity to verify Crashlytics setup.
     * 
     * @return true if Crashlytics is properly initialized
     */
    public static boolean isCrashlyticsInitialized() {
        try {
            FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
            crashlytics.log("Crashlytics initialization check");
            Log.i(TAG, "Crashlytics is properly initialized");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Crashlytics is NOT properly initialized: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Log a non-fatal exception to Crashlytics.
     * 
     * @param throwable The exception to log
     * @param message   Additional context message
     */
    public static void logException(@NonNull Throwable throwable, @Nullable String message) {
        try {
            FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();

            if (message != null) {
                crashlytics.log(message);
            }
            crashlytics.recordException(throwable);

            Log.e(TAG, message != null ? message : "Exception logged to Crashlytics", throwable);
        } catch (Exception e) {
            Log.e(TAG, "Failed to log exception to Crashlytics", e);
        }
    }

    /**
     * Log a non-fatal exception to Crashlytics.
     * 
     * @param throwable The exception to log
     */
    public static void logException(@NonNull Throwable throwable) {
        logException(throwable, null);
    }

    /**
     * Log a custom error message to Crashlytics.
     * 
     * @param message The error message to log
     */
    public static void logError(@NonNull String message) {
        try {
            FirebaseCrashlytics.getInstance().log("ERROR: " + message);
            Log.e(TAG, message);
        } catch (Exception e) {
            Log.e(TAG, "Failed to log error message to Crashlytics", e);
        }
    }

    /**
     * Log user information to Crashlytics to help identify user-specific issues.
     * 
     * @param userId Unique identifier for the user
     * @param email  User's email (can be null)
     */
    public static void setUserIdentifier(@NonNull String userId, @Nullable String email) {
        try {
            FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
            crashlytics.setUserId(userId);

            if (email != null) {
                crashlytics.setCustomKey("email", email);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to set user identifier in Crashlytics", e);
        }
    }

    /**
     * Add a custom key-value pair to Crashlytics reports.
     * 
     * @param key   The key for the custom value
     * @param value The value to associate with the key
     */
    public static void setCustomKey(@NonNull String key, @NonNull String value) {
        try {
            FirebaseCrashlytics.getInstance().setCustomKey(key, value);
        } catch (Exception e) {
            Log.e(TAG, "Failed to set custom key in Crashlytics: " + key, e);
        }
    }
}