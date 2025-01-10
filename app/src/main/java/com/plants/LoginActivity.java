package com.plants;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        createAccountButton = findViewById(R.id.create_account);

        // Set up login button click handler
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Since this is a dummy login, no validation needed
                startMainActivity();
            }
        });

        // Set up create account button click handler
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For now, just do the same as login
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        String email = emailInput.getText().toString(); // Retrieve email from EditTe
        intent.putExtra("USER_EMAIL", email); // Pass email to MainActivity

        startActivity(intent);
        finish(); // Close login activity
    }
}