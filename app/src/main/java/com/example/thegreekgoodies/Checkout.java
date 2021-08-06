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

    TextView tvSTotal, tvShipping, tvTotal, tvReturnBtn, tvLoginBtn ;
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
            tvLoginBtn = v.findViewById(R.id.tvLoginBtn);
            //==================MatchingGame==========================

            //=====================Setup==========================
            tvSTotal.setText(String.format("$%.2f", Double.parseDouble(TotalPrice)));
            double total = Double.valueOf(TotalPrice) + 1.8;
            tvTotal.setText(String.format("$%.2f", total));
            btnContinue.setText("CONTINUE");
            checkSharedPref();
            //=====================Setup==========================

            //---------------------------------SharedPref-------------------------------------------
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("totalprices", String.valueOf(total));

            editor.commit();
            //---------------------------------SharedPref-------------------------------------------



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

                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
                        String apikey = pref.getString("apikey", "");


                        if(apikey.isEmpty() == false) {
                            Intent intent = new Intent(getActivity(), PaymentGateway.class);
                            startActivity(intent);
                        }

                        else if(apikey.isEmpty() == true){
                            Fragment signInFrag = new SignInFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, signInFrag)
                                    .addToBackStack(null)
                                    .commit();
                        }
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

    private void checkSharedPref(){
        //------------------------GetSharedPrefData----------------------------
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String newsContact = prefs.getString("newsContact", "");
        String firstName = prefs.getString("firstName", "");
        String lastName = prefs.getString("lastName", "");
        String address = prefs.getString("address", "");
        String apartment = prefs.getString("apartment", "");
        String city = prefs.getString("city", "");
        String country = prefs.getString("country", "");
        String postal = prefs.getString("postal", "");
        String number = prefs.getString("number", "");
        String cbCheck = prefs.getString("cbCheck", "");
        //------------------------GetSharedPrefData----------------------------

        if (newsContact!=null && firstName!=null && lastName!=null && address!=null && apartment!=null && city!=null && country!=null && postal!=null &&
                number!=null && cbCheck!=null){
            etEmailPhone.setText(newsContact);
            etAddress.setText(address);
            etApartment.setText(apartment);
            etCity.setText(city);
            etCountry.setText(country);
            etFirstName.setText(firstName);
            etLastName.setText(lastName);
            etPhoneNumber.setText(number);
            etPostalCode.setText(postal);

            if (cbCheck == "Yes"){
                cbOffer.setChecked(true);
            }
            else if (cbCheck == "No"){
                cbOffer.setChecked(false);
            }
        }
    }
    //==========================Constructor=========================

}