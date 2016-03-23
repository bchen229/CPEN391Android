package com.example.brian.thebluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends AppCompatActivity{

    // declarations used for the navigation bar
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the navigation bar
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

        Button bluetooth_button = (Button) findViewById(R.id.button_bluetooth);
        Button map_button = (Button) findViewById(R.id.button_maps);
        Button button_message = (Button) findViewById(R.id.button_message);
        Button button_info = (Button) findViewById(R.id.button_info);

        map_button.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        button_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                startActivity(intent);
            }
        });

        button_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                startActivity(intent);
            }
        });

        bluetooth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), BluetoothAttempt.class);
                startActivity(intent);
            }
        });

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

    // helper function for adding items into the drawerList
    private void addDrawerItems() {
        String[] buttonArray = { "Bluetooth", "Maps", "Info", "Messages"};
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, buttonArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            // add listeners to each item on the list
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                String selectedItem =(mDrawerList.getItemAtPosition(itemPosition).toString());
                Intent intent;

                switch(selectedItem) {
                    case "Bluetooth":
                        intent = new Intent(getApplicationContext(), BluetoothAttempt.class);
                        startActivity(intent);
                        break;
                    case "Maps":
                        intent = new Intent(getApplicationContext(), MapActivity.class);
                        startActivity(intent);
                        break;
                    case "Info":
                        intent = new Intent(getApplicationContext(), InfoActivity.class);
                        startActivity(intent);
                        break;
                    case "Messages":
                        intent = new Intent(getApplicationContext(), MessageActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}

