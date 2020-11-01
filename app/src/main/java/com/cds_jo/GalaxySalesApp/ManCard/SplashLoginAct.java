package com.cds_jo.GalaxySalesApp.ManCard;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.cds_jo.GalaxySalesApp.NewHomePage.NewHomePage;
import com.cds_jo.GalaxySalesApp.Pos.Pos_Activity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.StartUpActivity;

public class SplashLoginAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        try{
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                ActivityCompat.requestPermissions(SplashLoginAct.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_ADMIN},
                        1);}

        }catch ( Exception ex){}
        ImageView imageView=(ImageView)findViewById(R.id.imageView) ;
        Animation animation = AnimationUtils.loadAnimation(SplashLoginAct.this, R.animator.moveimg);
        imageView.startAnimation(animation);
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
            //  startActivity(new Intent(SplashLoginAct.this, StartUpActivity.class));
             //   startActivity(new Intent(SplashLoginAct.this, NewHomePage.class));
                startActivity(new Intent(SplashLoginAct.this, Pos_Activity.class));
                finish();
            }
        }, secondsDelayed * 3000);

    }
}
