package com.example.mars.englishcon;

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

//import android.view.ViewGroup.LayoutParams;

public class MainActivity extends ActionBarActivity {

    public TextView tv;

    LinearLayout ll;
    LinearLayout  mainLayout;
    TableLayout tableLayout;

    DataBaseHelper dataBaseHelper;
    Resources.Theme themes;
    static final private int CHOOSE_THIEF = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Learn");
        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);

        themes = getTheme();

        dataBaseHelper = new DataBaseHelper(this); //подключение к базе
        SQLiteDatabase sdb = dataBaseHelper.getWritableDatabase();
        sdb.close();
        dataBaseHelper.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectDataFromDataBase();

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
        if( id == R.id.action_learn){

            return true;
        }

        if( id == R.id.action_record){
            Intent questionIntent = new Intent( this, RecortActivity.class);
            startActivityForResult(questionIntent, CHOOSE_THIEF);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_THIEF) {
            if (resultCode == RESULT_OK) {
                String thiefname = data.getStringExtra(RecortActivity.THIEF);
                //  infoTextView.setText(thiefname);
            }else {
                // infoTextView.setText(""); // стираем текст
            }
        }
    }
    public void vhideClick(View view) {
        Log.d("vhideClick", view.toString() );

        ViewGroup parent = (ViewGroup)view;
        TextView tv =(TextView) parent.getChildAt(0);

        Log.d("INVISIBLE",     tv.isShown()+"");

        if(tv instanceof TextView){

            if(tv.isShown()){
                TextView animTextView = ((TextView)tv);
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
                new HideText(animTextView , anim).start();
            }else {
                tv.setVisibility(View.VISIBLE);
            }

        }else{
            return;
        }
    }

    public void delRow(View view){

        new ActionElements(this, (LinearLayout)view.getParent()).delRow();
    }

    public class MyTextView {
        public TextView myView;
        public  boolean visible;

        public MyTextView(TextView myView) {
            this.myView = myView;
            visible = true;

        }

        public TextView getMyView() {
            return myView;
        }
    }

    private class HideText {
        LinearLayout parentLinLayaou;
        TextView animTextView;
        Animation anim;
        HideText(TextView animTextView, Animation anim){
            this.parentLinLayaou = (LinearLayout)animTextView.getParent();
            this.animTextView = animTextView;
            this.anim = anim;
        }
        public void start(){
         //   boolean v = (View.INVISIBLE).equals(animTextView.getVisibility());
            //int v = animTextView.INVISIBLE;
            //Log.d
            //Log.d("getVisibility",     ().(View.INVISIBLE)+"" );
            //Log.d("getVisibility",     v +"");
            animTextView.startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {


                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.d("End", "Anim end");
                    animTextView.setVisibility(View.INVISIBLE);
                    //parentLinLayaou.removeView(animTextView);
                    //ll.removeView(tv);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }


    public void selectDataFromDataBase(){
        mainLayout.removeAllViews();
        if(dataBaseHelper.selectData()!=null){
            ArrayList<Map> arrayList = dataBaseHelper.selectData();

            for (Map map : arrayList) {
                String valueEn =  map.get("EN").toString();
                String valueRu =  map.get("RU").toString();
                Log.d("LOG_TAG", valueEn);
                createItems(valueEn, valueRu);
            }
        }else{
            return;
        }
    }

    private void createItems(String en, String ru){

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_row, mainLayout, false);
        LayoutParams lp = view.getLayoutParams();
        LinearLayout rowLinearLayout = (LinearLayout)view;
        mainLayout.addView(rowLinearLayout);
        ViewGroup viewGroup = (ViewGroup) rowLinearLayout;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child, child2;
            ViewGroup viewGroup2;


            switch (i){
                case 0:
                    child = viewGroup.getChildAt(i);
                    viewGroup2 = (ViewGroup) child;
                    child2 = viewGroup2.getChildAt(0);
                    ((TextView)child2).setText(en);
                    break;
                case 1:
                    child = viewGroup.getChildAt(i);
                    viewGroup2 = (ViewGroup) child;
                    child2 = viewGroup2.getChildAt(0);
                    ((TextView)child2).setText(ru);
                    break;
            }
        }
    }
}
