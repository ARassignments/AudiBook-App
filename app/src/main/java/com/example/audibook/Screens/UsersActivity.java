package com.example.audibook.Screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static String UID = "";
    ListView listView;
    LinearLayout loader;
    EditText searchInput;
    ArrayList<UsersModel> dataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_users);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView = findViewById(R.id.listView);
        loader = findViewById(R.id.loader);
        searchInput = findViewById(R.id.searchInput);
        sharedPreferences = getSharedPreferences("myData",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        UID = sharedPreferences.getString("UID","").toString();

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

        MainActivity.db.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    dataList.clear();
                    for(DataSnapshot dd : snapshot.getChildren()){
                        if(!dd.getKey().equals(UID)){
                            if(dd.child("role").getValue().toString().equals("user")){
                                dataList.add(new UsersModel(dd.getKey().toString(),
                                        dd.child("name").getValue().toString(),
                                        dd.child("email").getValue().toString(),
                                        dd.child("image").getValue().toString(),
                                        dd.child("role").getValue().toString(),
                                        dd.child("created_on").getValue().toString(),
                                        dd.child("status").getValue().toString()
                                ));
                            }
                        }
                    }
                    loader.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    MyAdapter adapter = new MyAdapter(UsersActivity.this,dataList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UsersActivity.this, android.R.layout.simple_list_item_1,dataList);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsersActivity.super.onBackPressed();
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
                MainActivity.db.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            dataList.clear();
                            for(DataSnapshot dd : snapshot.getChildren()){
                                if(!dd.getKey().equals(UID)){
                                    if(dd.child("role").getValue().toString().equals("user")){
                                        dataList.add(new UsersModel(dd.getKey().toString(),
                                                dd.child("name").getValue().toString(),
                                                dd.child("email").getValue().toString(),
                                                dd.child("image").getValue().toString(),
                                                dd.child("role").getValue().toString(),
                                                dd.child("created_on").getValue().toString(),
                                                dd.child("status").getValue().toString()
                                        ));
                                    }
                                }
                            }
                            loader.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            MyAdapter adapter = new MyAdapter(UsersActivity.this,dataList);
                            listView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else {
                MainActivity.db.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            dataList.clear();
                            for(DataSnapshot dd : snapshot.getChildren()){
                                if(!dd.getKey().equals(UID)){
                                    if(dd.child("name").getValue().toString().toLowerCase().contains(input.toLowerCase())&&dd.child("role").getValue().toString().equals("user")){
                                        dataList.add(new UsersModel(dd.getKey().toString(),
                                                dd.child("name").getValue().toString(),
                                                dd.child("email").getValue().toString(),
                                                dd.child("image").getValue().toString(),
                                                dd.child("role").getValue().toString(),
                                                dd.child("created_on").getValue().toString(),
                                                dd.child("status").getValue().toString()
                                        ));
                                    }
                                }
                            }
                            loader.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            MyAdapter adapter = new MyAdapter(UsersActivity.this,dataList);
                            listView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
    }


    // Custom Adapter
    class MyAdapter extends BaseAdapter{

        Context context;
        ArrayList<UsersModel> data;

        public MyAdapter(Context context, ArrayList<UsersModel> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View customListItem = LayoutInflater.from(context).inflate(R.layout.users_custom_listview,null);
            LinearLayout listItem;
            TextView sno, name, status;
            CircleImageView image;

            listItem = customListItem.findViewById(R.id.listItem);
            sno = customListItem.findViewById(R.id.sno);
            name = customListItem.findViewById(R.id.name);
            status = customListItem.findViewById(R.id.status);
            image = customListItem.findViewById(R.id.image);

            sno.setText(""+(i+1));
            name.setText(data.get(i).getName());
            if(data.get(i).getStatus().equals("1")){
                status.setText("Activated");
            } else if(data.get(i).getStatus().equals("0")){
                status.setText("Deactivated");
            }

            listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,UserProfileActivity.class);
                    intent.putExtra("userId",data.get(i).getId());
                    startActivity(intent);
                }
            });

            return customListItem;
        }
    }
}