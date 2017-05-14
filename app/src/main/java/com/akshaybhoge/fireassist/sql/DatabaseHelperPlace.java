package com.akshaybhoge.fireassist.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.akshaybhoge.fireassist.model.Place;

/**
 * Created by akshaybhoge on 5/4/17.
 */

public class DatabaseHelperPlace extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "PlaceManager.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelperPlace(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String TABLE_PLACE = "place";

    public static final String COLUMN_PLACE_ID = "place_id";
    public static final String COLUMN_PLACE_LATITUDE = "place_latitude";
    public static final String COLUMN_PLACE_LONGITUDE = "place_longitude";
    public static final String COLUMN_PLACE_PLACENAME= "place_placeName";



    public String DROP_PLACE_TABLE = "DROP TABLE IF EXISTS " + TABLE_PLACE;



    @Override
    public void onCreate(SQLiteDatabase db){

        String CREATE_PLACE_TABLE = "CREATE TABLE " + TABLE_PLACE + "("
                + COLUMN_PLACE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PLACE_LATITUDE + " TEXT,"
                + COLUMN_PLACE_LONGITUDE + " TEXT," + COLUMN_PLACE_PLACENAME + " TEXT" + ")";

        db.execSQL(CREATE_PLACE_TABLE);

    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_PLACE_TABLE);
        onCreate(db);
    }

    public void addPlace(Place place){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLACE_LATITUDE, place.getLatitude());
        values.put(COLUMN_PLACE_LONGITUDE, place.getLongitude());
        values.put(COLUMN_PLACE_PLACENAME, place.getPlaceName());

        db.insert(TABLE_PLACE, null, values);
        db.close();
    }


    public boolean checkPlace(String placeName){
        String[] columns = {
                COLUMN_PLACE_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_PLACE_PLACENAME + " = ?";
        String[] selectionArgs = { placeName };

        Cursor cursor = db.query(TABLE_PLACE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public boolean checkPlace(String latitude, String longitude){
        String[] columns = {
                COLUMN_PLACE_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_PLACE_LATITUDE + " = ?" + "AND" + COLUMN_PLACE_LONGITUDE + " = ?";
        String[] selectionArgs = { latitude, longitude };

        Cursor cursor = db.query(TABLE_PLACE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }
}
