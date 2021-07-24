package com.example.thegreekgoodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Item> {
    Context context;
    ArrayList<Item> items;
    int resource;
    TextView tvName, tvBlank, tvPrice;
    ImageView ivImage;

    public ItemAdapter(Context context, int resource, ArrayList<Item> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.itemlist, parent, false);

        //-----------------Casting------------------
        tvName = rowView.findViewById(R.id.itemName);
        tvBlank = rowView.findViewById(R.id.blankSpace);
        tvPrice = rowView.findViewById(R.id.price);
        ivImage = rowView.findViewById(R.id.itemImage);
        //-----------------Casting------------------

        Item item = items.get(position);

        //------------------Getting--------------
        String name = item.getItemName();
        double price = item.getItemPrice();
        String photo = item.getItemPhoto();
        //------------------Getting--------------

        //-------------------Setup-----------------
        tvName.setText(name);
        tvBlank.setText("-----------------------------------------");
        tvPrice.setText(String.format("$%.2f", price));
        Glide.with(context).load(photo).into(ivImage);
        //-------------------Setup-----------------

        return rowView;
    }
}
