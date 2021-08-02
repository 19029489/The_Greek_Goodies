package com.example.thegreekgoodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SummarylistAdapter extends ArrayAdapter<CustomerOrder> {
    Context context;
    ArrayList<CustomerOrder> items;
    int resource;
    TextView tvName, tvQuantity, tvPrice, tvRemove;


    public SummarylistAdapter(Context context, int resource, ArrayList<CustomerOrder> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.summarylist, parent, false);

        //-----------------Casting------------------
        tvName = rowView.findViewById(R.id.tvItemName);
        tvQuantity = rowView.findViewById(R.id.tvQuantity);
        tvPrice = rowView.findViewById(R.id.tvPrice);
        tvRemove = rowView.findViewById(R.id.tvRemove);
        //-----------------Casting------------------


        //------------------Getting--------------
        CustomerOrder customer = items.get(position);
        String name = customer.getItemName();
        double price = customer.getItemPrice();
        int quantity = customer.getQuantity();
        int id = customer.get_id();
        //------------------Getting--------------

        //-------------------Setup-----------------
        tvName.setText(name);
        tvPrice.setText(String.format("$%.2f", price));
        tvQuantity.setText(quantity + " Packet");
        //-------------------Setup-----------------


        //=========================RemoveButtonHandle========================
        tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBCusOrderTemp db = new DBCusOrderTemp(context);
                db.deleteCusItem(id);
                db.close();

                //Refresh List To Be Included

            }
        });
        //=========================RemoveButtonHandle========================

        return rowView;
    }
}
