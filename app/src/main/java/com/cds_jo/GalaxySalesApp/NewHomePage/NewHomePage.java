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


import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.Companies;
import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.GlobaleVar;
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
            ComInfo.ComNo = Integer.parseInt(DB.GetValue(this, "ComanyInfo", "CompanyID", "1=1"));
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


          if(ComInfo.ComNo== Companies.Sector.getValue()){
                    lstHomeShow = new ArrayList<>();
                    // lstHomeShow.add(new Cls_Home_Show_Ditial("فاتورة المبيعات", R.drawable.invoice, 3,"assist.Sale_InvoiceActivity"));
                    lstHomeShow.add(new Cls_Home_Show_Ditial("فاتورة المبيعات", R.drawable.invoice, 3,"Pos.Pos_Activity"));
                    lstHomeShow.add(new Cls_Home_Show_Ditial("سند القبض", R.drawable.money, 5,"RecvVoucherActivity"));
                    lstHomeShow.add(new Cls_Home_Show_Ditial("استلام المواد", R.drawable.enter_qty, 5,"warehouse.ItemsRecepit"));
                    //lstHomeShow.add(new Cls_Home_Show_Ditial("سند الإدخال", R.drawable.enter_qty, 3,"warehouse.EnterQtyActivity"));
                    lstHomeShow.add(new Cls_Home_Show_Ditial("استعلام عن المواد", R.drawable.item_search, 4,"InquireItem.InquireItemACT"));
                    lstHomeShow.add(new Cls_Home_Show_Ditial("تحديث البيانات", R.drawable.transfer, 7,"UpdateDataToMobileActivity"));
                    lstHomeShow.add(new Cls_Home_Show_Ditial("كميات المستودع", R.drawable.item_search, 7,"TransQtyReportActivity"));
                    //  lstHomeShow.add(new Cls_Home_Show_Ditial("طباعة", R.drawable.log_out, 1,"TspPrinter.SampleReciptActivity"));
                    lstHomeShow.add(new Cls_Home_Show_Ditial("إعدادات", R.drawable.setting, 1,"SittingNew"));
                    lstHomeShow.add(new Cls_Home_Show_Ditial("خروج", R.drawable.log_out, 2,"NewLoginActivity"));
            } else if(ComInfo.ComNo== Companies.abohaltam.getValue())
          {
              if(sharedPreferences.getString("type", "").equals("1"))
              {
                  lstHomeShow = new ArrayList<>();
                  // lstHomeShow.add(new Cls_Home_Show_Ditial("فاتورة المبيعات", R.drawable.invoice, 3,"assist.Sale_InvoiceActivity"));
                  lstHomeShow.add(new Cls_Home_Show_Ditial("خط الانتاج", R.drawable.invoice, 3,"AbuHaltam.ProductionlineAct"));
                  lstHomeShow.add(new Cls_Home_Show_Ditial("سند اخراج", R.drawable.money, 5,"Delivery_VoucherAct"));
                  //   lstHomeShow.add(new Cls_Home_Show_Ditial("استلام المواد", R.drawable.enter_qty, 5,"warehouse.ItemsRecepit"));
                  //lstHomeShow.add(new Cls_Home_Show_Ditial("سند الإدخال", R.drawable.enter_qty, 3,"warehouse.EnterQtyActivity"));
                  //    lstHomeShow.add(new Cls_Home_Show_Ditial("استعلام عن المواد", R.drawable.item_search, 4,"InquireItem.InquireItemACT"));
                  lstHomeShow.add(new Cls_Home_Show_Ditial("تحديث البيانات", R.drawable.transfer, 7,"UpdateDataToMobileActivity"));
                  //      lstHomeShow.add(new Cls_Home_Show_Ditial("كميات المستودع", R.drawable.item_search, 7,"TransQtyReportActivity"));
                  //  lstHomeShow.add(new Cls_Home_Show_Ditial("طباعة", R.drawable.log_out, 1,"TspPrinter.SampleReciptActivity"));
                  lstHomeShow.add(new Cls_Home_Show_Ditial("إعدادات", R.drawable.setting, 1,"SittingNew"));
                  lstHomeShow.add(new Cls_Home_Show_Ditial("خروج", R.drawable.log_out, 2,"AbuHaltam.LoginHalitamNew"));
              }
              else
              {
                  lstHomeShow = new ArrayList<>();
             //     lstHomeShow.add(new Cls_Home_Show_Ditial("الجولات", R.drawable.startvisit, 1,"MainActivity"));
                  // lstHomeShow.add(new Cls_Home_Show_Ditial("فاتورة المبيعات", R.drawable.invoice, 3,"assist.Sale_InvoiceActivity"));
                  lstHomeShow.add(new Cls_Home_Show_Ditial("فاتورة المبيعات", R.drawable.invoice, 3,"Pos.Pos_Activity"));
                  lstHomeShow.add(new Cls_Home_Show_Ditial("سند طلب مواد", R.drawable.money, 5,"PreapareManQty"));
                  //   lstHomeShow.add(new Cls_Home_Show_Ditial("استلام المواد", R.drawable.enter_qty, 5,"warehouse.ItemsRecepit"));
                  //lstHomeShow.add(new Cls_Home_Show_Ditial("سند الإدخال", R.drawable.enter_qty, 3,"warehouse.EnterQtyActivity"));
                  //    lstHomeShow.add(new Cls_Home_Show_Ditial("استعلام عن المواد", R.drawable.item_search, 4,"InquireItem.InquireItemACT"));
                  lstHomeShow.add(new Cls_Home_Show_Ditial("تحديث البيانات", R.drawable.transfer, 7,"UpdateDataToMobileActivity"));
                  //      lstHomeShow.add(new Cls_Home_Show_Ditial("كميات المستودع", R.drawable.item_search, 7,"TransQtyReportActivity"));
                  //  lstHomeShow.add(new Cls_Home_Show_Ditial("طباعة", R.drawable.log_out, 1,"TspPrinter.SampleReciptActivity"));
                  lstHomeShow.add(new Cls_Home_Show_Ditial("إعدادات", R.drawable.setting, 1,"SittingNew"));
                  lstHomeShow.add(new Cls_Home_Show_Ditial("خروج", R.drawable.log_out, 2,"AbuHaltam.LoginHalitamNew"));
              }

          }
          else {
                lstHomeShow = new ArrayList<>();
                lstHomeShow.add(new Cls_Home_Show_Ditial("نقاط البيع", R.drawable.invoice, 3,"Pos.Pos_Activity"));
                lstHomeShow.add(new Cls_Home_Show_Ditial("الجولات", R.drawable.startvisit, 1,"MainActivity"));
                lstHomeShow.add(new Cls_Home_Show_Ditial("كشف الحساب", R.drawable.account_statment, 2,"assist.Acc_ReportActivity"));
                lstHomeShow.add(new Cls_Home_Show_Ditial("فاتورة المبيعات", R.drawable.invoice, 3,"assist.Sale_InvoiceActivity"));
                lstHomeShow.add(new Cls_Home_Show_Ditial("طلب البيع", R.drawable.salesorder, 4,"assist.OrdersItems"));
                lstHomeShow.add(new Cls_Home_Show_Ditial("سند القبض", R.drawable.money, 5,"RecvVoucherActivity"));
                lstHomeShow.add(new Cls_Home_Show_Ditial("المرتجعات", R.drawable.returns, 6,"Delivery_VoucherAct"));
                lstHomeShow.add(new Cls_Home_Show_Ditial("تحديث البيانات", R.drawable.transfer, 7,"UpdateDataToMobileActivity"));
                lstHomeShow.add(new Cls_Home_Show_Ditial("الإعدادات العامة", R.drawable.setting, 7,"SittingNew"));
                lstHomeShow.add(new Cls_Home_Show_Ditial("حفظ موقع العميل", R.drawable.gps_icon, 1,"CustLocations.CustomerLocation"));
                lstHomeShow.add(new Cls_Home_Show_Ditial("صور الجولات", R.drawable.image_man, 1,"VisitImges"));
                lstHomeShow.add(new Cls_Home_Show_Ditial("الملاحظات", R.drawable.notes_man, 1,"CustomerNotes"));
              lstHomeShow.add(new Cls_Home_Show_Ditial("خطة الجولات", R.drawable.gps_icon, 1,"ScheduleManActivity"));

              lstHomeShow.add(new Cls_Home_Show_Ditial("خروج", R.drawable.log_out, 1,"NewLoginActivity"));
            }


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
