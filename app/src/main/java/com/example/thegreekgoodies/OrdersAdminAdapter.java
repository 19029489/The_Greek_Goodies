package com.example.thegreekgoodies;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrdersAdminAdapter extends ArrayAdapter<ArrayList<Orders>> {

    private ArrayList<ArrayList<Orders>> alOrders;
    private Context context;
    private AsyncHttpClient client;

    private TextView tvAddress, tvOrderNum, tvUser;
    private Button btn;

    public OrdersAdminAdapter(Context context, int resource, ArrayList<ArrayList<Orders>> objects){
        super(context, resource, objects);
        alOrders = objects;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.orders_admin_row, parent, false);

        tvAddress = (TextView) rowView.findViewById(R.id.tvOrderAddress);
        tvOrderNum = (TextView) rowView.findViewById(R.id.tvOrderNumberAdmin);
        tvUser = (TextView) rowView.findViewById(R.id.tvOrderUser);
        btn = (Button) rowView.findViewById(R.id.btnOrders);

        ArrayList<Orders> currentArrayList = alOrders.get(position);

        if (currentArrayList.size() != 0) {
            tvOrderNum.setText("Order #: " + currentArrayList.get(0).getUserId() + currentArrayList.get(0).getSet());

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            String apikey = prefs.getString("apikey", "");

            client = new AsyncHttpClient();
            ArrayList<Address> al = new ArrayList<Address>();

            RequestParams params = new RequestParams();
            params.add("user_id", currentArrayList.get(0).getUserId());
            params.add("apikey", apikey);

            //for real devices, use the current location's ip address
            client.post("http://10.0.2.2/TheGreekGoodies/getAddressesById.php", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {

                    try {
                        Log.i("Addresses: ", response.toString());

                        for (int i = 0; i < response.length(); i++) {

                            JSONObject jsonObj = response.getJSONObject(i);

                            String addressId = jsonObj.getString("address_id");
                            String userId = jsonObj.getString("user_id");
                            String firstname = jsonObj.getString("firstname");
                            String lastname = jsonObj.getString("lastname");
                            String company = jsonObj.getString("company");
                            String address1 = jsonObj.getString("address1");
                            String address2 = jsonObj.getString("address2");
                            String city = jsonObj.getString("city");
                            String country = jsonObj.getString("country");
                            String postalcode = jsonObj.getString("postalcode");
                            String phone = jsonObj.getString("phone");
                            Integer defaultaddress = jsonObj.getInt("defaultaddress");

                            if (defaultaddress == 1) {
                                tvAddress.setText("" + address1 + " " + address2 + " " + city + " " + postalcode);
                            }

                            if (defaultaddress == 1) {
                                tvAddress.setText("" + address1 + " " + address2 + " " + city + " " + postalcode);
                                tvUser.setText("" + phone + " - " + firstname + " " + lastname );
                            }

                            Address add = new Address(addressId, userId, firstname, lastname, company, address1, address2, city, country, postalcode, phone, defaultaddress);

                            al.add(add);
                        }

                        if (tvAddress.getText().toString().equalsIgnoreCase("")) {
                            tvAddress.setText("" + al.get(0).getAddress1() + " " + al.get(0).getAddress2() + " " + al.get(0).getCity() + " " + al.get(0).getPostalCode());
                            tvUser.setText("" + al.get(0).getPhone() + " - " + al.get(0).getFirstname() + " " + al.get(0).getLastname() );
                        }


                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }//end onSuccess
            });

            if (currentArrayList.get(0).getStatus().equalsIgnoreCase("paid")){
                btn.setText("Assign Driver");
                btn.setEnabled(false);
            } else if (currentArrayList.get(0).getStatus().equalsIgnoreCase("requesting")){
                btn.setText("Requesting Driver " + currentArrayList.get(0).getRider() + "...");
                btn.setEnabled(false);
            } else {
                btn.setText("Driver: " + currentArrayList.get(0).getRider());
                btn.setEnabled(false);
            }


        }


        return rowView;
    }

}

