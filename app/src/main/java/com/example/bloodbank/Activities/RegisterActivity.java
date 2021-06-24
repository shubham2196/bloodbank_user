package com.example.bloodbank.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    Button continueBtn,generateOtpBtn;
    EditText mobileNo,otp;
    TextView login;
    FirebaseAuth firebaseAuth;
    CountryCodePicker countryCodePicker;
    String otpId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth=FirebaseAuth.getInstance();


        mobileNo=findViewById(R.id.et_mobileNo);
        otp=findViewById(R.id.et_enterOtp);
        login=findViewById(R.id.tv_alreadyLogin);
        continueBtn=findViewById(R.id.btn_continue);
        generateOtpBtn=findViewById(R.id.btn_generateOtp);
        countryCodePicker=findViewById(R.id.countryCodePicker);
        countryCodePicker.registerCarrierNumberEditText(mobileNo);



        generateOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mobileNo.getText().toString().isEmpty())
                {

                    otp.setVisibility(View.VISIBLE);
                    continueBtn.setVisibility(View.VISIBLE);
                    generateOtpBtn.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.INVISIBLE);
                    generateOtp(countryCodePicker.getFullNumberWithPlus());
                }
                else
                {
                    mobileNo.setError("is empty");
                }

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
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
//                    Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(otpId,otp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void generateOtp(String no) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(no)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                otpId=s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();

                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), UserDetailsActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Verification not completed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}