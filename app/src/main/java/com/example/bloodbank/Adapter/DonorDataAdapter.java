package com.example.bloodbank.Adapter;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.DataList.DonorData;
import com.example.bloodbank.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DonorDataAdapter extends RecyclerView.Adapter<DonorDataAdapter.MyViewHolder> {
    Context context;
    private Timer timer;
    ArrayList<DonorData> list;
    private ArrayList<DonorData> noteSources;

    public DonorDataAdapter(Context context, ArrayList<DonorData> list) {
        this.context = context;
        this.list = list;
        noteSources=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.search_donor_listitem, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonorData donorData = list.get(position);
        holder.name.setText(donorData.getUsername());
        holder.address.setText(donorData.getAddress() + ", " + donorData.getCity() + "," + donorData.getState() + "," + donorData.getCountry() + "," + donorData.getPinCode());
        holder.bloodGroup.setText(donorData.getBloodGroup());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialogbox_donar);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);

                TextView name, address, blood, phone, close;
                name = dialog.findViewById(R.id.dialog_donorName);
                address = dialog.findViewById(R.id.dialog_donorAddress);
                blood = dialog.findViewById(R.id.dialog_donorBlood);
                phone = dialog.findViewById(R.id.dialog_donorPhone);
                close = dialog.findViewById(R.id.dialog_close);

                name.setText(donorData.getUsername());
                address.setText(donorData.getAddress() + ", " + donorData.getCity() + "," + donorData.getState() + "," + donorData.getCountry() + "," + donorData.getPinCode());
                blood.setText(donorData.getBloodGroup());
                phone.setText(donorData.getMobileNo());

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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
                    list=noteSources;
                }
                else{
                    ArrayList<DonorData>  temp=new ArrayList<>();
                    for (DonorData note:noteSources){
                        if (note.getUsername().toLowerCase().contains(searchKeyword.toLowerCase()))
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, bloodGroup;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_donorName);
            address = (TextView) itemView.findViewById(R.id.tv_donorAddress);
            bloodGroup = (TextView) itemView.findViewById(R.id.tv_donorBloodGroup);

        }
    }

    public void filterList(ArrayList<DonorData> filterList) {
        list=filterList;
        notifyDataSetChanged();
    }
}
