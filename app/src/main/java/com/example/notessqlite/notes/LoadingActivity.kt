package com.example.notessqlite.notes

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.databinding.ActivityLoadingBinding

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding
   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       enableEdgeToEdge()
       binding = ActivityLoadingBinding.inflate(layoutInflater)
       setContentView(binding.root)
       Handler(Looper.getMainLooper()).postDelayed({
         finish()
       },5000)
   }
}