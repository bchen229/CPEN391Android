package com.example.brian.thebluetooth;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SQLActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName,editPhone,editPlace,editId;
    Button btnAddData;
    Button btnGetData;
    Button btnUpdate;
    Button btnDelete;

    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);
        myDb = new DatabaseHelper(this);//going to call the constructor

        editName = (EditText)findViewById(R.id.nameEditText);
        editPhone = (EditText)findViewById(R.id.phoneEditText);
        editPlace = (EditText)findViewById(R.id.placeEditText);
        editId =(EditText)findViewById(R.id.idEditText);
        btnAddData = (Button)findViewById(R.id.addButton);
        btnGetData = (Button)findViewById(R.id.diagnosisButton);
        btnUpdate = (Button)findViewById(R.id.updateButton);
        btnDelete = (Button)findViewById(R.id.deleteButton);
        AddData();
        viewAll();
        updateDate();
        deleteData();
        System.out.println("attempting to go to second page");
        Log.d(TAG, "trying to go to second page");
    }

    public void deleteData(){

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int delete = myDb.deleteData(editId.getText().toString());

                        if ( delete > 0 ){
                            Toast.makeText(SQLActivity.this, "Data delete", Toast.LENGTH_SHORT).show();
                            editId.getText().clear();

                        }
                        else
                            Toast.makeText(SQLActivity.this,"Deletion Failed",Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


    public void updateDate(){

        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean update = myDb.updateData(editId.getText().toString(), editName.getText().toString(),
                                editPhone.getText().toString(), editPlace.getText().toString());


                        if (update){
                            Toast.makeText(SQLActivity.this,"Data Updated", Toast.LENGTH_SHORT).show();
                            editId.getText().clear();
                            editName.getText().clear();
                            editPhone.getText().clear();
                            editPlace.getText().clear();
                        }
                        else
                            Toast.makeText(SQLActivity.this,"Data did not get updated",Toast.LENGTH_SHORT).show();
                    }
                }

        );


    }

    public void AddData(){

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted= myDb.insertData(editName.getText().toString(),
                                editPhone.getText().toString(),
                                editPlace.getText().toString());
                        if(isInserted) {
                            Toast.makeText(SQLActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            editName.getText().clear();
                            editPhone.getText().clear();
                            editPlace.getText().clear();
                        }

                        else
                            Toast.makeText(SQLActivity.this,"Data Insertion Failed",Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    public void viewAll(){

        btnGetData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SQLActivity.this, DatabaseActivity.class));
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

}
