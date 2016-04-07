package com.example.brian.thebluetooth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GridLauncher extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove dummy (this) from fragment back stack
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dummyview, container, false);
        Intent i = new Intent(getActivity(), NaviImage.class);

        // read bundle for specific patients' locations
        Bundle bun = this.getArguments();
        float lon = bun.getFloat("Longitude", (float) -123.2);
        float lat = bun.getFloat("Latitude", (float) 49.26);
        
        // package intent for next activity
        i.putExtra("Longitude", lon);
        i.putExtra("Latitude", lat);

        getActivity().startActivity(i);

        return v;
    }
}
