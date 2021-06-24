package com.example.bloodbank.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Activities.LoginActivity;
import com.example.bloodbank.Activities.MainActivity;
import com.example.bloodbank.Adapter.DonorDataAdapter;
import com.example.bloodbank.DataList.DonorData;
import com.example.bloodbank.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchDonor_Fragment extends Fragment  {
    Spinner spinner_blood;
    RecyclerView recyclerView;
    DonorDataAdapter dataAdapter;
    ArrayList<DonorData> list;
    EditText search;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_donor, container, false);

        getActivity().setTitle("Donor");

        recyclerView=view.findViewById(R.id.donorList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list=new ArrayList<>();
        dataAdapter=new DonorDataAdapter(getActivity(),list);
        recyclerView.setAdapter(dataAdapter);

        progressBar=view.findViewById(R.id.pg);


        spinner_blood = view.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Blood_Group));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_blood.setAdapter(arrayAdapter);
        spinner_blood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Blood Group"))
                {
                    new Upload().execute();
                        }
                else
                {
                    String selectedBloodGroup=arrayAdapter.getItem(position);
                    showSelectedBloodGroupDonor(selectedBloodGroup);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search=view.findViewById(R.id.search_text);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (list.size()!=0)
                {
                    dataAdapter.searchKeyword(s.toString());
                }
            }
        });

        return view;
    }

    private void showAllDonor() {
        list.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserDetails");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DonorData data=ds.getValue(DonorData.class);
                    list.add(data);
                    Log.d("TAG", data + " / ");
                }
                dataAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showSelectedBloodGroupDonor(String selectedBloodGroup) {
        list.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserDetails");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (selectedBloodGroup.equals(ds.child("bloodGroup").getValue().toString()))
                    {
                        DonorData data=ds.getValue(DonorData.class);
                        list.add(data);
                    }
                }
                dataAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void filter(String text) {
            ArrayList<DonorData> filterList=new ArrayList<>();

            for (DonorData item:list)
            {
                if (item.getUsername().toLowerCase().contains(text.toLowerCase()));
                {
                    filterList.add(item);
                }
            }

            dataAdapter.filterList(filterList);

    }

    class Upload extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
           showAllDonor();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);

            super.onPostExecute(aVoid);
        }
    }


}