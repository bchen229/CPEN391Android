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
    public static final String DATABASE_NAME ="AddressBook2.db";
    public static final String TABLE_NAME ="Addresses2";
    public static final String COL_id ="_id";
    public static final String COL_Name ="Name";
    public static final String COL_Phone ="Phone";
    public static final String COL_Place ="Place";
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
                "create table addresses2 " +
                        "(_id integer primary key, Name text,Phone text,Place text)"
        );
    }

    public boolean insertData(String name , String Phone, String place){
        //going to create the database and table
        db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(COL_Name,name);
        contentValue.put(COL_Phone,Phone);
        contentValue.put(COL_Place, place);
        long result = db.insert("addresses2",null,contentValue);
        //Now we make sure if the value are added
        if(result == -1){
            return false;
        }
        else
            return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS addresses2");
        onCreate(db);
    }
    //Cursor provides random read/write access
    public Cursor getAllData(){
        db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from addresses2",null);
        return result;

    }


    public boolean updateData(String id , String name , String Phone , String place){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();

        contentValue.put(COL_id,id);
        contentValue.put(COL_Name,name);
        contentValue.put(COL_Phone,Phone);
        contentValue.put(COL_Place, place);

        db.update("addresses2",contentValue,"ID = ?",new String[]{ id });
        return true;

    }

    public Integer deleteData(String id){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("addresses2","ID = ?",new String[]{id} );

    }

    public Cursor Getquery( String name){
        db = this.getWritableDatabase();
        Cursor result = db.rawQuery("Select * from addresses2 WHERE name = '"+name+"'",null);
        return result;
    }

}
