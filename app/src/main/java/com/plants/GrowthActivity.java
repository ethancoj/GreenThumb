package com.plants;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.plants.databinding.ActivityGrowthBinding;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

        binding.mtActivityGrowth.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menuEdit) {
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
            // New plant
            binding.layoutViewMode.setVisibility(View.GONE);
            binding.layoutEditMode.setVisibility(View.VISIBLE);
            binding.tvHistoryTitle.setVisibility(View.GONE);
            binding.rvGrowthHistory.setVisibility(View.GONE);
        }
    }

    private void loadGrowthHistory() {
        if (growth != null && growth.getId() != null) {
            Model.getGrowth()
                    .document(growth.getId())
                    .collection("history")
                    .orderBy("timestamp")
                    .addSnapshotListener((snapshot, e) -> {
                        if (snapshot != null) {
                            historyAdapter.updateHistory(snapshot.toObjects(Growth.class));
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

        binding