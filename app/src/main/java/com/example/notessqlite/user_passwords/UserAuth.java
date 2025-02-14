package com.example.notessqlite.user_passwords;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notessqlite.CodeBase;
import com.example.notessqlite.R;
import com.example.notessqlite.databases.PasswordDatabase;
import com.example.notessqlite.databinding.ActivityPasswordBinding;

public class UserAuth extends AppCompatActivity {
    private ActivityPasswordBinding binding;
    private PasswordDatabase db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPasswordBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        db = new PasswordDatabase(this);

        binding.zero.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                binding.tvFormula.setText(binding.tvFormula.getText().toString()+"0");
            }
        });
        binding.un.setOnClickListener(v -> binding.tvFormula.setText(binding.tvFormula.getText().toString()+"1"));
        binding.deaux.setOnClickListener(v -> binding.tvFormula.setText(binding.tvFormula.getText().toString()+"2"));
        binding.trois.setOnClickListener(v -> binding.tvFormula.setText(binding.tvFormula.getText().toString()+"3"));
        binding.quatre.setOnClickListener(v -> binding.tvFormula.setText(binding.tvFormula.getText().toString()+"4"));
        binding.cinq.setOnClickListener(v -> binding.tvFormula.setText(binding.tvFormula.getText().toString()+"5"));
        binding.sois.setOnClickListener(v -> binding.tvFormula.setText(binding.tvFormula.getText().toString()+"6"));
        binding.sept.setOnClickListener(v -> binding.tvFormula.setText(binding.tvFormula.getText().toString()+"7"));
        binding.huit.setOnClickListener(v -> binding.tvFormula.setText(binding.tvFormula.getText().toString()+"8"));
        binding.neuf.setOnClickListener(v -> binding.tvFormula.setText(binding.tvFormula.getText().toString()+"9"));
        binding.bckspc.setOnClickListener(v -> {
            String str = binding.tvFormula.getText().toString();
            if (!str.isEmpty()){
                str = str.substring(0,str.length()-1);
                binding.tvFormula.setText(str);
            }
        });
        binding.done.setOnClickListener(v -> {
            String myPassword = binding.tvFormula.getText().toString();
            Password password = new Password(0,myPassword);
            if (myPassword.length()<4){
                CodeBase.showToast(UserAuth.this, "Password should be more than 4 digits", R.drawable.ic_info);
            }else{
                db.createPassword(password);
                CodeBase.showToast(UserAuth.this,"Password created successfully!",R.drawable.butterfly_effect);
                finish();
            }
        });


    }
}