package com.example.audibook.Screens;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.audibook.MainActivity;
import com.example.audibook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    TextView nameText, emailText, createdOnText, roleText;
    static String UID = "";

    Button activateBtn, deactivateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            UID = extra.getString("userId");
        }

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        createdOnText = findViewById(R.id.createdOnText);
        roleText = findViewById(R.id.roleText);
        activateBtn = findViewById(R.id.activateBtn);
        deactivateBtn = findViewById(R.id.deactivateBtn);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileActivity.super.onBackPressed();
            }
        });

        MainActivity.db.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    nameText.setText(snapshot.child("name").getValue().toString().trim());
                    emailText.setText(snapshot.child("email").getValue().toString().trim());
                    createdOnText.setText(snapshot.child("created_on").getValue().toString().trim());
                    roleText.setText(snapshot.child("role").getValue().toString().trim());
                    String status = snapshot.child("status").getValue().toString().trim();
                    if(status.equals("1")){
                        deactivateBtn.setVisibility(View.VISIBLE);
                    } else if(status.equals("0")) {
                        activateBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}