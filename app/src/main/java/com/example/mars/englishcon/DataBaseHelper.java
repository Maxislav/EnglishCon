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
    public static final String IN_MIND = "in_mind";
    public static final String IN_GAME = "in_game";


    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE if not exists "
            + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + VALUE_EN + " VARCHAR(255), " + VALUE_RU + " VARCHAR(255), "
            + SHOW_RU + " INTEGER, " + IN_MIND + " INTEGER, " + IN_GAME+ " INTEGER "+ ");";


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


    public ArrayList<Map> selectData(int inMind) {
        String jquery;
        switch (inMind){
            case 0:
                jquery = "SELECT * FROM "+this.TABLE_NAME+" WHERE in_mind=0";
                break;
            case 1:
                jquery = "SELECT * FROM "+this.TABLE_NAME;
                break;
            case 2:
                jquery = "SELECT * FROM "+this.TABLE_NAME+" WHERE in_mind=1 AND in_game<10";
                break;
            default:
                jquery = "SELECT * FROM "+this.TABLE_NAME;
        }


        SQLiteDatabase sdb = this.getWritableDatabase();
        Map<String, String> map;
        ArrayList<Map> arrayList = new ArrayList<Map>();
        Cursor cursor;
        try {
            cursor = sdb.rawQuery(jquery,null);
        } catch (Exception e) {
            Log.d("Err", "table not exist");
            return null;
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(this.UID));
            String valueEn = cursor.getString(cursor.getColumnIndex(this.VALUE_EN));
            String valueRu = cursor.getString(cursor.getColumnIndex(this.VALUE_RU));
            String show_ru = cursor.getString(cursor.getColumnIndex(this.SHOW_RU));
            String in_mind = cursor.getString(cursor.getColumnIndex(this.IN_MIND));

            Log.d("LOG_TAG", "ROW " + id + " EN: " + valueEn + " RU: " + valueRu+" SHOW_RU: " +show_ru+ " IN_MIND: "+in_mind);

            map = new HashMap<String, String>();
            map.put("ID", id + "");
            map.put("EN", valueEn);
            map.put("RU", valueRu);
            map.put("SHOW_RU", show_ru);
            map.put("IN_MIND", in_mind);
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

    //вилим или не видим перевод
    public void setShow(String id){
        String jquery;
        String shown = "0";
        SQLiteDatabase sdb = this.getWritableDatabase();
        jquery = "SELECT show_ru FROM "+this.TABLE_NAME+" WHERE _id="+id;
        Cursor cursor = sdb.rawQuery(jquery,null);
        while (cursor.moveToNext()) {
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
    public void setInMind(String id, boolean in_mind){
        String jquery;
        String inMind = "0";
        SQLiteDatabase sdb = this.getWritableDatabase();
        jquery = "SELECT "+this.IN_MIND+" FROM "+this.TABLE_NAME+" WHERE _id="+id;
        Cursor cursor = sdb.rawQuery(jquery,null);
        while (cursor.moveToNext()) {
            inMind = cursor.getString(cursor.getColumnIndex(this.IN_MIND));
        }

        if(in_mind){
            jquery = "UPDATE " + this.TABLE_NAME+" SET in_mind = 1 WHERE _id="+id;
            Log.d("set inMind", "1" );
        }else{
            jquery = "UPDATE " + this.TABLE_NAME+" SET in_mind = 0 WHERE _id="+id;
            Log.d("set inMind", "0" );
        }


      /*
        if(inMind.equals("0")){
            jquery = "UPDATE " + this.TABLE_NAME+" SET in_mind = 1 WHERE _id="+id;
            Log.d("set inMind", "1" );
        }else{
            jquery = "UPDATE " + this.TABLE_NAME+" SET in_mind = 0 WHERE _id="+id;
            Log.d("set inMind", "0" );
        }*/
        sdb.execSQL(jquery);
        sdb.close();

    }

    //Выбор одной строки по ID
    public Map<String,String> selectById(String id){
        String valueEn, valueRu;
        String jquery = "SELECT "+ VALUE_EN +", "+VALUE_RU+" FROM "+ TABLE_NAME+" WHERE _id="+id;
        Map<String,String> map = new HashMap<String, String>();
        SQLiteDatabase sdb = getWritableDatabase();
        Cursor cursor = sdb.rawQuery(jquery,null);
        while (cursor.moveToNext()){
            //valueEn
            map.put(VALUE_EN, cursor.getString(cursor.getColumnIndex(this.VALUE_EN)));
            map.put(VALUE_RU, cursor.getString(cursor.getColumnIndex(this.VALUE_RU)));

        }
        sdb.close();
        return map;

    }

    //редактирование
    public boolean updateValueRow(String valueEn , String valueRu, String id){
        SQLiteDatabase sdb = getWritableDatabase();
        String jquery = "UPDATE " + this.TABLE_NAME+" SET value_en = '"+valueEn+"', value_ru='"+valueRu+"' WHERE _id="+id;
        try {
            sdb.execSQL(jquery);
        }catch (Exception e){
            e.printStackTrace();
            sdb.close();
            return false;
        }
        sdb.close();
        return true;
    }

    public boolean delRowById(String id){
        SQLiteDatabase sdb = getWritableDatabase();
        String jquery = "DELETE FROM "+TABLE_NAME+" WHERE _id="+id;
        try {
            sdb.execSQL(jquery);
        }catch (Exception e){
            e.printStackTrace();
            sdb.close();
            return false;
        }
        sdb.close();
        return true;
    }



    //создание колонки в таблице
    public void createColumn(String colName, String type){
        String jquery = "ALTER TABLE "+this.TABLE_NAME+" ADD COLUMN "+colName+" "+type;

        SQLiteDatabase sdb = this.getWritableDatabase();
       // sdb.execSQL(jquery);
       // jquery = "UPDATE " + this.TABLE_NAME+" SET in_mind=0 ";
        sdb.execSQL(jquery);



        sdb.close();

    }
    public Map<String, String> getRundom(){
        Map<String,String> map = new HashMap<String, String>();
        ArrayList<Map> arrayList = selectData(2);
        Log.d("MyLog","arrayList size:"+ arrayList.size()+"");
        int n = random_int(0,arrayList.size());
        map = arrayList.get(n);
        return map;
    }



    public int setGame(String id, boolean in_game){
        if(!in_game){
            setInMind(id, in_game);
        }
        SQLiteDatabase sdb = getWritableDatabase();
        String jquery = "SELECT "+ IN_GAME +" FROM "+ TABLE_NAME+" WHERE _id="+id;
        String inGame="";
        Cursor cursor = sdb.rawQuery(jquery,null);
        while (cursor.moveToNext()){
            inGame = cursor.getString(cursor.getColumnIndex(IN_GAME));
        }
        int n;
        if(inGame!=null){
            n  = Integer.parseInt(inGame);
            if(in_game){
                n++;
            }else if(n<1){
                n=0;
            }else{
                n--;
            }
            Log.d("MyLog", " inGame= "+ n);
            jquery = "UPDATE " + TABLE_NAME+" SET "+IN_GAME+"="+n+" WHERE _id="+id;
        }else{
            if(in_game){
                n=1;
            }else{
                n=0;
            }
            Log.d("MyLog", " inGame= "+ null);
            jquery = "UPDATE " + TABLE_NAME+" SET "+IN_GAME+"="+n+" WHERE _id="+id;
        }
        try {
            sdb.execSQL(jquery);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("MyLog","My SQL Exception   " + e.toString());

            //createColumn(IN_GAME, "INTEGER");
           // setGame(id, in_game);
        }
        sdb.close();
        return n;
    }

    private static int random_int(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }

}
