package com.cds_jo.GalaxySalesApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cds_jo.GalaxySalesApp.AbuHaltam.LoginHalitamNew;

public class StartUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
      startActivity(new Intent(this,NewLoginActivity.class));
          // startActivity(new Intent(this, LoginHalitamNew.class));

    }
}
