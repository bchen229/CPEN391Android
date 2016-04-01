package com.example.brian.thebluetooth;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import com.google.android.gms.location.LocationListener;



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
import java.util.Timer;
import java.util.TimerTask;

public class MapActivity extends Fragment implements OnMapReadyCallback {

    int THREAD_PRIORITY_BACKGROUND = 10;
    int THREAD_PRIORITY_LOWEST = 19;
    private GoogleMap mMap;
    private MapView mapView;
    Marker mkr;
    Marker mkrHome;
    private Thread thread1;
    private Handler handler = new Handler();
    private String TAG = "Thread Task";
    double Lat = 49.261818;
    double Lon = -123.042698;
    double homeLat = 49.261818;
    //double homeLong = -123.249698;
    double homeLong = -123.049698;
    LatLng homeLatLng = new LatLng(homeLat, homeLong);
    LatLng testLatLng = new LatLng(Lat,Lon);
    int distanceFlag = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_map, container, false);

        // create the map
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        thread1 = new Thread( new Runnable() {
            public void run() {
                GPSHandler();
                Log.d(TAG, "GPS Updated");
                handler.postDelayed(this, 10000);
            }
        });
        thread1.setPriority(THREAD_PRIORITY_BACKGROUND);
        thread1.start();


        new Thread(new Runnable() {
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this, 10000);

                        if(distanceFlag == 1){
                            setupWindowAnimations();
                            distanceFlag = 0;
                            startActivity(new Intent(getActivity(), Pop.class));
                            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);


                        }


                        mMap.clear();

                        LatLng van = new LatLng(Lat, Lon);
                        mkr =  mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Lat, Lon)).snippet("Name: John Doe\n Age: 68").snippet("Age: 68").snippet("123 Fake Street")
                                .title("Patient Location"));
                        mkr.setPosition(new LatLng(Lat, Lon));

                        mkrHome = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(homeLat, homeLong)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .title("Home Location"));
                        mkrHome.setPosition(new LatLng(homeLat, homeLong));
                        mkrHome.showInfoWindow();

                        CircleOptions circleOptions = new CircleOptions().center(new LatLng(homeLat, homeLong)).radius(300); // In meters
                        circleOptions.strokeWidth(5);
                        circleOptions.fillColor(Color.argb(20, 50, 0, 255));
                        // Get back the mutable Circle
                        Circle circle = mMap.addCircle(circleOptions);

                        String url = getMapsApiDirectionsUrl();
                        //Log.d("URL: ", url);
                        ReadTask downloadTask = new ReadTask();
                        downloadTask.execute(url);
                        //addLines();
                    }
                });

            }
        }).start();


        return view;
    }

    private String getMapsApiDirectionsUrl() {
        String waypoints = "waypoints=optimize:true|"
                + homeLat + "," + homeLong
                + "|" + "|" +  Lat + ","
                + Lon;
        String OriDest = "origin="+homeLat+","+homeLong+"&destination="+Lat+","+Lon;

        String sensor = "sensor=false";
        String params = OriDest+"&%20"+waypoints + "&" + sensor;
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

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    Log.d("LATLNG: ", position.toString());
                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(5);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);
        }
    }

    private void addLines(){

        mMap.addPolyline((new PolylineOptions())
                .add( homeLatLng,new LatLng(Lat, Lon)
                        ).width(5).color(Color.BLUE)
                .geodesic(true));

    }

    private void setupWindowAnimations() {
      /*  Slide slide = new Slide();
        slide.setDuration(1000);
        getActivity().getWindow().setExitTransition(slide);*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng van = new LatLng(Lat, Lon);


        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions().center(new LatLng(homeLat, homeLong)).radius(300); // In meters
        circleOptions.strokeWidth(5);
        circleOptions.fillColor(Color.argb(20, 50, 0, 255));
        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);

        try{
        mMap.setMyLocationEnabled(true);
        }
        catch(SecurityException E){}
        //LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);

        mkr =  mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Lat, Lon)).snippet("Name: John Doe\n Age: 68").snippet("Age: 68").snippet("123 Fake Street")
                .title("Patient Location"));
        mkr.setPosition(new LatLng(Lat, Lon));

        mkrHome = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(homeLat, homeLong)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("Home Location"));
        mkrHome.setPosition(new LatLng(homeLat, homeLong));
        mkrHome.showInfoWindow();

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
        while (!(check = ReadFromBTDevice()).equals("a")) {
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