package com.example.brian.thebluetooth;

/**
 * Created by david on 2016-04-06.
 */
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class sqlLauncher extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dummyview, container, false);
        getActivity().startActivity(new Intent(getActivity(), sqlActivity.class));

        return v;
    }
}
