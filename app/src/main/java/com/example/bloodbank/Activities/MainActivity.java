package com.example.bloodbank.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.bloodbank.Fragments.BloodBank_Fragment;
import com.example.bloodbank.Fragments.MyDetails_Fragment;
import com.example.bloodbank.R;
import com.example.bloodbank.Fragments.SearchDonor_Fragment;
import com.example.bloodbank.UserHelperClass;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager  fragmentManager;
    FragmentTransaction fragmentTransaction;
    FirebaseAuth firebaseAuth;
    String currentUser;


    TextView nav_name,nav_bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentUser= UserHelperClass.getCurrentUser_username();
        drawerLayout=findViewById(R.id.drawerLayout);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        navigationView=findViewById(R.id.navDrawer);
        View headerView = navigationView.getHeaderView(0);

        nav_name=headerView.findViewById(R.id.sideBar_donorName);
        nav_bloodGroup=headerView.findViewById(R.id.sideBar_donorBloodGroup);
        FirebaseDatabase.getInstance().getReference("UserDetails").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().toString().equals(currentUser)) {
                    nav_name.setText(snapshot.child("username").getValue().toString());
                    nav_bloodGroup.setText(snapshot.child("bloodGroup").getValue().toString());
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




        firebaseAuth=FirebaseAuth.getInstance();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (item.getItemId())
                {
                    case R.id.search_donor:
                        fragmentManager=getSupportFragmentManager();
                        fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_fragment,new SearchDonor_Fragment());
                        fragmentTransaction.commit();

                        break;

                    case R.id.blood_bank:
                        fragmentManager=getSupportFragmentManager();
                        fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_fragment,new BloodBank_Fragment());
                        fragmentTransaction.commit();

                        break;

                    case R.id.my_details:
                        fragmentManager=getSupportFragmentManager();
                        fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_fragment,new MyDetails_Fragment());
                        fragmentTransaction.commit();

                        break;
                    case R.id.logout:
                        firebaseAuth.signOut();
                        Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                }
                return true;
            }
        });
        //load default fragment
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment,new SearchDonor_Fragment());
        fragmentTransaction.commit();

    }

}