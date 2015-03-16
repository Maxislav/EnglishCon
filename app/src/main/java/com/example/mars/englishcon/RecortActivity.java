package com.example.mars.englishcon;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class RecortActivity extends ActionBarActivity {


    EditText editTextEn;
    EditText editTextRu;

    public final static String THIEF = "com.example.mars.englishcon.THIEF";
    DataBaseHelper dataBaseHelper;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        editTextEn = (EditText)findViewById(R.id.editTextEn);
        editTextRu = (EditText)findViewById(R.id.editTextRu);

    }


    @Override
    protected void onStart() {

        dataBaseHelper = new DataBaseHelper(this); //подключение к базе

        SQLiteDatabase sdb = dataBaseHelper.getWritableDatabase();
        dataBaseHelper.onCreate(sdb);

        sdb.close();
        dataBaseHelper.close();
        super.onStart();
     //   selectDataFromDataBase();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
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
        if (id == R.id.action_record) {
            return true;
        }

        if (id == R.id.action_learn) {
            Intent answerInent = new Intent();
            answerInent.putExtra(THIEF, "Сраный пёсик");
            setResult(RESULT_OK, answerInent);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void clickBtnAdd(View view) {



        ContentValues cv = new ContentValues();
        cv.put(dataBaseHelper.VALUE_EN, editTextEn.getText().toString());
        cv.put(dataBaseHelper.VALUE_RU, editTextRu.getText().toString());

        long nDb = dataBaseHelper.insertMyRow(cv);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Добавлена заись №: " + nDb,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        editTextEn.setText("");
        editTextRu.setText("");
    }


    public void clickBtnDrop(View view) {
        //dataBaseHelper.dropTable();

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popuapmenu);
        popupMenu.show();
        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case  R.id.menuDel:
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Данные удалены",
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        /*Toast toast = Toast.makeText(getApplicationContext(),
                "Данные удалены",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
*/


    }
}
