package com.example.brian.thebluetooth;

import android.app.Fragment;
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

public class NaviImage extends Fragment {

    private static final String DEFAULT_QUERY = "San Francisco";
    ImageManager mImageManager;
    private static boolean isSplashShown = false;
    private String query;
    private TextView textView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mImageManager = ImageManager.getInstance(getActivity());
        try {
            handleIntent(getActivity().getIntent());
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        } catch (final JSONException e) {
            e.printStackTrace();
        }

        // intiialize the gridview
        GridView gridView = (GridView) inflater.inflate(R.layout.image_grid, container, false);
        final ImageAdapter imageAdapter = new ImageAdapter(getActivity());
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Create an intent to show a particular item.
                startActivity(new Intent(getActivity(), ViewImage.class)
                        .putExtra(ImageManager.PANORAMIO_ITEM_EXTRA,
                                mImageManager.get(position)));
            }
        });
        gridView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            public void onChildViewAdded(View parent, View child) {
                ((ViewGroup) parent).getChildAt(0).setSelected(true);
            }

            public void onChildViewRemoved(View parent, View child) {
            }
        });

        return gridView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        mImageManager.clear();
    }

    private void handleIntent(Intent intent) throws IOException, URISyntaxException, JSONException {
        mImageManager.load((float) -123.250812, (float) -123.247403, (float) 49.260998, (float) 49.263114);
    }
}
