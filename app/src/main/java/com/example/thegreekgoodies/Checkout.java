package com.example.thegreekgoodies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        if (getArguments() != null) {
            String TotalPrice = getArguments().getString("totalprice");

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
            tvSTotal.setText(String.format("$%.2f", Double.parseDouble(TotalPrice)));
            double total = Double.valueOf(TotalPrice) + 1.8;
            tvTotal.setText(String.format("$%.2f", total));
            //=====================Setup==========================

            //---------------------------------SharedPref-------------------------------------------
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("totalprices", String.valueOf(total));
            editor.commit();
            //---------------------------------SharedPref-------------------------------------------


            //----------------------LoginTVTextHandle----------------------
//        tvBtnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Checkout.this, Login.class);
//                startActivity(intent);
//            }
//        });
            //----------------------LoginTVTextHandle----------------------

            //----------------------ReturnCartHandle----------------------
            tvReturnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Summarylist.class);
                    startActivity(intent);
                }
            });
            //----------------------ReturnCartHandle----------------------


            //----------------------ContinueButton----------------------
            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkEntry();
                    if (checkEntry() == false) {
                        Toast.makeText(getActivity(), "Certain fills cannot be empty",
                                Toast.LENGTH_LONG).show();
                    } else {
                        //---------------------------------SharedPref-------------------------------------------
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = prefs.edit();
                        //---------------------------------SharedPref-------------------------------------------

                        checkBox();
                        if (checkBox() == true) {
                            editor.putString("cbCheck", "Yes");
                        } else {
                            editor.putString("cbCheck", "No");
                        }
                        //------------------------ThrowAllInEditor-------------------------------
                        editor.putString("newsContact", etEmailPhone.getText().toString());
                        editor.putString("firstName", etFirstName.getText().toString());
                        editor.putString("lastName", etLastName.getText().toString());
                        editor.putString("address", etAddress.getText().toString());
                        editor.putString("apartment", etApartment.getText().toString());
                        editor.putString("city", etCity.getText().toString());
                        editor.putString("country", etCountry.getText().toString());
                        editor.putString("postal", etPostalCode.getText().toString());
                        editor.putString("number", etPhoneNumber.getText().toString());
                        editor.commit();
                        //------------------------ThrowAllInEditor-------------------------------

//                        Fragment signInFrag = new SignInFragment();
//
//                        getActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.content_frame, signInFrag)
//                                .addToBackStack(null)
//                                .commit();
                    }

                }
            });
            //----------------------ContinueButton----------------------
        }

        return v;

    }



    //==========================Constructor=========================
    private boolean checkBox(){
        if (cbOffer.isChecked() == true){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean checkEntry(){
        String ep = etEmailPhone.getText().toString();
        String ln = etEmailPhone.getText().toString();
        String ad = etEmailPhone.getText().toString();
        String ct = etEmailPhone.getText().toString();
        String c = etEmailPhone.getText().toString();
        String pc = etEmailPhone.getText().toString();
        String pn = etEmailPhone.getText().toString();

        if (ep.isEmpty() || ln.isEmpty() || ad.isEmpty() || ct.isEmpty() || c.isEmpty() || pc.isEmpty() || pn.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }
    //==========================Constructor=========================

}