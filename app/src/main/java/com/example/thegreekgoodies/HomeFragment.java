package com.example.thegreekgoodies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
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
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    private AsyncHttpClient client;

    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        //slider
        viewPager2 = v.findViewById(R.id.viewPagerImageSlider);

        //image views
        iv1 = v.findViewById(R.id.imageView1);
        iv2 = v.findViewById(R.id.imageView2);
        iv3 = v.findViewById(R.id.imageView3);
        iv4 = v.findViewById(R.id.imageView4);
        iv5 = v.findViewById(R.id.imageView5);
        iv6 = v.findViewById(R.id.imageView6);
        iv7 = v.findViewById(R.id.imageView7);
        iv8 = v.findViewById(R.id.imageView8);

        //TextViews
        tv1 = v.findViewById(R.id.textview1);
        tv2 = v.findViewById(R.id.textview2);
        tv3 = v.findViewById(R.id.textview3);
        tv4 = v.findViewById(R.id.textview4);
        tv5 = v.findViewById(R.id.textview5);
        tv6 = v.findViewById(R.id.textview6);
        tv7 = v.findViewById(R.id.textview7);
        tv8 = v.findViewById(R.id.textview8);

        //Preparing list of images from drawable
        //Can get from API as well

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.beyond_the_boundaries_of_taste));
        sliderItems.add(new SliderItem(R.drawable.eat_good_eat_greek));

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.setClipChildren(false);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 5000); //Slide duration 5 seconds
            }
        });

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
                        Log.i("Collection: ", response.toString());

                        String imageURL = response.getJSONObject("collection").getJSONObject("image").getString("src");
                        Picasso.with(getActivity()).load(imageURL).into(iv.get(finalI));
                        tv.get(finalI).setText(response.getJSONObject("collection").getString("title"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }//end onSuccess
            });
        }

        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Olives");
        categories.add("Chips");
        categories.add("Soda");
        categories.add("Rice Cakes");
        categories.add("Cheese");
        categories.add("Olive Oil");
        categories.add("Yogurt");
        categories.add("Pastry");

        for (int o = 0; o < iv.size(); o++) {
            int finalO = o;
            iv.get(o).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment productsFrag = new ProductsFragment();
                    Bundle args = new Bundle();
                    args.putString("product_type", categories.get(finalO));
                    args.putString("collection_id", collections.get(finalO));
                    productsFrag.setArguments(args);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, productsFrag)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        return v;
    }


    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };
}