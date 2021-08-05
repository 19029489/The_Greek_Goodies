package com.example.thegreekgoodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<Users> {

    private ArrayList<Users> user;
    private Context context;

    private TextView tvId, tvName, tvEmail;

    public UserAdapter(Context context, int resource, ArrayList<Users> objects){
        super(context, resource, objects);
        user = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.user_row, parent, false);

        tvId = (TextView) rowView.findViewById(R.id.tv_Userid);
        tvName = (TextView) rowView.findViewById(R.id.tvFullNameUser);
        tvEmail = (TextView) rowView.findViewById(R.id.tvUserEmail);

        Users currentUser = user.get(position);

        tvId.setText("ID: " + currentUser.getId());

        tvName.setText(currentUser.getFirstname() + " " + currentUser.getLastname());

        tvEmail.setText(currentUser.getEmail());

        return rowView;
    }

}