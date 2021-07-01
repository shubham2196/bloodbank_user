package com.example.bloodbank.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Activities.UserDetailsActivity;
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

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNo extends AppCompatActivity {
    TextView otp,resend;
    Button verify;
    String VerificationCode,phoneNoWithoutCountryCode;
    Dialog dialog;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);
        otp=findViewById(R.id.et_otp);
        resend=findViewById(R.id.resendOtp);
        verify=findViewById(R.id.btn_verifyLogin);



        String phoneNoWithoutCountryCode=getIntent().getStringExtra("phoneNo");
        String phoneNoWithCountryCode="+91"+phoneNoWithoutCountryCode;

        auth=FirebaseAuth.getInstance();
        sendVerificationCode(phoneNoWithCountryCode);
        setCurrentUser(phoneNoWithoutCountryCode);


        dialog = new Dialog(VerifyPhoneNo.this);
        dialog.setContentView(R.layout.dialogbox_register);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);
        dialog.setCancelable(false);
        dialog.show();


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Blank Field",Toast.LENGTH_SHORT).show();
                }else if (otp.getText().toString().length()!=6)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Otp",Toast.LENGTH_SHORT).show();
                }else
                {
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(VerificationCode,otp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(phoneNoWithCountryCode);
            }
        });

    }

    private void sendVerificationCode(String phoneNo) {

                PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                dialog.dismiss();
                                VerificationCode = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setCurrentUser(String phoneNoWithoutCountryCode) {
        FirebaseDatabase.getInstance().getReference().child("UserDetails").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if((snapshot.child("mobileNo").getValue().toString().equals(phoneNoWithoutCountryCode)))
                {
                    UserHelperClass.setCurrentUser_username(snapshot.getKey().toString());

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
}