package com.example.mars.englishcon;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
    private static boolean reverse;
    Intent questionIntent;

    LinearLayout ll;
    LinearLayout mainLayout;
    TableLayout tableLayout;

    DataBaseHelper dataBaseHelper;
    Resources.Theme themes;
    static final private int CHOOSE_THIEF = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        reverse = false;
        setTitle("Learn");
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        themes = getTheme();

        dataBaseHelper = new DataBaseHelper(this); //подключение к базе
        SQLiteDatabase sdb = dataBaseHelper.getWritableDatabase();
        sdb.close();
        dataBaseHelper.close();
        selectDataFromDataBase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateView();
        overridePendingTransition( R.anim.translate_left_show, R.anim.translate );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar_layout, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
      //  actionBar.setIcon(R.drawable.en_con);
        actionBar.setCustomView(v);
        menu.findItem(R.id.action_learn).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_learn) {
            return true;
        }

        if (id == R.id.action_record) {
            Intent questionIntent = new Intent(this, RecordActivity.class);
            startActivityForResult(questionIntent, CHOOSE_THIEF);
            return true;
        }

        if(id == R.id.action_game){
            if(this.questionIntent == null){
                questionIntent = new Intent(this, GameActivity.class);
            }
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
                String thiefname = data.getStringExtra(RecordActivity.THIEF);
                //  infoTextView.setText(thiefname);
            } else {
                // infoTextView.setText(""); // стираем текст
            }
        }
    }

    public void vhideClick(View view) {
        Log.d("vhideClick", view.toString());

        ViewGroup parent = (ViewGroup) view;
        TextView tv = (TextView) parent.getChildAt(0);

        Log.d("INVISIBLE", tv.isShown() + "");
        Animation anim;
        TextView animTextView;
        if (tv instanceof TextView) {
            if (tv.isShown()) {
                animTextView = ((TextView) tv);
                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
                new MyAnimation(animTextView, anim).hide();
            } else {

                //  tv.setVisibility(View.VISIBLE);
                animTextView = ((TextView) tv);
                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_show);
                new MyAnimation(animTextView, anim).show();
            }

        } else {
            return;
        }
    }

    public void delRow(View view) {
        new ActionElements(this, (LinearLayout) view.getParent()).delRow();
    }

    public class MyTextView {
        public TextView myView;
        public boolean visible;

        public MyTextView(TextView myView) {
            this.myView = myView;
            visible = true;

        }

        public TextView getMyView() {
            return myView;
        }
    }

    private class MyAnimation {
        LinearLayout parentLinLayaou;
        TextView animTextView;
        Animation anim;

        MyAnimation(TextView animTextView, Animation anim) {
            this.parentLinLayaou = (LinearLayout) animTextView.getParent();
            this.animTextView = animTextView;
            this.anim = anim;
        }

        public void hide() {
            animTextView.startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {


                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.d("End", "Anim end");
                    animTextView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        public void show() {
            animTextView.startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    animTextView.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.d("End", "Anim end");
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private void updateView(){
        mainLayout.removeAllViews();
        selectDataFromDataBase();
    }

    public void selectDataFromDataBase() {
       // mainLayout.removeAllViews();
        if (dataBaseHelper.selectData(0) != null) {
            ArrayList<Map> arrayList = dataBaseHelper.selectData(0);
            for (Map map : arrayList) {
                String valueEn = map.get("EN").toString();
                String valueRu = map.get("RU").toString();
                String _id = map.get("ID").toString();
                String show_ru = map.get("SHOW_RU").toString();
             //  Log.d("SHOW_RU", show_ru);
                createItems(valueEn, valueRu, _id, show_ru);
            }
        } else {
            return;
        }
    }
    private void createItems(String en, String ru, String _id , String show_ru) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_row, mainLayout, false);
        LayoutParams lp = view.getLayoutParams();
        LinearLayout rowLinearLayout = (LinearLayout) view;
        mainLayout.addView(rowLinearLayout);
        ViewGroup viewGroup = (ViewGroup) rowLinearLayout;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child, child2;
            ViewGroup viewGroup2;

            switch (i) {
                case 0:
                    child = viewGroup.getChildAt(i);
                    clickListener(child, _id, i);
                    viewGroup2 = (ViewGroup) child;
                    child2 = viewGroup2.getChildAt(0);
                    if(reverse){
                        ((TextView) child2).setText(ru);
                    }else{
                        ((TextView) child2).setText(en);
                    }

                    break;
                case 1:
                    child = viewGroup.getChildAt(i);
                    clickListener(child, _id, i);
                    viewGroup2 = (ViewGroup) child;
                    child2 = viewGroup2.getChildAt(0);
                    if(show_ru.equals("0")){
                        ((TextView) child2).setVisibility(View.INVISIBLE);
                    }
                    if(reverse){
                        ((TextView) child2).setText(en);
                    }else{
                        ((TextView) child2).setText(ru);
                    }
                    break;
            }
        }
    }
    public void clickBtnReverse(View view){
        rott1();
    }

    public  void rott1(){
        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.flip_left_in);
        rotateAnim(anim, false);
    }
    public  void rott2(){
        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.flip_left_out);
        rotateAnim(anim, true);
    }

    private void rotateAnim(Animator anim, final boolean fill){

        final Context c = this.getApplicationContext();
        anim.setTarget(mainLayout);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(fill){
                    updateView();
                }
            }
            @Override
            public void onAnimationEnd(Animator animation) {
               if(!fill){
                   if(reverse){
                       reverse = false;
                   }else{
                       reverse = true;
                   }
                   rott2();
               }
            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }


    public void clickListener(View view, String _id, int n) {
        final String ID = _id;
        final int N = n;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (N) {
                    case 0:
                        dataBaseHelper.setInMind(ID);
                        delRow(v);
                        break;
                    default:
                        dataBaseHelper.setShow(ID);
                        vhideClick(v);

                }
            }
        });
    }
}
