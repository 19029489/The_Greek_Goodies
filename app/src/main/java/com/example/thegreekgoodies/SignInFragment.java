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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    TextView tvSignUp, tvForgot;
    EditText etEmail, etPassword;
    Button btnSignIn;

    private AsyncHttpClient client;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
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

        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        tvSignUp = v.findViewById(R.id.tvSignUp);
        tvForgot = v.findViewById(R.id.tvForgot);

        etEmail = v.findViewById(R.id.etEmail);
        etPassword = v.findViewById(R.id.etPassword);

        btnSignIn = v.findViewById(R.id.btnSignIn);

        client = new AsyncHttpClient();

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send email to change password (random generated code)
                //Future Addition
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment signUpFrag= new SignUpFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, signUpFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter email.", Toast.LENGTH_LONG).show();

                } else if (password.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter password.", Toast.LENGTH_LONG).show();

                } else {
                    Login(v);
                }
            }
        });

        return v;
    }

    private void Login(View v) {

        RequestParams params = new RequestParams();
        params.add("email", etEmail.getText().toString());
        params.add("password", etPassword.getText().toString());

        //for real devices, use the current location's ip address
        client.post("http://10.0.2.2/TheGreekGoodies/doLogin.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    Log.i("JSON Results: ", response.toString());

                    Boolean authenticated = response.getBoolean("authenticated");

                    if (authenticated == true) {
                        String apikey = response.getString("apikey");
                        String role = response.getString("role");
                        String userId = response.getString("user_id");

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("role", role);
                        editor.putString("apikey", apikey);
                        editor.putString("userId", userId);
                        editor.commit();

                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);

                    } else {
                        Toast.makeText(getActivity(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }//end onSuccess
        });

    }
}