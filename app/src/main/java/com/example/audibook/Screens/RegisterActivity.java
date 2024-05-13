package com.example.audibook.Screens;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.audibook.R;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText emailInput, pwdInput;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        emailInput = findViewById(R.id.emailInput);
        pwdInput = findViewById(R.id.pwdInput);
        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailInput.getText().toString().equals("")){
                    emailInput.setError("Email Address is required!!!");
                } else {
                    emailInput.setError(null);
                }

                if(pwdInput.getText().toString().equals("")){
                    pwdInput.setError("Password is required!!!");
                } else {
                    pwdInput.setError(null);
                }

                if(!(emailInput.getText().toString().equals("")&&pwdInput.getText().toString().equals(""))){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(emailInput.getText().toString(),pwdInput.getText().toString());
                }
            }
        });

        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.super.onBackPressed();
            }
        });
    }
}