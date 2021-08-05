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
import android.widget.Spinner;
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
 * Use the {@link ViewUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewUsersFragment extends Fragment {

    Spinner spn;
    TextView tvSelected;
    ListView lv;
    ArrayList<Users> al;
    ArrayAdapter aa;
    String chosen;
    Button btnAdd;

    private AsyncHttpClient client;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewRidersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewUsersFragment newInstance(String param1, String param2) {
        ViewUsersFragment fragment = new ViewUsersFragment();
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
        View v = inflater.inflate(R.layout.fragment_view_users, container, false);

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

        spn = v.findViewById(R.id.spinner);
        tvSelected = v.findViewById(R.id.tvSelected);
        lv = v.findViewById(R.id.lvChoose);

        btnAdd = v.findViewById(R.id.btnAddUsers);
        al = new ArrayList<Users>();

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                tvSelected.setText(spn.getSelectedItem().toString());

                if (spn.getSelectedItem().toString().equalsIgnoreCase("users") || spn.getSelectedItem().toString().equalsIgnoreCase("admins")){
                    aa = new UserAdapter(getActivity(), R.layout.user_row, al);
                } else {
                    aa = new RiderAdapter(getActivity(), R.layout.rider_row, al);
                }

                lv.setAdapter(aa);

                if (spn.getSelectedItem().toString().equalsIgnoreCase("users")) {
                    chosen = "user";
                    btnAdd.setActivated(false);
                } else if (spn.getSelectedItem().toString().equalsIgnoreCase("admins")) {
                    chosen = "admin";
                    btnAdd.setActivated(true);
                } else {
                    chosen = "rider";
                    btnAdd.setActivated(true);
                }

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (chosen.equalsIgnoreCase("rider")){
                            Fragment createRiderFrag = new CreateRiderFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, createRiderFrag)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            Fragment createAdminFrag = new CreateAdminFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, createAdminFrag)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });

                //retrieve users
                RequestParams params = new RequestParams();
                params.add("user_id", userId);
                params.add("apikey", apikey);
                params.add("role", chosen);
                //for real devices, use the current location's ip address
                client.post("http://192.168.2.167/TheGreekGoodies/getAllUsers.php", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {

                        try {
                            Log.i("Users: ", response.toString());

                            al.clear();

                            if (response.length() == 0){
                                aa.notifyDataSetChanged();
                            } else {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject jsonObj = response.getJSONObject(i);

                                    String userId = jsonObj.getString("user_id");
                                    String firstname = jsonObj.getString("firstname");
                                    String lastname = jsonObj.getString("lastname");
                                    String contact = jsonObj.getString("contact");
                                    String email = jsonObj.getString("email");
                                    String photo = jsonObj.getString("photo");

                                    Users user = new Users(userId, firstname, lastname, email, contact, photo);

                                    al.add(user);
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