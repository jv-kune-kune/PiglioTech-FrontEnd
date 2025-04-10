package com.northcoders.pigliotech_frontend;

import android.app.Application;
import android.content.Context;

public class PiglioTechApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        PiglioTechApp.context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}