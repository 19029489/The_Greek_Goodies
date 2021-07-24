package com.example.thegreekgoodies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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


    //===================UpdatesRaph======================
    ListView lv;
    ArrayAdapter<Item> adapter;
    ArrayList<Item> list;
    //===================UpdatesRaph======================

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




        //=====================ImplementationRaph========================
        //=================SetupUI/ETC=====================
        client = new AsyncHttpClient();
        lv = (ListView) v.findViewById(R.id.lvItems);
        list = new ArrayList<Item>();
        adapter = new ArrayAdapter<Item>(getActivity(), android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        //=================SetupUI/ETC=====================
        //==================SharedPref==================
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String catID = prefs.getString("catID", "");
        RequestParams params = new RequestParams();
        params.add("categoryId", catID);
        //==================SharedPref==================
        //=================StupidListView=====================
        client.post("http://10.0.2.2/greek_goodies/getProductInfo.php", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObj = response.getJSONObject(i);
                        //============-----Identify----=============
                        int id = jsonObj.getInt("id");
                        String name = jsonObj.getString("name");
                        String details = jsonObj.getString("details");
                        String photo = jsonObj.getString("photo");
                        int category = jsonObj.getInt("category");
                        double price = jsonObj.getDouble("price");
                        //============-----Identify----=============
                        Item items = new Item(id, name, details, photo, category, price);
                        list.add(items);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new ItemAdapter(getContext(), R.layout.itemlist, list);
                lv.setAdapter(adapter);
            }
        });
        //=================StupidListView=====================
        //---------------------LVClickHandle------------------------
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long identity) {
                Item target = list.get(position);
                Intent i = new Intent(getActivity(),InfoList.class);
                i.putExtra("positionData", target);
                startActivity(i);
            }
        });
        //---------------------LVClickHandle------------------------
        //=====================ImplementationRaph========================


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