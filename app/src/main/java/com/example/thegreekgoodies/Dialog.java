package com.example.thegreekgoodies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;

public class Dialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        android.app.Dialog v = super.onCreateDialog(savedInstanceState);

        ListView lv = new ListView(getActivity());

        ArrayList<Users> rider = new ArrayList<Users>();

        AsyncHttpClient client =  new AsyncHttpClient();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog, null);

        builder.setCancelable(true);

        return v;
    }
}
