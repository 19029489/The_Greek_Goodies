package com.example.thegreekgoodies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

public class PaymentGateway extends AppCompatActivity implements PaymentResultListener {

    TextView tvSet;
    private AsyncHttpClient client;
    ArrayAdapter aa;
    ArrayList<CustomerOrder> al;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_payment_gateway);
        Checkout.preload(getApplicationContext());
        client = new AsyncHttpClient();
        tvSet = findViewById(R.id.tvSet);

        checkout();


    }


    //====================================Function=========================================
    private void checkout() {
        //------------------------GetSharedPrefData----------------------------
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String totalPrice = prefs.getString("totalprices", "");
        double a = Double.valueOf(totalPrice);
        double capture = a * 100;
        //------------------------GetSharedPrefData----------------------------

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_dJMePFpITDSfJY");
        checkout.setImage(R.drawable.ic_launcher_foreground);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put("name", "The Greek Goodies");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");

            // options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#000000");
            options.put("currency", "SGD");
            options.put("amount", String.format("%.2f",capture));//300 X 100
            checkout.open(activity, options);

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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
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
        DBCusOrderTemp db = new DBCusOrderTemp(getApplicationContext());
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
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame_main, home)
                        .addToBackStack(null)
                        .commit();

                Toast.makeText(PaymentGateway.this, "Payment Successful. Thank you for purchasing with us.",
                        Toast.LENGTH_LONG).show();
            }
        });
        //==============================HandleDetailsAdd=============================
    }


    @Override
    public void onPaymentError(int i, String s) {
        tvSet.setText(s);
        Fragment home = new Summarylist();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame_main, home)
                .addToBackStack(null)
                .commit();

        Toast.makeText(PaymentGateway.this, "Payment Unsuccessful. Please try again.",
                Toast.LENGTH_LONG).show();

    }
    //================================HandleEvent==============================




}