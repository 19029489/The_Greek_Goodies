package com.example.thegreekgoodies;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Checkout extends Fragment {

    TextView tvSTotal, tvShipping, tvTotal, tvBtnLogin, tvReturnBtn ;
    EditText etEmailPhone, etFirstName, etLastName, etAddress, etApartment, etCity, etCountry, etPostalCode, etPhoneNumber;
    Button btnContinue;
    CheckBox cbOffer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checkout, container, false);

        Intent intent = getActivity().getIntent();
        String TotalPrice = intent.getStringExtra("totalprice");

        //==================MatchingGame==========================
        //----------------------TextView------------------
        tvSTotal = v.findViewById(R.id.tvSubtotal);
        tvShipping = v.findViewById(R.id.tvShipping);
        tvTotal = v.findViewById(R.id.tvTotal);
        tvBtnLogin = v.findViewById(R.id.tvLoginBtn);
        tvReturnBtn = v.findViewById(R.id.tvReturnBtn);

        //----------------------EditText------------------
        etEmailPhone = v.findViewById(R.id.etEmailPhone);
        etFirstName = v.findViewById(R.id.etFirstName);
        etLastName = v.findViewById(R.id.etLastName);
        etAddress = v.findViewById(R.id.etAddress);
        etApartment = v.findViewById(R.id.etApartment);
        etCity = v.findViewById(R.id.etCity);
        etCountry = v.findViewById(R.id.etCountry);
        etPostalCode = v.findViewById(R.id.etPostalCode);
        etPhoneNumber = v.findViewById(R.id.etPhoneNumber);

        //----------------------Button/CheckBox------------------
        cbOffer = v.findViewById(R.id.cbOffer);
        btnContinue = v.findViewById(R.id.btnContinue);
        //==================MatchingGame==========================


        //=====================Setup==========================
        tvSTotal.setText("$" + TotalPrice);
        double total = Double.valueOf(TotalPrice) + 1.8;
        tvTotal.setText("$" + total);
        //=====================Setup==========================


        //----------------------LoginTVTextHandle----------------------
        tvBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //----------------------LoginTVTextHandle----------------------

        //----------------------ReturnCartHandle----------------------
        tvReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CartFragment.class);
                startActivity(intent);
            }
        });
        //----------------------ReturnCartHandle----------------------


        //----------------------ContinueButton----------------------
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CheckEntire
                //PassDB
                //NextPage
                //Payment
                //if success transfer to actual db
            }
        });
        //----------------------ContinueButton----------------------


        return v;
    }
}