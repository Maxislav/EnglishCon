package com.atlas.mars.englishcon;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

/**
 * Created by mars on 3/16/15.
 */
public class ActionElements {
    View view;
    LinearLayout linearLayout;
    LinearLayout parentLinearLayout;
    Activity activity;


    public ActionElements(Activity activity, View view){
        this.activity = activity;
        this.view = view;

    }

    public void delRow(){
        linearLayout = (LinearLayout)view;
        parentLinearLayout = (LinearLayout)activity.findViewById(R.id.mainLayout);
        Animation anim = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.translate);
        linearLayout.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout.removeAllViews();
                new Handler().post(new Runnable() {
                    public void run() {
                        parentLinearLayout.removeView(linearLayout);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
