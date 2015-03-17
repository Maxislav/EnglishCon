package com.example.mars.englishcon;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class RecortActivity extends ActionBarActivity {


    EditText editTextEn;
    EditText editTextRu;
    LinearLayout mainLayout;

    public final static String THIEF = "com.example.mars.englishcon.THIEF";
    DataBaseHelper dataBaseHelper;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        editTextEn = (EditText)findViewById(R.id.editTextEn);
        editTextRu = (EditText)findViewById(R.id.editTextRu);
        dataBaseHelper = new DataBaseHelper(this);
        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        getData();
    }

    private void getData(){
        ArrayList<Map> arrayList = dataBaseHelper.selectData(1);
        if (arrayList != null) {
            for (Map map : arrayList) {
                String valueEn = map.get("EN").toString();
                String valueRu = map.get("RU").toString();
                String _id = map.get("ID").toString();
                String show_ru = map.get("SHOW_RU").toString();
                String in_mind = map.get("IN_MIND").toString();
                // Log.d("SHOW_RU", show_ru);
                createItems(valueEn, valueRu, _id, show_ru, in_mind);
            }
        } else {
            return;
        }
    }


    private void createItems(String en, String ru, String _id , String show_ru, String in_mind){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_row_rec, mainLayout, false);
        LinearLayout rowLinearLayout = (LinearLayout) view;
        mainLayout.addView(rowLinearLayout);
        ViewGroup viewGroup = (ViewGroup) rowLinearLayout;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child, child2, childInMind;
            ViewGroup viewGroup2;

            switch (i) {
                case 0:
                    child = viewGroup.getChildAt(i);
                  //  clickListener(child, _id, i);

                    viewGroup2 = (ViewGroup) child;
                    child2 = viewGroup2.getChildAt(0);
                    ((TextView) child2).setText(en);

                    childInMind = viewGroup2.getChildAt(1);
                    if(in_mind.equals("0")){
                        childInMind.setVisibility(View.INVISIBLE);
                    }else{
                        clickListener(childInMind, _id, i);
                    }
                    break;
                case 1:
                    child = viewGroup.getChildAt(i);

                   // clickListener(child, _id, i);

                    viewGroup2 = (ViewGroup) child;
                    child2 = viewGroup2.getChildAt(0);


                    ((TextView) child2).setText(ru);
                    break;
            }
        }

    }




    @Override
    protected void onStart() {
        //подключение к базе
        SQLiteDatabase sdb = dataBaseHelper.getWritableDatabase();
        dataBaseHelper.onCreate(sdb);
        sdb.close();
        dataBaseHelper.close();
        super.onStart();
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
        String valueEn, valueRu;
        valueEn = editTextEn.getText().toString();
        valueRu = editTextRu.getText().toString();
        //Log.d("replaceAll", valueEn);
        valueEn = valueEn.replaceAll("^\\s+","");
        valueRu = valueRu.replaceAll("^\\s+","");
        Log.d("replaceAll", valueEn);

        if(valueRu.isEmpty() || valueEn.isEmpty() ){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Пустое значение поля",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            cv.put(dataBaseHelper.VALUE_EN, valueEn );
            cv.put(dataBaseHelper.VALUE_RU, valueRu);
            cv.put(dataBaseHelper.SHOW_RU, 1);
            cv.put(dataBaseHelper.IN_MIND, 0);
            long nDb = dataBaseHelper.insertMyRow(cv);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Добавлена заись №: " + nDb,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            editTextEn.setText("");
            editTextRu.setText("");
        }
    }
    public void clickListener(View view, String _id, int n) {
        final String ID = _id;
        final int N = n;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                dataBaseHelper.setInMind(ID);
            }
        });
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
    //создание колонки
    public void createColumn(View view){
        //dataBaseHelper.createColumn();
    }

   /* public void setShow(View view) {
        dataBaseHelper.setShow();
    }*/
}
