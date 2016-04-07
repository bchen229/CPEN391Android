package com.example.brian.thebluetooth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SQLLauncher extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove dummy (this) from fragment back stack
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dummyview, container, false);
        getActivity().startActivity(new Intent(getActivity(), SQLActivity.class));

        return v;
    }
}