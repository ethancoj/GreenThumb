package com.plants

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.plants.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bnvActivityMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mActivityMainInformations -> InformationsFragment()
                R.id.mActivityMainGrowth -> GrowthFragment()
                R.id.mActivityMainHome -> MainFragment()
                R.id.mActivityMainProfile -> MainFragment()
                else -> null
            }?.let {
                supportFragmentManager.commit { replace(binding.fcvActivityMain.id, it) }
                true
            } ?: run { false }
        }
    }
}