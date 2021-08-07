package com.example.bloodbank.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Adapter.BloodBankAdapter;
import com.example.bloodbank.Adapter.DonorDataAdapter;
import com.example.bloodbank.DataList.BloodBankData;
import com.example.bloodbank.DataList.DonorData;
import com.example.bloodbank.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.xml.namespace.QName;


public class BloodBank_Fragment extends Fragment {
    RecyclerView recyclerView;
    BloodBankAdapter bloodBankAdapter;
    ArrayList<BloodBankData> list;
    EditText search;

    TextView OP, AP, BP, ABP, ON, AN, BN, ABN;
    String BG = " O+ A+ B+ AB+ O- A- B- AB-";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_blood_bank, container, false);
        getActivity().setTitle("Blood Bank");

        OP = view.findViewById(R.id.OPP);
        AP = view.findViewById(R.id.APP);
        BP = view.findViewById(R.id.BPP);
        ABP = view.findViewById(R.id.ABPP);

        ON = view.findViewById(R.id.ONN);
        AN = view.findViewById(R.id.ANN);
        BN = view.findViewById(R.id.BNN);
        ABN = view.findViewById(R.id.ABNN);


        OP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" O+")) {
                    BG = BG.replace(" O+", "");
                    OP.setBackgroundResource(R.drawable.unselected);
                    OP.setTextColor(Color.parseColor("#2E6090"));
                    common();
                } else {
                    BG += " O+";
                    OP.setBackgroundResource(R.drawable.selected);
                    OP.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        AP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" A+")) {
                    BG = BG.replace(" A+", "");
                    AP.setBackgroundResource(R.drawable.unselected);
                    AP.setTextColor(Color.parseColor("#2E6090"));
                    common();
                } else {
                    BG += " A+";
                    AP.setBackgroundResource(R.drawable.selected);
                    AP.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        BP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" B+")) {
                    BG = BG.replace(" B+", "");
                    BP.setBackgroundResource(R.drawable.unselected);
                    BP.setTextColor(Color.parseColor("#2E6090"));
                    common();
                } else {
                    BG += " B+";
                    BP.setBackgroundResource(R.drawable.selected);
                    BP.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        ABP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" AB+")) {
                    BG = BG.replace(" AB+", "");
                    ABP.setBackgroundResource(R.drawable.unselected);
                    ABP.setTextColor(Color.parseColor("#2E6090"));
                    common();
                } else {
                    BG += " AB+";
                    ABP.setBackgroundResource(R.drawable.selected);
                    ABP.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        ON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" O-")) {
                    BG = BG.replace(" O-", "");
                    ON.setBackgroundResource(R.drawable.unselected);
                    ON.setTextColor(Color.parseColor("#2E6090"));
                    common();
                } else {
                    BG += " O-";
                    ON.setBackgroundResource(R.drawable.selected);
                    ON.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        AN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" A-")) {
                    BG = BG.replace(" A-", "");
                    AN.setBackgroundResource(R.drawable.unselected);
                    AN.setTextColor(Color.parseColor("#2E6090"));
                    common();
                } else {
                    BG += " A-";
                    AN.setBackgroundResource(R.drawable.selected);
                    AN.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        BN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" B-")) {
                    BG = BG.replace(" B-", "");
                    BN.setBackgroundResource(R.drawable.unselected);
                    BN.setTextColor(Color.parseColor("#2E6090"));
                    common();
                } else {
                    BG += " B-";
                    BN.setBackgroundResource(R.drawable.selected);
                    BN.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        ABN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" AB-")) {
                    BG = BG.replace(" AB-", "");
                    ABN.setBackgroundResource(R.drawable.unselected);
                    ABN.setTextColor(Color.parseColor("#2E6090"));
                    common();
                } else {
                    BG += " AB-";
                    ABN.setBackgroundResource(R.drawable.selected);
                    ABN.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        search=view.findViewById(R.id.search_text);


        recyclerView=view.findViewById(R.id.bloodBankList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list=new ArrayList<>();
        bloodBankAdapter=new BloodBankAdapter(getActivity(),list);
        recyclerView.setAdapter(bloodBankAdapter);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BloodBank");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    BloodBankData data=ds.getValue(BloodBankData.class);
                    list.add(data);
                }
                bloodBankAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bloodBankAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (list.size()!=0)
                {
                    bloodBankAdapter.searchKeyword(s.toString());
                }
            }
        });

        return view;
    }

    public void common() {
        list.clear();
        FirebaseDatabase.getInstance().getReference().child("BloodBank").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if ((Boolean) snapshot.child("O+").getValue() && BG.contains(" O+") && !list.contains(snapshot.child("name").getValue().toString())){
                    if (!list.contains(snapshot.getValue(BloodBankData.class))) {
                        list.add(snapshot.getValue(BloodBankData.class));
                    }

                }
                else if ((Boolean) snapshot.child("A+").getValue() && BG.contains(" A+") && !list.contains(snapshot.child("name").getValue().toString())){
                    if (!list.contains(snapshot.getValue(BloodBankData.class))) {
                        list.add(snapshot.getValue(BloodBankData.class));
                    }
                }
                else if ((Boolean) snapshot.child("B+").getValue() && BG.contains(" B+") && !list.contains(snapshot.child("name").getValue().toString())){
                    if (!list.contains(snapshot.getValue(BloodBankData.class))) {
                        list.add(snapshot.getValue(BloodBankData.class));
                    }
                }
                else if ((Boolean) snapshot.child("AB+").getValue() && BG.contains(" AB+") && !list.contains(snapshot.child("name").getValue().toString())){
                    if (!list.contains(snapshot.getValue(BloodBankData.class))) {
                        list.add(snapshot.getValue(BloodBankData.class));
                    }
                }

                else if ((Boolean) snapshot.child("O-").getValue() && BG.contains(" O-") && !list.contains(snapshot.child("name").getValue().toString())){
                    if (!list.contains(snapshot.getValue(BloodBankData.class))) {
                        list.add(snapshot.getValue(BloodBankData.class));
                    }

                }
                else if ((Boolean) snapshot.child("A-").getValue() && BG.contains(" A-") && !list.contains(snapshot.child("name").getValue().toString())){
                    if (!list.contains(snapshot.getValue(BloodBankData.class))) {
                        list.add(snapshot.getValue(BloodBankData.class));
                    }
                }
                else if ((Boolean) snapshot.child("B-").getValue() && BG.contains(" B-") && !list.contains(snapshot.child("name").getValue().toString())){
                    if (!list.contains(snapshot.getValue(BloodBankData.class))) {
                        list.add(snapshot.getValue(BloodBankData.class));
                    }
                }
                else if ((Boolean) snapshot.child("AB-").getValue() && BG.contains(" AB-") && !list.contains(snapshot.child("name").getValue().toString())){
                    if (!list.contains(snapshot.getValue(BloodBankData.class))) {
                        list.add(snapshot.getValue(BloodBankData.class));
                    }
                }
  
                recyclerView.setAdapter(bloodBankAdapter);
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


    private void filter(String text) {
        ArrayList<BloodBankData> filterList=new ArrayList<>();

        for (BloodBankData item:list)
        {
            if (item.getName().toLowerCase().contains(text.toLowerCase()));
            {
                filterList.add(item);
            }
        }

        bloodBankAdapter.filterList(filterList);

    }
}