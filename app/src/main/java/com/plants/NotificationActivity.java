package com.plants;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NotificationActivity extends AppCompatActivity {
    private TextView txtHeader;
    private Button btnBack;
    private Switch switch1;
    private Switch switch2;
    private TimePicker timePicker;
    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String KEY_SWITCH1_STATE = "switch1_state";
    private static final String KEY_SWITCH2_STATE = "switch2_state";
    private static final String KEY_TIME_PICKER_HOUR = "time_picker_hour";
    private static final String KEY_TIME_PICKER_MINUTE = "time_picker_minute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtHeader = findViewById(R.id.TVHeader);
        btnBack = findViewById(R.id.btnBack);
        switch1 = findViewById(R.id.sNotification);
        switch2 = findViewById(R.id.sReminder);
        timePicker = findViewById(R.id.simpleTimePicker);

        // Load states
        loadStates();

        Button btnFeedback = findViewById(R.id.BtnSet);

        btnFeedback.setOnClickListener(v -> {
            String timeString = TimeUtils.getTimeStringFromTimePicker(timePicker);
            String message = "Reminder is set to " + timeString;
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });

        // Back button action
        btnBack.setOnClickListener(v -> finish());

        // Save states when they change
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> saveStates());
        switch2.setOnCheckedChangeListener((buttonView, isChecked) -> saveStates());
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> saveStates());
    }

    private void loadStates() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean switch1State = prefs.getBoolean(KEY_SWITCH1_STATE, false);
        boolean switch2State = prefs.getBoolean(KEY_SWITCH2_STATE, false);
        int savedHour = prefs.getInt(KEY_TIME_PICKER_HOUR, 0);
        int savedMinute = prefs.getInt(KEY_TIME_PICKER_MINUTE, 0);

        switch1.setChecked(switch1State);
        switch2.setChecked(switch2State);
        timePicker.setHour(savedHour);
        timePicker.setMinute(savedMinute);
    }

    private void saveStates() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(KEY_SWITCH1_STATE, switch1.isChecked());
        editor.putBoolean(KEY_SWITCH2_STATE, switch2.isChecked());
        editor.putInt(KEY_TIME_PICKER_HOUR, timePicker.getHour());
        editor.putInt(KEY_TIME_PICKER_MINUTE, timePicker.getMinute());
        editor.apply();
    }
}