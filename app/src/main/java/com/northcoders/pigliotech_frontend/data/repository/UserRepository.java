package com.northcoders.pigliotech_frontend.data.repository;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;

import com.northcoders.pigliotech_frontend.data.api.UserApiService;
import com.northcoders.pigliotech_frontend.data.models.Isbn;
import com.northcoders.pigliotech_frontend.data.models.Match;
import com.northcoders.pigliotech_frontend.data.models.SwapDismissal;
import com.northcoders.pigliotech_frontend.data.models.SwapRequest;
import com.northcoders.pigliotech_frontend.data.models.User;
import com.northcoders.pigliotech_frontend.data.network.RetrofitInstance;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final String TAG = "UserRepository";
    private final UserApiService userApiService;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 5000;

    public UserRepository() {
        this.userApiService = RetrofitInstance.getService();
    }

    private abstract class RetryingCallback<T> implements Callback<T> {
        private int retryCount = 0;
        private final Consumer<?> consumer;
        private final Call<T> originalCall;

        RetryingCallback(Call<T> call, Consumer<?> consumer) {
            this.consumer = consumer;
            this.originalCall = call;
        }

        @Override
        public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
            boolean shouldRetry = false;
            String errorType = "unknown";

            // Check for various network-related exceptions
            if (t instanceof IOException) {
                if (t.getMessage() != null && t.getMessage().contains("Backend is starting up")) {
                    shouldRetry = true;
                    errorType = "backend-starting";
                } else if (t.getMessage() != null && t.getMessage().contains("timeout")) {
                    shouldRetry = true;
                    errorType = "timeout";
                } else if (t instanceof java.net.SocketTimeoutException) {
                    shouldRetry = true;
                    errorType = "socket-timeout";
                } else if (t instanceof java.net.ConnectException) {
                    shouldRetry = true;
                    errorType = "connection-refused";
                } else if (t instanceof java.net.UnknownHostException) {
                    shouldRetry = true;
                    errorType = "unknown-host";
                } else {
                    shouldRetry = true;
                    errorType = "io-exception";
                }
            }

            if (shouldRetry && retryCount < MAX_RETRIES) {
                retryCount++;
                long delay = RETRY_DELAY_MS * (long) retryCount; // Cast to long to prevent overflow

                Log.i(TAG, String.format("Network error (%s), retry attempt %d/%d in %d ms",
                        errorType, retryCount, MAX_RETRIES, delay));

                handler.postDelayed(() -> {
                    // Use clone of the original call to retry
                    Call<T> retryCall = originalCall.clone();
                    retryCall.enqueue(this);
                }, delay);
            } else {
                consumer.accept(null);
                Log.e(TAG, String.format("Network failure (%s) after %d retries: %s",
                        errorType, retryCount, t.getMessage()), t);
            }
        }
    }

    public void addUser(User user, Consumer<Integer> addUserConsumer) {
        Call<User> call = userApiService.addUser(user);
        call.enqueue(new RetryingCallback<>(call, addUserConsumer) {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.code() == 201 && response.body() != null) {
                    addUserConsumer.accept(response.code());
                    Log.i(TAG, "ADDED USER: " + response.body());
                } else {
                    addUserConsumer.accept(response.code());
                    Log.e(TAG, "USER NOT ADDED: " + response.code());
                }
            }
        });
    }

    public void getUser(String id, Consumer<User> getUserConsumer) {
        Call<User> call = userApiService.getCurrentUser(id);
        Log.i(TAG, "Requested User Id: " + id);

        call.enqueue(new RetryingCallback<>(call, getUserConsumer) {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.code() == 200 && response.body() != null) {
                    User user = response.body();
                    getUserConsumer.accept(user);
                    Log.i(TAG, "GET USER: Successfully retrieved " + response.body());
                } else {
                    getUserConsumer.accept(null);
                    Log.e(TAG, "GET USER: Unsuccessful response code " + response.code());
                }
            }
        });
    }

    public void getUsersByRegion(String regionEnum, String currentUserId, Consumer<List<User>> usersByRegionConsumer) {
        Call<List<User>> call = userApiService.getUsersByRegion(regionEnum, currentUserId);
        Log.i(TAG, "getUsersByRegion Called, Region: " + regionEnum + ", UserID: " + currentUserId);

        call.enqueue(new RetryingCallback<>(call, usersByRegionConsumer) {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.code() == 200 && response.body() != null) {
                    List<User> usersByRegion = response.body();
                    usersByRegionConsumer.accept(usersByRegion);
                    Log.i(TAG, "GET USERS BY REGION: Successfully retrieved " + response.body());
                } else {
                    usersByRegionConsumer.accept(null);
                    Log.e(TAG, "GET USERS BY REGION: NOT retrieved " + response.code());
                }
            }
        });
    }

    public void addBook(String userId, Isbn isbn, Consumer<Integer> addBookConsumer) {
        Call<User> call = userApiService.addBook(userId, isbn);
        Log.i(TAG, "addBook Called, userId: " + userId + ", ISBN: " + isbn);

        call.enqueue(new RetryingCallback<>(call, addBookConsumer) {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.code() == 201 && response.body() != null) {
                    addBookConsumer.accept(response.code());
                    Log.i(TAG, "ADD BOOK: " + response.body());
                } else {
                    addBookConsumer.accept(response.code());
                    Log.e(TAG, "ADD BOOK: " + response.code());
                }
            }
        });
    }

    public void deleteBook(String userId, String isbnString, Consumer<Integer> deleteBookConsumer) {
        Call<Void> call = userApiService.deleteBook(userId, isbnString);
        Log.i(TAG, "deleteBook Called, userId: " + userId + ", ISBN: " + isbnString);

        call.enqueue(new RetryingCallback<>(call, deleteBookConsumer) {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == 204) {
                    deleteBookConsumer.accept(response.code());
                    Log.i(TAG, "DELETE BOOK: Successful");
                } else {
                    deleteBookConsumer.accept(response.code());
                    Log.e(TAG, "DELETE BOOK: Failed: " + response.code());
                }
            }
        });
    }

    public void createSwapRequest(SwapRequest swapRequest, Consumer<Integer> likeBookConsumer) {
        Call<SwapRequest> call = userApiService.createSwapRequest(swapRequest);

        call.enqueue(new RetryingCallback<>(call, likeBookConsumer) {
            @Override
            public void onResponse(@NonNull Call<SwapRequest> call, @NonNull Response<SwapRequest> response) {
                if (response.code() == 201 && response.body() != null) {
                    likeBookConsumer.accept(response.code());
                    Log.i(TAG, "CREATED SWAP REQUEST: " + response.body());
                } else if (response.code() == 409) {
                    likeBookConsumer.accept(response.code());
                    Log.i(TAG, "SWAP REQUEST ALREADY EXISTS: " + response.code());
                } else {
                    likeBookConsumer.accept(response.code());
                    Log.e(TAG, "SWAP REQUEST NOT CREATED: " + response.code());
                }
            }
        });
    }

    public void getMatchesForCurrentUser(String currentUserId, Consumer<List<Match>> usersMatchesConsumer) {
        Call<List<Match>> call = userApiService.getMatches(currentUserId);
        Log.i(TAG, "getMatches UserID: " + currentUserId);

        call.enqueue(new RetryingCallback<>(call, usersMatchesConsumer) {
            @Override
            public void onResponse(@NonNull Call<List<Match>> call, @NonNull Response<List<Match>> response) {
                if (response.code() == 200 && response.body() != null) {
                    List<Match> usersMatches = response.body();
                    usersMatchesConsumer.accept(usersMatches);
                    Log.i(TAG, "GET USERS MATCHES: Successfully retrieved " + response.body());
                } else {
                    usersMatchesConsumer.accept(null);
                    Log.e(TAG, "GET USERS MATCHES: NOT retrieved " + response.code());
                }
            }
        });
    }

    public void dismissMatch(SwapDismissal swapDismissal, Consumer<Integer> dismissMatchConsumer) {
        Call<Void> call = userApiService.dismissMatch(swapDismissal);
        Log.i(TAG, "dismissMatch Called : " + swapDismissal);

        call.enqueue(new RetryingCallback<>(call, dismissMatchConsumer) {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == 204) {
                    dismissMatchConsumer.accept(response.code());
                    Log.i(TAG, "DISMISS MATCH: Successful");
                } else {
                    dismissMatchConsumer.accept(response.code());
                    Log.e(TAG, "DISMISS MATCH: Failed: " + response.code());
                }
            }
        });
    }
}