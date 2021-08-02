package com.example.thegreekgoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String role = prefs.getString("role", "");
        String apikey = prefs.getString("apikey", "");
        String userId = prefs.getString("userId", "");

        if (role.equalsIgnoreCase("") || apikey.equalsIgnoreCase("") || userId.equalsIgnoreCase("")) {
            //guest
            Fragment userFrag = new UserFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame_main, userFrag)
                    .addToBackStack(null)
                    .commit();
        } else {
            if (role.equalsIgnoreCase("user")) {
                //user
                Fragment userFrag = new UserFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame_main, userFrag)
                        .addToBackStack(null)
                        .commit();
            } else if (role.equalsIgnoreCase("admin")) {
                //admin
                Fragment adminFrag = new AdminFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame_main, adminFrag)
                        .addToBackStack(null)
                        .commit();
            } else {
                //rider
                Fragment riderFrag = new RiderFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame_main, riderFrag)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}