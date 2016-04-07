package com.example.brian.thebluetooth;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by lh3nry on 16-04-06.
 */
public class PatientPage extends Fragment implements View.OnClickListener {
//    private PatientsItem pInfo;

    private float lat;
    private float lon;
    private String name;
    private String address;
    private String phone;
    private TextView tv;
    private Button photobutton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // get the context for the application
        View view = inflater.inflate(R.layout.patient_page, container, false);
        Bundle bun = this.getArguments();
        lon =  bun.getFloat("Longitude", (float) -123.2);
        lat = bun.getFloat("Latitude", (float) 49.26);
        name = bun.getString("Name", "Someone");
        address = bun.getString("Address", "123 Real Street");
        phone = bun.getString("Phone","9119111");

        tv = (TextView) view.findViewById(R.id.patient_name_field);
        tv.setText(name);
        tv = (TextView) view.findViewById(R.id.patient_address_field);
        tv.setText(address);
        tv = (TextView) view.findViewById(R.id.patient_phone_field);
        tv.setText(phone);

        photobutton = (Button) view.findViewById(R.id.images_button);
        photobutton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.images_button:
                Fragment fragment = new GridLauncher();

                Bundle bun2 = new Bundle();
                bun2.putFloat("Longitude",lon);
                bun2.putFloat("Latitude", lat);


                fragment.setArguments(bun2);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_frame, fragment)
                        .commit();
                break;
        }
    }
}
