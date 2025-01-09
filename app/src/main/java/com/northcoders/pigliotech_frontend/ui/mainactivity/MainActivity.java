package com.northcoders.pigliotech_frontend.ui.mainactivity;

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
import com.northcoders.pigliotech_frontend.ui.fragments.addbook.AddBookFragment;
import com.northcoders.pigliotech_frontend.ui.fragments.home.HomeFragment;
import com.northcoders.pigliotech_frontend.ui.fragments.landingpage.LandingPageFragment;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.ui.fragments.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private MainActivityViewModel viewModel;

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

        // Instantiate the ViewModel
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // Initialising the BottomNavigationBar and assigning the setOnItemSelectedListener
        NavigationBarView bottomNav = findViewById(R.id.bottom_nav_bar);
        bottomNav.setOnItemSelectedListener(this);

        // Observing the MainActivityEvents LiveData from the ViewModel.
        viewModel.getEvents().observe(this, event -> {
            if (event != null){ // Code will only run if event is not null
                switch (event){
                    case NAVIGATE_TO_LANDING_PAGE:
                        Fragment landingPage = new LandingPageFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout_fragment, landingPage)
                                .commit();
                        break;
                    case NAVIGATE_TO_HOME_PAGE:
                        Fragment homeFragment = new HomeFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout_fragment, homeFragment)
                                .commit();
                        break;
                }
                viewModel.eventSeen(); // Sets the event to null
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_fragment, new HomeFragment())
                    .commit();
            return true;
        }
        if(item.getItemId() == R.id.profile) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_fragment, new ProfileFragment())
                    .commit();
            return true;
        }
        if(item.getItemId() == R.id.addBook) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_fragment, new AddBookFragment())
                    .commit();
            return true;
        }
        return false;
    }
}