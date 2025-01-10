package com.plants;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import com.plants.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String userEmail; // Variable to store the email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the email passed from LoginActivity
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        binding.bnvActivityMain.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.mActivityMainHome) {
                fragment = new DashboardFragment();  // Dashboard as home
            } else if (itemId == R.id.mActivityMainGrowth) {
                fragment = new GrowthFragment();
            } else if (itemId == R.id.mActivityMainInformations) {
                fragment = new InformationsFragment();
            } else if (itemId == R.id.mActivityMainGuide) {
                fragment = new GuideFragment();
            } else if (itemId == R.id.mActivityMainCalculator) {
                fragment = new CalculateFragment();
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(binding.fcvActivityMain.getId(), fragment)
                        .commit();
                return true;
            }
            return false;
        });

        // Set the default fragment to DashboardFragment with email
        if (savedInstanceState == null) {
            Fragment defaultFragment = new DashboardFragment();
            Bundle bundle = new Bundle();
            bundle.putString("USER_EMAIL", userEmail);
            defaultFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(binding.fcvActivityMain.getId(), defaultFragment)
                    .commit();
        }
    }
}