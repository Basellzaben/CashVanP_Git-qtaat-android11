package com.cds_jo.GalaxySalesApp.assist;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SmanChart.Cls_Monthly_Items_Amount;


import com.cds_jo.GalaxySalesApp.UpdateDataToMobileActivity;
import com.cds_jo.GalaxySalesApp.We_Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import header.Header_Frag;

public class Monthly_Items_AmountAct extends AppCompatActivity{
    TextView tv;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    private android.support.v4.app.FragmentManager fragmentManager;
    private SectionsPageAdapter mSectionsPageAdapter;
    SharedPreferences sharedPreferences;
    private ViewPager mViewPager;
    Cls_Monthly_Items_Amount obj;

      Spinner VouchType;
    private TextView mDisplayFDate,mDisplayTDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener1;

  //  @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp__chart);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Monthly_Items_AmountAct.this);
        getDataSer.ChartDate=new ArrayList<>();
        mDisplayFDate = (TextView) findViewById(R.id.fdate);
        mDisplayTDate = (TextView) findViewById(R.id.tdate);



        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();
        VouchType = (Spinner) findViewById(R.id.sp_VouchType);
        FillMonth();

        Calendar cal = Calendar.getInstance();
       // defShow(    cal.get(Calendar.MONTH) + 1 );
        VouchType.setSelection(cal.get(Calendar.MONTH)   );
        VouchType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Setdate(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


    }
    private  void  Setdate(int i){

        defShow(i+1);
    }
    private void FillMonth() {




        ArrayList<Cls_Cur> VouchTypeList = new ArrayList<Cls_Cur>();
        VouchTypeList.clear();

        Cls_Cur cls_cur = new Cls_Cur();
        cls_cur.setName("01");
        cls_cur.setNo("01");
        VouchTypeList.add(cls_cur);


        cls_cur = new Cls_Cur();
        cls_cur.setName("02");
        cls_cur.setNo("02");
        VouchTypeList.add(cls_cur);


        cls_cur = new Cls_Cur();
        cls_cur.setName("03");
        cls_cur.setNo("03");
        VouchTypeList.add(cls_cur);


        cls_cur = new Cls_Cur();
        cls_cur.setName("04");
        cls_cur.setNo("04");
        VouchTypeList.add(cls_cur);


        cls_cur = new Cls_Cur();
        cls_cur.setName("05");
        cls_cur.setNo("05");
        VouchTypeList.add(cls_cur);


        cls_cur = new Cls_Cur();
        cls_cur.setName("06");
        cls_cur.setNo("06");
        VouchTypeList.add(cls_cur);


        cls_cur = new Cls_Cur();
        cls_cur.setName("07");
        cls_cur.setNo("07");
        VouchTypeList.add(cls_cur);


        cls_cur = new Cls_Cur();
        cls_cur.setName("08");
        cls_cur.setNo("08");
        VouchTypeList.add(cls_cur);

        cls_cur = new Cls_Cur();
        cls_cur.setName("09");
        cls_cur.setNo("09");
        VouchTypeList.add(cls_cur);

        cls_cur = new Cls_Cur();
        cls_cur.setName("10");
        cls_cur.setNo("10");
        VouchTypeList.add(cls_cur);

        cls_cur = new Cls_Cur();
        cls_cur.setName("11");
        cls_cur.setNo("11");
        VouchTypeList.add(cls_cur);

        cls_cur = new Cls_Cur();
        cls_cur.setName("12");
        cls_cur.setNo("13");
        VouchTypeList.add(cls_cur);
        Cls_Month_Adapter cls_cur_adapter = new Cls_Month_Adapter(
                this, VouchTypeList);
        VouchType.setAdapter(cls_cur_adapter);


    }
    private void defShow( int currentMonth) {

        Calendar cal = Calendar.getInstance();

        int res = cal.getActualMaximum(Calendar.DATE);


        int currentYear = cal.get(Calendar.YEAR);
        String fDate= "";
        String tDate="";
        if(currentMonth==1){
              tDate=currentYear+"/01/31";
              fDate= currentYear+"/01/01";
        }else if   (currentMonth==2){
            tDate=currentYear+"/02/29";
            fDate= currentYear+"/02/01";
        }else if   (currentMonth==3){
            tDate=currentYear+"/03/31";
            fDate= currentYear+"/03/01";
        }else if   (currentMonth==4){
            tDate=currentYear+"/04/30";
            fDate= currentYear+"/04/01";
        }else if   (currentMonth==5){
            tDate=currentYear+"/05/31";
            fDate= currentYear+"/05/01";
        }else if   (currentMonth==6){
            tDate=currentYear+"/06/30";
            fDate= currentYear+"/06/01";
        }else if   (currentMonth==7){
            tDate=currentYear+"/07/31";
            fDate= currentYear+"/07/01";
        }else if   (currentMonth==8){
            tDate=currentYear+"/08/31";
            fDate= currentYear+"/08/01";
        }else if   (currentMonth==9){
            tDate=currentYear+"/09/30";
            fDate= currentYear+"/09/01";
        }else if   (currentMonth==10){
            tDate=currentYear+"/10/31";
            fDate= currentYear+"/10/01";
        }else if   (currentMonth==12){
            tDate=currentYear+"/11/30";
            fDate= currentYear+"/11/01";
        }else if   (currentMonth==13){
            tDate=currentYear+"/12/31";
            fDate= currentYear+"/12/01";
        }



        TextView f_date = (TextView)findViewById(R.id.fdate);
        TextView t_date = (TextView)findViewById(R.id.tdate);



        f_date.setText(fDate);
        t_date.setText(tDate+"");



            /*mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());


            mViewPager = (ViewPager) findViewById(R.id.container);
            setupViewPager(mViewPager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);*/
        getdata_servar(fDate,tDate);

    }



    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new AmountListFragment(), "المبيعات-مبلغ");
        adapter.addFragment(new QuantitieslistFragment(), "المبيعات-كميات");
        adapter.addFragment(new AmountFragment(), "المبيعات-مبلغ-بياني");
        adapter.addFragment(new QuantitiesFragment(), "المبيعات-كمية-بياني");
        adapter.addFragment(new PaymentsChartFragment(), "التحصيلات");



        viewPager.setAdapter(adapter);
    }
    private void getdata_servar(final String Fdate, final String Tdate)
    {
        getDataSer.ChartDate=new ArrayList<>();
        final Handler _handler = new Handler();
        final String man = sharedPreferences.getString("UserID", "");
        tv = new TextView(getApplicationContext());
        lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setLayoutParams(lp);
        tv.setPadding(10, 15, 10, 15);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.BLUE);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(Monthly_Items_AmountAct.this);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("الهدف الشهري");
        progressDialog.setCustomTitle(tv);
        progressDialog.setProgressDrawable(greenProgressbar);
        progressDialog.show();
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(Monthly_Items_AmountAct.this);
                ws.Get_ManQuotaReport1(man,Fdate,Tdate,"-1");
                try {
                    Integer i;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Type_Name = js.getJSONArray("Type_Name");
                    JSONArray js_Quantity = js.getJSONArray("Quantity");
                    JSONArray js_Price = js.getJSONArray("Price");
                    JSONArray js_SalesQty = js.getJSONArray("SalesQty");
                    JSONArray js_RetQty = js.getJSONArray("RetQty");
                    JSONArray js_SalesAmt = js.getJSONArray("SalesAmt");
                    JSONArray js_RetAmt = js.getJSONArray("RetAmt");
                    JSONArray js_AmtPrecent = js.getJSONArray("AmtPrecent");
                    JSONArray js_QtyPrecent = js.getJSONArray("QtyPrecent");
                    JSONArray js_PaymnetAmt = js.getJSONArray("PaymnetAmt");
                    JSONArray js_CollectionsAmount = js.getJSONArray("CollectionsAmount");

                    for (i = 0; i < js_Type_Name.length(); i++) {
                        obj = new Cls_Monthly_Items_Amount(js_Type_Name.getString(i), js_Quantity.getString(i), js_Price.getString(i), js_SalesQty.getString(i), js_RetQty.getString(i), js_SalesAmt.getString(i), js_RetAmt.getString(i), js_AmtPrecent.getString(i), js_QtyPrecent.getString(i)
                            ,js_CollectionsAmount.getString(i),js_PaymnetAmt.getString(i));
                        getDataSer.ChartDate.add(obj);
                    }
                    progressDialog.setMax(js_Type_Name.length());
                    progressDialog.incrementProgressBy(1);
                    if (progressDialog.getProgress() == progressDialog.getMax()) {
                        progressDialog.dismiss();
                    }
                    _handler.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            ShowChart();
                        }
                    });
                } catch (final Exception e) {
                    progressDialog.dismiss();
                }
            }
        };
        thread.start();
    }
    @Override
    public void onBackPressed() {
        getDataSer.ChartDate=new ArrayList<>();
        Intent k = new Intent(this, JalMasterActivity.class);
        startActivity(k);

    }
    public void btn_back(View view)
    {
        getDataSer.ChartDate=new ArrayList<>();

        Intent k = new Intent(this, JalMasterActivity.class);
        startActivity(k);

    }
    public void show_chart(View view) {
        String fDate = mDisplayFDate.getText().toString();
        String tDate = mDisplayTDate.getText().toString();
        getdata_servar(fDate, tDate);
        ShowChart();
    }
    private  void ShowChart(){
                 mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
                  mViewPager = (ViewPager) findViewById(R.id.container);
                setupViewPager(mViewPager);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);
        }



}