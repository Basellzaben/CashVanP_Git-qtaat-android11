package com.cds_jo.GalaxySalesApp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class GalaxyMainActivity extends FragmentActivity {
    private Context context;
    private LinearLayout RR1,RR2,RR3,R_Content;
    private RelativeLayout R_Back;
    private FloatingActionMenu actionMenu1,actionMenu2;
    private ImageView Img_Menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxy_main);
        Initi();
        Cir1();
        Cir2();
    }
//mohammad12345
private void Initi1() {}
    private void Initi() {
        context=GalaxyMainActivity.this;
        RR1= (LinearLayout) findViewById(R.id.RR1);
        RR2= (LinearLayout) findViewById(R.id.RR2);
        RR3= (LinearLayout) findViewById(R.id.RR3);
        R_Content= (LinearLayout) findViewById(R.id.R_Content);
        R_Back= (RelativeLayout) findViewById(R.id.R_Back);
        Img_Menu=(ImageView)findViewById(R.id.imageView6);
    }

    /////////////////////////////////////////////////////////////////////////////////////
    private void Cir1() {
        RelativeLayout.LayoutParams params;
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.cir_green));

        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageResource(R.mipmap.aa1 );
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.mipmap.aa2 );
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.mipmap.aa3 );
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();


        ImageView itemIcon4 = new ImageView(this);
        itemIcon4.setImageResource(R.mipmap.aa4 );
        SubActionButton button4 = itemBuilder.setContentView(itemIcon4).build();


        params = new RelativeLayout.LayoutParams(250, 250);
        button4.setLayoutParams(params);
        button3.setLayoutParams(params);
        button2.setLayoutParams(params);
        button1.setLayoutParams(params);

        actionMenu1 = new FloatingActionMenu.Builder(this)
                .setStartAngle(0) // A whole circle!
                .setEndAngle(180)
                .setRadius(300)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(RR1)

                .build();


    }

    /////////////////////////////////////////////////////////////////////////////////
    private void Cir2() {
        RelativeLayout.LayoutParams params;
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.cir_blue));

        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageResource(R.mipmap.bb1 );
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.mipmap.bb2 );
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.mipmap.bb3 );
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();


        ImageView itemIcon4 = new ImageView(this);
        itemIcon4.setImageResource(R.mipmap.bb4 );
        SubActionButton button4 = itemBuilder.setContentView(itemIcon4).build();


        params = new RelativeLayout.LayoutParams(250, 250);
        button4.setLayoutParams(params);
        button3.setLayoutParams(params);
        button2.setLayoutParams(params);
        button1.setLayoutParams(params);

        actionMenu2 = new FloatingActionMenu.Builder(this)
                .setStartAngle(180) // A whole circle!
                .setEndAngle(0)
                .setRadius(300)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(RR2)

                .build();

    }

    public void btn_PopNotes(View view) {

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////


}
