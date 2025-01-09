package com.northcoders.pigliotech_frontend.ui.mainactivity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.northcoders.pigliotech_frontend.ui.fragments.addbook.AddBookFragment;
import com.northcoders.pigliotech_frontend.ui.fragments.home.HomeFragment;
import com.northcoders.pigliotech_frontend.ui.fragments.landingpage.LandingPageFragment;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.ui.fragments.profile.ProfileFragment;
import com.northcoders.pigliotech_frontend.ui.fragments.signup.SignUpFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private NavigationBarView bottomNav;
    private ProfileFragment profileFragment = new ProfileFragment(); // TODO Rename to ProfileFragment
    private HomeFragment homeFragment = new HomeFragment();
    private AddBookFragment addBookFragment = new AddBookFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        bottomNav = findViewById(R.id.bottom_nav_bar);
        bottomNav.setOnItemSelectedListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_fragment, new SignUpFragment())
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        ProfileFragment profileFragment = new ProfileFragment();
        LandingPageFragment landingPageFragment = new LandingPageFragment();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_fragment, profileFragment)
                    .commit();

            NavigationBarView bottomNavBar = findViewById(R.id.bottom_nav_bar);


        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_fragment, landingPageFragment)
                    .commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_fragment, homeFragment)
                    .commit();
            return true;
        }
        if(item.getItemId() == R.id.profile) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_fragment, profileFragment)
                    .commit();
            return true;
        }
        if(item.getItemId() == R.id.addBook) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_fragment, addBookFragment)
                    .commit();
            return true;
        }
        return false;
    }
}