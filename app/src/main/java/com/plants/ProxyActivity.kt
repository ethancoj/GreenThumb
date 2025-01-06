package com.plants

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class ProxyActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        startActivity(Intent(this, MainActivity::class.java))
    }
}