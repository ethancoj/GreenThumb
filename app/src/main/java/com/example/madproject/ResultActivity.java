package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    private TextView txtHeader, txtCurrentCarbonFootprint, txtCommutePercentage, txtElectricityPercentage, txtFoodPercentage, txtWastePercentage, txtRecommendation, txtDate;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Bind views
        txtHeader = findViewById(R.id.txtHeader);
        txtCurrentCarbonFootprint = findViewById(R.id.txtCurrentCarbonFootprint);
        txtCommutePercentage = findViewById(R.id.txtCommutePercentage);
        txtElectricityPercentage = findViewById(R.id.txtElectricityPercentage);
        txtFoodPercentage = findViewById(R.id.txtFoodPercentage);
        txtWastePercentage = findViewById(R.id.txtWastePercentage);
        txtRecommendation = findViewById(R.id.txtRecommendation);
        txtDate = findViewById(R.id.txtDate); // Bind txtDate
        btnBack = findViewById(R.id.btnBack);

        // Get data from Intent
        Intent intent = getIntent();
        double commuteCarbon = intent.getDoubleExtra("commuteCarbon", 0.0);
        double electricityCarbon = intent.getDoubleExtra("electricityCarbon", 0.0);
        double foodCarbon = intent.getDoubleExtra("foodCarbon", 0.0);
        double wasteCarbon = intent.getDoubleExtra("wasteCarbon", 0.0);
        double totalCarbonFootprint = intent.getDoubleExtra("totalCarbonFootprint", 0.0);

        // Calculate percentages
        double commutePercentage = (commuteCarbon / totalCarbonFootprint) * 100;
        double electricityPercentage = (electricityCarbon / totalCarbonFootprint) * 100;
        double foodPercentage = (foodCarbon / totalCarbonFootprint) * 100;
        double wastePercentage = (wasteCarbon / totalCarbonFootprint) * 100;

        // Update header dynamically
        if (totalCarbonFootprint > 1500) {
            txtHeader.setText("Needs Improvement!");
            txtHeader.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else if (totalCarbonFootprint > 833) {
            txtHeader.setText("Good Effort!");
            txtHeader.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            txtHeader.setText("Great Job!");
            txtHeader.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

        // Update result information
        txtCurrentCarbonFootprint.setText(String.format("Monthly Carbon Footprint: %.2f kg CO2", totalCarbonFootprint));

        // Update percentage breakdown
        txtCommutePercentage.setText(String.format("Commute: %.2f%%", commutePercentage));
        txtElectricityPercentage.setText(String.format("Electricity: %.2f%%", electricityPercentage));
        txtFoodPercentage.setText(String.format("Food: %.2f%%", foodPercentage));
        txtWastePercentage.setText(String.format("Waste: %.2f%%", wastePercentage));

        // Generate recommendation
        generateRecommendation(commuteCarbon, electricityCarbon, foodCarbon, wasteCarbon);

        // Set current date
        String currentDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        txtDate.setText(String.format("Date: %s", currentDate));

        // Back button action
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(backIntent);
            finish();
        });
    }

    private void generateRecommendation(double commuteCarbon, double electricityCarbon, double foodCarbon, double wasteCarbon) {
        String recommendation;

        // Identify the highest contributor
        if (commuteCarbon > electricityCarbon && commuteCarbon > foodCarbon && commuteCarbon > wasteCarbon) {
            recommendation = "Try using public transport or carpooling to reduce your commute emissions.";
        } else if (electricityCarbon > foodCarbon && electricityCarbon > wasteCarbon) {
            recommendation = "Reduce electricity usage by turning off unused devices and using energy-efficient appliances.";
        } else if (foodCarbon > wasteCarbon) {
            recommendation = "Consider reducing meat consumption and opting for plant-based meals to lower food-related emissions.";
        } else {
            recommendation = "Try recycling and reducing waste to minimize your environmental impact.";
        }

        // Update recommendation TextView
        txtRecommendation.setText(recommendation);
    }
}
