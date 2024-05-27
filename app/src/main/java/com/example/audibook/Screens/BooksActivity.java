package com.example.audibook.Screens;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.audibook.MainActivity;
import com.example.audibook.Models.UsersModel;
import com.example.audibook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BooksActivity extends AppCompatActivity {
    GridView gridView;
    LinearLayout loader;
    EditText searchInput;
    FloatingActionButton addBtn;

//    Dialog Elements
    Button cancelBtn, addDataBtn;
    EditText nameInput, autherInput, ratingInput, summaryInput;
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
        addBtn = findViewById(R.id.addBtn);

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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(BooksActivity.this);
                dialog.setContentView(R.layout.dialog_add_books);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
                cancelBtn = dialog.findViewById(R.id.cancelBtn);
                addDataBtn = dialog.findViewById(R.id.addDataBtn);
                nameInput = dialog.findViewById(R.id.nameInput);
                autherInput = dialog.findViewById(R.id.autherInput);
                ratingInput = dialog.findViewById(R.id.ratingInput);
                summaryInput = dialog.findViewById(R.id.summaryInput);
                dialog.show();

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
                autherInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        autherValidation();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                addDataBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    public boolean nameValidation(){
        String input = nameInput.getText().toString().trim();
        String regex = "^[a-zA-Z\\s]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(input.equals("")){
            nameInput.setError("Book Name is Required!!!");
            return false;
        } else if(input.length() < 6){
            nameInput.setError("Book Name at least 6 Characters!!!");
            return false;
        } else if(!matcher.matches()){
            nameInput.setError("Only text allowed!!!");
            return false;
        } else {
            nameInput.setError(null);
            return true;
        }
    }

    public boolean autherValidation(){
        String input = autherInput.getText().toString().trim();
        String regex = "^[a-zA-Z\\s]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(input.equals("")){
            autherInput.setError("Book Auther is Required!!!");
            return false;
        } else if(input.length() < 3){
            autherInput.setError("Book Auther at least 3 Characters!!!");
            return false;
        } else if(!matcher.matches()){
            autherInput.setError("Only text allowed!!!");
            return false;
        } else {
            autherInput.setError(null);
            return true;
        }
    }

    public boolean ratingValidation(){
        String input = ratingInput.getText().toString().trim();
        String regex = "^[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(input.equals("")){
            autherInput.setError("Book Auther is Required!!!");
            return false;
        } else if(input.length() < 3){
            autherInput.setError("Book Auther at least 3 Characters!!!");
            return false;
        } else if(!matcher.matches()){
            autherInput.setError("Only text allowed!!!");
            return false;
        } else {
            autherInput.setError(null);
            return true;
        }
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