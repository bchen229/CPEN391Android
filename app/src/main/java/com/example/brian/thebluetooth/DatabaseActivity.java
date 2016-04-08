package com.example.brian.thebluetooth;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class DatabaseActivity extends AppCompatActivity {

    Cursor items;
    SimpleCursorAdapter adapter;
    ListView listView;
    EditText editText;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_content);
        myDb = new DatabaseHelper(this);//going to call the constructor
        listView = (ListView)findViewById(R.id.listView);
        editText = (EditText)findViewById(R.id.textSearch);
        BuildTable();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( s.toString().equals("")){
                    //reset List
                    BuildTable();
                }
                else{
                    //perform Search
                    DatabaseActivity.this.adapter.getFilter().filter(s);
                    //searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // SecondActivity.this.adapter.getFilter().filter(s.toString());

            }
        });
    }

    private void BuildTable() {

        items = myDb.getAllData();

        final String[] columns = new String[]{
                DatabaseHelper.COL_id,
                DatabaseHelper.COL_Name,
                DatabaseHelper.COL_Phone,
                DatabaseHelper.COL_Place
        };
        int[] toViewIDs = new int[]{R.id.txt1,R.id.txtViewItem,R.id.textPhone, R.id.textPlace};
        adapter = new SimpleCursorAdapter(getBaseContext(),R.layout.activity_database_item,items,
                columns,toViewIDs,0);

        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {

            public Cursor runQuery(CharSequence constraint) {
                Log.d("tag" , "runQuery constraint:" + constraint);
                return myDb.Getquery(constraint.toString());
            }

        });
        adapter.notifyDataSetChanged();

    }
}
