package com.example.thegreekgoodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class RiderAdapter extends ArrayAdapter<Users> {

    private ArrayList<Users> rider;
    private Context context;

    private TextView tvId, tvName, tvEmail, tvContact;
    private ImageView ivPhoto;

    public RiderAdapter(Context context, int resource, ArrayList<Users> objects){
        super(context, resource, objects);
        rider = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rider_row, parent, false);

        tvId = (TextView) rowView.findViewById(R.id.tvRiderId);
        tvName = (TextView) rowView.findViewById(R.id.tvFullNameRider);
        tvEmail = (TextView) rowView.findViewById(R.id.tvRiderEmail);
        tvContact = (TextView) rowView.findViewById(R.id.tvContactRider);

        ivPhoto = (ImageView) rowView.findViewById(R.id.ivPhoto);

        Users currentRider = rider.get(position);

        tvId.setText("ID: " + currentRider.getId());

        tvName.setText(currentRider.getFirstname() + " " + currentRider.getLastname());

        tvEmail.setText(currentRider.getEmail());

        tvContact.setText(currentRider.getContact());

        return rowView;
    }

}
