package com.example.brian.thebluetooth;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void OnGetClick( View v){


        // String total_price = quantity_1;
        String TotalCost = "Location : 49.2611 , -123.253" ;
        TextView Total = (TextView) findViewById (R.id.textView3) ;
        Total.setText(TotalCost) ;
        String Destination = "717 Jervis St.";
        TextView Dest = (TextView) findViewById(R.id.textView4);
        Dest.setText(Destination);


    }





}
