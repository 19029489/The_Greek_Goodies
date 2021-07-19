package com.example.thegreekgoodies;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    TextView tvSignIn;
    EditText etFirstName, etLastName, etEmail, etPassword;
    Button btnCreate;

    private AsyncHttpClient client;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        tvSignIn = v.findViewById(R.id.tvSignIn);

        etEmail = v.findViewById(R.id.etNewEmail);
        etFirstName = v.findViewById(R.id.etNewFirstName);
        etLastName = v.findViewById(R.id.etNewLastName);
        etPassword = v.findViewById(R.id.etNewPassword);
        btnCreate = v.findViewById(R.id.btnCreate);

        client = new AsyncHttpClient();

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment signInFragment = new AccountFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, signInFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstname = etFirstName.getText().toString();
                String email = etEmail.getText().toString();
                String lastname = etLastName.getText().toString();
                String password = etPassword.getText().toString();

                //validation
                if (password.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || email.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill in all the blanks", Toast.LENGTH_SHORT).show();
                } else {
                    //Check email
                    if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                        etEmail.setError("Please input a valid email e.g. xxx@xyz.com");

                        //check password
                    } else if (password.length() < 8) {
                        etPassword.setError("Password must be at least 8 characters long");

                    } else {

                        //check for duplicate emails
                        client.post("http://10.0.2.2/TheGreekGoodies/getAllEmails.php", new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                                Boolean isDuplicate = false;

                                try {

                                    Log.i("Emails: ", response.toString());

                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject jsonObj = response.getJSONObject(i);

                                        String storedEmail = jsonObj.getString("email");

                                        if (email.equalsIgnoreCase(storedEmail) == true) {
                                            isDuplicate = true;
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (isDuplicate == true){
                                    etEmail.setError("Email already exists, if you forgot your password, please reset it");
                                } else {
                                    Create(v);
                                }

                            }//end onSuccess

                        });

                    }

                }
            }
        });

        return v;
    }


    private void Create(View v) {

        String firstname = etFirstName.getText().toString();
        String email = etEmail.getText().toString();
        String lastname = etLastName.getText().toString();
        String password = etPassword.getText().toString();

        RequestParams params = new RequestParams();
        params.add("email", email);
        params.add("firstname", firstname);
        params.add("lastname", lastname);
        params.add("password", password);

        //for real devices, use the current location's ip address
        client.post("http://10.0.2.2/TheGreekGoodies/createUserAccount.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Toast.makeText(getActivity(), "Account created successfully", Toast.LENGTH_LONG).show();

                Fragment signInFragment = new AccountFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, signInFragment)
                        .addToBackStack(null)
                        .commit();

            }//end onSuccess
        });

    }


}