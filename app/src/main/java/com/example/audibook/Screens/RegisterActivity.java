package com.example.audibook.Screens;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.audibook.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText nameInput, emailInput, pwdInput, cpwdInput;
    Button registerBtn;

    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        pwdInput = findViewById(R.id.pwdInput);
        cpwdInput = findViewById(R.id.cpwdInput);
        registerBtn = findViewById(R.id.registerBtn);
        loader = findViewById(R.id.loader);


        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameValidation();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailValidation();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pwdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pwdValidation();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cpwdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cpwdValidation();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.super.onBackPressed();
            }
        });
    }

    public boolean nameValidation(){
        String input = nameInput.getText().toString().trim();
        String regex = "^[a-zA-Z\\s]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(input.equals("")){
            nameInput.setError("Name is Required!!!");
            return false;
        } else if(input.length() < 3){
            nameInput.setError("Name at least 3 Characters!!!");
            return false;
        } else if(!matcher.matches()){
            nameInput.setError("Only text allowed!!!");
            return false;
        } else {
            nameInput.setError(null);
            return true;
        }
    }

    public boolean emailValidation(){
        String input = emailInput.getText().toString().trim();
        if(input.equals("")){
            emailInput.setError("Email Address is Required!!!");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(input).matches()){
            emailInput.setError("Enter Valid Email Address!!!");
            return false;
        } else {
            emailInput.setError(null);
            return true;
        }
    }

    public boolean pwdValidation(){
        String input = pwdInput.getText().toString().trim();
        if(input.equals("")){
            pwdInput.setError("Password is Required!!!");
            return false;
        } else if(input.length() < 8){
            pwdInput.setError("Password at least 8 Characters!!!");
            return false;
        } else {
            pwdInput.setError(null);
            return true;
        }
    }

    public boolean cpwdValidation(){
        String input = cpwdInput.getText().toString().trim();
        String pwd = pwdInput.getText().toString().trim();
        if(input.equals("")){
            cpwdInput.setError("Confirm Password is Required!!!");
            return false;
        } else if(input.length() < 8){
            cpwdInput.setError("Confirm Password at least 8 Characters!!!");
            return false;
        } else if(!input.equals(pwd)){
            cpwdInput.setError("Confirm Password is not matched!!!");
            return false;
        } else {
            cpwdInput.setError(null);
            return true;
        }
    }

    public void validation(){
        boolean nameErr = false, emailErr = false, pwdErr = false, cpwdErr = false;
        nameErr = nameValidation();
        emailErr = emailValidation();
        pwdErr = pwdValidation();
        cpwdErr = cpwdValidation();
        if((nameErr && emailErr && pwdErr && cpwdErr) == true){
            loader.setVisibility(View.VISIBLE);
            registerBtn.setVisibility(View.GONE);
            FirebaseAuth auth = FirebaseAuth.getInstance();
//            auth.createUserWithEmailAndPassword(emailInput.getText().toString(),pwdInput.getText().toString())
//                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                        @Override
//                        public void onSuccess(AuthResult authResult) {
//
//                        }
//                    });
        }
    }

}