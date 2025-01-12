package com.plants;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

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
    private boolean isImageChanged = false;
    private List<Growth> historyList = new ArrayList<>();

    private final ActivityResultLauncher<PickVisualMediaRequest> photoPicker =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    isImageChanged = true;
                    loadImage(uri);
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

    private void loadImage(Uri uri) {
        try {
            // Load image into both edit mode and view mode ImageViews
            Glide.with(this)
                    .load(uri)
                    .centerCrop()
                    .error(R.drawable.ic_error_24dp)
                    .into(binding.ivGrowthImageEdit);

            Glide.with(this)
                    .load(uri)
                    .centerCrop()
                    .error(R.drawable.ic_error_24dp)
                    .into(binding.ivGrowthImage);
        } catch (Exception e) {
            showError("Failed to load image: " + e.getMessage());
        }
    }

    private void setupHistoryRecyclerView() {
        historyAdapter = new GrowthHistoryAdapter();
        binding.rvGrowthHistory.setAdapter(historyAdapter);
        binding.rvGrowthHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        binding.mtActivityGrowth.setNavigationOnClickListener(v -> {
            if (hasUnsavedChanges()) {
                showUnsavedChangesDialog();
            } else {
                finish();
            }
        });

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

        binding.btnSaveGrowth.setOnClickListener(v -> validateAndSaveGrowthData());
    }

    private boolean hasUnsavedChanges() {
        if (binding.layoutEditMode.getVisibility() != View.VISIBLE) {
            return false;
        }

        String currentName = binding.etGrowthName.getText().toString();
        String currentHeight = binding.etGrowthHeight.getText().toString();
        String currentLeafCount = binding.etGrowthLeafCount.getText().toString();

        return isImageChanged ||
                (growth != null && (!currentName.equals(growth.getPlantName()) ||
                        !currentHeight.equals(String.valueOf(growth.getHeight())) ||
                        !currentLeafCount.equals(String.valueOf(growth.getLeafCount()))));
    }

    private void showUnsavedChangesDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Unsaved Changes")
                .setMessage("You have unsaved changes. Do you want to discard them?")
                .setPositiveButton("Discard", (dialog, which) -> finish())
                .setNegativeButton("Keep Editing", null)
                .show();
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
                    .orderBy("timestamp", Query.Direction.ASCENDING)  // Changed to ASCENDING for proper trends
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            showError("Error loading history: " + e.getMessage());
                            return;
                        }
                        if (snapshot != null) {
                            historyList.clear();
                            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                                Growth historyGrowth = doc.toObject(Growth.class);
                                if (historyGrowth != null) {
                                    historyGrowth.setId(doc.getId());
                                    historyList.add(historyGrowth);
                                }
                            }

                            // Add current growth as the latest record
                            historyList.add(growth);

                            // Calculate growth rates
                            calculateGrowthRates();

                            // Update history adapter
                            historyAdapter.updateHistory(historyList);

                            // Update trends visualization
                            updateTrendsVisualization();
                        }
                    });
        }
    }

    private void calculateGrowthRates() {
        if (historyList.size() < 2) {
            return;
        }

        for (int i = 1; i < historyList.size(); i++) {
            Growth current = historyList.get(i);
            Growth previous = historyList.get(i - 1);
            current.calculateGrowthRates(previous);
        }
    }

    private void updateTrendsVisualization() {
        if (historyList.size() >= 2) {
            // Show trends only if we have at least 2 measurements
            binding.trendContainer.setVisibility(View.VISIBLE);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            GrowthTrendsFragment trendsFragment = GrowthTrendsFragment.newInstance(historyList);
            transaction.replace(R.id.trendContainer, trendsFragment);
            transaction.commit();
        } else {
            binding.trendContainer.setVisibility(View.GONE);
        }
    }

    private void validateAndSaveGrowthData() {
        String plantName = binding.etGrowthName.getText().toString().trim();
        String heightStr = binding.etGrowthHeight.getText().toString().trim();
        String leafCountStr = binding.etGrowthLeafCount.getText().toString().trim();

        // Validate plant name
        if (TextUtils.isEmpty(plantName)) {
            binding.etGrowthName.setError("Plant name is required");
            binding.etGrowthName.requestFocus();
            return;
        }

        // Validate height
        double height;
        try {
            height = Double.parseDouble(heightStr);
            if (height <= 0 || height > 1000) { // Assuming max height is 1000cm
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            binding.etGrowthHeight.setError("Enter a valid height between 0 and 1000 cm");
            binding.etGrowthHeight.requestFocus();
            return;
        }

        // Validate leaf count
        int leafCount;
        try {
            leafCount = Integer.parseInt(leafCountStr);
            if (leafCount < 0 || leafCount > 1000) { // Assuming max leaf count is 1000
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            binding.etGrowthLeafCount.setError("Enter a valid leaf count between 0 and 1000");
            binding.etGrowthLeafCount.requestFocus();
            return;
        }

        // Show progress indicator
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnSaveGrowth.setEnabled(false);

        saveGrowthData(plantName, height, leafCount);
    }

    private void saveGrowthData(String plantName, double height, int leafCount) {
        String imagePath = growth != null ? growth.getImagePath() : "";

        if (selectedImageUri != null) {
            try {
                imagePath = saveImageLocally(selectedImageUri);
                if (growth != null && growth.getImagePath() != null && !growth.getImagePath().isEmpty()) {
                    new File(growth.getImagePath()).delete();
                }
            } catch (IOException e) {
                showError("Failed to save image: " + e.getMessage());
                return;
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
                    .add(historyGrowth)
                    .addOnSuccessListener(documentReference -> updateMainGrowthDocument(plantName, height, leafCount, finalImagePath))
                    .addOnFailureListener(e -> showError("Failed to save history: " + e.getMessage()));
        } else {
            // Add new growth
            Growth newGrowth = new Growth(plantName, height, leafCount, finalImagePath);
            Model.getGrowth().add(newGrowth)
                    .addOnSuccessListener(documentReference -> {
                        showSuccess("Growth record added successfully");
                        finish();
                    })
                    .addOnFailureListener(e -> showError("Failed to add growth: " + e.getMessage()));
        }
    }

    private void updateMainGrowthDocument(String plantName, double height, int leafCount, String imagePath) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("plantName", plantName);
        updates.put("height", height);
        updates.put("leafCount", leafCount);
        updates.put("imagePath", imagePath);

        Model.getGrowth().document(growth.getId())
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    showSuccess("Growth record updated successfully");
                    finish();
                })
                .addOnFailureListener(e -> showError("Failed to update growth: " + e.getMessage()));
    }

    private String saveImageLocally(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            throw new IOException("Failed to open input stream");
        }

        File imageFile = new File(getExternalFilesDir(null),
                "growth_" + UUID.randomUUID().toString() + ".jpg");

        try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return imageFile.getAbsolutePath();
        } finally {
            inputStream.close();
        }
    }

    private void showError(String message) {
        binding.progressBar.setVisibility(View.GONE);
        binding.btnSaveGrowth.setEnabled(true);
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG)
                .setAction("Dismiss", v -> {})
                .show();
    }

    private void showSuccess(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void updateViewMode() {
        binding.tvGrowthName.setText(growth.getPlantName());
        binding.tvGrowthHeight.setText(String.format("%d cm", (int)growth.getHeight()));
        binding.tvGrowthLeafCount.setText(String.valueOf(growth.getLeafCount()));

        if (growth.getImagePath() != null && !growth.getImagePath().isEmpty()) {
            File imageFile = new File(growth.getImagePath());
            if (imageFile.exists()) {
                Glide.with(this)
                        .load(imageFile)
                        .centerCrop()
                        .error(R.drawable.ic_error_24dp)
                        .into(binding.ivGrowthImage);
            }
        }

        binding.layoutViewMode.setVisibility(View.VISIBLE);
        binding.layoutEditMode.setVisibility(View.GONE);
    }

    private void switchToEditMode() {
        binding.etGrowthName.setText(growth.getPlantName());
        binding.etGrowthHeight.setText(String.valueOf(growth.getHeight()));
        binding.etGrowthLeafCount.setText(String.valueOf(growth.getLeafCount()));

        if (growth.getImagePath() != null && !growth.getImagePath().isEmpty()) {
            File imageFile = new File(growth.getImagePath());
            if (imageFile.exists()) {
                Glide.with(this)
                        .load(imageFile)
                        .centerCrop()
                        .error(R.drawable.ic_error_24dp)
                        .into(binding.ivGrowthImageEdit);
            }
        }

        binding.layoutViewMode.setVisibility(View.GONE);
        binding.layoutEditMode.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isFinishing() && selectedImageUri != null && growth != null && growth.getImagePath() != null) {
            new File(growth.getImagePath()).delete();
        }
    }
}