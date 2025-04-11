package com.northcoders.pigliotech_frontend.presentation.features.main;

import android.os.Bundle;
import android.view.MenuItem;

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

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

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