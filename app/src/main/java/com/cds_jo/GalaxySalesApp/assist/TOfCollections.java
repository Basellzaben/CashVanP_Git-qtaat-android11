package com.cds_jo.GalaxySalesApp.assist;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.Select_Customer;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import header.Header_Frag;

public class TOfCollections extends AppCompatActivity {
    EditText FromDate;
    EditText ToDate;
    private int year, month, day;
    private Calendar c;
    EditText acc;
    SqlHandler sql_Handler;

    public ProgressDialog loadingdialog;
    SharedPreferences sharedPreferences;
    ArrayList<cls_TableOfCollection> TList;
    TableOfCollectoinAdapter adapter;
    ListView lv;
    final Handler _handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tof_collections);
        FromDate= (EditText) findViewById(R.id.FromDate1);
        ToDate= (EditText) findViewById(R.id.ToDate1);
        Fragment frag = new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();

        lv=(ListView)findViewById(R.id.k);
        //mo();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TOfCollections.this);
        acc = (EditText) findViewById(R.id.tv_acc);

        String a=GetDay();
        FromDate.setText(a);
        ToDate.setText(a);

    }

    private String GetDay() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat mdformatyear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String stryear = mdformatyear.format(calendar.getTime());

        SimpleDateFormat mdformatmonth = new SimpleDateFormat("MM", Locale.ENGLISH);
        String strmonth = mdformatmonth.format(calendar.getTime());

        SimpleDateFormat mdformatday = new SimpleDateFormat("dd", Locale.ENGLISH);
        String strday = mdformatday.format(calendar.getTime());
        return strday+"/"+strmonth+"/"+stryear;
    }

    public void btn_SearchCust(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "RecVoch");
        FragmentManager Manager = getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);

    }
    public void Set_Cust(final String No, String Nm) {

        acc.setText(No);
        acc.setError(null);

    }
    public void btn_GetData (View view)
    {
        if (acc.getText().toString().equals("") &&FromDate.getText().toString().equals("") && ToDate.getText().toString().equals(""))
        { if(acc.getText().toString().equals(""))
        {
            new SweetAlertDialog(TOfCollections.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    //.setCustomImage(R.mipmap.icon_delete)
                    .setContentText("الرجاء ادخال العميل")
                    .setConfirmText("رجــــوع")
                    .show();
            acc.setError("required!");
            acc.requestFocus();
            return;
        }
        else if(FromDate.getText().toString().equals(""))
        {
            new SweetAlertDialog(TOfCollections.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    //.setCustomImage(R.mipmap.icon_delete)
                    .setContentText("الرجاء تاريخ البداية")
                    .setConfirmText("رجــــوع")
                    .show();
            FromDate.setError("required!");
            FromDate.requestFocus();
            return;
        }
        else if(ToDate.getText().toString().equals(""))
        {
            new SweetAlertDialog(TOfCollections.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    //.setCustomImage(R.mipmap.icon_delete)
                    .setContentText("الرجاء تاريخ النهاية")
                    .setConfirmText("رجــــوع")
                    .show();
            ToDate.setError("required!");
            ToDate.requestFocus();
            return;
        }
        }
        else{
            final String acc1 = acc.getText().toString();

            getData(acc1);


        }

    }
    public void bt_back(View v)
    {
        Intent i=new Intent(TOfCollections.this, JalMasterActivity.class);
        startActivity(i);
        finish();
    }

    public void mo() {


        sql_Handler = new SqlHandler(TOfCollections.this);
        TList = new ArrayList<>();
        String query = "Select * from InvoicePaymentSchedule ";
        Integer i = 1;
        Cursor c1 = sql_Handler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    try{
                        String day=GetDay();
                        String d=c1.getString(c1
                                .getColumnIndex("Tr_date"));

                        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date d1 = sdf.parse(d);
                        Date day2 = sdf.parse(day);
                        if(sdf.format(d1).equals(sdf.format( day2)))
                        {
                            cls_TableOfCollection a=new cls_TableOfCollection();
                            a.setNameCust(c1.getString(c1.getColumnIndex("custName")));
                            a.setTr_date(c1.getString(c1.getColumnIndex("Tr_date")));
                            a.setNewTr_date(c1.getString(c1.getColumnIndex("New_Tr_date")));
                            a.setCustNo(c1.getString(c1.getColumnIndex("custNo")));
                            a.setOrderNo(c1.getString(c1.getColumnIndex("orderNo")));
                            a.setAmt(c1.getString(c1.getColumnIndex("Amt")));
                            a.setNewAmt(c1.getString(c1.getColumnIndex("New_Amt")));
                            a.setOrderDate(c1.getString(c1.getColumnIndex("orderDate")));
                            a.setInoviceAmt(c1.getString(c1.getColumnIndex("InoviceAmt")));
                            a.setNotes(c1.getString(c1.getColumnIndex("Notes")));
                            a.setSupervisorNutes(c1.getString(c1.getColumnIndex("SupervisorNutes")));


                            TList.add(a);
                            adapter = new TableOfCollectoinAdapter(TOfCollections.this, TList);
                            lv.setAdapter(adapter);
                        }else
                        {
                            getData("-1");
                        }



                    }catch(ParseException ex){
                        // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
                    }


                    i = i + 1;
                } while (c1.moveToNext());


            }
            c1.close();
        }
        getData("-1");


    }
    String q = "";
    SqlHandler sqlHandler;
    public void getData(String acc2)
    {
        sqlHandler = new SqlHandler(this);
        q = "Delete from InvoicePaymentSchedule";
        sqlHandler.executeQuery(q);
        lv.setAdapter(null);

        loadingdialog = ProgressDialog.show(TOfCollections.this, "الرجاء الانتظار ...", "العمل جاري على ترحيل الدفعات", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
//    final String UserID = sharedPreferences.getString("UserID", "");
        final String FD = FromDate.getText().toString();
        final String TD = ToDate.getText().toString();
        final String acc1 = acc2;//acc.getText().toString();


        TList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(TOfCollections.this);
                ws.GatTableCollections("1", FD, TD, acc1);
                try {
                    loadingdialog.dismiss();
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray NameCust = js.getJSONArray("NameCust");
                    JSONArray Tr_date = js.getJSONArray("Tr_date");
                    JSONArray new_Tr_date = js.getJSONArray("NewTr_date");
                    JSONArray custNo1 = js.getJSONArray("custNo1");
                    JSONArray orderNo = js.getJSONArray("orderNo");
                    JSONArray Amt = js.getJSONArray("Amt");
                    JSONArray InoviceAmt = js.getJSONArray("InoviceAmt");
                    JSONArray Notes = js.getJSONArray("Notes");
                    JSONArray SupervisorNutes = js.getJSONArray("SupervisorNutes");
                    JSONArray newAmt = js.getJSONArray("NewAmt");
                    JSONArray orderDate = js.getJSONArray("Order_date");

                    q = "Delete from InvoicePaymentSchedule";
                    sqlHandler.executeQuery(q);
                    q = " delete from sqlite_sequence where name='InvoicePaymentSchedule'";
                    sqlHandler.executeQuery(q);

                    for (int i = 0; i < NameCust.length(); i++) {
                        q = "INSERT INTO InvoicePaymentSchedule(custName,custNo,New_Amt ,orderNo ,Amt  ,InoviceAmt ,Notes,SupervisorNutes,Tr_date,New_Tr_date,orderDate ) values ('"
                                +  NameCust.get(i).toString()
                                + "','" + custNo1.get(i).toString()
                                + "','" + newAmt.get(i).toString()
                                + "','" + orderNo.get(i).toString()
                                + "','" + Amt.get(i).toString()
                                + "','" + InoviceAmt.get(i).toString()
                                + "','" + Notes.get(i).toString()
                                + "','" + SupervisorNutes.get(i).toString()
                                + "','" + Tr_date.get(i).toString()
                                + "','" + new_Tr_date.get(i).toString()
                                + "','" + orderDate.get(i).toString()
                                + "')";
                        sqlHandler.executeQuery(q);
                        TList.add(new cls_TableOfCollection(NameCust.get(i).toString(), Tr_date.get(i).toString(), new_Tr_date.get(i).toString(), custNo1.get(i).toString(), orderNo.get(i).toString(), Amt.get(i).toString(),newAmt.get(i).toString(),orderDate.get(i).toString(), InoviceAmt.get(i).toString(), Notes.get(i).toString(), SupervisorNutes.get(i).toString()));
                    }
                    _handler.post(new Runnable() {
                        public void run() {
                            adapter = new TableOfCollectoinAdapter(TOfCollections.this, TList);
                            lv.setAdapter(adapter);

                        }
                    });


                    //  mo(TList);
                } catch (Exception e) {
                    loadingdialog.dismiss();

                }


            }

        }).start();
    }
}