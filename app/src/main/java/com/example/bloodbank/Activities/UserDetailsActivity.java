package com.example.bloodbank.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.bloodbank.Activities.MainActivity;
import com.example.bloodbank.R;
import com.example.bloodbank.UserHelperClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserDetailsActivity extends AppCompatActivity {
    TextView et_userName, et_email, et_mobileNo, et_password, et_address, et_pinCode, et_city, et_state, et_country;
    TextView tv_dob;
    RadioGroup radioGroup_gender, radioGroup_suffered;
    RadioButton radioButton_gender, radioButton_suffered;
    Spinner spinner_bloodGroup;
    Button submit;
    int day;
    int month;
    int year;
    String suffered, gender;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        et_userName = findViewById(R.id.et_userName);
        et_email = findViewById(R.id.et_email);
        et_mobileNo = findViewById(R.id.et_mobileNo);
        et_password = findViewById(R.id.et_password);
        et_address = findViewById(R.id.et_address);
        et_pinCode = findViewById(R.id.et_pinCode);
        et_city = findViewById(R.id.et_city);
        et_state = findViewById(R.id.et_state);
        et_country = findViewById(R.id.et_country);
        tv_dob = findViewById(R.id.tv_dob);


        Calendar calendar = Calendar.getInstance();

        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(UserDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int d = dayOfMonth;
                        int m = month + 1;
                        int y = year;

                        tv_dob.setText(d + "/" + m + "/" + y);
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });


        tv_dob.setText("");

        radioGroup_gender = findViewById(R.id.radioGp_gender);
        radioGroup_suffered = findViewById(R.id.radioGp_suffered);


        submit = findViewById(R.id.submitBtn);

        spinner_bloodGroup = findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Blood_Group));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bloodGroup.setAdapter(arrayAdapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_userName.getText().toString();
                String emailId = et_email.getText().toString();
                String mobileNo = et_mobileNo.getText().toString();
                String password = et_password.getText().toString();
                String address = et_address.getText().toString();
                String pinCode = et_pinCode.getText().toString();
                String city = et_city.getText().toString();
                String state = et_state.getText().toString();
                String country = et_country.getText().toString();
                String dob = tv_dob.getText().toString();

                String bloodGroup = spinner_bloodGroup.getSelectedItem().toString();


                if (radioGroup_suffered.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Select Have you suffered from co-vid  ", Toast.LENGTH_SHORT).show();
                } else {
                    int selectedId_suffered = radioGroup_suffered.getCheckedRadioButtonId();
                    radioButton_suffered = (RadioButton) findViewById(selectedId_suffered);
                    suffered = radioButton_suffered.getText().toString();
                }

                if (radioGroup_gender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                } else {
                    int selectedId_gender = radioGroup_gender.getCheckedRadioButtonId();
                    radioButton_gender = (RadioButton) findViewById(selectedId_gender);
                    gender = radioButton_gender.getText().toString();

                }
//
//                int selectedId_gender = radioGroup_gender.getCheckedRadioButtonId();
//                int selectedId_suffered = radioGroup_suffered.getCheckedRadioButtonId();
//
//                radioButton_gender = (RadioButton) findViewById(selectedId_gender);
//                radioButton_suffered = (RadioButton) findViewById(selectedId_suffered);
//
//                String gender=radioButton_gender.getText().toString();
//                String suffered=radioButton_suffered.getText().toString();


//                String regexStr = "^[0-9]$";
//                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//                if (username.isEmpty())
//                {
//                    et_userName.setError("Required");
//
//                }
//                if (emailId.isEmpty()) {
//                    et_email.setError("Required");
//                }
//                else if (!(emailId.matches(emailPattern) && emailId.length() > 0))
//                {
//                    Toast.makeText(UserDetailsActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
////                        et_email.setError("Invalid email");
//                }
//                else if(mobileNo.isEmpty())
//                {
//                    et_mobileNo.setError("Required");
//                }
//                else if (mobileNo.length()<10 || mobileNo.matches(regexStr)==false )
//                {
//                    Toast.makeText(UserDetailsActivity.this," Invalid Mobile No", Toast.LENGTH_SHORT).show();
////                    et_mobileNo.setError("Invalid Mobile No");
//                }
//                    else if (password.isEmpty())
//                {
//                    et_password.setError("Required");
//                }else if (address.isEmpty())
//                {
//                    et_address.setError("Required");
//                }else if (pinCode.isEmpty())
//                {
//                    et_pinCode.setError("Required");
//                } else if (pinCode.length()<6)
//                {
//                    Toast.makeText(UserDetailsActivity.this,"Invalid Code", Toast.LENGTH_SHORT).show();
//                    et_pinCode.setError("Invalid Code");
//                } else if (city.isEmpty())
//                {
//                    et_city.setError("Required");
//                }else if (state.isEmpty())
//                {
//                    et_state.setError("Required");
//                }else if (country.isEmpty())
//                {
//                    et_country.setError("Required");
//                }else if(spinner_bloodGroup.getSelectedItem().toString()== null)
//                {
//                    Toast.makeText(getApplicationContext(),"Select Blood Group",Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    storeNewUserDetails(username,emailId,mobileNo,password,dob,gender,bloodGroup,address,pinCode,city,state,country,suffered);
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    finish();
//                }
                if (!validateUserName() | !validatePassword() | !validatePhoneNo() | !validateEmail() | !validateDob() | !validateBloodGroup() | !validateAddress() | !validatePinCode() | !validateCity() | !validateState() | !validateCountry()) {

//                    storeNewUserDetails(username, emailId, mobileNo, password, dob, gender, bloodGroup, address, pinCode, city, state, country, suffered);
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    finish();
//                    return;
                }
                else
                {
                    storeNewUserDetails(username, emailId, mobileNo, password, dob, gender, bloodGroup, address, pinCode, city, state, country, suffered);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                    return;
                }


            }
        });
    }

    private void storeNewUserDetails(String username, String emailId, String mobileNo, String password, String dob, String gender, String bloodGroup, String address, String pinCode, String city, String state, String country, String suffered) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("UserDetails");

        UserHelperClass addNewUser = new UserHelperClass(username, emailId, mobileNo, password, dob, gender, bloodGroup, address, pinCode, city, state, country, suffered);
        reference.child(username).setValue(addNewUser);
        UserHelperClass.setCurrentUser_username(et_userName.getText().toString());
    }

    private Boolean validateUserName() {
        if (et_userName.getText().toString().isEmpty()) {
            et_userName.setError("Field cannot be empty");
            return false;
        } else {
            et_userName.setError(null);
            return true;
        }
    }


    private Boolean validateEmail() {
        String val = et_email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            et_email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            et_email.setError("Invalid email address");
            return false;
        } else {
            et_email.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = et_mobileNo.getText().toString();

        if (val.isEmpty()) {
            et_mobileNo.setError("Field cannot be empty");
            return false;
        }
        else if (!(val.length()==10)) {
            et_mobileNo.setError("Invalid");
            return  false;
        }
            else
         {
            et_mobileNo.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = et_password.getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            et_password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            et_password.setError("Password is too weak");
            return false;
        } else {
            et_password.setError(null);
            return true;
        }
    }

    private boolean validateCountry() {
        if (et_country.getText().toString().isEmpty()) {
            et_country.setError("Field cannot be empty");
            return false;
        } else {
            et_country.setError(null);
            return true;
        }
    }

    private boolean validateState() {
        if (et_state.getText().toString().isEmpty()) {
            et_state.setError("Field cannot be empty");
            return false;
        } else {
            et_state.setError(null);
            return true;
        }
    }

    private boolean validateCity() {
        if (et_city.getText().toString().isEmpty()) {
            et_city.setError("Field cannot be empty");
            return false;
        } else {
            et_city.setError(null);
            return true;
        }
    }

    private boolean validatePinCode() {
        if (et_pinCode.getText().toString().isEmpty()) {
            et_pinCode.setError("Field cannot be empty");
            return false;
        } else if (et_pinCode.getText().toString().length() < 6) {
            et_pinCode.setError("Invalid PinCode");
            return false;
        } else {
            et_pinCode.setError(null);
            return true;
        }
    }

    private boolean validateAddress() {
        if (et_address.getText().toString().isEmpty()) {
            et_address.setError("Field cannot be empty");
            return false;
        } else {
            et_address.setError(null);
            return true;
        }
    }

    private boolean validateBloodGroup() {
        String bloodGroup = spinner_bloodGroup.getSelectedItem().toString();
        if (bloodGroup.equals("Blood Group")) {
            Toast.makeText(this, "Select Blood Group", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    private boolean validateDob() {
        String val = tv_dob.getText().toString();

        if (val.isEmpty()) {
            tv_dob.setError("Field cannot be empty");
            return false;
        } else {
            tv_dob.setError(null);
            return true;
        }
    }
}