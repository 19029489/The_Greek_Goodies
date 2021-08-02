package com.example.thegreekgoodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AddressAdapter extends ArrayAdapter<Address> {

    private ArrayList<Address> address;
    private Context context;

    private TextView tvDefault, tvAddress;

    public AddressAdapter(Context context, int resource, ArrayList<Address> objects){
        super(context, resource, objects);
        address = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.address_row, parent, false);

        tvDefault = (TextView) rowView.findViewById(R.id.tvDefault);
        tvAddress = (TextView) rowView.findViewById(R.id.tvAddress);

        Address currentAddress = address.get(position);

        if (currentAddress.getDefaultAddress() == 1) {
            tvDefault.setText("Default");
        }

        tvAddress.setText(currentAddress.toString());

        return rowView;
    }

}