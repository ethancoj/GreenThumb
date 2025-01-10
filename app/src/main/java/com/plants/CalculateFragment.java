package com.plants;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CalculateFragment extends Fragment {
    private EditText inputCommute, inputElectricity, inputFood, inputWaste;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind input views
        inputCommute = view.findViewById(R.id.inputCommute);
        inputElectricity = view.findViewById(R.id.inputElectricity);
        inputFood = view.findViewById(R.id.inputFood);
        inputWaste = view.findViewById(R.id.inputWaste);

        // Set calculate button click listener
        view.findViewById(R.id.btnEnterForResult).setOnClickListener(v -> calculateCarbonFootprint());
    }

    private void calculateCarbonFootprint() {
        try {
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

            // Pass the data to CalculateResultActivity
            Intent intent = new Intent(getActivity(), CalculateResultActivity.class);
            intent.putExtra("commuteCarbon", commuteCarbon);
            intent.putExtra("electricityCarbon", electricityCarbon);
            intent.putExtra("foodCarbon", foodCarbon);
            intent.putExtra("wasteCarbon", wasteCarbon);
            intent.putExtra("totalCarbonFootprint", totalCarbonFootprint);
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Please enter valid numbers in all fields.", Toast.LENGTH_SHORT).show();
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