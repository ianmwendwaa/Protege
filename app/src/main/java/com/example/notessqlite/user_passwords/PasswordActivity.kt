package com.example.notessqlite.user_passwords

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.databases.PasswordDatabase
import com.example.notessqlite.databinding.ActivityPasswordBinding

class PasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordBinding
    private lateinit var db: PasswordDatabase
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        db = PasswordDatabase(this)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
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
            val password = Password(myPassword)
            if (myPassword.length<4){
                Toast.makeText(this,"Password should be at least 4 characters!", Toast.LENGTH_SHORT).show()
            }else{
                db.createPassword(password)
                Toast.makeText(this,"Password created successfully",Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}

