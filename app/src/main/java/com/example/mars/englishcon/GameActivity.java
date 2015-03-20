package com.example.mars.englishcon;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by mars on 3/19/15.
 */
public class GameActivity extends ActionBarActivity {
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
    ArrayList <Map> arrayList;

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

        fillTextVeiw = (TextView)findViewById(R.id.fillTextVeiw);
        findTextView = (TextView)findViewById(R.id.findTextView);
        fillTex ="";
        dataBaseHelper = new DataBaseHelper(this);
        displayMetrics = this.getResources().getDisplayMetrics();
        density = displayMetrics.density;
        dpHeight = displayMetrics.heightPixels / density;
        dpWidth = displayMetrics.widthPixels / density;
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        inflater = getLayoutInflater();
        nElements = 0;
        summWidth = 0;
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
            finish();
            return true;
        }

        if (id == R.id.action_record) {
            Log.d("MyLog", "Home");
            finish();
            return true;
        }

        if(id == R.id.action_game){
            return true;
           /* Intent questionIntent = new Intent(this, GameActivity.class);
            startActivityForResult(questionIntent, CHOOSE_THIEF);*/
        }

        if(id ==  android.R.id.home){
            Log.d("MyLog", "Home");
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void onInit() {
        arrayList = new ArrayList<Map>();

        Map<String, String> map = dataBaseHelper.getRundom();
        value = map.get("EN");
        findTextView.setText(map.get("RU"));
        nElements = 0;
        fillTex ="";
        arrayValues = value.split("");
        ArrayList<String> imgList = new ArrayList<String>(Arrays.asList(arrayValues));
        long seed = System.nanoTime();
        Collections.shuffle(imgList, new Random(seed));
        String mix = "";
        for (String item : imgList) {
            String a = item;
            if (!item.isEmpty()) {
                Log.d(LOG, "char: " + a.toString());
                mix += a.toString();
            }
        }
        mixValue = mix;
        arrayValues = mixValue.split("");
        elementsLength = arrayValues.length;
        createRow();
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
        //Log.d(LOG, "before: "+t.getWidth()+"");
        nElements++;
        if (nElements < arrayValues.length) {
            t.setText(arrayValues[nElements] + "");
            clickListener(latterView, "0" , arrayValues[nElements] );
        }
        getWidth(latterView, 1);
    }


    private void getWidth(View _v, int _c) {
        final View v = _v;
        final int c = _c;
        v.post(new Runnable() {
            @Override
            public void run() {
                v.getWidth(); //height is ready

                // Log.d(LOG, "Width: " + v.getWidth() + "");
                // sum =((float)v.getWidth());
                switch (c) {
                    case 0:
                        setMainLayoutWidth(v.getWidth());
                        createColum(rowLinearLayout);
                        break;
                    default:
                        summWidth += v.getHeight();
                        if (summWidth < mainLayoutWidth && nElements < arrayValues.length - 1) {
                            createColum(rowLinearLayout);
                        } else if (nElements < arrayValues.length - 1) {
                            createRow();
                        }
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
                Map<String, View> map = new HashMap<String, View>();
                fillTex+=letter;
                fillTextVeiw.setText(fillTex);
                map.put(inflater.toString(), v);
                arrayList.add(map);
                v.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void clickBack(View v){
        Map<String, View> map;
        map = arrayList.remove(arrayList.size()-1);


    }
}
