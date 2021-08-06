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
        //------------------------SpecialPref----------------------------
        SharedPreferences specialPref = PreferenceManager.getDefaultSharedPreferences(PaymentGateway.this);
        SharedPreferences.Editor editor = specialPref.edit();
        editor.putString("setMeal", String.valueOf(1));
        editor.commit();
        //------------------------SpecialPref----------------------------


        //------------------------GetSharedPrefData----------------------------
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String newsContact = prefs.getString("newsContact", "");
        String firstName = prefs.getString("firstName", "");
        String lastName = prefs.getString("lastName", "");
        String address = prefs.getString("address", "");
        String apartment = prefs.getString("apartment", "");
//        String city = prefs.getString("city", "");
//        String country = prefs.getString("country", "");
        String postal = prefs.getString("postal", "");
        String number = prefs.getString("number", "");
        String userId = prefs.getString("userId", "");
        String cbCheck = prefs.getString("cbCheck", "");
        String setMeal = prefs.getString("setMeal", "");
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
            params.add("set", setMeal);


            System.out.println("==========================");
            System.out.println(params);
            System.out.println("==========================");
            client.post("http://10.0.2.2/TheGreekGoodies/addCusOrder.php", params, new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    System.out.println("==========================");
                    System.out.println("Completed Item");
                    System.out.println("==========================");
                    db.close();
                }
            });
        }

        setMeal = String.valueOf(Integer.parseInt(setMeal) + 1);
        //==============================HandleCusOrderAdd=============================


        //==============================HandleDetailsAdd=============================
        RequestParams paramsB = new RequestParams();
        paramsB.add("user_id", userId);
        paramsB.add("firstname", firstName);
        paramsB.add("lastname", lastName);
        paramsB.add("add1", address);
        paramsB.add("add2", apartment);
        paramsB.add("postal", postal);
        paramsB.add("phone", number);
        paramsB.add("upnews", cbCheck);
        paramsB.add("default", String.valueOf(0));
        paramsB.add("upcontact", newsContact);


        System.out.println("==========================");
        System.out.println(paramsB);
        System.out.println("==========================");
        client.post("http://10.0.2.2/TheGreekGoodies/addAddress.php", paramsB, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //-----------------------------RemoveAllItemsOnceDone--------------------------
                //Clear SQLite false Database
                //Wipe listView clean
                //-----------------------------RemoveAllItemsOnceDone--------------------------
                System.out.println("==========================");
                System.out.println("Completed Address");
                System.out.println("==========================");

                Intent intent = new Intent(PaymentGateway.this, MainActivity.class);
                startActivity(intent);

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