package com.plants;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FeedbackActivity extends AppCompatActivity {

    private TextView txtHeader;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);
        RatingBar RateBarFeedback = findViewById(R.id.RateBarFeedback);
        TextView TVRating = findViewById(R.id.TVRating);
        EditText ETFeedback = findViewById(R.id.ETFeedback);
        Button BtnFeedback = findViewById(R.id.BtnFeedback);

        BtnFeedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                String message = "Thank you for your feedback!";
                if (!ETFeedback.getText().toString().isEmpty())
                    message = message;
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        RateBarFeedback.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                TVRating.setText("You have rated " + rating);
            }
        });





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        txtHeader = findViewById(R.id.txtHeader);
        btnBack = findViewById(R.id.btnBack);
        // Back button action
        btnBack.setOnClickListener(v -> finish());
    }
}