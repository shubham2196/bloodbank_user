package com.example.bloodbank.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Activities.EditProfileActivity;
import com.example.bloodbank.R;
import com.example.bloodbank.UserHelperClass;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MyDetails_Fragment extends Fragment {

    TextView p_name,p_address,p_phone,p_gender,p_dob,p_blood,p_email;
    String currentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_details, container, false);
        getActivity().setTitle("Profile");
        setHasOptionsMenu(true);


        p_name=view.findViewById(R.id.username);
        p_address=view.findViewById(R.id.address);
        p_phone=view.findViewById(R.id.phone);
        p_gender=view.findViewById(R.id.gender);
        p_dob=view.findViewById(R.id.dob);
        p_blood=view.findViewById(R.id.bloodGroup);
        p_email=view.findViewById(R.id.emailId);


        currentUser=UserHelperClass.getCurrentUser_username();
//        Toast.makeText(getActivity(), currentUser, Toast.LENGTH_SHORT).show();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("UserDetails");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().toString().equals(currentUser)) {


                    p_name.setText(snapshot.child("username").getValue(String.class));
                    p_address.setText(snapshot.child("address").getValue(String.class) + "," +
                            snapshot.child("city").getValue(String.class) + "," +
                            snapshot.child("state").getValue(String.class) + "," +
                            snapshot.child("pinCode").getValue(String.class) + "," +
                            snapshot.child("country").getValue(String.class));
                    p_phone.setText(snapshot.child("mobileNo").getValue(String.class));
                    p_gender.setText(snapshot.child("gender").getValue(String.class));
                    p_dob.setText(snapshot.child("dob").getValue(String.class));
                    p_blood.setText(snapshot.child("bloodGroup").getValue(String.class));
                    p_email.setText(snapshot.child("emailId").getValue(String.class));
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


        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.edit_pencil:
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
                break;

            default:
                break;
        }

        return true;
    }
}



