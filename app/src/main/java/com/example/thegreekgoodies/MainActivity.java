package com.example.thegreekgoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelperStatus dbh = new DBHelperStatus(MainActivity.this);
        String status = dbh.getStatus();

        if (status.equalsIgnoreCase("false")) {
            //guest
            Fragment userFrag = new UserFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame_main, userFrag)
                    .addToBackStack(null)
                    .commit();
        } else {
            String role = dbh.getRole();

            if (role.equalsIgnoreCase("user")) {
                //user
                Fragment userFrag = new UserFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame_main, userFrag)
                        .addToBackStack(null)
                        .commit();
            } else if (role.equalsIgnoreCase("admin")) {
                //admin
                Fragment userFrag = new UserFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame_main, userFrag)
                        .addToBackStack(null)
                        .commit();
            } else {
                //rider
                Fragment userFrag = new UserFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame_main, userFrag)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

}