package com.example.thegreekgoodies;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<ArrayList<Orders>> {

    private ArrayList<ArrayList<Orders>> alOrders;
    private Context context;

    private LinearLayout llOrder;

    private TextView tvTotalPrice, tvOrderNum;

    public OrderAdapter(Context context, int resource, ArrayList<ArrayList<Orders>> objects){
        super(context, resource, objects);
        alOrders = objects;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.orderlist_row, parent, false);

        tvTotalPrice = (TextView) rowView.findViewById(R.id.tvTotalPrice);
        tvOrderNum = (TextView) rowView.findViewById(R.id.tvOrderNumberUser);

        llOrder = (LinearLayout) rowView.findViewById(R.id.llOrders);

        ArrayList<Orders> currentArrayList = alOrders.get(position);

        double total = 0.0;

        for (int i = 0; i < currentArrayList.size(); i++){
            LinearLayout llHorizontal = new LinearLayout(context);

            llHorizontal.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            llHorizontal.setOrientation(LinearLayout.HORIZONTAL);

            llOrder.addView(llHorizontal);

            Resources r = getContext().getResources();
            
            float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics());

            Typeface font = getContext().getResources().getFont(R.font.gothic);

            TextView tvProduct = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 3;
            tvProduct.setLayoutParams(params);
            tvProduct.setTypeface(font);
            tvProduct.setTextColor(getContext().getResources().getColor(R.color.black));
            tvProduct.setText(currentArrayList.get(i).getProductname());

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params2.weight = 1;
            params2.gravity = Gravity.CENTER_HORIZONTAL;

            TextView tvQuantity = new TextView(getContext());
            tvQuantity.setLayoutParams(params2);
            tvQuantity.setTypeface(font);
            tvQuantity.setTextColor(getContext().getResources().getColor(R.color.black));
            tvQuantity.setText("" + currentArrayList.get(i).getQuantity());

            TextView tvPrice = new TextView(getContext());
            tvPrice.setLayoutParams(params2);
            tvPrice.setTypeface(font);
            tvPrice.setTextColor(getContext().getResources().getColor(R.color.black));
            tvPrice.setText("" + currentArrayList.get(i).getTotalprice());

            llHorizontal.addView(tvProduct);
            llHorizontal.addView(tvQuantity);
            llHorizontal.addView(tvPrice);

            total += currentArrayList.get(i).getTotalprice();
        }

        tvOrderNum.setText("Order Id: " + currentArrayList.get(0).getSet());

        tvTotalPrice.setText("Total Price: $" + total);

        return rowView;
    }

}
