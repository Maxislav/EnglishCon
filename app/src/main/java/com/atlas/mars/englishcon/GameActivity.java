package com.atlas.mars.englishcon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//import java.util.logging.Handler;

/**
 * Created by mars on 3/19/15.
 */
public class GameActivity extends ActionBarActivity {

    private  ArrayList<String> mixArrayList; //миксованый

    private String findValue;


    private float dpHeight, dpWidth, density;
    DisplayMetrics displayMetrics;
    static final private int CHOOSE_THIEF = 0;
    final private String LOG = "MyLog";
    private LayoutInflater inflater;
    private int nElements;
    private int elementsLength;
    private float summWidth;
    private float mainLayoutWidth;
    private TextView fillTextVeiw;
    private String fillTex;
    private TextView findTextView;
    private String value;
    private String mixValue;
    private String[] arrayValues;
    private LinearLayout rowLinearLayout;
    private DataBaseHelper dataBaseHelper;
    private String currentId;
    private TextView tvInMind, tvReady, tvCurrent;
    ArrayList<Map> arrayList;


    public void setRowLinearLayout(LinearLayout rowLinearLayout) {
        this.rowLinearLayout = rowLinearLayout;
    }

    public void setMainLayoutWidth(float mainLayoutWidth) {
        this.mainLayoutWidth = mainLayoutWidth;
    }

    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);




        fillTextVeiw = (TextView) findViewById(R.id.fillTextVeiw);
        findTextView = (TextView) findViewById(R.id.findTextView);
        fillTex = "";
        dataBaseHelper = new DataBaseHelper(this);
        displayMetrics = this.getResources().getDisplayMetrics();
        density = displayMetrics.density;
        dpHeight = displayMetrics.heightPixels / density;
        dpWidth = displayMetrics.widthPixels / density;
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        inflater = getLayoutInflater();
        nElements = 0;
        summWidth = 0;
        tvInMind = (TextView)findViewById(R.id.tvInMind);
        tvReady = (TextView)findViewById(R.id.tvReady);
        tvCurrent = (TextView)findViewById(R.id.tvCurrent);
        onInit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.translate_show, R.anim.translate_left_hide);
        Log.d(LOG, "Density: " + density + " Width dp: " + dpWidth + " Width Pixels: " + displayMetrics.widthPixels);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar_layout, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        //  actionBar.setIcon(R.drawable.en_con);
        actionBar.setCustomView(v);
        menu.findItem(R.id.action_game).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_learn) {
            Log.d("MyLog", "Home");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_record) {
            Intent intent = new Intent(this, RecordActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_game) {

            return true;
           /* Intent questionIntent = new Intent(this, GameActivity.class);
            startActivityForResult(questionIntent, CHOOSE_THIEF);*/
        }

        if (id == android.R.id.home) {
            Log.d("MyLog", "Home");
            finish();
            return true;
        }

        Log.d("MyLog", "Home");
        finish();

        return super.onOptionsItemSelected(item);
    }


    public void onInit() {
        if (value == null) {
          //  mixValue = getRandom();
            findValue = getRandom();
            if(findValue.isEmpty()){
                return;
            }
            mixArrayList = new ArrayList<>();

            String arrayValues[] = findValue.split("");
                   //arrayValues = findValue.split("");
            for(int i = 0; i<arrayValues.length; i++){
                if(!arrayValues[i].isEmpty()){
                    mixArrayList.add(arrayValues[i]);
                }
            }

            //elementsLength = arrayValues.length;
            elementsLength = mixArrayList.size();
            arrayList = new ArrayList<Map>();
            generate();
            getCountInMind();
            getCountReady();
        }
    }

    private void generate() {
        createRow();
    }


    private String getRandom() {
        Map<String, String> map = dataBaseHelper.getRundom();
        if(map.size()<1){
            return "";
        }

        value = map.get("EN");
        currentId =  map.get("ID");
        findTextView.setText(map.get("RU"));
        tvCurrent.setText(map.get("IN_GAME"));
        nElements = 0;
        fillTex = "";
        arrayValues = value.split("");
        ArrayList<String> imgList = new ArrayList<String>(Arrays.asList(arrayValues));
        long seed = System.nanoTime();
        Collections.shuffle(imgList, new Random(seed));
        String mix = "";
        for (String item : imgList) {
            String a = item;
            if (!item.isEmpty()) {
                //Log.d(LOG, "char: " + a.toString());
                mix += a.toString();
            }
        }
        return mix;
    }

    private void createRow() {
        summWidth = 0;
        View view = inflater.inflate(R.layout.letter_row_layout, mainLayout, false);
        LinearLayout rowLinearLayout = (LinearLayout) view;
        mainLayout.addView(rowLinearLayout);
        setRowLinearLayout(rowLinearLayout);
        getWidth(mainLayout, 0);

    }

    private void createColum(LinearLayout rowLinearLayout) {
        View latterView = inflater.inflate(R.layout.letter_layout, rowLinearLayout, false);
        rowLinearLayout.addView(latterView);
        TextView t = (TextView) ((ViewGroup) latterView).getChildAt(0);
        if (nElements < mixArrayList.size()+1) {
            t.setText(mixArrayList.get(nElements) + "");
            clickListener(latterView, "0", mixArrayList.get(nElements));
            nElements++;
            getWidth(latterView, 1);
        }

    }


    private void getWidth(View _v, int _c) {
        final View v = _v;
        final int c = _c;
        v.post(new Runnable() {
            @Override
            public void run() {
                v.getWidth(); //height is ready
                switch (c) {
                    case 0:
                        setMainLayoutWidth(v.getWidth());
                        createColum(rowLinearLayout);
                        break;
                    case 1:
                        summWidth += v.getWidth()+5;
                        if (summWidth < mainLayoutWidth && nElements < mixArrayList.size()) {
                            createColum(rowLinearLayout);
                        } else if (nElements < mixArrayList.size()) {
                            createRow();
                        }
                        break;
                    default:
                }
            }
        });
    }

    public void clickListener(View view, String _id, String _letter) {
        final String ID = _id;
        final String letter = _letter;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                Map<String, View> map = new HashMap<String, View>();
                fillTex += letter;
                fillTextVeiw.setText(fillTex);
                map.put(letter, v);
                arrayList.add(map);
                if (arrayList.size() == mixArrayList.size()) {
                    checkNext();
                }
                final View _v = v;
               // animTextView = ((TextView) tv);
                Animation    anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_out);
                _v.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        _v.setVisibility(View.INVISIBLE);
                        _v.setClickable(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                //v.setVisibility(View.INVISIBLE);
            }
        });
    }


    public void clickBack(View v) {
        if (arrayList == null || arrayList.size() < 1) {
            return;
        }
        Map<String, View> map;
        map = arrayList.remove(arrayList.size() - 1);
        String k = "";
        for (String key : map.keySet()) {
            k = key.toString();
            Log.d(LOG, "Key: " + key.toString() + "   ");
        }
        map.get(k).setVisibility(View.VISIBLE);
        String[] fillTex = this.fillTex.split("");

        String newFillText = "";
        for (int i = 1; i < fillTex.length - 1; i++) {
            newFillText += fillTex[i];
        }
        this.fillTex = newFillText;
        fillTextVeiw.setText(this.fillTex);
    }

    public void clickRegen(View v) {
        regenParam();
    }

    public void regenParam() {
        value = null;
        mainLayout.removeAllViews();
        fillTextVeiw.setText("...");
        onInit();
    }

    private class MyHundler  extends Handler{
        public static final int ID_0 = 0;
        public static final int ID_1 = 1;
        public static final int ID_2 = 2;
        @Override
        public void handleMessage(android.os.Message msg) {

            switch (msg.what){
                case 0:
                    Log.d(LOG, msg.obj.toString());

                    Toast toast = Toast.makeText(getApplicationContext(),
                            msg.obj.toString(),
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    regenParam();
                    break;
                case 1:
                    tvInMind.setText(msg.obj.toString());
                    break;
                case 2:
                    tvReady.setText(msg.obj.toString());
                    break;
                default:

            }


        }
    }
    public void clickTip(View view){
        Toast toast = Toast.makeText(getApplicationContext(),
                value,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void getCountInMind(){
       final Handler h = new MyHundler();
        Thread thread;
        thread = new Thread() {
            public void run() {
                try {
                    int n =  dataBaseHelper.countRow(dataBaseHelper.IN_MIND+" = 1 ");
                    Message msg = Message.obtain(h, MyHundler.ID_1);
                    msg.obj = n+"";
                    h.sendMessage(msg);
                } catch (Exception e) {
                    Log.e(LOG,"Err dataBaseHelper.IN_MIND: " , e);
                }
            }
        };
        thread.start();
    }
    private void getCountReady(){
        final Handler h = new MyHundler();
        Thread thread;
        thread = new Thread() {
            public void run() {
                try {
                    int n =  dataBaseHelper.countRow(dataBaseHelper.IN_GAME+"='"+dataBaseHelper.maxCountRepeat+"' ");
                    Message msg = Message.obtain(h, MyHundler.ID_2);
                    msg.obj = n+"";
                    h.sendMessage(msg);
                } catch (Exception e) {
                    Log.e(LOG,"Err dataBaseHelper.IN_GAME: " , e);
                }
            }
        };
        thread.start();
    }

    private void checkNext() {
        if (fillTex.equals(value)) {
            final int n = dataBaseHelper.setGame(currentId, true);
            final Handler h = new MyHundler();
            Thread thread;
            thread = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(500);
                        Message msg = Message.obtain(h, MyHundler.ID_0);
                        msg.obj = "Correct result:" +n;
                        h.sendMessage(msg);
                    } catch (InterruptedException e) {
                        Log.e(LOG, "run in thread", e);
                    }
                }
            };
            thread.start();
        }else{
            if(currentId!=null){
                dataBaseHelper.setGame(currentId, false);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Wrong",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            }
        }
    }
}
