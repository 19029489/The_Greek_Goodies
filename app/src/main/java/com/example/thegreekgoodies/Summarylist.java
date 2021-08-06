package com.example.thegreekgoodies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Summarylist extends Fragment {

    ListView lv;
    ArrayAdapter aa;
    ArrayList<CustomerOrder> newCus;
    TextView tvSubText, tvSubTotal;
    Button btnCheckout, btnAddMore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_summarylist, container, false);

        //==================MatchingGame==========================
        lv = (ListView) v.findViewById(R.id.lvlist);
        tvSubText = v.findViewById(R.id.SubTotalText);
        tvSubTotal = v.findViewById(R.id.subTotal);
        btnCheckout = v.findViewById(R.id.btnCheckout);
        btnAddMore = v.findViewById(R.id.btnReturn);
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
            Fragment cartEmptyFrag = new CartEmpty();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, cartEmptyFrag)
                    .addToBackStack(null)
                    .commit();
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
        tvSubTotal.setText(String.format("$%.2f",totalPrice));
        //==================------------SubTotal-----------=========================


        //==============================BtnCheckOutHandle=========================
        double finalTotalPrice = totalPrice;
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment checkoutFrag = new Checkout();
                Bundle args = new Bundle();
                args.putString("totalprice", String.valueOf(finalTotalPrice));
                checkoutFrag.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, checkoutFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });
        //==============================BtnCheckOutHandle=========================

        //==============================BtnAddMore=========================
        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment mainMenu = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, mainMenu)
                        .addToBackStack(null)
                        .commit();
            }
        });
        //==============================BtnAddMore=========================




    return v;
    }



}