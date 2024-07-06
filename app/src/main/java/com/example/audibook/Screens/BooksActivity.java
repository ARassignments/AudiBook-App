package com.example.audibook.Screens;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.audibook.MainActivity;
import com.example.audibook.Models.BookModel;
import com.example.audibook.Models.UsersModel;
import com.example.audibook.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BooksActivity extends AppCompatActivity {
    GridView gridView;
    LinearLayout loader, notifyBar, notfoundContainer, searchContainer;
    EditText searchInput;
    TextView searchedWord, totalCount;
    FloatingActionButton addBtn;
    Uri imageUri;
    StorageReference mStorage;
    StorageTask uploadTask;
    ArrayList<BookModel> datalist = new ArrayList<>();

//    Dialog Elements
    Dialog addBookDialog;
    Button cancelBtn, addDataBtn;
    TextInputEditText nameInput, autherInput, ratingInput, summaryInput;
    TextInputLayout nameLayout, autherLayout, ratingLayout, summaryLayout;
    TextView imageErrTextView, title;
    ImageView addImageBtn, imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_books);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mStorage = FirebaseStorage.getInstance().getReference();

        gridView = findViewById(R.id.gridView);
        notifyBar = findViewById(R.id.notifyBar);
        notfoundContainer = findViewById(R.id.notfoundContainer);
        searchContainer = findViewById(R.id.searchContainer);
        loader = findViewById(R.id.loader);
        searchedWord = findViewById(R.id.searchedWord);
        totalCount = findViewById(R.id.totalCount);
        searchInput = findViewById(R.id.searchInput);
        addBtn = findViewById(R.id.addBtn);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchedWord.setText(searchInput.getText().toString().trim());
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
                bookForm("add","");
            }
        });

        fetchData("");
    }

    public void fetchData(String data){
        MainActivity.db.child("Books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    datalist.clear();
                    for (DataSnapshot ds: snapshot.getChildren()){
                        if(data.equals("")){
                            BookModel model = new BookModel(ds.getKey(),
                                    ds.child("name").getValue().toString(),
                                    ds.child("image").getValue().toString(),
                                    ds.child("auther").getValue().toString(),
                                    ds.child("rating").getValue().toString(),
                                    ds.child("summary").getValue().toString(),
                                    ds.child("bookcontent").getValue().toString(),
                                    ds.child("status").getValue().toString()
                            );
                            datalist.add(model);
                        } else {
                            if(ds.child("name").getValue().toString().trim().toLowerCase().contains(data.toLowerCase().trim())||ds.child("auther").getValue().toString().trim().toLowerCase().contains(data.toLowerCase().trim())){
                                BookModel model = new BookModel(ds.getKey(),
                                        ds.child("name").getValue().toString(),
                                        ds.child("image").getValue().toString(),
                                        ds.child("auther").getValue().toString(),
                                        ds.child("rating").getValue().toString(),
                                        ds.child("summary").getValue().toString(),
                                        ds.child("bookcontent").getValue().toString(),
                                        ds.child("status").getValue().toString()
                                );
                                datalist.add(model);
                            }
                        }

                    }
                    if(datalist.size() > 0){
                        loader.setVisibility(View.GONE);
                        gridView.setVisibility(View.VISIBLE);
                        searchContainer.setVisibility(View.GONE);
                        notfoundContainer.setVisibility(View.GONE);
                        Collections.reverse(datalist);
                        MyAdapter adapter = new MyAdapter(BooksActivity.this,datalist);
                        gridView.setAdapter(adapter);
                    } else {
                        gridView.setVisibility(View.GONE);
                        if(data.equals("")){
                            searchContainer.setVisibility(View.VISIBLE);
                        } else {
                            notfoundContainer.setVisibility(View.VISIBLE);
                        }
                    }
                    totalCount.setText(datalist.size()+" found");
                } else {
                    loader.setVisibility(View.GONE);
                    searchContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void bookForm(String purpose, String productId){
        addBookDialog = new Dialog(BooksActivity.this);
        addBookDialog.setContentView(R.layout.dialog_add_books);
        addBookDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addBookDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addBookDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        addBookDialog.getWindow().setGravity(Gravity.CENTER);
        addBookDialog.setCancelable(false);
        addBookDialog.setCanceledOnTouchOutside(false);
        cancelBtn = addBookDialog.findViewById(R.id.cancelBtn);
        addDataBtn = addBookDialog.findViewById(R.id.addDataBtn);
        addImageBtn = addBookDialog.findViewById(R.id.addImageBtn);
        imageView = addBookDialog.findViewById(R.id.imageView);
        imageErrTextView = addBookDialog.findViewById(R.id.imageErrTextView);
        title = addBookDialog.findViewById(R.id.title);
        nameInput = addBookDialog.findViewById(R.id.nameInput);
        autherInput = addBookDialog.findViewById(R.id.autherInput);
        ratingInput = addBookDialog.findViewById(R.id.ratingInput);
        summaryInput = addBookDialog.findViewById(R.id.summaryInput);
        nameLayout = addBookDialog.findViewById(R.id.nameLayout);
        autherLayout = addBookDialog.findViewById(R.id.autherLayout);
        ratingLayout = addBookDialog.findViewById(R.id.ratingLayout);
        summaryLayout = addBookDialog.findViewById(R.id.summaryLayout);

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
        ratingInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ratingValidation();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        summaryInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                summaryValidation();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 420);
            }
        });

        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(BooksActivity.this, "Image Upload In Process!!!", Toast.LENGTH_SHORT).show();
                } else {
                    if(purpose.equals("add")){
                        validation("false",purpose, productId);
                    } else if(purpose.equals("edit")){
                        validation("true",purpose, productId);
                    }
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBookDialog.dismiss();
            }
        });

        if(purpose.equals("edit")){
            title.setText("Edit Book");
            MainActivity.db.child("Books").child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Glide.with(BooksActivity.this).load(snapshot.child("image").getValue().toString().trim()).into(imageView);
                        nameInput.setText(snapshot.child("name").getValue().toString().trim());
                        autherInput.setText(snapshot.child("auther").getValue().toString().trim());
                        ratingInput.setText(snapshot.child("rating").getValue().toString().trim());
                        summaryInput.setText(snapshot.child("summary").getValue().toString().trim());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        addBookDialog.show();
    }

    public boolean nameValidation(){
        String input = nameInput.getText().toString().trim();
        String regex = "^[a-zA-Z\\s]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(input.equals("")){
            nameLayout.setError("Book Name is Required!!!");
            return false;
        } else if(input.length() < 6){
            nameLayout.setError("Book Name at least 6 Characters!!!");
            return false;
        } else if(!matcher.matches()){
            nameLayout.setError("Only text allowed!!!");
            return false;
        } else {
            nameLayout.setError(null);
            return true;
        }
    }

    public boolean autherValidation(){
        String input = autherInput.getText().toString().trim();
        String regex = "^[a-zA-Z\\s]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(input.equals("")){
            autherLayout.setError("Book Auther is Required!!!");
            return false;
        } else if(input.length() < 3){
            autherLayout.setError("Book Auther at least 3 Characters!!!");
            return false;
        } else if(!matcher.matches()){
            autherLayout.setError("Only text allowed!!!");
            return false;
        } else {
            autherLayout.setError(null);
            return true;
        }
    }

    public boolean ratingValidation(){
        String input = ratingInput.getText().toString().trim();
        String regex = "^[0-9.]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(input.equals("")){
            ratingLayout.setError("Book Rating is Required!!!");
            return false;
        } else if(!matcher.matches()){
            ratingLayout.setError("Only digits allowed!!!");
            return false;
        } else {
            ratingLayout.setError(null);
            return true;
        }
    }

    public boolean summaryValidation(){
        String input = summaryInput.getText().toString().trim();
        String regex = "^[a-zA-Z.,\\s]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(input.equals("")){
            summaryLayout.setError("Book Summary is Required!!!");
            return false;
        } else if(input.length() < 20){
            summaryLayout.setError("Book Summary at least 20 Characters!!!");
            return false;
        } else if(!matcher.matches()){
            summaryLayout.setError("Only text allowed!!!");
            return false;
        } else {
            summaryLayout.setError(null);
            return true;
        }
    }

    public boolean imageValidation(){
        if(imageUri == null){
            imageErrTextView.setText("Book Image is Required!!!");
            imageErrTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            imageErrTextView.setText("");
            imageErrTextView.setVisibility(View.GONE);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 420 && resultCode == RESULT_OK){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void validation(String imageStatus, String purpose, String productId) {
        boolean imageErr = false, nameErr = false, autherErr = false, ratingErr = false, summaryErr = false;
        nameErr = nameValidation();
        autherErr = autherValidation();
        ratingErr = ratingValidation();
        summaryErr = summaryValidation();
        if(imageStatus.equals("true")){
            imageErr = true;
        } else {
            imageErr = imageValidation();
        }
        if((nameErr && autherErr && ratingErr && summaryErr && imageErr) == true){
            book(purpose, productId);
        }
    }

    private void book(String purpose, String productId){
        if(MainActivity.connectionCheck(BooksActivity.this)){
            if(imageUri != null){
                Dialog loading = new Dialog(BooksActivity.this);
                loading.setContentView(R.layout.dialog_loading);
                loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                loading.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                loading.getWindow().setGravity(Gravity.CENTER);
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);
                TextView message = loading.findViewById(R.id.msgDialog);
                if(purpose.equals("edit")){
                    message.setText("Modifying...");
                } else {
                    message.setText("Creating...");
                }
                loading.show();
                uploadTask = mStorage.child("Images/"+System.currentTimeMillis()+"."+getFileExtension(imageUri)).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                loading.dismiss();
                                String photoLink = uri.toString();

                                Dialog alertdialog = new Dialog(BooksActivity.this);
                                alertdialog.setContentView(R.layout.dialog_success);
                                alertdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                alertdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertdialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                alertdialog.getWindow().setGravity(Gravity.CENTER);
                                alertdialog.setCancelable(false);
                                alertdialog.setCanceledOnTouchOutside(false);
                                TextView message = alertdialog.findViewById(R.id.msgDialog);
                                alertdialog.show();

                                if(purpose.equals("add")){
                                    HashMap<String, String> mydata = new HashMap<String, String>();
                                    mydata.put("image", "" + photoLink);
                                    mydata.put("name", nameInput.getText().toString().trim());
                                    mydata.put("auther", autherInput.getText().toString().trim());
                                    mydata.put("rating", ratingInput.getText().toString().trim());
                                    mydata.put("summary", summaryInput.getText().toString().trim());
                                    mydata.put("bookcontent", "");
                                    mydata.put("status", "1");
                                    MainActivity.db.child("Books").push().setValue(mydata);
                                    message.setText("Book Added Successfully!!!");
                                } else if(purpose.equals("edit")){
                                    MainActivity.db.child("Books").child(productId).child("image").setValue(photoLink);
                                    MainActivity.db.child("Books").child(productId).child("name").setValue(nameInput.getText().toString().trim());
                                    MainActivity.db.child("Books").child(productId).child("auther").setValue(autherInput.getText().toString().trim());
                                    MainActivity.db.child("Books").child(productId).child("rating").setValue(ratingInput.getText().toString().trim());
                                    MainActivity.db.child("Books").child(productId).child("summary").setValue(summaryInput.getText().toString().trim());
                                    message.setText("Book Edited Successfully!!!");
                                }

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        alertdialog.dismiss();
                                        addBookDialog.dismiss();
                                    }
                                },2000);

                            }
                        });
                    }
                });
            } else {
                Dialog alertdialog = new Dialog(BooksActivity.this);
                alertdialog.setContentView(R.layout.dialog_success);
                alertdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                alertdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertdialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                alertdialog.getWindow().setGravity(Gravity.CENTER);
                alertdialog.setCancelable(false);
                alertdialog.setCanceledOnTouchOutside(false);
                TextView message = alertdialog.findViewById(R.id.msgDialog);
                alertdialog.show();

                if(purpose.equals("edit")){
                    MainActivity.db.child("Books").child(productId).child("name").setValue(nameInput.getText().toString().trim());
                    MainActivity.db.child("Books").child(productId).child("auther").setValue(autherInput.getText().toString().trim());
                    MainActivity.db.child("Books").child(productId).child("rating").setValue(ratingInput.getText().toString().trim());
                    MainActivity.db.child("Books").child(productId).child("summary").setValue(summaryInput.getText().toString().trim());
                    message.setText("Book Edited Successfully!!!");
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertdialog.dismiss();
                        addBookDialog.dismiss();
                    }
                },2000);
            }
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
                notifyBar.setVisibility(View.GONE);
                fetchData("");
            } else {
                notifyBar.setVisibility(View.VISIBLE);
                fetchData(searchInput.getText().toString().trim());
            }
        }
    }

    class MyAdapter extends BaseAdapter{

        Context context;
        ArrayList<BookModel> data;

        public MyAdapter(Context context, ArrayList<BookModel> data) {
            this.context = context;
            this.data = data;
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View bookItem = LayoutInflater.from(context).inflate(R.layout.custom_book_listview,null);
            ImageView image, editBtn, deleteBtn;
            TextView name, autherName;
            image = bookItem.findViewById(R.id.image);
            editBtn = bookItem.findViewById(R.id.editBtn);
            deleteBtn = bookItem.findViewById(R.id.deleteBtn);
            name = bookItem.findViewById(R.id.name);
            autherName = bookItem.findViewById(R.id.autherName);
            Glide.with(context).load(data.get(i).getImage()).into(image);
            name.setText(data.get(i).getName());
            autherName.setText(data.get(i).getAuther());
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookForm("edit",""+data.get(i).getId());
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog loaddialog = new Dialog(context);
                    loaddialog.setContentView(R.layout.dialog_confirm);
                    loaddialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    loaddialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    loaddialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    loaddialog.getWindow().setGravity(Gravity.CENTER);
                    loaddialog.setCancelable(false);
                    loaddialog.setCanceledOnTouchOutside(false);
                    Button cancelBtn, yesBtn;
                    yesBtn = loaddialog.findViewById(R.id.yesBtn);
                    cancelBtn = loaddialog.findViewById(R.id.cancelBtn);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loaddialog.dismiss();
                        }
                    });
                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(MainActivity.connectionCheck(context)){
                                loaddialog.dismiss();
                                MainActivity.db.child("Books").child(data.get(i).getId()).removeValue();
                            }
                        }
                    });

                    loaddialog.show();
                }
            });
            return bookItem;
        }
    }
}