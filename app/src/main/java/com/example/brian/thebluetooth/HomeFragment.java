package com.example.brian.thebluetooth;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GridView gView = (GridView)inflater.inflate(R.layout.content_main, container, false);
        gView.setAdapter(new ImageAdapter(getActivity()));

        // set the listeners for the images
        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Fragment fragment = new HomeFragment();

                switch(position) {
                    case 0:
                        fragment = new GridLauncher();
                        break;
                    case 1:
                        fragment = new BluetoothAttempt();
                        break;
                    case 2:
                        fragment = new MapActivity();
                        break;
                    case 3:
                        fragment = new Messaging();
                        break;
                    default:
                        break;
                }

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });

        return gView;
    }
}
