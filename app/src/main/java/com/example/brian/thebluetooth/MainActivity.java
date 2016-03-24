package com.example.brian.thebluetooth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity{

    // declarations used for the navigation bar
    String[] buttonArray = {"Home", "Bluetooth"};
    //, "Maps", "Info", "Messages"
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the navigation bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navList);
        if(mDrawerList != null) {
            addDrawerItems();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Swaps fragments in the main content view
    private void selectItem(int position) {

        Fragment fragment = new HomeFragment();

        switch(position) {
            case 1:
                fragment = new BluetoothAttempt();
                break;
            default:
                break;
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(buttonArray[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    // helper function for adding items into the drawerList
    private void addDrawerItems() {
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_selectable_list_item, buttonArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // add listeners to each item on the list
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                selectItem(position);
            }
        });
    }
}

