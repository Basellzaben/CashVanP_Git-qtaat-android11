package com.cds_jo.GalaxySalesApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class QuestneerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questneer);
    }

    public void btn_back(View view) {
        Intent k= new Intent(this, JalMasterActivity.class);
        startActivity(k);

    }
}
