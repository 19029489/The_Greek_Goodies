package com.example.thegreekgoodies;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewAddressesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAddressesFragment extends Fragment {

    Button btnAdd;
    TextView tvBack;
    private ArrayList<Address> al;
    private ArrayAdapter aa;
    private ListView lv;
    private AsyncHttpClient client;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewAddressesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewAddresses.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewAddressesFragment newInstance(String param1, String param2) {
        ViewAddressesFragment fragment = new ViewAddressesFragment();
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
        View v = inflater.inflate(R.layout.fragment_view_addresses, container, false);

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

        btnAdd = v.findViewById(R.id.btnAddAddress);
        tvBack = v.findViewById(R.id.backBtn);
        lv = (ListView) v.findViewById(R.id.lvAddresses);

        client = new AsyncHttpClient();

        al = new ArrayList<Address>();
        aa = new AddressAdapter(getActivity(), R.layout.address_row, al);
        lv.setAdapter(aa);

        RequestParams params = new RequestParams();
        params.add("user_id", userId);
        params.add("apikey", apikey);

        //for real devices, use the current location's ip address
        client.post("http://10.0.2.2/TheGreekGoodies/getAddressesById.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {

                try {
                    Log.i("Addresses List: ", response.toString());

                    al.clear();

                    if (response.length() == 0){
                        aa.notifyDataSetChanged();
                    } else {
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

                            Address address = new Address(addressId, userId, firstname, lastname, company, address1, address2, city, country, postalcode, phone, defaultaddress);

                            if (defaultaddress == 1) {
                                al.add(0, address);
                            } else {
                                al.add(address);
                            }
                        }
                    }

                    aa.notifyDataSetChanged();

                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }//end onSuccess

        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Address selected = al.get(position);

                Fragment EditFrag = new EditAddressFragment();
                Bundle args = new Bundle();
                args.putString("addressId", selected.getAddressId());
                args.putString("firstname", selected.getFirstname());
                args.putString("lastname", selected.getLastname());
                args.putString("company", selected.getCompany());
                args.putString("add1", selected.getAddress1());
                args.putString("add2", selected.getAddress2());
                args.putString("postal", selected.getPostalCode());
                args.putString("phone", selected.getPhone());
                args.putInt("default", selected.getDefaultAddress());

                EditFrag.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, EditFrag)
                        .addToBackStack(null)
                        .commit();

            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment profileFrag = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, profileFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addFrag = new AddAddressFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, addFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return v;
    }
}