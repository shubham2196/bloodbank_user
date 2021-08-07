package com.example.bloodbank.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.example.bloodbank.UserHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    Button submit;
    TextView et_mobileNo, createNewAccount;
    FirebaseAuth firebaseAuth;
    String otpId;
    private  Boolean check=false;
    ArrayList<String> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference().child("UserDetails").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                list.add(snapshot.child("mobileNo").getValue().toString());
//                            if ((snapshot.child("mobileNo").getValue().toString().equals(mobileNo))) {
//                                check=true;
////                                Intent intent = new Intent(getApplicationContext(), VerifyPhoneNo.class);
////                                intent.putExtra("phoneNo", mobileNo);
////                                startActivity(intent);
//                            }

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

        et_mobileNo = findViewById(R.id.et_mobileNo);
        submit = findViewById(R.id.submit);
        createNewAccount = findViewById(R.id.btn_createAccount);
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = et_mobileNo.getText().toString();

                if (mobileNo.isEmpty()) {
                    et_mobileNo.setError("Field cannot be empty");
                } else if (!(mobileNo.length() == 10)) {
                    et_mobileNo.setError("Invalid No");
                } else {
                    et_mobileNo.setError(null);
                    if (list.contains(mobileNo))
                    {
                        Intent intent = new Intent(getApplicationContext(), VerifyPhoneNo.class);
                        intent.putExtra("phoneNo", mobileNo);
                        startActivity(intent);
                    }
                    else
                        {
                                Toast.makeText(RegisterActivity.this, "You are not Valid user, Create Account", Toast.LENGTH_SHORT).show();
                                return;
                            }

                }
            }
        });

    }

//    private void generateOtp(String no) {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(firebaseAuth)
//                        .setPhoneNumber(no)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // Activity (for callback binding)
//                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//                            @Override
//                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                super.onCodeSent(s, forceResendingToken);
//                                otpId = s;
//                            }
//
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
//
//                                signInWithPhoneAuthCredential(phoneAuthCredential);
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        })          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }

}