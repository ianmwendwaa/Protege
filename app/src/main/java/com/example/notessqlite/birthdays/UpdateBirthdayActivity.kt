package com.example.notessqlite.birthdays

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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