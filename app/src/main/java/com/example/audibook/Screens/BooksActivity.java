package com.example.audibook.Screens;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.audibook.MainActivity;
import com.example.audibook.Models.UsersModel;
import com.example.audibook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BooksActivity extends AppCompatActivity {
    GridView gridView;
    LinearLayout loader;
    EditText searchInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_books);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gridView = findViewById(R.id.gridView);
        loader = findViewById(R.id.loader);
        searchInput = findViewById(R.id.searchInput);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchValidation();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BooksActivity.super.onBackPressed();
            }
        });
    }

    public void searchValidation(){
        String input = searchInput.getText().toString().trim();
        String regex = "^[a-zA-Z\\s]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(!matcher.matches()){
            searchInput.setError("Only text allowed!!!");
        } else {
            searchInput.setError(null);
            if(input.isEmpty()){
//                MainActivity.db.child("Users").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            dataList.clear();
//                            for(DataSnapshot dd : snapshot.getChildren()){
//                                if(!dd.getKey().equals(UID)){
//                                    if(dd.child("role").getValue().toString().equals("user")){
//                                        dataList.add(new UsersModel(dd.getKey().toString(),
//                                                dd.child("name").getValue().toString(),
//                                                dd.child("email").getValue().toString(),
//                                                dd.child("image").getValue().toString(),
//                                                dd.child("role").getValue().toString(),
//                                                dd.child("created_on").getValue().toString(),
//                                                dd.child("status").getValue().toString()
//                                        ));
//                                    }
//                                }
//                            }
//                            loader.setVisibility(View.GONE);
//                            listView.setVisibility(View.VISIBLE);
//                            UsersActivity.MyAdapter adapter = new UsersActivity.MyAdapter(UsersActivity.this,dataList);
//                            listView.setAdapter(adapter);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

            } else {
//                MainActivity.db.child("Users").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            dataList.clear();
//                            for(DataSnapshot dd : snapshot.getChildren()){
//                                if(!dd.getKey().equals(UID)){
//                                    if(dd.child("name").getValue().toString().toLowerCase().contains(input.toLowerCase())&&dd.child("role").getValue().toString().equals("user")){
//                                        dataList.add(new UsersModel(dd.getKey().toString(),
//                                                dd.child("name").getValue().toString(),
//                                                dd.child("email").getValue().toString(),
//                                                dd.child("image").getValue().toString(),
//                                                dd.child("role").getValue().toString(),
//                                                dd.child("created_on").getValue().toString(),
//                                                dd.child("status").getValue().toString()
//                                        ));
//                                    }
//                                }
//                            }
//                            loader.setVisibility(View.GONE);
//                            listView.setVisibility(View.VISIBLE);
//                            UsersActivity.MyAdapter adapter = new UsersActivity.MyAdapter(UsersActivity.this,dataList);
//                            listView.setAdapter(adapter);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

            }
        }
    }
}