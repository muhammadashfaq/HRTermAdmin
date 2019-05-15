package com.example.muhammadashfaq.recieveapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.recieveapp.Common;
import com.example.muhammadashfaq.recieveapp.HomeActivity;
import com.example.muhammadashfaq.recieveapp.Interface.ItemClickListner;
import com.example.muhammadashfaq.recieveapp.Model.IMEINModel;
import com.example.muhammadashfaq.recieveapp.Model.PhoneNoModel;
import com.example.muhammadashfaq.recieveapp.R;

import java.util.ArrayList;

public class PhoneNoAdapter extends RecyclerView.Adapter<PhoneNoAdapter.PhoneNoViewHolder> {


    Context context;
    ArrayList<PhoneNoModel> list;
    String mobileModel;
    int recyler_item_desing;
    LayoutInflater inflater;

    public PhoneNoAdapter(Context context, ArrayList<PhoneNoModel> list, int recyler_imei_item_desing){
        this.context=context;
        this.list=list;
        this.recyler_item_desing=recyler_imei_item_desing;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PhoneNoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView=inflater.inflate(recyler_item_desing,viewGroup,false);
        PhoneNoAdapter.PhoneNoViewHolder imeiViewHolder=new PhoneNoAdapter.PhoneNoViewHolder(itemView);
        return imeiViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneNoViewHolder phoneNoViewHolder, final int position) {
        phoneNoViewHolder.txtVuPhoneno.setText(list.get(position).getPhone_no());

        phoneNoViewHolder.txtVuPhoneno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,list.get(position).getPhone_no(), Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(context, HomeActivity.class);
//                intent.putExtra("device_name",list.get(position).getPhone_no());
//                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PhoneNoViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtVuPhoneno;
        private ItemClickListner itemClickListnerFood;

        public PhoneNoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtVuPhoneno=itemView.findViewById(R.id.tv_phone_numbers);
        }
    }
}
