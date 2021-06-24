package com.example.bloodbank.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.DataList.BloodBankData;
import com.example.bloodbank.DataList.DonorData;
import com.example.bloodbank.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BloodBankAdapter extends RecyclerView.Adapter<BloodBankAdapter.MyViewHolder> {

    Context context;
    private Timer timer;
    ArrayList<BloodBankData> list;
    private ArrayList<BloodBankData> Sources;

    public BloodBankAdapter(Context context, ArrayList<BloodBankData> list) {
        this.context = context;
        this.list = list;
        Sources=list;
    }

    @NonNull
    @Override
    public BloodBankAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.blood_bank_listitem,parent,false);
        return new BloodBankAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BloodBankData bloodBankData=list.get(position);
        holder.name.setText(bloodBankData.getName());
        holder.mobileNo.setText(bloodBankData.getMobile());
        holder.address.setText(bloodBankData.getArealine()+","+bloodBankData.getLandmark()+","+bloodBankData.getCity()+","+bloodBankData.getDistrict()+","+bloodBankData.getPin());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialogbox_bloodbank);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);


                TextView dialog_name, dialog_address, dialog_blood, dialog_phone,close,direction;
                dialog_name = dialog.findViewById(R.id.dialog_bankName);
                dialog_address = dialog.findViewById(R.id.dialog_bankAddress);
                dialog_blood = dialog.findViewById(R.id.dialog_bankBlood);
                dialog_phone = dialog.findViewById(R.id.dialog_bankPhone);
                close = dialog.findViewById(R.id.dialog_close);
                direction=dialog.findViewById(R.id.dialog_direction);

                dialog_name.setText(bloodBankData.getName());
                dialog_address.setText(bloodBankData.getArealine()+","+bloodBankData.getLandmark()+","+bloodBankData.getCity()+","+bloodBankData.getDistrict()+","+bloodBankData.getPin());
                dialog_phone.setText(bloodBankData.getMobile());
                dialog_blood.setText(" ");

                FirebaseDatabase.getInstance().getReference().child("BloodBank").orderByChild("name").equalTo(bloodBankData.getName()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Boolean OP, ON, AP, AN, BP, BN, ABP, ABN;

                        OP = (Boolean) snapshot.child("O+").getValue();
                        ON = (Boolean) snapshot.child("O-").getValue();
                        AP = (Boolean) snapshot.child("A+").getValue();
                        BP = (Boolean) snapshot.child("B+").getValue();

                        ABP = (Boolean) snapshot.child("AB+").getValue();
                        AN = (Boolean) snapshot.child("A-").getValue();
                        BN = (Boolean) snapshot.child("B-").getValue();
                        ABN = (Boolean) snapshot.child("AB-").getValue();

                        if (OP) {
                            dialog_blood.setText(dialog_blood.getText().toString()+"O+");
                        }
                        if (AP) {
                            dialog_blood.setText(dialog_blood.getText().toString()+ " A+");
                        }
                        if (BP) {
                            dialog_blood.setText(dialog_blood.getText().toString()+" B+");
                        }
                        if (ABP) {
                            dialog_blood.setText(dialog_blood.getText().toString()+  " AB+");
                        }
                        if (ON) {
                            dialog_blood.setText(dialog_blood.getText().toString()+  " O-");
                        }
                        if (AN) {
                            dialog_blood.setText(dialog_blood.getText().toString()+  " A-");
                        }
                        if (BN) {
                            dialog_blood.setText(dialog_blood.getText().toString()+  " B-");
                        }
                        if (ABN) {
                            dialog_blood.setText(dialog_blood.getText().toString()+  " AB-");
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


                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                dialog.setCancelable(true);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,mobileNo,address;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_bloodBankName);
            mobileNo = (TextView) itemView.findViewById(R.id.tv_bloodBankNo);
            address = (TextView) itemView.findViewById(R.id.tv_bloodAddress);

        }
    }

    public void cancelTimer() {

        if (timer!=null)
        {
            timer.cancel();
        }
    }


    public void searchKeyword(String searchKeyword) {
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty())
                {
                    list=Sources;
                }
                else{
                    ArrayList<BloodBankData>  temp=new ArrayList<>();
                    for (BloodBankData note:Sources){
                        if (note.getName().toLowerCase().contains(searchKeyword.toLowerCase()))
//
//                        || note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase())
//                                || note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())
                        {
                            temp.add(note);
                        }
                    }
                    list=temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        },500);
    }
    public void filterList(ArrayList<BloodBankData> filterList) {
        list=filterList;
        notifyDataSetChanged();
    }
}
