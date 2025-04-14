package com.northcoders.pigliotech_frontend.presentation.features.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.navigation.NavigationBarView;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.presentation.features.addbook.AddBookFragment;
import com.northcoders.pigliotech_frontend.presentation.features.home.HomeFragment;
import com.northcoders.pigliotech_frontend.presentation.features.landing.LandingPageFragment;
import com.northcoders.pigliotech_frontend.presentation.features.profile.ProfileFragment;
import com.northcoders.pigliotech_frontend.presentation.features.swapbook.SwapFragment;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.northcoders.pigliotech_frontend.utils.CrashReportingHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.northcoders.pigliotech_frontend.utils.DevMenuManager;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private MainActivityViewModel viewModel;
    private DevMenuManager devMenuManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        MainActivityViewModel viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        NavigationBarView bottomNav = findViewById(R.id.bottom_nav_bar);
        bottomNav.setOnItemSelectedListener(this);

        viewModel.getEvents().observe(this, event -> {
            if (event != null) {
                if (event == MainActivityEvents.NAVIGATE_TO_LANDING_PAGE) {
                    replaceFragment(new LandingPageFragment());
                } else if (event == MainActivityEvents.NAVIGATE_TO_HOME_PAGE) {
                    replaceFragment(new HomeFragment());
                }
                viewModel.eventSeen();
            }
        });

        // Check if Crashlytics is properly initialized
        boolean crashlyticsInitialized = CrashReportingHelper.isCrashlyticsInitialized();
        String message = crashlyticsInitialized
                ? "Crashlytics initialized successfully"
                : "Crashlytics initialization failed";

        // We'll use a constant for debugMode since we can't access BuildConfig
        // For a real app, you'd use BuildConfig.DEBUG here
        final boolean debugMode = true; // Set to false for production release

        // Only show toast in debug builds
        if (debugMode) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            // Initialize the DevMenuManager (shake to show)
            devMenuManager = new DevMenuManager(this, debugMode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (devMenuManager != null) {
            devMenuManager.register();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (devMenuManager != null) {
            devMenuManager.unregister();
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_fragment, fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            replaceFragment(new HomeFragment());
            return true;
        }
        if (item.getItemId() == R.id.swap) {
            replaceFragment(new SwapFragment());
            return true;
        }
        if (item.getItemId() == R.id.profile) {
            replaceFragment(new ProfileFragment());
            return true;
        }
        if (item.getItemId() == R.id.addBook) {
            replaceFragment(new AddBookFragment());
            return true;
        }
        return false;
    }
}