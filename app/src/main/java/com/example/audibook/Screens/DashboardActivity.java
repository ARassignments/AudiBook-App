package com.example.audibook.Screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.audibook.MainActivity;
import com.example.audibook.R;
import com.example.audibook.Screens.Fragments.HomeFragment;
import com.example.audibook.Screens.Fragments.LibraryFragment;
import com.example.audibook.Screens.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static String UID = "";
    static String name, email, role, image, created_on;

    BottomNavigationView bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = getSharedPreferences("myData",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        bottomAppBar = findViewById(R.id.bottomAppBar);

        // Check Login Status By SharedPreferences
        if(!sharedPreferences.contains("loginStatus")){
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }

        if(!sharedPreferences.getString("UID","").equals("")){
            UID = sharedPreferences.getString("UID","").toString();
            MainActivity.db.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        name = snapshot.child("name").getValue().toString();
                        email = snapshot.child("email").getValue().toString();
                        image = snapshot.child("image").getValue().toString();
                        role = snapshot.child("role").getValue().toString();
                        created_on = snapshot.child("created_on").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        // Check User's Status
        MainActivity.checkStatus(DashboardActivity.this,UID);

        // Set Default Home Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.frame,new HomeFragment()).commit();

        // Add Listener On BottomAppBar Item Then Replace Fragment
        bottomAppBar.setOnItemSelectedListener(item -> {
            switch (item.getTitle().toString()){
                case "Home":
                    replaceFragment(new HomeFragment());
                    break;
                case "Search":
                    replaceFragment(new SearchFragment());
                    break;
                case "Library":
                    replaceFragment(new LibraryFragment());
                    break;
            }
            return true;
        });

    }

    // Replace Fragment
    public void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();
    }

    // User Getter
    public static String getName(){
        return name;
    }

    public static String getEmail(){
        return email;
    }

    public static String getImage(){
        return image;
    }

    public static String getRole(){
        return role;
    }

    public static String getCreatedOn(){
        return created_on;
    }
}