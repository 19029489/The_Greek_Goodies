package com.example.thegreekgoodies;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link UserViewOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserViewOrdersFragment extends Fragment {

    TextView tvBack;
    private ArrayList<ArrayList<Orders>> al;
    private ArrayAdapter aa;
    private ListView lv;

    int totalNum = 0;

    private AsyncHttpClient client;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserViewOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserViewOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserViewOrdersFragment newInstance(String param1, String param2) {
        UserViewOrdersFragment fragment = new UserViewOrdersFragment();
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

        View v = inflater.inflate(R.layout.fragment_user_view_orders, container, false);

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

        tvBack = v.findViewById(R.id.backBtnOrder);
        lv = v.findViewById(R.id.lvOrdersUser);
        al = new ArrayList<ArrayList<Orders>>();
        aa = new OrderAdapter(getActivity(), R.layout.orderlist_row, al);
        lv.setAdapter(aa);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        client = new AsyncHttpClient();

        if (getArguments() != null) {
            totalNum = getArguments().getInt("totalNum");

            RequestParams params = new RequestParams();
            params.add("user_id", userId);
            params.add("apikey", apikey);

            client.post("http://10.0.2.2/TheGreekGoodies/getOrdersByUser.php", params, new JsonHttpResponseHandler() {
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

                        //Get specific sets
                        for (int u = 0; u < totalNum; u++) {
                            ArrayList<Orders> alSet = new ArrayList<Orders>();
                            for (int o = 0; o < alOrders.size(); o++) {
                                if (alOrders.get(o).getSet() == u+1) {
                                    alSet.add(alOrders.get(o));
                                }
                            }
                            al.add(alSet);
                        }
                        aa.notifyDataSetChanged();

                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }//end onSuccess
            });
        }

        return v;
    }
}