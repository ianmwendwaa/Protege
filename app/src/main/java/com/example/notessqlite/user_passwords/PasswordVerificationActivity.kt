package com.example.notessqlite.user_passwords

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.databases.PasswordDatabase
import com.example.notessqlite.databinding.ActivityPasswordVerificationBinding
import com.example.notessqlite.notes.AddNoteActivity

class PasswordVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordVerificationBinding
    private lateinit var db: PasswordDatabase
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        db = PasswordDatabase(this)
        binding = ActivityPasswordVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.zero.setOnClickListener {
            binding.tvFormula.text = (binding.tvFormula.text.toString()+ "0")
        }
        binding.un.setOnClickListener {
            binding.tvFormula.text = (binding.tvFormula.text.toString() + "1")
        }
        binding.deaux.setOnClickListener {
            binding.tvFormula.text = (binding.tvFormula.text.toString() + "2")
        }
        binding.trois.setOnClickListener {
            binding.tvFormula.text = (binding.tvFormula.text.toString() + "3")
        }
        binding.quatre.setOnClickListener {
            binding.tvFormula.text = (binding.tvFormula.text.toString() + "4")
        }
        binding.cinq.setOnClickListener {
            binding.tvFormula.text = (binding.tvFormula.text.toString() + "5")
        }
        binding.sois.setOnClickListener {
            binding.tvFormula.text = (binding.tvFormula.text.toString() + "6")
        }
        binding.sept.setOnClickListener {
            binding.tvFormula.text = (binding.tvFormula.text.toString() + "7")
        }
        binding.huit.setOnClickListener {
            binding.tvFormula.text = (binding.tvFormula.text.toString() + "8")
        }
        binding.neuf.setOnClickListener {
            binding.tvFormula.text = (binding.tvFormula.text.toString() + "9")
        }
        binding.bckspc.setOnClickListener {
            var str: String = binding.tvFormula.text.toString()
            if(str != ""){
                str = str.substring(0, str.length - 1)
                binding.tvFormula.text = str
            }
        }
        binding.done.setOnClickListener {
            val myPassword = binding.tvFormula.text.toString()
            val defPass = db.fetchPasswordRequest()
            if(myPassword.equals(defPass)){
                startActivity(Intent(this,AddNoteActivity::class.java))
            }else{
            }

        }

    }
}

