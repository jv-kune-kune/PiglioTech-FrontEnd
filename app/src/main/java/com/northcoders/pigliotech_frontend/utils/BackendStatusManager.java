package com.northcoders.pigliotech_frontend.manager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.northcoders.pigliotech_frontend.model.service.UserApiService;
import com.northcoders.pigliotech_frontend.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Manages backend status checks.
 */
public class BackendStatusManager {
    private static final String TAG = "BackendStatusManager";
    private static final int CHECK_INTERVAL_MS = 5000; // 5 seconds interval
    private boolean isBackendAvailable = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final UserApiService apiService;
    private BackendStatusListener statusListener;

    public BackendStatusManager() {
        this.apiService = RetrofitInstance.getInstance().create(UserApiService.class);
    }

    /**
     * Starts periodic checks for backend status.
     */
    public void startCheckingBackendStatus() {
        handler.post(checkBackendRunnable);
    }

    /**
     * Stops periodic checks.
     */
    public void stopCheckingBackendStatus() {
        handler.removeCallbacks(checkBackendRunnable);
    }

    private final Runnable checkBackendRunnable = new Runnable() {
        @Override
        public void run() {
            checkBackendStatus();
            handler.postDelayed(this, CHECK_INTERVAL_MS);
        }
    };

    private void checkBackendStatus() {
        apiService.pingBackend().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                boolean status = response.isSuccessful();
                if (status != isBackendAvailable) {
                    isBackendAvailable = status;
                    if (statusListener != null) {
                        statusListener.onBackendStatusChanged(status);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (isBackendAvailable) {
                    isBackendAvailable = false;
                    if (statusListener != null) {
                        statusListener.onBackendStatusChanged(false);
                    }
                }
                Log.e(TAG, "Backend check failed", t);
            }
        });
    }

    public void setBackendStatusListener(BackendStatusListener listener) {
        this.statusListener = listener;
    }

    public interface BackendStatusListener {
        void onBackendStatusChanged(boolean isAvailable);
    }
}