package com.cds_jo.GalaxySalesApp.NewHomePage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.cds_jo.GalaxySalesApp.NewLoginActivity;
import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;
import java.util.List;

import header.Header_Frag;


public class NewHomePage extends FragmentActivity {
    List<Cls_Home_Show_Ditial> lstHomeShow ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxy_main2);
        try {
            Fragment frag = new Header_Frag();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();


            lstHomeShow = new ArrayList<>();
            lstHomeShow.add(new Cls_Home_Show_Ditial("الجولات",R.drawable.startvisit,1));
            lstHomeShow.add(new Cls_Home_Show_Ditial("كشف الحساب",R.drawable.account_statment,2));
            lstHomeShow.add(new Cls_Home_Show_Ditial("فاتورة المبيعات",R.drawable.invoice,3));
            lstHomeShow.add(new Cls_Home_Show_Ditial("طلب البيع",R.drawable.salesorder,4));
            lstHomeShow.add(new Cls_Home_Show_Ditial("سند القبض",R.drawable.money,5));
            lstHomeShow.add(new Cls_Home_Show_Ditial("المرتجعات",R.drawable.returns,6));
            lstHomeShow.add(new Cls_Home_Show_Ditial("تحديث البيانات",R.drawable.transfer,7));
            lstHomeShow.add(new Cls_Home_Show_Ditial("الإعدادات العامة",R.drawable.setting,7));
            lstHomeShow.add(new Cls_Home_Show_Ditial("موقع العميل",R.drawable.gps_icon,1));
            lstHomeShow.add(new Cls_Home_Show_Ditial("صور الجولات",R.drawable.image_man,1));
            lstHomeShow.add(new Cls_Home_Show_Ditial("الملاحظات",R.drawable.notes_man,1));
            lstHomeShow.add(new Cls_Home_Show_Ditial("خروج",R.drawable.log_out,1));



            RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
            Cls_Home_Show_Ditial_Adapter myAdapter = new Cls_Home_Show_Ditial_Adapter(this,lstHomeShow);
            myrv.setLayoutManager(new GridLayoutManager(this,6));
            myrv.setAdapter(myAdapter);
        }
        catch ( Exception ex){
            Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String Login = sharedPreferences.getString("Login", "No");
        if (Login.toString().equals("No")) {
            Intent i = new Intent(this, NewLoginActivity.class);
            startActivity(i);
        }
        return;
    }

}
