package com.example.brian.thebluetooth;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GridLauncher extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dummyview, container, false);
        Intent i = new Intent(getActivity(), NaviImage.class);

        //TODO need to add a try catch in case the bundle doesn't exist (or add a default bundle)
        Bundle bun = this.getArguments();
        float lon = bun.getFloat("Longitude", (float) -123.2);
        float lat = bun.getFloat("Latitude", (float) 49.26);

//        i.putExtra("Longitude", (float) -123.2);
//        i.putExtra("Latitude", (float) 49.26);

        i.putExtra("Longitude", lon);
        i.putExtra("Latitude", lat);

        getActivity().startActivity(i);
//        getActivity().startActivity(new Intent(getActivity(), Pop.class));

        return v;
    }
}
