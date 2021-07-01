package com.example.bloodbank.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.DataList.DonorData;
import com.example.bloodbank.Fragments.MyDetails_Fragment;
import com.example.bloodbank.R;
import com.example.bloodbank.UserHelperClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    EditText et_userName, et_email, et_mobileNo, et_address, et_pinCode, et_city, et_state, et_country;
    TextView tv_dob;
    RadioGroup radioGroup_gender, radioGroup_suffered;
    RadioButton radioButton_gender, radioButton_suffered;
    RadioButton radioButton_gender_male, radioButton_gender_female, radioButton_gender_other;
    RadioButton radioButton_suffered_yes, radioButton_suffered_no;
    private DatabaseReference mReff;
    FragmentManager  fragmentManager;
    FragmentTransaction fragmentTransaction;
    Spinner spinner_bloodGroup;
    Button save;
    DatePicker picker;
    String currentUser;
    DatabaseReference reference;
    int day;
    int month;
    int year;
    ImageView iv_back;

    String username, emailId, mobileNo, address, pinCode, city, state, country, bloodGroup, gender, suffered, dob ,userCurrentPassword;
    String passwordVal = "^" +
            //"(?=.*[0-9])" +         //at least 1 digit
            //"(?=.*[a-z])" +         //at least 1 lower case letter
            //"(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setTitle("Profile");

        iv_back=findViewById(R.id.backBtn);
        et_userName = findViewById(R.id.et_userName);
        et_email = findViewById(R.id.et_email);
        et_mobileNo = findViewById(R.id.et_mobileNo);
        et_address = findViewById(R.id.et_address);
        et_pinCode = findViewById(R.id.et_pinCode);
        et_city = findViewById(R.id.et_city);
        et_state = findViewById(R.id.et_state);
        et_country = findViewById(R.id.et_country);
        tv_dob = findViewById(R.id.tv_dob);

        radioGroup_gender = findViewById(R.id.radioGp_gender);
        radioGroup_suffered = findViewById(R.id.radioGp_suffered);

        radioButton_gender_male = findViewById(R.id.radioBtn_male);
        radioButton_gender_female = findViewById(R.id.radioBtn_female);
        radioButton_gender_other = findViewById(R.id.radioBtn_other);

        radioButton_suffered_yes = findViewById(R.id.radioBtn_yes);
        radioButton_suffered_no = findViewById(R.id.radioBtn_no);

        Calendar calendar = Calendar.getInstance();


        save = findViewById(R.id.saveBtn);

        spinner_bloodGroup = findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Blood_Group));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bloodGroup.setAdapter(arrayAdapter);

        currentUser = UserHelperClass.getCurrentUser_username();

        reference = FirebaseDatabase.getInstance().getReference("UserDetails");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().toString().equals(currentUser)) {
                    username = snapshot.child("username").getValue(String.class);
                    address = snapshot.child("address").getValue(String.class);
                    city = snapshot.child("city").getValue(String.class);
                    state = snapshot.child("state").getValue(String.class);
                    country = snapshot.child("country").getValue(String.class);
                    pinCode = snapshot.child("pinCode").getValue(String.class);
                    mobileNo = snapshot.child("mobileNo").getValue(String.class);
                    dob = snapshot.child("dob").getValue(String.class);
                    emailId = snapshot.child("emailId").getValue(String.class);
                    bloodGroup = snapshot.child("bloodGroup").getValue(String.class);
                    gender = snapshot.child("gender").getValue(String.class);
                    suffered = snapshot.child("suffered").getValue(String.class);
                    userCurrentPassword=snapshot.child("password").getValue(String.class);

                    if (snapshot.child("gender").getValue(String.class).equals("Male")) {
                        radioButton_gender_male.setChecked(true);

                    }
                    if (snapshot.child("gender").getValue(String.class).equals("Female")) {
                        radioButton_gender_female.setChecked(true);

                    }
                    if (snapshot.child("gender").getValue(String.class).equals("Other")) {
                        radioButton_gender_other.setChecked(true);
                    }


                    if (snapshot.child("suffered").getValue(String.class).equals("Yes")) {
                        radioButton_suffered_yes.setChecked(true);

                    }
                    if (snapshot.child("suffered").getValue(String.class).equals("No")) {
                        radioButton_suffered_no.setChecked(true);

                    }
                    et_userName.setText(username);
                    et_userName.setText(username);
                    et_address.setText(address);
                    et_city.setText(city);
                    et_state.setText(state);
                    et_country.setText(country);
                    et_pinCode.setText(pinCode);
                    et_mobileNo.setText(mobileNo);
                    tv_dob.setText(dob);
                    et_email.setText(emailId);

                    int pos =arrayAdapter.getPosition(bloodGroup);
                    spinner_bloodGroup.setSelection(pos);
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

        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int d=dayOfMonth;
                        int m=month+1;
                        int y=year;

                        tv_dob.setText(d+"/"+m+"/"+y);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditProfileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
//        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog dialog = new Dialog(EditProfileActivity.this);
//                dialog.setContentView(R.layout.dialogbox_change_password);
//                dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);
//
//
//                TextView cpass, npass, rpass,change,close;
//                cpass = dialog.findViewById(R.id.et_current_password);
//                npass = dialog.findViewById(R.id.et_new_password);
//                rpass = dialog.findViewById(R.id.et_reEnter_password);
//                change = dialog.findViewById(R.id.dialog_change);
//                close = dialog.findViewById(R.id.close);
//
//                close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                change.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!npass.getText().toString().equals(rpass.getText().toString())) {
//                            Toast.makeText(getApplicationContext(), "Password Not Matched", Toast.LENGTH_SHORT).show();
//                            dialog.cancel();
//                        }
//                        else if (!(npass.getText().toString().matches(passwordVal)))
//                        {
//                            Toast.makeText(getApplicationContext(), "Password is too weak", Toast.LENGTH_SHORT).show();
//                        }else if (!cpass.getText().toString().equals(userCurrentPassword)) {
//                            Toast.makeText(getApplicationContext(), "Current Password is Wrong.", Toast.LENGTH_SHORT).show();
//                            dialog.cancel();
//                        } else {
//                            FirebaseDatabase.getInstance().getReference().child("UserDetails")
//                                    .child(currentUser)
//                                    .child("password").setValue(npass.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
////                                    UserHolder.getInstance().setPass(npass.getText().toString());
//                                    Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT).show();
//                                    dialog.cancel();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
//                                    dialog.cancel();
//                                }
//                            });}
//                    }
//                });
//
//                dialog.setCancelable(true);
//                dialog.show();
//            }
//        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_userName.getText().toString();
                String emailId = et_email.getText().toString();
                String mobileNo = et_mobileNo.getText().toString();
                String address = et_address.getText().toString();
                String pinCode = et_pinCode.getText().toString();
                String city = et_city.getText().toString();
                String state = et_state.getText().toString();
                String country = et_country.getText().toString();

                String bloodGroup = spinner_bloodGroup.getSelectedItem().toString();

                int selectedId_gender = radioGroup_gender.getCheckedRadioButtonId();
                int selectedId_suffered = radioGroup_suffered.getCheckedRadioButtonId();

                radioButton_gender = (RadioButton) findViewById(selectedId_gender);
                radioButton_suffered = (RadioButton) findViewById(selectedId_suffered);

                String gender = radioButton_gender.getText().toString();
                String suffered = radioButton_suffered.getText().toString();

                try {
                    int day = picker.getDayOfMonth();
                    int month = picker.getMonth() + 1;
                    int year = picker.getYear();
                } catch (Exception e){

                }
                String dob = day + "/" + month + "/" + year;
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (username.isEmpty()) {
                    et_userName.setError("Required");
                } else if (emailId.isEmpty()) {
                    et_email.setError("Required");
                } else if (mobileNo.isEmpty()) {
                    et_mobileNo.setError("Required");
                } else if (address.isEmpty()) {
                    et_address.setError("Required");
                } else if (pinCode.isEmpty()) {
                    et_pinCode.setError("Required");
                } else if (city.isEmpty()) {
                    et_city.setError("Required");
                } else if (state.isEmpty()) {
                    et_state.setError("Required");
                } else if (country.isEmpty()) {
                    et_country.setError("Required");
                }
                else if(!emailId.matches(emailPattern))
                {
                    et_email.setError("Invalid");
                }
                else if (!(mobileNo.length()==10)) {
                    et_mobileNo.setError("Invalid");
                }
                else if (pinCode.length() < 6) {
                    et_pinCode.setError("Invalid PinCode");
                }
                else if (spinner_bloodGroup.getSelectedItem().toString().equals("Blood Group"))
                {
                    Toast.makeText(getApplicationContext(),"Select Blood Group",Toast.LENGTH_SHORT).show();
                }
                else if(spinner_bloodGroup.getSelectedItem().toString()== null)
                {
                    Toast.makeText(getApplicationContext(),"Select Blood Group",Toast.LENGTH_SHORT).show();
                }else
                    {
                        HashMap data = new HashMap();
                    data.put("username",currentUser );
                    data.put("emailId", emailId);
                    data.put("mobileNo", mobileNo);
                    data.put("address", address);
                    data.put("pinCode", pinCode);
                    data.put("city", city);
                    data.put("state", state);
                    data.put("country", country);
                    data.put("bloodGroup", bloodGroup);
                    data.put("gender",gender);
                    data.put("suffered",suffered);
                    data.put("dob",tv_dob.getText().toString());
                        mReff = FirebaseDatabase.getInstance().getReference().child("UserDetails");
                    mReff.child(currentUser).updateChildren(data, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                                UserHelperClass.setCurrentUser_username(et_userName.getText().toString());
                            } else {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    });
                }
//                    {
//
//                    updateUserDetails();
//                    Intent i = new Intent(EditProfileActivity.this, MainActivity.class);
//                    startActivity(i);
//                }
            }
        });

    }

    private void updateUserDetails() {
        if (isUserNameChanged() || isEmailIdChanged() || isMobileNoChanged() || isDobChanged() || isGenderChanged() || isBloodGroupChanged() || isAddressChanged() || isPinCodeChanged() || isCityChanged() || isStateChanged() || isCountryChanged() || isSufferedChanged()) {
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data can not be updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isBloodGroupChanged() {
        if (!bloodGroup.equals(spinner_bloodGroup.getSelectedItem().toString())) {
            reference = FirebaseDatabase.getInstance().getReference("UserDetails");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getKey().toString().equals(currentUser)){
                        Object ds = snapshot.getValue();

                        reference.child(et_userName.getText().toString()).child("bloodGroup").setValue(spinner_bloodGroup.getSelectedItem().toString());

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
            return true;
        } else {
            return false;
        }
    }

    private boolean isSufferedChanged() {
        if (!suffered.equals(radioButton_suffered.getText().toString())) {
            reference = FirebaseDatabase.getInstance().getReference("UserDetails");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getKey().toString().equals(currentUser)){
                        Object ds = snapshot.getValue();

                        reference.child(et_userName.getText().toString()).child("suffered").setValue(radioButton_suffered.getText().toString());

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
            return true;
        } else {
            return false;
        }
    }

    private boolean isGenderChanged() {
        if (!gender.equals( radioButton_gender.getText().toString())) {
            reference = FirebaseDatabase.getInstance().getReference("UserDetails");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getKey().toString().equals(currentUser)){
                        Object ds = snapshot.getValue();

                        reference.child(et_userName.getText().toString()).child("gender").setValue( radioButton_gender.getText().toString());

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
            return true;
        } else {
            return false;
        }
    }

    private boolean isCountryChanged() {
        if (!country.equals(et_country.getText().toString())) {
            reference = FirebaseDatabase.getInstance().getReference("UserDetails");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getKey().toString().equals(currentUser)){
                        Object ds = snapshot.getValue();

                        reference.child(et_userName.getText().toString()).child("country").setValue(et_country.getText().toString());

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
            return true;
        } else {
            return false;
        }
    }

    private boolean isStateChanged() {
        if (!state.equals(et_state.getText().toString())) {
            reference = FirebaseDatabase.getInstance().getReference("UserDetails");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getKey().toString().equals(currentUser)){
                        Object ds = snapshot.getValue();

                        reference.child(et_userName.getText().toString()).child("state").setValue(et_state.getText().toString());

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
            return true;
        } else {
            return false;
        }
    }

    private boolean isCityChanged() {
        if (!city.equals(et_city.getText().toString())) {
            reference = FirebaseDatabase.getInstance().getReference("UserDetails");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getKey().toString().equals(currentUser)){
                        Object ds = snapshot.getValue();

                        reference.child(et_userName.getText().toString()).child("city").setValue(et_city.getText().toString());

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
            return true;
        } else {
            return false;
        }
    }

    private boolean isPinCodeChanged() {
        if (!pinCode.equals(et_pinCode.getText().toString())) {
            reference = FirebaseDatabase.getInstance().getReference("UserDetails");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getKey().toString().equals(currentUser)){
                        Object ds = snapshot.getValue();

                        reference.child(et_userName.getText().toString()).child("pinCode").setValue(et_pinCode.getText().toString());

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
            return true;
        } else {
            return false;
        }
    }

    private boolean isAddressChanged() {
        if (!address.equals(et_address.getText().toString())) {
            reference = FirebaseDatabase.getInstance().getReference("UserDetails");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getKey().toString().equals(currentUser)){
                        Object ds = snapshot.getValue();

                        reference.child(et_userName.getText().toString()).child("address").setValue(et_address.getText().toString());

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
            return true;
        } else {
            return false;
        }
    }

    private boolean isDobChanged() {
        if (!dob.equals(tv_dob.getText().toString())) {
            reference = FirebaseDatabase.getInstance().getReference("UserDetails");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getKey().toString().equals(currentUser)){
                        Object ds = snapshot.getValue();

                        reference.child(et_userName.getText().toString()).child("dob").setValue(tv_dob.getText().toString());

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
            return true;
        } else {
            return false;
        }
    }

    private boolean isMobileNoChanged() {
        if (!mobileNo.equals(et_mobileNo.getText().toString())) {
            reference = FirebaseDatabase.getInstance().getReference("UserDetails");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getKey().toString().equals(currentUser)){
                        Object ds = snapshot.getValue();

                        reference.child(et_userName.getText().toString()).child("mobileNo").setValue(et_mobileNo.getText().toString());

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
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailIdChanged() {
        if (!emailId.equals(et_email.getText().toString())) {
            reference = FirebaseDatabase.getInstance().getReference("UserDetails");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getKey().toString().equals(currentUser)){
                        Object ds = snapshot.getValue();

                        reference.child(et_userName.getText().toString()).child("emailId").setValue(et_email.getText().toString());

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
            return true;
        } else {
            return false;
        }
    }

    private boolean isUserNameChanged() {
        if (!username.equals(et_userName.getText().toString())) {

            reference = FirebaseDatabase.getInstance().getReference("UserDetails");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getKey().toString().equals(currentUser)){
                        Object ds = snapshot.getValue();

                        reference.child(currentUser).setValue(null);
                        reference.child(et_userName.getText().toString()).setValue(ds);
                        reference.child(et_userName.getText().toString()).child("username").setValue(et_userName.getText().toString());

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
            UserHelperClass.setCurrentUser_username(et_userName.getText().toString());
            return true;
        } else {
            return false;
        }
    }

}

