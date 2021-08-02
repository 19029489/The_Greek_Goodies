package com.example.thegreekgoodies;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    ListView lv;
    ArrayAdapter aa;
    ArrayList<CustomerOrder> newCus;
    TextView tvSubText, tvSubTotal;
    Button btnCheckout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        //==================MatchingGame==========================
        lv = (ListView) v.findViewById(R.id.lvlist);
        tvSubText = v.findViewById(R.id.SubTotalText);
        tvSubTotal = v.findViewById(R.id.subTotal);
        btnCheckout = v.findViewById(R.id.btnCheckout);
        //==================MatchingGame==========================

        //==================Setup==========================
        tvSubText.setText("Subtotal");
        //==================Setup==========================

        //-------------------DBSetupArray-------------------
        DBCusOrderTemp dbh = new DBCusOrderTemp(getActivity());
        newCus = new ArrayList<CustomerOrder>();
        newCus.clear();
        newCus.addAll(dbh.getCustomerData());
        dbh.close();
        //-------------------------IfListEmptyHandle------------------------
        if (newCus.size() < 1){
            Intent intent = new Intent(getActivity(), CartEmpty.class);
            startActivity(intent);
        }
        else {
            aa = new SummarylistAdapter(getActivity(), R.layout.summarylist, newCus);
            lv.setAdapter(aa);
        }
        //-------------------------IfListEmptyHandle------------------------
        //-------------------DBSetupArray-------------------


        //==================------------SubTotal-----------=========================
        double totalPrice = 0;
        for (int i = 0; i < newCus.size(); i++ ){
            double price = dbh.getCustomerData().get(i).itemPrice;
            totalPrice = totalPrice + price;
        }
        tvSubTotal.setText("$" + totalPrice);

        //==================------------SubTotal-----------=========================


        //==============================BtnCheckOutHandle=========================
        double finalTotalPrice = totalPrice;
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Checkout.class);
                i.putExtra("totalprice", String.valueOf(finalTotalPrice));
                startActivity(i);
            }
        });
        //==============================BtnCheckOutHandle=========================

        return v;
    }

}






