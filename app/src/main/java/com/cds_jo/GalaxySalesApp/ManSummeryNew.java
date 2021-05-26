package com.cds_jo.GalaxySalesApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;


import com.cds_jo.GalaxySalesApp.XprinterDoc.Xprinter_ManSummeryTo_img;
import com.cds_jo.GalaxySalesApp.XprinterDoc.Xprinter_RecVoucher;
import com.cds_jo.GalaxySalesApp.XprinterDoc.Xprinter_SalesInvoice;
import com.cds_jo.GalaxySalesApp.assist.Convert_ManSummery_To_Img;
import com.cds_jo.GalaxySalesApp.assist.Convert_RecVouch_To_Img;
import com.cds_jo.GalaxySalesApp.assist.ManAttenActivity;


import Methdes.MyTextView;
import header.Header_Frag;

public class ManSummeryNew extends FragmentActivity {
    private Context context = ManSummeryNew.this;
    private MyTextView T1, T2, T3, T4, T5, T6;
    private Fragment frag;
    private Xprinter_ManSummeryTo_img flag1;
    private android.support.v4.app.FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_summery_new);
        Initi();
        Click();

        frag = new Header_Frag();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();


    }

    private void Click() {
        T1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T1.setBackgroundColor(getResources().getColor(R.color.Blue));
                T1.setTextColor(Color.WHITE);

                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                T6.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T6.setTextColor(getResources().getColor(R.color.Blue));


                frag = new Tab_SalesSummery();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }
        });

        //////////////////////////////////////////////////////////////

        T2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));

                T2.setBackgroundColor(getResources().getColor(R.color.Blue));
                T2.setTextColor(Color.WHITE);


                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                T6.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T6.setTextColor(getResources().getColor(R.color.Blue));

                frag = new Tab_Payments();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }
        });

        ////////////////////////////////////////////////////////////////////

        T3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));

                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackgroundColor(getResources().getColor(R.color.Blue));
                T3.setTextColor(Color.WHITE);

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                T6.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T6.setTextColor(getResources().getColor(R.color.Blue));

                frag = new Tab_SalesOrders();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }
        });

        //////////////////////////////////////////////////////////////

        T4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));

                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackgroundColor(getResources().getColor(R.color.Blue));
                T4.setTextColor(Color.WHITE);

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                T6.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T6.setTextColor(getResources().getColor(R.color.Blue));

                frag = new Tab_UsedCode();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }
        });

        ////////////////////////////////////////////////////////////////////

        T5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));

                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackgroundColor(getResources().getColor(R.color.Blue));
                T5.setTextColor(Color.WHITE);

                T6.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T6.setTextColor(getResources().getColor(R.color.Blue));

                frag = new Tab_UnpostedTransaction();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }
        });

        ////////////////////////////////////////////////////////////////////

        T6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));

                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));


                T6.setBackgroundColor(getResources().getColor(R.color.Blue));
                T6.setTextColor(Color.WHITE);

                Intent k = new Intent(ManSummeryNew.this, Xprinter_ManSummeryTo_img.class);
                startActivity(k);


/*            if (ComInfo.ComNo == Companies.Ukrania.getValue()) {

                }else{
                frag = new Convert_ManSummery_To_Img();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }*/
            //  }

        }

        });

    }

    private void Initi() {
        T1 = (MyTextView) findViewById(R.id.T1);
        T2 = (MyTextView) findViewById(R.id.T2);
        T3 = (MyTextView) findViewById(R.id.T3);
        T4 = (MyTextView) findViewById(R.id.T4);
        T5 = (MyTextView) findViewById(R.id.T5);
        T6 = (MyTextView) findViewById(R.id.T6);

    }

    public void btn_print() {
        if (ComInfo.ComNo == Companies.Afrah.getValue()) {
            Intent k = new Intent(this, Xprinter_ManSummeryTo_img.class);
            startActivity(k);
        }
        else {
          //  Intent k = new Intent(this, Convert_ManSummery_To_Img.class);
            Intent k = new Intent(this, Xprinter_ManSummeryTo_img.class);
            startActivity(k);
        }
    }
}
