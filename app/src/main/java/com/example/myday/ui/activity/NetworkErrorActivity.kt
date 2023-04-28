package com.example.myday.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myday.databinding.ActivityNetworkErrorBinding
import com.example.myday.util.`object`.Network.isNetworkAvailable

class NetworkErrorActivity : AppCompatActivity() {

    private val binding: ActivityNetworkErrorBinding by lazy { ActivityNetworkErrorBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.btnUpdate.setOnClickListener {
            if (isNetworkAvailable(applicationContext)) {
                startActivity(Intent(applicationContext, SplashActivity::class.java))
            }
        }
        binding.btnFinishApp.setOnClickListener {
            finishAffinity()
        }
    }
}