package com.northcoders.pigliotech_frontend.data.network;

// ...existing imports...
import com.northcoders.pigliotech_frontend.data.api.GooglePingService;
import com.northcoders.pigliotech_frontend.data.api.UserApiService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Dns;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("unused")
public class RetrofitInstance {
    private static final String TAG = "RetrofitInstance";
    private static final String BASE_URL = "https://pigliotech-backend.onrender.com/api/v1/";

    // Regular request timeouts
    private static final int CONNECT_TIMEOUT_SECONDS = 30;
    private static final int READ_TIMEOUT_SECONDS = 30;
    private static final int WRITE_TIMEOUT_SECONDS = 30;

    // Ping service timeouts - more lenient for cold starts
    private static final int PING_CONNECT_TIMEOUT_SECONDS = 180; // 3 minutes
    private static final int PING_READ_TIMEOUT_SECONDS = 180; // 3 minutes
    private static final int PING_WRITE_TIMEOUT_SECONDS = 180; // 3 minutes

    private static UserApiService regularService = null;
    private static UserApiService pingService = null;
    private static GooglePingService googlePingService = null;

    // Custom DNS resolver to handle DNS issues on some devices
    private static final Dns CUSTOM_DNS = new Dns() {
        private final Dns defaultDns = Dns.SYSTEM;
        private final List<String> fallbackDnsServers = Arrays.asList(
                "8.8.8.8", // Google DNS
                "8.8.4.4", // Google DNS secondary
                "1.1.1.1" // Cloudflare DNS
        );

        @Override
        public List<InetAddress> lookup(String hostname) throws UnknownHostException {
            try {
                // Try system DNS first
                return defaultDns.lookup(hostname);
            } catch (UnknownHostException e) {
                // If system DNS fails, try alternative approaches
                for (String dnsServer : fallbackDnsServers) {
                    try {
                        // Try to manually resolve using fallback DNS
                        InetAddress dnsAddress = InetAddress.getByName(dnsServer);
                        InetAddress resolved = InetAddress.getByName(hostname);
                        return Arrays.asList(resolved);
                    } catch (Exception ignored) {
                        // Continue to next DNS server
                    }
                }

                // All resolution attempts failed, throw the original exception
                throw e;
            }
        }
    };

    public static UserApiService getService() {
        if (regularService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .dns(CUSTOM_DNS)
                    .addInterceptor(new BackendAvailabilityInterceptor())
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            regularService = retrofit.create(UserApiService.class);
        }
        return regularService;
    }

    public static UserApiService getPingService() {
        if (pingService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(PING_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(PING_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(PING_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .dns(CUSTOM_DNS)
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            pingService = retrofit.create(UserApiService.class);
        }
        return pingService;
    }

    public static GooglePingService getGooglePingService() {
        if (googlePingService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .dns(CUSTOM_DNS)
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.google.com/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            googlePingService = retrofit.create(GooglePingService.class);
        }
        return googlePingService;
    }
}