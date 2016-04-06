package com.example.brian.thebluetooth;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;

public class NaviImage extends Activity {

    private static final String DEFAULT_QUERY = "San Francisco";
    ImageManager mImageManager;
    private static boolean isSplashShown = false;
    private String query;
    private TextView textView;
    private ProgressBar progressBar;
    private Context mContext;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        mImageManager = ImageManager.getInstance(mContext);
        try {
            handleIntent(getIntent());
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        } catch (final JSONException e) {
            e.printStackTrace();
        }


        initGridView();
    }

    private void initGridView() {
        setContentView(R.layout.image_grid);
        gridView = (GridView) findViewById(R.id.photoview);
        final NaviImageAdapter imageAdapter = new NaviImageAdapter(mContext);
        gridView.setAdapter(imageAdapter);
        progressBar = (ProgressBar) findViewById(R.id.a_progressbar);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Create an intent to show a particular item.
                final Intent i = new Intent(NaviImage.this, ViewImage.class);
                Log.d("Click", Integer.toString(mImageManager.size()));
                Log.d("Click", mImageManager.get(position).getThumbUrl());
                i.putExtra(ImageManager.PANORAMIO_ITEM_EXTRA, mImageManager.get(position));
                startActivity(i);
            }
        });
        gridView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            public void onChildViewAdded(View parent, View child) {
                progressBar.setVisibility(View.INVISIBLE);
                ((ViewGroup) parent).getChildAt(0).setSelected(true);
            }

            public void onChildViewRemoved(View parent, View child) {
            }
        });

//        textView = (TextView) findViewById(R.id.place_name);
//        textView.setText(query);
//        PanoramioLeftNavService.getLeftNavBar(this);
        gridView.requestFocus();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        progressBar.setVisibility(View.VISIBLE);
        try {
            handleIntent(intent);
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        } catch (final JSONException e) {
            e.printStackTrace();
        }
        initGridView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageManager.clear();
    }

    private void handleIntent(Intent intent) throws IOException, URISyntaxException, JSONException {
        mImageManager.load((float) -123.250812, (float) -123.247403, (float) 49.260998, (float) 49.263114);
    }
}
