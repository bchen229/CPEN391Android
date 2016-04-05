package com.example.brian.thebluetooth;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static com.google.android.gms.internal.zzir.runOnUiThread;

public class Messaging extends Fragment {
    EditText msg;
    Button send;
    private Thread RX_THREAD;
    private Handler RX_HANDLER = new Handler();
    private Thread TX_THREAD;
    private Handler TX_HANDLER = new Handler();
//
    LinearLayout llLayout;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    boolean runRX = true;
    boolean runTX = true;
    String messageRX;

    boolean finishedTX = true;
    boolean finishedRX = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        llLayout = (LinearLayout) inflater.inflate(R.layout.activity_messaging, container, false);
        send = (Button) llLayout.findViewById(R.id.send_button);
        msg = (EditText) llLayout.findViewById(R.id.enter_message);

        runRX = true;
        if (savedInstanceState != null) {
            String[] values = savedInstanceState.getStringArray("messageStrings");
            if (values != null) {
                Collections.addAll(listItems, values);
            }
        }
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listItems);

        ListView chat = (ListView) llLayout.findViewById(R.id.listView);
        chat.setAdapter(adapter);

        RX_THREAD = new Thread() {
            public void run() {
                if (runRX && finishedTX) {
                    RXHandler();

                    // Listener();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            updateAdapter(messageRX);
                        }
                    });
                    RX_HANDLER.postDelayed(this,2000);
                }
            }
        };
        RX_THREAD.start();

        /*TX_THREAD = new Thread() {
            public void run() {
                if (runTX) {
                   try {
                        Thread.sleep(7000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //TX_HANDLER.postDelayed(this, 5000);

                    Listener();
                }
            }
        };
        TX_THREAD.start();
*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finishedTX = false;

                WriteToBTDevice(";RCV;");

                //   if((ReadFromBTDevice()).equals("y")) {
                Log.d("ABC", "123");
                WriteToBTDevice(";" + msg.getText().toString() + ";");
                String message = "You: " + msg.getText().toString();
                // this line adds the data of your EditText and puts in your array
                listItems.add(message);
                // next thing you have to do is check if your adapter has changed
                adapter.notifyDataSetChanged();
                msg.setText("");
                finishedTX = true;
                //      }
            }
        });

        return llLayout; // We must return the loaded Layout
    }

    @Override
    public void onPause() {
        Log.v("LOL", "onPause");
        runRX = false;
        runTX = false;
        RX_THREAD.interrupt();
        //TX_THREAD.interrupt();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);

        // save the strings
        String[] values = new String[listItems.size()];
        for (int i = 0; i < listItems.size(); i++) {
            values[i] = listItems.get(i);
        }
        savedState.putStringArray("messageStrings", values);
    }

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

    private void RXHandler() {

        while(!finishedTX);

        String check;
        String message;
        String command = ";MSG;";

        WriteToBTDevice(command);

/*        while (!(check = ReadFromBTDevice()).equals("y")) {
            WriteToBTDevice(command);
            Log.d("MSG Check: ", check);
            Log.d("MSG HANDLER: ", "Stopping here");
        }*/
        check = ReadFromBTDevice();
        if (check.equals("y")) {
        WriteToBTDevice(";send;");
        message = ReadFromBTDevice();
        messageRX = "Patient: " + message;
        // this line adds the data of your EditText and puts in your array
      /*  listItems.add(message);
        // next thing you have to do is check if your adapter has changed
        adapter.notifyDataSetChanged();
        NotificationManager myNotificationManager = (NotificationManager) getActivity()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getActivity());
        Notification notification = builder.build();
        notification.sound = Uri.parse("android.resource://"
                + getActivity().getPackageName() + "/" + R.raw.msn);*/
        //myNotificationManager.notify(0, notification);
        }
    }

    private void Listener() {

        String check;


        while (!(check = ReadFromBTDevice()).equals("m"));
        Log.d("Listener Log", check);


        //if (check.equals("msg")) {

        WriteToBTDevice(";send;");

        messageRX = "Patient: " + ReadFromBTDevice();

        updateAdapter(messageRX);
/*

            NotificationManager myNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getActivity());
            Notification notification = builder.build();
            notification.sound = Uri.parse("android.resource://"
                    + getActivity().getPackageName() + "/" + R.raw.msn);*/

        // myNotificationManager.notify(0, notification);

        //}
    }
//
    public void updateAdapter(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if( !(messageRX == null)) {

                    if(!messageRX.isEmpty()) {

                        Log.d("Listener Log", messageRX);

                        listItems.add(messageRX);
                        // next thing you have to do is check if your adapter has changed
                        adapter.notifyDataSetChanged();
                        messageRX = "";
                    }
                }
            }
        });
    }
}