package com.example.bloodbank.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bloodbank.R;
import com.example.bloodbank.UserHelperClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView userName,password;
    TextView createAccount;
    Button login;
    ProgressBar progressBar;
    private ImageView passView;
    private Boolean isView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName=findViewById(R.id.et_username);
        password=findViewById(R.id.et_password);
        login=findViewById(R.id.btn_login);
        createAccount=findViewById(R.id.btn_createAccount);
        progressBar=findViewById(R.id.pg);
        passView = findViewById(R.id.passView);
        isView = false;


        passView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isView){
                    passView.setImageResource(R.drawable.password);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isView = false;
                } else {
                    passView.setImageResource(R.drawable.password_2);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isView = true;
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() | !validatePassword())
                {
                    return;
                }
                else
                {
                    isUser();
                }
            }

        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void isUser() {
        String userEnteredUsername = userName.getText().toString().trim();
        String userEnteredPassword = password.getText().toString().trim();

        FirebaseDatabase.getInstance().getReference().child("UserDetails").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                userName.setError(null );
                password.setError(null);
                if (userEnteredUsername.contains("@"))
                {
                    if((snapshot.child("emailId").getValue().toString().equals(userEnteredUsername) ||
                            snapshot.getKey().toString().equals(userEnteredUsername)))
                    {
                        userName.setError(null);
                        if (snapshot.child("password").getValue().toString().equals(userEnteredPassword)) {
                            password.setError(null);
                            UserHelperClass.setCurrentUser_username(snapshot.getKey().toString());
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            password.setError("Wrong Password");
                        }
                    }
                    else
                    {
                        userName.setError("Invalid Email-Id");
                    }
                }
                else
                {
                    if((snapshot.child("mobileNo").getValue().toString().equals(userEnteredUsername)))
                    {
                        userName.setError(null);
                        if (snapshot.child("password").getValue().toString().equals(userEnteredPassword)) {
                            password.setError(null);
                            UserHelperClass.setCurrentUser_username(snapshot.getKey().toString());
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            password.setError("Wrong Password");
                        }
                    }
                    else
                    {
                        userName.setError("Invalid Mobile No");
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validateUsername() {
        String value=userName.getText().toString();
        if (value.isEmpty())
        {
            userName.setError("Field  cannot be empty");
//
            return false;
        }
        else
        {
            userName.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String value=password.getText().toString();
        if (value.isEmpty())
        {
           password.setError("Field  cannot be empty");
            return false;
        }
        else
        {
            password.setError(null);
            return true;
        }
    }

    class Upload extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            isUser();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);

            super.onPostExecute(aVoid);
        }
    }
}