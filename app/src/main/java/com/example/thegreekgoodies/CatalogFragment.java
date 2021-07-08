package com.example.thegreekgoodies;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogFragment extends Fragment {

    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;

    private AsyncHttpClient client;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CatalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CatalogFragment newInstance(String param1, String param2) {
        CatalogFragment fragment = new CatalogFragment();
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
        View v = inflater.inflate(R.layout.fragment_catalog, container, false);

        //image views
        iv1 = v.findViewById(R.id.ivCat1);
        iv2 = v.findViewById(R.id.ivCat2);
        iv3 = v.findViewById(R.id.ivCat3);
        iv4 = v.findViewById(R.id.ivCat4);
        iv5 = v.findViewById(R.id.ivCat5);
        iv6 = v.findViewById(R.id.ivCat6);
        iv7 = v.findViewById(R.id.ivCat7);
        iv8 = v.findViewById(R.id.ivCat8);

        //TextViews
        tv1 = v.findViewById(R.id.tvCat1);
        tv2 = v.findViewById(R.id.tvCat2);
        tv3 = v.findViewById(R.id.tvCat3);
        tv4 = v.findViewById(R.id.tvCat4);
        tv5 = v.findViewById(R.id.tvCat5);
        tv6 = v.findViewById(R.id.tvCat6);
        tv7 = v.findViewById(R.id.tvCat7);
        tv8 = v.findViewById(R.id.tvCat8);

        //Connecting to API

        //collection id number
        ArrayList<String> collections = new ArrayList<String>();
        collections.add("160858603610");
        collections.add("218101055642");
        collections.add("218101743770");
        collections.add("218107314330");
        collections.add("218571571354");
        collections.add("218459275418");
        collections.add("218572882074");
        collections.add("244129726642");

        ArrayList<ImageView> iv = new ArrayList<ImageView>();
        iv.add(iv1);
        iv.add(iv2);
        iv.add(iv3);
        iv.add(iv4);
        iv.add(iv5);
        iv.add(iv6);
        iv.add(iv7);
        iv.add(iv8);

        ArrayList<TextView> tv = new ArrayList<TextView>();
        tv.add(tv1);
        tv.add(tv2);
        tv.add(tv3);
        tv.add(tv4);
        tv.add(tv5);
        tv.add(tv6);
        tv.add(tv7);
        tv.add(tv8);

        for (int i = 0; i < collections.size(); i++) {
            client = new AsyncHttpClient();
            int finalI = i;
            client.get("https://d10489d24d3e88405b3f523453ff2dca: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@superfoursixty-co.myshopify.com/admin/api/2021-04/collections/" + collections.get(i) + ".json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        Log.i("JSON Results: ", response.toString());

                        String imageURL = response.getJSONObject("collection").getJSONObject("image").getString("src");
                        Picasso.with(getActivity()).load(imageURL).into(iv.get(finalI));
                        tv.get(finalI).setText(response.getJSONObject("collection").getString("title"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }//end onSuccess
            });
        }

        return v;
    }
}