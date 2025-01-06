package com.plants

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.plants.databinding.ActivityInformationBinding

class InformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("information")?.also {
            getGson().fromJson(it, Information::class.java)?.also {
                binding.mtActivityInformation.subtitle = it.scientificName
                binding.mtActivityInformation.title = it.name
                binding.tvAdapterInformations.text = it.description
                binding.tvAdapterInformationsDescription.text = it.longDescription
            }
        }

        binding.mtActivityInformation.setNavigationOnClickListener { finish() }
    }
}