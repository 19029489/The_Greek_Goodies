package com.example.thegreekgoodies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PaymentGateway extends Fragment implements PaymentResultListener {

    TextView tvSet;
    private AsyncHttpClient client;
    ArrayAdapter aa;
    ArrayList<CustomerOrder> al;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment_gateway, container, false);
        Checkout.preload(getContext());
        client = new AsyncHttpClient();
        tvSet = v.findViewById(R.id.tvSet);

        checkout();


        return  v;
    }


    //====================================Function=========================================
    private void checkout() {
        //------------------------GetSharedPrefData----------------------------
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String totalPrice = prefs.getString("totalprices", "");
        double a = Double.valueOf(totalPrice);
        a = a * 100;
        //------------------------GetSharedPrefData----------------------------

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_dJMePFpITDSfJY");
        checkout.setImage(R.drawable.ic_launcher_foreground);
        final Activity activity = getActivity();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "The Greek Goodies");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");

            // options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#000000");
            options.put("currency", "SGD");
            options.put("amount", String.valueOf(a));//300 X 100
            checkout.open(activity, options);

            System.out.println("===========================");
            System.out.println("WonderFul");
            System.out.println("===========================");
        }
        catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }
    //====================================Function=========================================


    //================================HandleEvent==============================
    @Override
    public void onPaymentSuccess(String s) {
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
        String userId = prefs.getString("userId", "");
        String cbCheck = prefs.getString("cbCheck", "");
        //------------------------GetSharedPrefData----------------------------

        //==============================HandleCusOrderAdd=============================
        DBCusOrderTemp db = new DBCusOrderTemp(getContext());
        for (int i = 0; i < db.getCustomerData().size(); i++) {
            String name = db.getCustomerData().get(i).itemName;
            String price = String.valueOf(db.getCustomerData().get(i).itemPrice);
            String quantity = String.valueOf(db.getCustomerData().get(i).quantity);
            RequestParams params = new RequestParams();
            params.add("userid", userId);
            params.add("productname", name);
            params.add("quantity", quantity);
            params.add("totalprice", price);
            client.post("http://10.0.2.2/greek_goodies/addCusOrder.php", params, new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    db.close();
                }
            });
        }
        //==============================HandleCusOrderAdd=============================


        //==============================HandleDetailsAdd=============================
        RequestParams paramsB = new RequestParams();
        paramsB.add("userid", userId);
        paramsB.add("firstname", firstName);
        paramsB.add("lastname", lastName);
        paramsB.add("address", address);
        paramsB.add("apartment", apartment);
        paramsB.add("city", city);
        paramsB.add("country", country);
        paramsB.add("postal", postal);
        paramsB.add("phonenum", number);
        paramsB.add("upnews", cbCheck);
        paramsB.add("upcontact", newsContact);

        client.post("http://10.0.2.2/greek_goodies/addCusInfo.php", paramsB, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //-----------------------------RemoveAllItemsOnceDone--------------------------
                //Clear SQLite false Database
                //Wipe listView clean
                //-----------------------------RemoveAllItemsOnceDone--------------------------

                Fragment home = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, home)
                        .addToBackStack(null)
                        .commit();

                Toast.makeText(getActivity(), "Payment Successful. Thank you for purchasing with us.",
                        Toast.LENGTH_LONG).show();
            }
        });
        //==============================HandleDetailsAdd=============================
    }


    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getActivity(), "Payment Unsuccessful. Please try again.",
                Toast.LENGTH_LONG).show();

        tvSet.setText(s);
        Fragment ret = new com.example.thegreekgoodies.Checkout();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, ret)
                .addToBackStack(null)
                .commit();
    }
    //================================HandleEvent==============================


}