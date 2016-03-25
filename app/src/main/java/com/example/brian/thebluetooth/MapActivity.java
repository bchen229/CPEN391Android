package com.example.brian.thebluetooth;

import android.app.Fragment;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class MapActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private Thread thread;
    private Handler handler = new Handler();
    private String TAG = "Thread Task";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_map, container, false);

        // create the map
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        thread = new Thread() {
            public void run() {
                GPSHandler();
                Log.d(TAG, "GPS Updated");
                handler.postDelayed(this, 1000);
            }
        };
        thread.run();
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng van = new LatLng(49.2611 , -123.253);
        mMap.addMarker(new MarkerOptions().position(van).title("West Coast"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(van));
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
    public void WriteToBTDevice (String message) {
        String s = "\r\n" ;
        byte[] msgBuffer = message.getBytes();
        byte[] newline = s.getBytes();

        try {
            BluetoothAttempt.mmOutStream.write(msgBuffer) ;
            BluetoothAttempt.mmOutStream.write(newline) ;
        } catch (IOException e) { }
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

    private void GPSHandler(){

        String check;
        String command = ";GPS;";
        String coordinates;
        String send = ";send;";

        //Fix this once we have bluetooth

       /* WriteToBTDevice(command);

        if((check = ReadFromBTDevice()).equals("ack")){

            WriteToBTDevice(send);

            coordinates = ReadFromBTDevice();

            //Parse Here
            //Post Here


        }
*/

    }

}