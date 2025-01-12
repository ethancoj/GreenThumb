package com.plants;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DashboardFragment extends Fragment {
    private ConstraintLayout rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_dashboard, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve email from arguments
        TextView emailTextView = view.findViewById(R.id.TVEmail);
        TextView usernameTextView = view.findViewById(R.id.TVUsername);

        String userEmail = null;
        if (getActivity() != null) {
            userEmail = ((MainActivity) getActivity()).getUserEmail();
        }

        if (userEmail != null && !userEmail.isEmpty()) {
            emailTextView.setText(userEmail);
            String username = extractUsername(userEmail);
            usernameTextView.setText(username);
        } else if (getArguments() != null) {
            userEmail = getArguments().getString("USER_EMAIL");
            if (userEmail != null) {
                emailTextView.setText(userEmail);
                String username = extractUsername(userEmail);
                usernameTextView.setText(username);
            }
        }

        // Growth Tracker - Navigate using bottom nav
        view.findViewById(R.id.BtnGrowthTracker).setOnClickListener(v -> {
            requireActivity().findViewById(R.id.mActivityMainGrowth).performClick();
        });

        // Plant Information - Navigate using bottom nav
        view.findViewById(R.id.BtnPlantInformation).setOnClickListener(v -> {
            requireActivity().findViewById(R.id.mActivityMainInformations).performClick();
        });

        // Sustainable Living Guide - Navigate using bottom nav
        view.findViewById(R.id.BtnSustainableLivingGuide).setOnClickListener(v -> {
            requireActivity().findViewById(R.id.mActivityMainGuide).performClick();
        });

        // Carbon Footprint Calculator - Navigate using bottom nav
        view.findViewById(R.id.BtnCarbonFootprintCalcu).setOnClickListener(v -> {
            requireActivity().findViewById(R.id.mActivityMainCalculator).performClick();
        });

        // Logout functionality
        view.findViewById(R.id.BtnLogOut).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
        });

        view.findViewById(R.id.BtnEditAccount).setOnClickListener(v -> {
            // Placeholder for future implementation
        });

        view.findViewById(R.id.BtnNotification).setOnClickListener(v -> {
            // Placeholder for future implementation
        });

        view.findViewById(R.id.BtnHelpCentre).setOnClickListener(v -> {
            // Placeholder for future implementation
        });

        view.findViewById(R.id.BtnFeedback).setOnClickListener(v -> {
            // Placeholder for future implementation
        });

        view.findViewById(R.id.BtnAboutUs).setOnClickListener(v -> {
            // Placeholder for future implementation
        });
    }

    // Helper method to extract username from email
    private String extractUsername(String email) {
        if (email != null && email.contains("@")) {
            // Split the email at the "@" symbol and return the first part
            return email.split("@")[0];
        }
        return "";
    }
}