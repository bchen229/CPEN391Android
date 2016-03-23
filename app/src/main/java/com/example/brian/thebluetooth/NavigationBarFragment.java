package com.example.brian.thebluetooth;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Navigation bar class for the swipe
 */
public class NavigationBarFragment extends Fragment{

    // declarations used for the navigation bar
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_bar_fragment, container, false);
        // initialize the navigation bar
        mDrawerList = (ListView) view.findViewById(R.id.navList);
        mDrawerList.setBackgroundColor(Color.BLACK);
        addDrawerItems();

        return view;
    }

    // helper function for adding items into the drawerList
    private void addDrawerItems() {
        String[] buttonArray = { "Bluetooth", "Maps", "Info", "Messages"};
        mAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_selectable_list_item, buttonArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // add listeners to each item on the list
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                String selectedItem =(mDrawerList.getItemAtPosition(itemPosition).toString());
                Intent intent;

                switch(selectedItem) {
                    case "Bluetooth":
                        intent = new Intent(getActivity().getApplicationContext(), BluetoothAttempt.class);
                        startActivity(intent);
                        break;
                    case "Maps":
                        intent = new Intent(getActivity().getApplicationContext(), MapActivity.class);
                        startActivity(intent);
                        break;
                    case "Info":
                        intent = new Intent(getActivity().getApplicationContext(), InfoActivity.class);
                        startActivity(intent);
                        break;
                    case "Messages":
                        intent = new Intent(getActivity().getApplicationContext(), MessageActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
