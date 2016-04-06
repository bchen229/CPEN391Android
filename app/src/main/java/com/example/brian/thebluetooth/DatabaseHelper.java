package com.example.brian.thebluetooth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by kimia on 2016-03-31.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //Declare variables for database
    public static final String DATABASE_NAME ="AddressBook.db";
    public static final String TABLE_NAME ="Addresses";
    public static final String COL_1 ="ID";
    public static final String COL_2 ="NAME";
    public static final String COL_3 ="PHONE";
    public static final String COL_4 ="PLACE";
    private Context ourcontext;
    private DatabaseHelper dbhelper;
    private SQLiteDatabase db;
    //When This constructor is called the database will be created
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }
    ////To create a table inside the database this should be called
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table addresses " +
                        "(id integer primary key, name text,phone text,place text)"
        );
    }

    public boolean insertData(String name , String Phone, String place){
        //going to create the database and table
        db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(COL_2,name);
        contentValue.put(COL_3,Phone);
        contentValue.put(COL_4, place);
        long result = db.insert("addresses",null,contentValue);
        //Now we make sure if the value are added
        if(result == -1){
            return false;
        }
        else
            return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS addresses");
        onCreate(db);
    }
    //Cursor provides random read/write access
    public Cursor getAllData(){
        db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from addresses",null);
        return result;

    }


    public boolean updateData(String id , String name , String Phone , String place){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();

        contentValue.put(COL_1,id);
        contentValue.put(COL_2,name);
        contentValue.put(COL_3,Phone);
        contentValue.put(COL_4, place);

        db.update("addresses",contentValue,"ID = ?",new String[]{ id });
        return true;

    }

    public Integer deleteData(String id){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("addresses","ID = ?",new String[]{id} );

    }


}
