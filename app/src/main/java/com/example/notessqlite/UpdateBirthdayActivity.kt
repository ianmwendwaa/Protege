package com.example.notessqlite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notessqlite.databinding.ActivityUpdateBirthdayBinding

class UpdateBirthdayActivity : AppCompatActivity(){
    private lateinit var binding:ActivityUpdateBirthdayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateBirthdayBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}