package com.northcoders.pigliotech_frontend.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

/**
 * A utility class for managing a hidden developer menu.
 * Activated by shaking the device (only in debug builds).
 */
public class DevMenuManager implements SensorEventListener {
    private static final float SHAKE_THRESHOLD = 10.0f;
    private static final int MIN_TIME_BETWEEN_SHAKES_MS = 1000;
    private long mLastShakeTime = 0;
    private final SensorManager mSensorManager;
    private final Context mContext;
    private final Sensor mAccelerometer;
    private boolean mIsRegistered = false;
    private final boolean mIsDebugBuild;

    public DevMenuManager(Context context, boolean isDebugBuild) {
        mContext = context;
        mIsDebugBuild = isDebugBuild;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager != null ? mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) : null;
    }

    /**
     * Registers the shake detector. Call in onResume().
     */
    public void register() {
        // Only register in debug builds
        if (!mIsDebugBuild || mAccelerometer == null) {
            return;
        }

        if (!mIsRegistered) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
            mIsRegistered = true;
        }
    }

    /**
     * Unregisters the shake detector. Call in onPause().
     */
    public void unregister() {
        if (mIsRegistered && mSensorManager != null) {
            mSensorManager.unregisterListener(this);
            mIsRegistered = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gForce = (float) Math.sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH;

        if (gForce > SHAKE_THRESHOLD) {
            long now = System.currentTimeMillis();
            if (mLastShakeTime + MIN_TIME_BETWEEN_SHAKES_MS > now) {
                return;
            }

            mLastShakeTime = now;
            showDevMenu();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this implementation
    }

    /**
     * Shows the developer menu dialog with test options.
     */
    private void showDevMenu() {
        new AlertDialog.Builder(mContext)
                .setTitle("Developer Menu")
                .setItems(new String[] {
                        "Test Crash",
                        "Test Non-Fatal Error",
                        "Clear Crashlytics User",
                        "Cancel"
                }, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            FirebaseCrashlytics.getInstance().log("Test crash triggered from dev menu");
                            throw new RuntimeException("Test Crash from Dev Menu");
                        case 1:
                            try {
                                throw new IllegalStateException("Test non-fatal error");
                            } catch (Exception e) {
                                CrashReportingHelper.logException(e, "Non-fatal test from dev menu");
                                Toast.makeText(mContext, "Non-fatal event logged", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            FirebaseCrashlytics.getInstance().setUserId("");
                            Toast.makeText(mContext, "Crashlytics user cleared", Toast.LENGTH_SHORT).show();
                            break;
                    }
                })
                .show();
    }
}