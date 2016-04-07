package com.example.brian.thebluetooth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

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
    private String medicalHistory;
   // private String file;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // get the context for the application
        View view = inflater.inflate(R.layout.patient_page, container, false);
        Bundle bun = this.getArguments();
        //ImageView image = (ImageView) getActivity().findViewById(R.id.imageViewPhoto);
        lon =  bun.getFloat("Longitude", (float) -123.2);
        lat = bun.getFloat("Latitude", (float) 49.26);
        name = bun.getString("Name", "Someone");
        address = bun.getString("Address", "123 Real Street");
        phone = bun.getString("Phone","9119111");
        medicalHistory = bun.getString("Med", "No Medical Issues");


        double Lat = lon;
        double Lon = lat;

        if(name.equalsIgnoreCase("Donald Trump")){

            Lat = MapActivity.Lat;
            Lon = MapActivity.Lon;

        }

        String loc = String.valueOf(Lat) + " N, " + String.valueOf(Lon) + " E";


        //file = bun.getString("file");

       /* image.setImageResource(R.drawable.donald_trump_square);*/
        //Resources resources = getResources();
       // image.setImageResource(resources.getIdentifier("donald_trump_square", "drawable", getActivity().getPackageName()));


 /*       int imageResource = getResources().getIdentifier(file, null, getActivity().getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResource);
        image.setImageDrawable(imageResource);*/

        tv = (TextView) view.findViewById(R.id.textViewName);
        tv.setText(name);
        tv = (TextView) view.findViewById(R.id.textViewAddresses);
        tv.setText(address);
        tv = (TextView) view.findViewById(R.id.textViewPhone);
        tv.setText(phone);

        tv = (TextView) view.findViewById(R.id.textViewMed);
        tv.setText(medicalHistory);

        tv = (TextView) view.findViewById(R.id.textViewLocation);
        tv.setText(loc);

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
