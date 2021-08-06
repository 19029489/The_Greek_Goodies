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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewOrdersFragment extends Fragment {

    private ListView lv;
    private ArrayList<ArrayList<Orders>> al;
    private ArrayAdapter aa;
    private String status;
    private Spinner spn;
    int highestNum = 0;
    int highestNumUser = 0;
    int totalOrders = 0;

    private AsyncHttpClient client;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewOrdersFragment newInstance(String param1, String param2) {
        ViewOrdersFragment fragment = new ViewOrdersFragment();
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

        View v = inflater.inflate(R.layout.fragment_view_orders, container, false);

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

        lv = v.findViewById(R.id.lvOrdersAdmin);
        al = new ArrayList<ArrayList<Orders>>();
        aa = new OrdersAdminAdapter(getActivity(), R.layout.orders_admin_row, al);
        lv.setAdapter(aa);
        spn = v.findViewById(R.id.spinStatus);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);

                if (spn.getSelectedItem().toString().equalsIgnoreCase("new")) {
                    status = "paid";
                } else if (spn.getSelectedItem().toString().equalsIgnoreCase("requesting")) {
                    status = "requesting";
                } else if (spn.getSelectedItem().toString().equalsIgnoreCase("delivering")) {
                    status = "delivering";
                } else {
                    status = "completed";
                }


                //retrieve users
                RequestParams params = new RequestParams();
                params.add("user_id", userId);
                params.add("apikey", apikey);
                params.add("status", status);
                //for real devices, use the current location's ip address
                client.post("http://10.0.2.2/TheGreekGoodies/getOrdersByStatus.php", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {

                        try {
                            Log.i("Orders: ", response.toString());

                            ArrayList<Orders> alOrders = new ArrayList<Orders>();

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObj = response.getJSONObject(i);

                                String orderId = jsonObj.getString("order_id");
                                String userId = jsonObj.getString("user_id");
                                String productName = jsonObj.getString("productname");
                                int quantity = jsonObj.getInt("quantity");
                                double totalprice = jsonObj.getDouble("totalprice");
                                String status = jsonObj.getString("status");
                                String rider = jsonObj.getString("rider");
                                int set = jsonObj.getInt("set");

                                Orders order = new Orders(orderId, userId, productName, quantity, totalprice, status, rider, set);

                                alOrders.add(order);
                            }

                            Log.i("alOrders", "" + alOrders);

                            for (int o = 0; o < alOrders.size(); o++){
                                if (Integer.parseInt(alOrders.get(o).getUserId()) > highestNumUser){
                                    highestNumUser = Integer.parseInt(alOrders.get(o).getUserId());
                                }
                            }

                            Log.i("Number of Users", "" + highestNumUser);

                            for (int w = 0; w < highestNumUser; w++ ){
                                totalOrders += highestNum;
                                for (int a = 0; a < alOrders.size(); a++){
                                    if (alOrders.get(a).getSet() > highestNum){
                                        highestNum = alOrders.get(a).getSet();
                                    }
                                }
                            }

                            Log.i("Total number of Orders", "" + highestNum);

                            for (int w = 0; w < highestNumUser; w++ ){
                                for (int u = 0; u < highestNum; u++) {
                                    ArrayList<Orders> alSet = new ArrayList<Orders>();
                                    for (int o = 0; o < alOrders.size(); o++) {
                                        if (alOrders.get(o).getSet() == u+1 && Integer.parseInt(alOrders.get(o).getUserId()) == w+1) {
                                            alSet.add(alOrders.get(o));
                                        }
                                    }
                                    if (alSet != null) {
                                        al.add(alSet);
                                        Log.i("1234567", ""+al);
                                    }

                                }

                            }

                            for (int i = 0; i< al.size(); i++) {
                                if(al.get(i) == null || al.get(i).isEmpty() == true){
                                    al.remove(i);
                                }
                            }

                            aa.notifyDataSetChanged();

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }


                    }//end onSuccess

                });


            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return v;
    }
}