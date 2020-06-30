package com.example.delivery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.delivery.R;
import com.example.delivery.models.Delivery;

import java.util.ArrayList;

public class DeliveryAdapter extends ArrayAdapter<Delivery> {
    public DeliveryAdapter(Context context, ArrayList<Delivery> deliveries) {
        super(context, 0, deliveries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Delivery delivery = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_listview, parent, false);
        }

        TextView tvAddresseeName = convertView.findViewById(R.id.tvAddresseeName);
        TextView tvAddress = convertView.findViewById(R.id.tvAddress);
        tvAddresseeName.setText(delivery.addreseeName);
        tvAddress.setText(delivery.address);

//        LinearLayout llDelivery = convertView.findViewById(R.id.llDelivery);
//        llDelivery.setTag(position);
//        llDelivery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = (Integer) view.getTag();
//                Delivery delivery1 = getItem(position);
//                Log.d("Main_Screen", delivery1.getAddreseeName());
//            }
//        });
        return convertView;
    }
}