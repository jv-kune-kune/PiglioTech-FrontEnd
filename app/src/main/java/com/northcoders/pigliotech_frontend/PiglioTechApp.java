package com.northcoders.pigliotech_frontend;

import android.app.Application;
import android.content.Context;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class PiglioTechApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        PiglioTechApp.context = getApplicationContext();

        // Initialize Firebase Crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
    }

    public static Context getContext() {
        return context;
    }
}
