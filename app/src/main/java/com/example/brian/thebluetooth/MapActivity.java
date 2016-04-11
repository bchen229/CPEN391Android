package com.example.brian.thebluetooth;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    Marker mkr;
    Marker mkrHome;
    private Thread thread1;
    private Thread thread2;
    private Handler handler = new Handler();

    private String TAG = "Thread Task";
    private double distanceFence = 300; //circle distance in metres
    static double Lat = 49.261818;
    static double Lon = -123.049464;
    double homeLat = 49.261818;
    double homeLong = -123.049698;
    private int distanceFlag = 0;
    private boolean runGPS = true;
    private int popped = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_map, container, false);

        // create the map
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        runGPS = true;


        thread1 = new Thread(new Runnable() {
            public void run() {
                while (runGPS) {
                    Log.d("test1", "test1");
                        GPSHandler();
/*                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }*/
                }
            }
        });
        thread1.start();


        thread2 = new Thread(new Runnable() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this, 10000);
                        Log.d("test2", "test2");

                        double dist = distFrom(homeLat, homeLong, Lat, Lon);

                        if (dist > distanceFence) {

                            distanceFlag = 1;

                        } else {

                            distanceFlag = 0;

                        }

                        if (distanceFlag == 1 && popped == 0) {
                            setupWindowAnimations();
                            distanceFlag = 0;
                            startActivity(new Intent(getActivity(), Pop.class));
                            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                            popped = 1;
                        }
                        //Our implementation forces us to clear the map on update, due to the directions API
                        mMap.clear();

                        LatLng patientPos = new LatLng(Lat, Lon);
                        mkr = mMap.addMarker(new MarkerOptions()
                                .position(patientPos).snippet("Name: John Doe\n Age: 68").snippet("Age: 68").snippet("123 Fake Street")
                                .title("Patient Location"));
                        mkr.setPosition(patientPos);

                        LatLng homeLatLng = new LatLng(Lat, Lon);

                        mkrHome = mMap.addMarker(new MarkerOptions()
                                .position(homeLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .title("Home Location"));
                        mkrHome.setPosition(homeLatLng);
                        mkrHome.showInfoWindow();

                        CircleOptions circleOptions = new CircleOptions().center(homeLatLng).radius(300); // In meters
                        circleOptions.strokeWidth(5);
                        circleOptions.fillColor(Color.argb(20, 50, 0, 255));

                        // Get back the mutable Circle
                        Circle circle = mMap.addCircle(circleOptions);

                        String url = getMapsApiDirectionsUrl();
                        //begin requesting directions coordinates
                        ReadTask downloadTask = new ReadTask();
                        downloadTask.execute(url);
                        //addLines();
                    }
                });

            }
        });
        thread2.start();
        return view;
    }

    private String getMapsApiDirectionsUrl() {
        String waypoints = "waypoints=optimize:true|"
                + homeLat + "," + homeLong
                + "|" + "|" + Lat + ","
                + Lon;
        String OriDest = "origin=" + homeLat + "," + homeLong + "&destination=" + Lat + "," + Lon;

        String sensor = "sensor=false";
        String params = OriDest + "&%20" + waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }

    /*
*  Obtained from http://javapapers.com/android/draw-path-on-google-maps-android-api/
* */

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            if (routes == null) {
                return;
            }

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    //Log.d("LATLNG: ", position.toString());
                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(5);
                polyLineOptions.color(Color.BLUE);
            }
            try {
                mMap.addPolyline(polyLineOptions);
            } catch (NullPointerException npe) {

            }
        }
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getActivity().getWindow().setExitTransition(slide);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng patientPos = new LatLng(Lat, Lon);
        LatLng homeLatLng = new LatLng(homeLat, homeLong);


        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions().center(homeLatLng).radius(distanceFence); // In meters
        circleOptions.strokeWidth(5);
        circleOptions.fillColor(Color.argb(20, 50, 0, 255));

        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);

        mkr = mMap.addMarker(new MarkerOptions()
                .position(patientPos).snippet("Name: John Doe\n Age: 68").snippet("Age: 68").snippet("123 Fake Street")
                .title("Patient Location"));
        mkr.setPosition(patientPos);

        mkrHome = mMap.addMarker(new MarkerOptions()
                .position(homeLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("Home Location"));
        mkrHome.setPosition(homeLatLng);
        mkrHome.showInfoWindow();

        float zoomLevel = 16;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel));

    }

    @Override
    public void onResume() {
        runGPS = true;
        Log.d("maps tag", thread1.getState().toString());
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        thread1.interrupt();
        thread2.interrupt();
        runGPS = false;
        super.onPause();
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
// to the bluetooth_colour device and then sends the string “\r\n”
// (required by the bluetooth dongle)
//
    public void WriteToBTDevice(String message) throws NullPointerException {
        String s = "\r\n";
        byte[] msgBuffer = message.getBytes();
        byte[] newline = s.getBytes();

        if (BluetoothAttempt.mmOutStream == null) {
            throw new NullPointerException("No Bluetooth Output Stream");
        }

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

        try {
            WriteToBTDevice(command);
        } catch (NullPointerException npe) {
            return;
        }

        while (!(check = ReadFromBTDevice()).equals("a")) {
            WriteToBTDevice(command);
            Log.d("GPS HANDLER Check: ", check);
            Log.d("GPS HANDLER: ", "Stopping here");
        }
        WriteToBTDevice(send);

        String longLat = ReadFromBTDevice();
        String[] longLatArray = longLat.split("N");

        if (longLatArray.length < 2) {
            return;
        }

        //Parse out unwanted characters

        Latitude = longLatArray[0];
        Latitude = Latitude.replaceAll("\n", "");
        Latitude = Latitude.replaceAll("[A-Za-z]", "");
        Latitude = Latitude.replace(",", "");

        Longitude = longLatArray[1];
        Longitude = Longitude.replaceAll("\n", "");
        Longitude = Longitude.replaceAll("[A-Za-z]", "");
        Longitude = Longitude.replaceAll(",", "");

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
//

    }

    /*
    * Found here
    * http://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java*/
    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }
}