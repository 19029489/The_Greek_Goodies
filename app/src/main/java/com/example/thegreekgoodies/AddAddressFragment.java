package com.example.thegreekgoodies;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAddressFragment extends Fragment {

    EditText etFirst, etLast, etCompany, etAdd1, etAdd2, etPostal, etPhone;
    CheckBox cbDefault;
    Button btnCancel, btnAdd;

    private AsyncHttpClient client;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAddressFragment newInstance(String param1, String param2) {
        AddAddressFragment fragment = new AddAddressFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_address, container, false);

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

        client = new AsyncHttpClient();

        etFirst = v.findViewById(R.id.etFirstName);
        etLast = v.findViewById(R.id.etLastName);
        etCompany = v.findViewById(R.id.etCompany);
        etAdd1 = v.findViewById(R.id.etAddress1);
        etAdd2 = v.findViewById(R.id.etAddress2);
        etPostal = v.findViewById(R.id.etPostalCode);
        etPhone = v.findViewById(R.id.etPhone);

        cbDefault = v.findViewById(R.id.cbDefault);

        btnAdd = v.findViewById(R.id.btnAdd);
        btnCancel = v.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //only company is optional
                if (etFirst.getText().toString().equalsIgnoreCase("") || etLast.getText().toString().equalsIgnoreCase("") || etAdd1.getText().toString().equalsIgnoreCase("") || etAdd2.getText().toString().equalsIgnoreCase("") || etPostal.getText().toString().equalsIgnoreCase("") || etPhone.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please fill in all the blanks. Only company is optional.", Toast.LENGTH_SHORT).show();
                } else {
                    //check phone
                   if (etPhone.getText().toString().trim().length() != 8) {
                       etPhone.setError("Please input a valid phone number");
                   } else if (etPostal.getText().toString().trim().length() != 6){
                       etPostal.setError("Please input a valid postal code");
                   } else {
                       AddAddress(v);
                   }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment profileFrag = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, profileFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return v;
    }

    private void AddAddress(View v) {

        String firstname = etFirst.getText().toString();
        String lastname = etLast.getText().toString();
        String company = etCompany.getText().toString();
        String add1 = etAdd1.getText().toString();
        String add2 = etAdd2.getText().toString();
        String postal = etPostal.getText().toString();
        String phone = etPhone.getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String apikey = prefs.getString("apikey", "");
        String userId = prefs.getString("userId", "");

        String isDefault = "";
        if (cbDefault.isChecked() == true) {
            isDefault = "1";
        } else {
            isDefault = "0";
        }

        RequestParams params = new RequestParams();
        params.add("apikey", apikey);
        params.add("user_id", userId);
        params.add("firstname", firstname);
        params.add("lastname", lastname);
        params.add("company", company);
        params.add("add1", add1);
        params.add("add2", add2);
        params.add("postal", postal);
        params.add("phone", phone);
        params.add("default", isDefault);
        params.add("upnews", "No");
        params.add("upcontact", phone);

        //for real devices, use the current location's ip address
        client.post("http://10.0.2.2/TheGreekGoodies/addAddress.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Toast.makeText(getActivity(), "Address created successfully", Toast.LENGTH_LONG).show();

                Fragment profileFrag = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, profileFrag)
                        .addToBackStack(null)
                        .commit();

            }//end onSuccess
        });

    }

}