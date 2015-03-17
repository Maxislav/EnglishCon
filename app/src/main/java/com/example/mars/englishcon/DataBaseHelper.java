package com.example.mars.englishcon;

/**
 * Created by mars on 3/13/15.
 *
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DataBaseHelper extends SQLiteOpenHelper {
    /*public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/

    SQLiteDatabase myBase;

    private static final String DATABASE_NAME = "en.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "dictionary";

    public static final String UID = "_id";
    //  public static final String CATNAME = "catname";
    public static final String VALUE_EN = "value_en";
    public static final String VALUE_RU = "value_ru";
    public static final String SHOW_RU = "show_ru";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE if not exists "
            + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + VALUE_EN + " VARCHAR(255), " + VALUE_RU + " VARCHAR(255)" + ");";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DataBaseHelper", "Create");
        db.execSQL(SQL_CREATE_ENTRIES);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertMyRow(ContentValues cv) {
        long i;
        SQLiteDatabase sdb = this.getWritableDatabase();


        i = sdb.insert(this.TABLE_NAME, null, cv);
        Log.d("DataBaseHelper", i + "");
        sdb.close();
        return i;

    }


    public ArrayList<Map> selectData() {
        SQLiteDatabase sdb = this.getWritableDatabase();
        Map<String, String> map;
        // Array map;
        ArrayList<Map> arrayList = new ArrayList<Map>();


        Cursor cursor;
        try {
            cursor = sdb.query(this.TABLE_NAME, new String[]{
                            this.UID, this.VALUE_EN, this.VALUE_RU, this.SHOW_RU},
                    null,
                    null,
                    null,
                    null,
                    null
            );
        } catch (Exception e) {
            Log.d("Err", "table not exist");

            return null;
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(this.UID));
            String valueEn = cursor.getString(cursor.getColumnIndex(this.VALUE_EN));
            String valueRu = cursor.getString(cursor.getColumnIndex(this.VALUE_RU));
            String show_ru = cursor.getString(cursor.getColumnIndex(this.SHOW_RU));

           // Log.d("LOG_TAG", "ROW " + id + " EN: " + valueEn + " RU: " + valueRu+"    " +show_ru);

            map = new HashMap<String, String>();
            map.put("ID", id + "");
            map.put("EN", valueEn);
            map.put("RU", valueRu);
            map.put("SHOW_RU", show_ru);
            arrayList.add(map);
        }
        sdb.close();
        return arrayList;
    }

    public void dropTable() {
        SQLiteDatabase sdb = this.getWritableDatabase();
        sdb.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME + "'");
        sdb.close();
    }

    //создание колонки в таблице
    public void createColumn(){
        String jquery = "ALTER TABLE "+this.TABLE_NAME+" ADD show_ru INTEGER";
        SQLiteDatabase sdb = this.getWritableDatabase();
        sdb.execSQL(jquery);
        sdb.close();

    }
    public void setShow(String id){
        String jquery;
        String shown = "0";
        SQLiteDatabase sdb = this.getWritableDatabase();

        jquery = "SELECT show_ru FROM "+this.TABLE_NAME+" WHERE _id="+id;
        Cursor cursor = sdb.rawQuery(jquery,null);

        while (cursor.moveToNext()) {
            //Log.d("SELECT show_ru", cursor.getString(cursor.getColumnIndex(this.SHOW_RU)));
            shown = cursor.getString(cursor.getColumnIndex(this.SHOW_RU));
        }
        if(shown.equals("1")){
            jquery = "UPDATE " + this.TABLE_NAME+" SET show_ru = 0 WHERE _id="+id;
            Log.d("set show_ru", "0" );
        }else{
            jquery = "UPDATE " + this.TABLE_NAME+" SET show_ru = 1 WHERE _id="+id;
            Log.d("set show_ru", "1" );
        }

       // jquery = "UPDATE " + this.TABLE_NAME+" SET show_ru = 1 ";

        sdb.execSQL(jquery);
        sdb.close();

    }



}
