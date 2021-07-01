package com.example.thegreekgoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.*;
import cz.msebera.android.httpclient.*;
import com.loopj.android.http.AsyncHttpClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    private AsyncHttpClient client;

    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //slider
        viewPager2 = findViewById(R.id.viewPagerImageSlider);

        //image views
        iv1 = findViewById(R.id.imageView1);
        iv2 = findViewById(R.id.imageView2);
        iv3 = findViewById(R.id.imageView3);
        iv4 = findViewById(R.id.imageView4);
        iv5 = findViewById(R.id.imageView5);
        iv6 = findViewById(R.id.imageView6);
        iv7 = findViewById(R.id.imageView7);
        iv8 = findViewById(R.id.imageView8);

        //TextViews
        tv1 = findViewById(R.id.textview1);
        tv2 = findViewById(R.id.textview2);
        tv3 = findViewById(R.id.textview3);
        tv4 = findViewById(R.id.textview4);
        tv5 = findViewById(R.id.textview5);
        tv6 = findViewById(R.id.textview6);
        tv7 = findViewById(R.id.textview7);
        tv8 = findViewById(R.id.textview8);

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
                float r =1 - Math.abs(position);
                page.setScaleY(0.85f + r *0.15f);
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

        for (int i = 0; i<collections.size(); i++) {
            client = new AsyncHttpClient();
            int finalI = i;
            client.get("https://d10489d24d3e88405b3f523453ff2dca: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@superfoursixty-co.myshopify.com/admin/api/2021-04/collections/" + collections.get(i) + ".json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        Log.i("JSON Results: ", response.toString());

                        String imageURL = response.getJSONObject("collection").getJSONObject("image").getString("src");
                        Picasso.with(MainActivity.this).load(imageURL).into(iv.get(finalI));
                        tv.get(finalI).setText(response.getJSONObject("collection").getString("title"));


                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }//end onSuccess
            });
        }

    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };
}