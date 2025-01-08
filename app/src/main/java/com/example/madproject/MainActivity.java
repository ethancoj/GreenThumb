package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText inputCommute, inputElectricity, inputFood, inputWaste;
    private Button btnEnterForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        inputCommute = findViewById(R.id.inputCommute);
        inputElectricity = findViewById(R.id.inputElectricity);
        inputFood = findViewById(R.id.inputFood);
        inputWaste = findViewById(R.id.inputWaste);
        btnEnterForResult = findViewById(R.id.btnEnterForResult);

        // Set button click listener
        btnEnterForResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCarbonFootprint();
            }
        });
    }

    private void calculateCarbonFootprint() {
        try {
            // Retrieve and parse inputs
            double commute = parseInput(inputCommute); // km per week
            double electricity = parseInput(inputElectricity); // RM per month
            double food = parseInput(inputFood); // meals per day
            double waste = parseInput(inputWaste); // kg per week

            // Calculate monthly carbon footprints
            double commuteCarbon = (commute * 4) * 0.12; // 4 weeks in a month, 0.12 kg CO2 per km
            double electricityCarbon = electricity * 0.85; // 0.85 kg CO2 per RM
            double foodCarbon = (food * 30) * 1.5; // 30 days in a month, 1.5 kg CO2 per meal
            double wasteCarbon = (waste * 4) * 0.2; // 4 weeks in a month, 0.2 kg CO2 per kg

            // Total carbon footprint for the month
            double totalCarbonFootprint = commuteCarbon + electricityCarbon + foodCarbon + wasteCarbon;

            // Pass the data to ResultActivity
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("commuteCarbon", commuteCarbon);
            intent.putExtra("electricityCarbon", electricityCarbon);
            intent.putExtra("foodCarbon", foodCarbon);
            intent.putExtra("wasteCarbon", wasteCarbon);
            intent.putExtra("totalCarbonFootprint", totalCarbonFootprint);
            startActivity(intent);

        } catch (NumberFormatException e) {
            // Show error message for invalid input
            Toast.makeText(MainActivity.this, "Please enter valid numbers in all fields.", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to parse input safely
    private double parseInput(EditText editText) throws NumberFormatException {
        String input = editText.getText().toString().trim();
        if (input.isEmpty()) {
            return 0.0; // Default value if input is empty
        }
        return Double.parseDouble(input);
    }
}
