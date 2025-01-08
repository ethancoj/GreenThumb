package com.plants;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.plants.databinding.ActivityInformationBinding;

import java.util.List;

public class InformationActivity extends AppCompatActivity {
    private static final String TAG = "InformationActivity";
    private ActivityInformationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String informationJson = getIntent().getStringExtra("information");
        if (informationJson != null) {
            Log.d(TAG, "Received complete JSON: " + informationJson);
            Information information = Utils.getGson().fromJson(informationJson, Information.class);
            if (information != null) {
                setupPlantInformation(information);
            }
        }

        binding.mtActivityInformation.setNavigationOnClickListener(v -> finish());
    }

    private void setupPlantInformation(Information information) {
        binding.mtActivityInformation.setSubtitle(information.getScientificName());
        binding.mtActivityInformation.setTitle(information.getName());
        binding.tvAdapterInformations.setText(information.getDescription());


        // Set long description
        if (information.getLongDescription() != null && !information.getLongDescription().isEmpty()) {
            binding.tvAdapterInformationsDescription.setText(information.getLongDescription());
        }

        // Set plant image
        String plantName = information.getName().toLowerCase().replace(" ", "_");
        try {
            int imageResourceId = getResources().getIdentifier(
                    "img_" + plantName,
                    "drawable",
                    getPackageName()
            );
            if (imageResourceId != 0) {
                binding.ivPlantImage.setImageResource(imageResourceId);
            } else {
                binding.ivPlantImage.setImageResource(R.drawable.ic_nature_24dp);
            }
        } catch (Exception e) {
            binding.ivPlantImage.setImageResource(R.drawable.ic_nature_24dp);
        }

        // Set special features
        if (information.getSpecialFeatures() != null && !information.getSpecialFeatures().isEmpty()) {
            StringBuilder features = new StringBuilder();
            for (String feature : information.getSpecialFeatures()) {
                features.append("• ").append(feature).append("\n");
            }
            // Remove last newline
            if (features.length() > 0) {
                features.setLength(features.length() - 1);
            }
            binding.llSpecialFeatures.setText(features.toString());
        }

        // Set care tips
        if (information.getCareTips() != null && !information.getCareTips().isEmpty()) {
            StringBuilder tips = new StringBuilder();
            if (information.getCareTips().containsKey("light")) {
                tips.append("• Required Light: ").append(information.getCareTips().get("light")).append("\n");
            }
            if (information.getCareTips().containsKey("water")) {
                tips.append("• ").append(information.getCareTips().get("water")).append("\n");
            }
            if (information.getCareTips().containsKey("temperature")) {
                tips.append("• ").append(information.getCareTips().get("temperature"));
            }
            binding.llPlantCareTips.setText(tips.toString());
        }
    }
}