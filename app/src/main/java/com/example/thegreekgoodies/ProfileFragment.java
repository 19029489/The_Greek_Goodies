package com.example.thegreekgoodies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    Button btnLogOut;
    TextView tvOrderHistory, tvAddress, tvViewAddresses;

    private AsyncHttpClient client;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String role = prefs.getString("role", "");
        String apikey = prefs.getString("apikey", "");
        String userId = prefs.getString("userId", "");

        if (role.equalsIgnoreCase("") || apikey.equalsIgnoreCase("") || userId.equalsIgnoreCase("")) {
            //guest
            Fragment signInFrag = new SignInFragment();
            Fragment userFrag = new UserFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, signInFrag)
                    .replace(R.id.content_frame_main,userFrag)
                    .addToBackStack(null)
                    .commit();
        }

        btnLogOut = v.findViewById(R.id.btnLogOutUser);
        tvAddress = v.findViewById(R.id.tvAddress);
        tvViewAddresses = v.findViewById(R.id.tvViewAddresses);

        client = new AsyncHttpClient();

        //Getting addresses
        RequestParams params = new RequestParams();
        params.add("user_id", userId);
        params.add("apikey", apikey);

        //for real devices, use the current location's ip address
        client.post("http://10.0.2.2/TheGreekGoodies/getAddressesById.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {

                try {
                    Log.i("Addresses: ", response.toString());

                    if (response.length() == 0) {
                        tvAddress.setText("You haven't added any addresses yet.");
                        tvViewAddresses.setText("View Addresses (0)");
                    } else {

                        tvViewAddresses.setText("View Addresses ("  + response.length() + ")");

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
                            Boolean defaultaddress = jsonObj.getBoolean("defaultaddress");

                            Address address = new Address(addressId, userId, firstname, lastname, company, address1, address2, city, country, postalcode, phone, defaultaddress);

                            if (defaultaddress == true) {
                                tvAddress.setText(address.toString());
                            }
                        }
                    }

                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }//end onSuccess
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();

                Toast.makeText(getActivity(), "Successfully Logged Out", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        return v;
    }
}