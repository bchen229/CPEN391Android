package com.example.brian.thebluetooth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class PatientsFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GridView gView = (GridView)inflater.inflate(R.layout.content_patient, container, false);
        gView.setAdapter(new PatientImageAdapter(getActivity()));

        // set the listeners for the images
        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                final Intent i = new Intent(.this, ViewImage.class);
                Fragment fragment = new PatientPage();
                Bundle bun = new Bundle();
                switch(position) {
                    case 0:
                        // "Hell's Kitchen Park, NY": 40.763275, -73.992564
                        bun.putFloat("Longitude",(float) -73.992564);
                        bun.putFloat("Latitude", (float) 40.763275);
                        bun.putString("Name", "Donald Drumpf");
                        bun.putString("Address", "Hell lol");
                        bun.putString("Phone","2126666666");
                        break;
                    case 1:
                        // Near London Bridge: 51.500756, -0.124851
                        bun.putFloat("Longitude",(float) -0.124851);
                        bun.putFloat("Latitude", (float) 51.500756);
                        bun.putString("Name", "Margaret Thatcher");
                        bun.putString("Address","Palace of Westminster");
                        bun.putString("Phone","44 20 7219 3000");
                        break;
                    case 2:
                        // Hollywood-ish: 34.130990, -118.320642
                        bun.putFloat("Longitude",(float) -118.320642);
                        bun.putFloat("Latitude", (float) 34.130990);
                        bun.putString("Name", "George Clooney");
                        bun.putString("Address","Hollydood");
                        bun.putString("Phone","9545104513");
                        break;
                    case 3:
                        // Near Beverly Hills: 34.063454, -118.358575
                        bun.putFloat("Longitude",(float) -118.358575);
                        bun.putFloat("Latitude", (float) 34.063454);
                        bun.putString("Name", "Jake Gyllenhaal");
                        bun.putString("Address","Hollyfood");
                        bun.putString("Phone","9541423112");
                        break;
                    default:
                        break;
                }

                fragment.setArguments(bun);
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
