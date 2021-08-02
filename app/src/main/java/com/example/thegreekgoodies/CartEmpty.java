package com.example.thegreekgoodies;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CartEmpty extends Fragment {

    TextView tvEmpty, tvContinue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart_empty, container, false);
        //==================MatchingGame==========================
        tvEmpty = v.findViewById(R.id.tvEmpty);
        tvContinue = v.findViewById(R.id.tvContinue);
        //==================MatchingGame==========================

        //==================Setup==========================
        tvEmpty.setText("Your cart is currently empty.");
        tvContinue.setText("Continue browsing here.");
        //==================Setup==========================

        return v;
    }
}