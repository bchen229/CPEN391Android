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
                        bun.putFloat("Longitude",(float) -123.2);
                        bun.putFloat("Latitude", (float) 49.26);
                        bun.putString("Name", "Donald Drumpf");
                        bun.putString("Address", "Hell lol");
                        bun.putString("Phone","6666666");
                        break;
                    case 1:
//                        51.500756, -0.124851
                        bun.putFloat("Longitude",(float) -0.124851);
                        bun.putFloat("Latitude", (float) 51.500756);
                        bun.putString("Name", "Margaret Thatcher");
                        bun.putString("Address","London Bridge");
                        bun.putString("Phone","1534123");
                        break;
                    case 2:
                        bun.putFloat("Longitude",(float) -123.2);
                        bun.putFloat("Latitude", (float) 49.26);
                        bun.putString("Name", "George Clooney");
                        bun.putString("Address","Hollydood");
                        bun.putString("Phone","5104513");
                        break;
                    case 3:
                        bun.putFloat("Longitude",(float) -123.2);
                        bun.putFloat("Latitude", (float) 49.26);
                        bun.putString("Name", "????");
                        bun.putString("Address","???");
                        bun.putString("Phone","????");
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
