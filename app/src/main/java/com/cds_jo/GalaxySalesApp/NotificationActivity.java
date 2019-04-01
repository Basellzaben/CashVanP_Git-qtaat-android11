package com.cds_jo.GalaxySalesApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }
     public void btn_show_Notification (View view){
        startService(new Intent(getBaseContext(),GetManMessage.class));
    }

    public void btn_close_Notification (View view){
        stopService(new Intent(getBaseContext(),GetManMessage.class));
    }

}
