package com.example.thegreekgoodies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.Html;
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

public class ProductsFragment extends Fragment {

    private AsyncHttpClient client;

    String collection, product_type;
    TextView tvDesc, tvCat;
    String title;

    //===================UpdatesRaph======================
    ListView lv;
    ArrayAdapter<Item> adapter;
    ArrayList<Item> list;
    //===================UpdatesRaph======================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_products, container, false);

        tvCat = v.findViewById(R.id.tvCategory);
        tvDesc = v.findViewById(R.id.tvDescription);

        //=================SetupUI/ETC=====================
        lv = v.findViewById(R.id.lvItems);
        list = new ArrayList<Item>();
        adapter = new ItemAdapter(getActivity(), R.layout.itemlist, list);
        lv.setAdapter(adapter);
        client = new AsyncHttpClient();
        //=================SetupUI/ETC=====================

        if (getArguments() != null) {
            collection = getArguments().getString("collection_id");
            product_type = getArguments().getString("product_type");
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

                        String catName;

                        if (cat.equalsIgnoreCase("Kinda Cheesy")){
                            catName = "Cheese";
                        } else if (cat.equalsIgnoreCase("Oil of live, Oil of Olive")) {
                            catName = "Olive Oil";
                        } else if (cat.equalsIgnoreCase("Yoghurt for you")){
                            catName = "Yogurt";
                        } else {
                            catName = response.getJSONObject("collection").getJSONArray("rules").getJSONObject(0).getString("condition");
                        }

                        //Get Products
                        client.get("https://d10489d24d3e88405b3f523453ff2dca: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@superfoursixty-co.myshopify.com/admin/api/2021-04/products.json", new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                try {
                                    Log.i("Products: ", response.toString());

                                    JSONArray jsonArray = response.getJSONArray("products");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObj = jsonArray.getJSONObject(i);

                                        String category = jsonObj.getString("product_type");

                                        if (category.equalsIgnoreCase(catName)) {
                                            int id = jsonObj.getInt("id");
                                            String productName = jsonObj.getString("title");

                                            String htmlDetails = jsonObj.getString("body_html");
                                            String details = Html.fromHtml(htmlDetails).toString();

                                            String photo = jsonObj.getJSONArray("images").getJSONObject(0).getString("src");
                                            double price = Double.parseDouble(jsonObj.getJSONArray("variants").getJSONObject(0).getString("price"));

                                            Item items = new Item(id, productName, details, photo, category, price);
                                            list.add(items);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }//end onSuccess
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }//end onSuccess
            });





            //=================ListView=====================
            //---------------------LVClickHandle------------------------
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long identity) {

                    Item target = list.get(position);

                    //==================SharedPref==================
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String role = prefs.getString("role", "");
                    String apikey = prefs.getString("apikey", "");
                    String userId = prefs.getString("userId", "");
                    //==================SharedPref==================


                    //==================Admin==================
                    if (!(role.equalsIgnoreCase("") || apikey.equalsIgnoreCase("") || userId.equalsIgnoreCase(""))) {
                        if (role.equalsIgnoreCase("admin")) {
                            Fragment infoListFrag = new InfoList();
                            Bundle args = new Bundle();
                            args.putString("itemName", target.itemName);
                            args.putString("itemDetails", target.itemDetails);
                            args.putString("itemPhoto", target.itemPhoto);
                            args.putString("category", target.category);
                            args.putDouble("itemPrice", target.itemPrice);
                            infoListFrag.setArguments(args);

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, infoListFrag)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                    //==================Admin==================


                    //==================Guest==================
                    Fragment infoListFrag = new InfoList();
                    Bundle args = new Bundle();
                    args.putString("itemName", target.itemName);
                    args.putString("itemDetails", target.itemDetails);
                    args.putString("itemPhoto", target.itemPhoto);
                    args.putString("category", target.category);
                    args.putDouble("itemPrice", target.itemPrice);
                    infoListFrag.setArguments(args);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, infoListFrag)
                            .addToBackStack(null)
                            .commit();
                    //==================SharedPref==================

                }
            });

            //---------------------LVClickHandle------------------------
        }
        return v;
    }

}