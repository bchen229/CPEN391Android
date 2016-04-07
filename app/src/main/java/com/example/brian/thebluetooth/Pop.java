package com.example.brian.thebluetooth;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.MapFragment;

/**
 * Created by david on 2016-03-28.
 */
public class Pop extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        setupWindowAnimations();

        Button msg = (Button)findViewById(R.id.button4);
        msg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                Fragment fragment = new Messaging();

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        //.addToBackStack(null)
                        .replace(R.id.flFragmentPlaceHolder, fragment)
                        .commit();

            }
});

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*0.8), (int) (height*0.6));


    }
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }
}
