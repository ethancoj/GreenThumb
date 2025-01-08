package com.plants;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.plants.databinding.ActivityGrowthBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GrowthActivity extends AppCompatActivity {
    private ActivityGrowthBinding binding;
    private GrowthHistoryAdapter historyAdapter;
    private Growth growth;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<PickVisualMediaRequest> photoPicker =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    Glide.with(this)
                            .load(uri)
                            .centerCrop()
                            .into(binding.ivGrowthImageEdit);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrowthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHistoryRecyclerView();
        setupClickListeners();
        loadGrowthData();
    }

    private void setupHistoryRecyclerView() {
        historyAdapter = new GrowthHistoryAdapter();
        binding.rvGrowthHistory.setAdapter(historyAdapter);
        binding.rvGrowthHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        binding.mtActivityGrowth.setNavigationOnClickListener(v -> finish());

        binding.mtActivityGrowth.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menuEdit) {
                switchToEditMode();
                return true;
            }
            return false;
        });

        binding.btnSelectImage.setOnClickListener(v ->
                photoPicker.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build())
        );

        binding.btnSaveGrowth.setOnClickListener(v -> saveGrowthData());
    }

    private void loadGrowthData() {
        String growthJson = getIntent().getStringExtra("growth");
        if (growthJson != null) {
            growth = Utils.getGson().fromJson(growthJson, Growth.class);
            updateViewMode();
            loadGrowthHistory();
        } else {
            binding.layoutViewMode.setVisibility(View.GONE);
            binding.layoutEditMode.setVisibility(View.VISIBLE);
            binding.tvHistoryTitle.setVisibility(View.GONE);
            binding.rvGrowthHistory.setVisibility(View.GONE);
        }
    }

    private void loadGrowthHistory() {
        if (growth != null && growth.getId() != null) {
            Model.getGrowth().document(growth.getId()).collection("history")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .addSnapshotListener((snapshot, e) -> {
                        if (snapshot != null) {
                            List<Growth> historyList = new ArrayList<>();
                            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                                Growth historyGrowth = doc.toObject(Growth.class);
                                if (historyGrowth != null) {
                                    historyGrowth.setId(doc.getId());
                                    historyList.add(historyGrowth);
                                }
                            }
                            historyAdapter.updateHistory(historyList);
                        }
                    });
        }
    }

    private void updateViewMode() {
        binding.tvGrowthName.setText(growth.getPlantName());
        binding.tvGrowthHeight.setText(String.format("%d cm", (int) growth.getHeight()));
        binding.tvGrowthLeafCount.setText(String.valueOf(growth.getLeafCount()));

        if (growth.getImagePath() != null && !growth.getImagePath().isEmpty()) {
            Glide.with(this)
                    .load(new File(growth.getImagePath()))
                    .centerCrop()
                    .into(binding.ivGrowthImage);
        }

        binding.layoutViewMode.setVisibility(View.VISIBLE);
        binding.layoutEditMode.setVisibility(View.GONE);
    }

    private void switchToEditMode() {
        binding.etGrowthName.setText(growth.getPlantName());
        binding.etGrowthHeight.setText(String.valueOf(growth.getHeight()));
        binding.etGrowthLeafCount.setText(String.valueOf(growth.getLeafCount()));

        if (growth.getImagePath() != null && !growth.getImagePath().isEmpty()) {
            Glide.with(this)
                    .load(new File(growth.getImagePath()))
                    .centerCrop()
                    .into(binding.ivGrowthImageEdit);
        }

        binding.layoutViewMode.setVisibility(View.GONE);
        binding.layoutEditMode.setVisibility(View.VISIBLE);
    }

    private void saveGrowthData() {
        String plantName = binding.etGrowthName.getText().toString();
        double height = 0;
        try {
            height = Double.parseDouble(binding.etGrowthHeight.getText().toString());
        } catch (NumberFormatException e) {
            // Use default value of 0
        }
        int leafCount = 0;
        try {
            leafCount = Integer.parseInt(binding.etGrowthLeafCount.getText().toString());
        } catch (NumberFormatException e) {
            // Use default value of 0
        }

        if (!plantName.isEmpty()) {
            String imagePath = growth != null ? growth.getImagePath() : "";
            if (selectedImageUri != null) {
                imagePath = saveImageLocally(selectedImageUri);
                if (growth != null && growth.getImagePath() != null && !growth.getImagePath().isEmpty()) {
                    new File(growth.getImagePath()).delete();
                }
            }

            final String finalImagePath = imagePath;

            if (growth != null && growth.getId() != null) {
                // Save current state to history
                Growth historyGrowth = new Growth(
                        growth.getPlantName(),
                        growth.getHeight(),
                        growth.getLeafCount(),
                        growth.getImagePath()
                );

                Model.getGrowth().document(growth.getId())
                        .collection("history")
                        .add(historyGrowth);

                // Update main document
                Map<String, Object> updates = new HashMap<>();
                updates.put("plantName", plantName);
                updates.put("height", height);
                updates.put("leafCount", leafCount);
                updates.put("imagePath", finalImagePath);

                Model.getGrowth().document(growth.getId())
                        .update(updates)
                        .addOnSuccessListener(aVoid -> finish());
            } else {
                // Add new growth
                Growth newGrowth = new Growth(plantName, height, leafCount, finalImagePath);
                Model.getGrowth().add(newGrowth)
                        .addOnSuccessListener(documentReference -> finish());
            }
        }
    }

    private String saveImageLocally(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                File imageFile = new File(getExternalFilesDir(null),
                        "growth_" + UUID.randomUUID().toString() + ".jpg");

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
                return imageFile.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isFinishing() && selectedImageUri != null && growth != null && growth.getImagePath() != null) {
            new File(growth.getImagePath()).delete();
        }
    }
}