package com.cds_jo.GalaxySalesApp.ManCard;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.StartUpActivity;

public class SplashLoginAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);
        ImageView imageView=(ImageView)findViewById(R.id.imageView) ;
        Animation animation = AnimationUtils.loadAnimation(SplashLoginAct.this, R.animator.moveimg);
        imageView.startAnimation(animation);
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashLoginAct.this, StartUpActivity.class));
                finish();
            }
        }, secondsDelayed * 5000);

    }
}
