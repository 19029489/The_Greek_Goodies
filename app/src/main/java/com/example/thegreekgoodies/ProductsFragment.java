package com.example.thegreekgoodies;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment {

    private AsyncHttpClient client;
    String collection, product_type;

    TextView tvDesc, tvCat;
    Context context;
    CardView cardview;
    LinearLayout.LayoutParams layoutparams;
    TextView textview;
    ImageView iv;
    LinearLayout linearLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
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
        View v = inflater.inflate(R.layout.fragment_products, container, false);

        tvCat = v.findViewById(R.id.tvCategory);
        tvDesc = v.findViewById(R.id.tvDescription);

        if (getArguments() != null) {
            collection = getArguments().getString("collection_id");
            product_type = getArguments().getString("product_type");

            client = new AsyncHttpClient();
            client.get("https://d10489d24d3e88405b3f523453ff2dca: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@superfoursixty-co.myshopify.com/admin/api/2021-04/collections/" + collection + ".json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        Log.i("JSON Results: ", response.toString());

                        String cat = response.getJSONObject("collection").getString("title");
                        tvCat.setText(cat);
                        if (response.getJSONObject("collection").has("body_html")) {

                            String desc = response.getJSONObject("collection").optString("body_html");
                            tvDesc.setText(desc);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }//end onSuccess
            });

        }

//        linearLayout = v.findViewById(R.id.linearLayout);
//        CreateCardViewProgrammatically();


        //Enter code here Raphael


        return v;
    }

//    public void CreateCardViewProgrammatically() {
//        cardview = new CardView(context);
//        layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        cardview.setLayoutParams(layoutparams);
//        cardview.setRadius(15);
//        cardview.setPadding(25, 25, 25, 25);
//        cardview.setMaxCardElevation(30);
//        cardview.setMaxCardElevation(6);
//
//        iv = new ImageView(context);
//        textview.setLayoutParams(layoutparams);
//
//
//        textview = new TextView(context);
//        textview.setLayoutParams(layoutparams);
//        textview.setText("CardView Programmatically");
////        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
//        textview.setTextColor(Color.BLACK);
//        textview.setPadding(25, 25, 25, 25);
//        textview.setGravity(Gravity.CENTER);
//        cardview.addView(textview);
//        linearLayout.addView(cardview);
//    }
}