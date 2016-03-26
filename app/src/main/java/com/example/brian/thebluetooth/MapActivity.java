package com.example.brian.thebluetooth;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class MapActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    Marker mkr;
    private Thread thread1, thread2;
    private Handler handler = new Handler();
    private Handler handlerUI = new Handler();
    private String TAG = "Thread Task";
    double Lat = 49.261818;
    double Lon = -123.249698;
    double homeLat = 49.261818;
    double homeLong = -123.249698;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_map, container, false);

        // create the map
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        thread1 = new Thread() {
            public void run() {
                //GPSHandler();
                Log.d(TAG, "GPS Updated");
                handler.postDelayed(this, 10000);
            }
        };
        thread1.start();


        new Thread(new Runnable() {
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this, 10000);

                        LatLng van = new LatLng(Lat, Lon);
                        //mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(van).title("Your Location"));
                    }
                });

            }
        }).start();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng van = new LatLng(Lat, Lon);


        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions().center(new LatLng(homeLat, homeLong)).radius(50); // In meters
        circleOptions.strokeWidth(5);
        circleOptions.fillColor(Color.argb(20, 50, 0, 255));
        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);

        mMap.addMarker(new MarkerOptions().position(van).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(van));
        float zoomLevel = new Float(16);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(van, zoomLevel));


    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //
// This function write a line of text (in the form of an array of bytes)
// to the Bluetooth device and then sends the string “\r\n”
// (required by the bluetooth dongle)
//
    public void WriteToBTDevice(String message) {
        String s = "\r\n";
        byte[] msgBuffer = message.getBytes();
        byte[] newline = s.getBytes();

        try {
            BluetoothAttempt.mmOutStream.write(msgBuffer);
            BluetoothAttempt.mmOutStream.write(newline);
        } catch (IOException e) {
        }
    }

    public String ReadFromBTDevice() {
        byte c;
        String s = "";

        try { // Read from the InputStream using polling and timeout
            for (int i = 0; i < 200; i++) { // try to read for 2 seconds max
                SystemClock.sleep(10);
                if (BluetoothAttempt.mmInStream.available() > 0) {
                    if ((c = (byte) BluetoothAttempt.mmInStream.read()) != '\r') // '\r' terminator
                        s += (char) c; // build up string 1 byte by byte
                    else
                        return s;
                }
            }
        } catch (IOException e) {
            return "-- No Response --";
        }
        return s;
    }

    private void GPSHandler() {

        String check;
        String command = ";GPS;";
        String Latitude;
        String Longitude;
        String send = ";send;";

        //Fix this once we have bluetooth
        while (!(check = ReadFromBTDevice()).equals("ack")) {

            WriteToBTDevice(command);

        }
        WriteToBTDevice(send);
        Latitude = ReadFromBTDevice();
        Latitude = Latitude.replaceAll("\n", "");
        Latitude = Latitude.replaceAll(",", "");
        Latitude = Latitude.replaceAll("N", "");

        WriteToBTDevice(send);

        Longitude = ReadFromBTDevice();
        Longitude = Longitude.replaceAll("\n", "");
        Longitude = Longitude.replaceAll(",", "");
        Longitude = Longitude.replaceAll("W", "");

        Log.d(TAG, check);
        Log.d("Latitude:", Latitude);
        Log.d("Longitude:", Longitude);

        if (Latitude != null && !Latitude.isEmpty()) {
            Lat = Double.parseDouble(Latitude);
        }

        if (Longitude != null && !Longitude.isEmpty()) {
            Lon = Double.parseDouble(Longitude);
            Lon = (-1) * Lon;

        }

    }

}