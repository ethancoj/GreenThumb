package com.plants;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.plants.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bnvActivityMain.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.mActivityMainInformations) {
                fragment = new InformationsFragment();
            } else if (itemId == R.id.mActivityMainGrowth) {
                fragment = new GrowthFragment();
            } else if (itemId == R.id.mActivityMainHome) {
                fragment = new MainFragment();
            } else if (itemId == R.id.mActivityMainProfile) {
                fragment = new MainFragment();
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(binding.fcvActivityMain.getId(), fragment)
                        .commit();
                return true;
            }
            return false;
        });
    }
}