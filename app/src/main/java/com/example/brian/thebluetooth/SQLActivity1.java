package com.example.brian.thebluetooth;

import android.app.AlertDialog;
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

        editName = (EditText)findViewById(R.id.editText);
        editPhone = (EditText)findViewById(R.id.editText2);
        editPlace = (EditText)findViewById(R.id.editText3);
        editId =(EditText)findViewById(R.id.editText4);
        btnAddData = (Button)findViewById(R.id.button);
        btnGetData = (Button)findViewById(R.id.button2);
        btnUpdate = (Button)findViewById(R.id.button3);
        btnDelete = (Button)findViewById(R.id.button4);
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
                            Toast.makeText(SQLActivity.this, "Data delete", Toast.LENGTH_LONG).show();
                            editId.getText().clear();

                        }
                        else
                            Toast.makeText(SQLActivity.this,"Deletion Failed",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(SQLActivity.this,"Data Updated", Toast.LENGTH_LONG).show();
                            editId.getText().clear();
                            editName.getText().clear();
                            editPhone.getText().clear();
                            editPlace.getText().clear();
                        }
                        else
                            Toast.makeText(SQLActivity.this,"Data did not get updated",Toast.LENGTH_LONG).show();
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
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0){
                            showMessage("Error","Nothing Found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while(res.moveToNext()){
                            buffer.append("Id:"+ res.getString(0)+"\n");
                            buffer.append("Name:"+ res.getString(1)+"\n");
                            buffer.append("Phone:"+ res.getString(2)+"\n");
                            buffer.append("Place:"+ res.getString(3)+"\n\n");
                        }
                        showMessage("Address Book",buffer.toString());
                    }
                }
        );

    }

    public void showMessage(String title,String message ){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setCancelable(true);
        build.setTitle(title);
        build.setMessage(message);
        build.show();

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
