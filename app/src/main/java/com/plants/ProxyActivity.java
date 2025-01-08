package com.plants;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ProxyActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}