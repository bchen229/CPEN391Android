package com.example.brian.thebluetooth;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Fade;
import android.util.DisplayMetrics;

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
