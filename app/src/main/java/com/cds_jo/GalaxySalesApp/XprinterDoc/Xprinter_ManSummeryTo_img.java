package com.cds_jo.GalaxySalesApp.XprinterDoc;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.Cls_CustWithoutPayment_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_CustomerWithoutPayment;
import com.cds_jo.GalaxySalesApp.assist.ESCPSample3;
import com.cds_jo.GalaxySalesApp.assist.OrdersItems;
import com.cds_jo.GalaxySalesApp.assist.Sale_ReturnActivity;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.BitmapCallback;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterPos80;
import net.posprinter.utils.PosPrinterDev;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Methdes.MyTextView;

public class Xprinter_ManSummeryTo_img extends FragmentActivity {
    public static String DISCONNECT="com.posconsend.net.disconnetct";
    public static IMyBinder binder;
    private Bitmap b1;
    private Bitmap b2;
    SqlHandler sqlHandler;
    ListView lvCustomList;
    private Button mButton;
    private View mView;
    private Button mButton2;
    private ESCPOSPrinter posPtr;
    ESCPSample3 obj_print=new ESCPSample3();
    ImageView img_Logo,imgSig;
    private Context context;
    View ReportView;
    String currentDateandTime;
    String q;
    ServiceConnection conn= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //Bind successfully
            binder= (IMyBinder) iBinder;
            Log.e("binder","connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("disbinder","disconnected");
        }
    };

    public static boolean ISCONNECT;
    Button BTCon ;
    AlertDialog dialog;
    String mac;
    int pos ;
    String   BPrinter_MAC_ID;
    Receiver netReciever;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xprinter_man_summery);
        Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
        //bind service，get ImyBinder object
        Intent intent=new Intent(this,PosprinterService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");

        netReciever=new Receiver();
        registerReceiver(netReciever,new IntentFilter(Xprinter_ManSummeryTo_img.DISCONNECT));
        Tiny.getInstance().init(getApplication());


        BTCon= (Button) findViewById(R.id.btn_Print);

        Button   back= (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(Xprinter_ManSummeryTo_img.this, JalMasterActivity.class);
                startActivity(k);
            }
        });

        BTCon.setBackgroundColor(getResources().getColor(R.color.Blue));
        BTCon.setTextColor(Color.WHITE);
        BTCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connetBle();
            }
        });
        Intent i = getIntent();
        File imgFile = new  File("//sdcard/Android/Cv_Images/logo.jpg");
        img_Logo = (ImageView) findViewById(R.id.img_Logo);





        String u = sharedPreferences.getString("UserName", "");
        MyTextView tv_UserNm = (MyTextView)  findViewById(R.id.tv_UserNm);
        tv_UserNm.setText(u);
        String UserID = sharedPreferences.getString("UserID", "");
        MyTextView ed_date = (MyTextView)  findViewById(R.id.ed_date);

        MyTextView tv_CompName = (MyTextView)  findViewById(R.id.tv_CompName);
        tv_CompName.setText(sharedPreferences.getString("CompanyNm", ""));


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        currentDateandTime =preferences.getString("spinnerdateselected", "");


        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        currentDateandTime = sdf.format(new Date());
       */
        ed_date.setText(" التاريـخ  : " + currentDateandTime);


        TextView tv_SalesCount = (TextView)  findViewById(R.id.tv_SalesCount);
        TextView tv_PayCashCount = (TextView) findViewById(R.id.tv_PayCashCount);
        TextView tv_SalesOrdersCount = (TextView)  findViewById(R.id.tv_SalesOrdersCount);
        TextView tv_PayCheckCount = (TextView)  findViewById(R.id.tv_PayCheckCount);


        sqlHandler = new SqlHandler( this);

        q = "Select    ifnull( sum(ifnull(s.Net_Total,0.000)),0.000) as Amt  from  Sal_invoice_Hdr s   where  UserID='" + sharedPreferences.getString("UserID", "") + "' " +
                " and s.inovice_type = '-1'  and  s.date ='" + currentDateandTime + "'";
        TextView tv_Sales = (TextView)  findViewById(R.id.tv_Sales);
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_Sales.setText(c1.getString(c1.getColumnIndex("Amt")));
            }
            c1.close();
        } else {
            tv_Sales.setText("0.000");
        }


        q = "Select  ifnull( sum(ifnull(s.Net_Total,0.000)),0.000) as Amt  from  Sal_invoice_Hdr s   where  UserID='" + sharedPreferences.getString("UserID", "") + "' " +
                " and s.inovice_type != '-1'  and  s.date ='" + currentDateandTime + "'";

        c1 = sqlHandler.selectQuery(q);

        TextView tv_SalesCash = (TextView) findViewById(R.id.tv_SalesCash);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesCash.setText(c1.getString(c1.getColumnIndex("Amt")));
            }
            c1.close();
        } else {
            tv_SalesCash.setText("0.000");
        }


        q = "Select  ifnull(count(Seq),0)  as Seq  from  Sal_invoice_Hdr s   where  UserID='" + sharedPreferences.getString("UserID", "") + "' " +
                "  and  s.date ='" + currentDateandTime + "'" +"and inovice_type='-1'"  ;

        c1 = sqlHandler.selectQuery(q);


        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesCount.setText(c1.getString(c1.getColumnIndex("Seq")));

            }
            c1.close();
        } else {
            tv_SalesCount.setText("0");
        }

        q = "Select  ifnull(count(Seq),0)  as Seq  from  Sal_invoice_Hdr s   where  UserID='" + sharedPreferences.getString("UserID", "") + "' " +
                "  and  s.date ='" + currentDateandTime + "'" +"and inovice_type='0'"  ;

        c1 = sqlHandler.selectQuery(q);

        TextView tv_SalesCount_Cash=(TextView)findViewById(R.id.tv_SalesCount_Cash);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesCount_Cash.setText(c1.getString(c1.getColumnIndex("Seq")));

            }
            c1.close();
        } else {
            tv_SalesCount.setText("0");
        }





        q = "Select      ifnull( sum(ifnull( RecVoucher.Cash,0.000)),0.000) as Cash , ifnull( sum(ifnull( RecVoucher.CheckTotal,0.000)),0.000) as CheckTotal            from RecVoucher   " +
                " where  RecVoucher.UserID ='" + UserID + "' and  RecVoucher.TrDate ='" + currentDateandTime + "'";


        c1 = sqlHandler.selectQuery(q);

      //  TextView tv_PayCash = (TextView)  findViewById(R.id.tv_PayCash);
        TextView tv_Paycheck = (TextView)  findViewById(R.id.tv_Paycheck);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
         //       tv_PayCash.setText(c1.getString(c1.getColumnIndex("Cash")));
                tv_Paycheck.setText(c1.getString(c1.getColumnIndex("CheckTotal")));
            }
            c1.close();
        } else {
         //   tv_PayCash.setText("0.000");
            tv_Paycheck.setText("0.000");
        }

/**/

        q = "Select      ifnull( sum(ifnull( RecVoucher.Cash,0.000)),0.000) as Cash , ifnull( sum(ifnull( RecVoucher.CheckTotal,0.000)),0.000) as CheckTotal            from RecVoucher   " +
                " where  RecVoucher.UserID ='" + UserID + "' and  RecVoucher.TrDate ='" + currentDateandTime + "' and RecVoucher.FromSales='0' ";


        c1 = sqlHandler.selectQuery(q);

        TextView tv_PayCash = (TextView)  findViewById(R.id.tv_PayCash);
       // TextView tv_Paycheck = (TextView)  findViewById(R.id.tv_Paycheck);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_PayCash.setText(c1.getString(c1.getColumnIndex("Cash")));
             //   tv_Paycheck.setText(c1.getString(c1.getColumnIndex("CheckTotal")));
            }
            c1.close();
        } else {
            tv_PayCash.setText("0.000");
           // tv_Paycheck.setText("0.000");
        }
/**/



        q = "Select  ifnull(count(Seq),0)  as Seq  from  RecVoucher s   where  (VouchType='1' or VouchType='3'    ) and   UserID='" + sharedPreferences.getString("UserID", "") + "' " +
                "  and  s.TrDate ='" + currentDateandTime + "'";

        c1 = sqlHandler.selectQuery(q);


        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_PayCashCount.setText(c1.getString(c1.getColumnIndex("Seq")));
            }
            c1.close();
        } else {
            tv_PayCashCount.setText("0");
        }


      /*  q = "Select  ifnull(count(Seq),0)  as Seq  from  RecVoucher s   where  (VouchType='2' or VouchType='3'  ) and   UserID='" + sharedPreferences.getString("UserID", "") + "' " +
                "  and  s.TrDate ='" + currentDateandTime + "'";
*/

        q = "Select  ifnull(count(Seq),0)  as Seq  from  RecVoucher s   where  (VouchType='2' or VouchType='3'    ) and   UserID='" + sharedPreferences.getString("UserID", "") + "' " +
                "  and  s.TrDate ='" + currentDateandTime + "'";

        c1 = sqlHandler.selectQuery(q);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_PayCheckCount.setText(c1.getString(c1.getColumnIndex("Seq")));
            }
            c1.close();
        } else {
            tv_PayCheckCount.setText("0");
        }


        q = "Select  count(1) as s1 ,0 as s2,0 as  s3    from  Sal_invoice_Hdr s  where  s.UserID='" + sharedPreferences.getString("UserID", "") + "' and  s.Post ='-1'";
        c1 = sqlHandler.selectQuery(q);

        q = q + "  UNION ALL Select  0 as1 ,count(1) as  s2,0 as  s3    from RecVoucher   where  RecVoucher.UserID ='" + sharedPreferences.getString("UserID", "") + "' and  RecVoucher.Post =-1";

        q = q + " UNION ALL  Select  0 as1 ,0 as   s2,count(1) as  s3   from Po_Hdr po   " +
                " where  userid='" + sharedPreferences.getString("UserID", "") + "' and po.posted =-1";


        c1 = sqlHandler.selectQuery(q);

        TextView tv_Unposted = (TextView)  findViewById(R.id.tv_Unposted);
        int c = 0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {

                do {

                    c = c + (Integer.parseInt(c1.getString(c1.getColumnIndex("s1")))
                            + Integer.parseInt(c1.getString(c1.getColumnIndex("s2"))) + Integer.parseInt(c1.getString(c1.getColumnIndex("s3"))));


                } while (c1.moveToNext());
                tv_Unposted.setText(c + "");
            }
            c1.close();
        } else {

            tv_Unposted.setText("0");
        }


        q = " Select   ifnull( sum(ifnull( po.Net_Total,0.000)),0.000) as Net_Total from Po_Hdr po where  po.userid='" + sharedPreferences.getString("UserID", "") +
                "' and  po.date ='" + currentDateandTime + "'";


        c1 = sqlHandler.selectQuery(q);

        TextView tv_SalesOrders = (TextView)  findViewById(R.id.tv_SalesOrders);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesOrders.setText(c1.getString(c1.getColumnIndex("Net_Total")));


            }
            c1.close();
        } else {

            tv_Unposted.setText("0");
        }


        q = "Select  ifnull(count(Seq),0)  as Seq  from  Po_Hdr s   where  userid='" + sharedPreferences.getString("UserID", "") + "' " +
                "  and  s.date ='" + currentDateandTime + "'";

        c1 = sqlHandler.selectQuery(q);


        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesOrdersCount.setText(c1.getString(c1.getColumnIndex("Seq")));
            }
            c1.close();
        } else {
            tv_SalesOrdersCount.setText("0");
        }


        TextView tv_EndTime = (TextView)  findViewById(R.id.tv_EndTime);
        TextView tv_StratTime = (TextView)   findViewById(R.id.tv_StratTime);


        q = " select    min(Start_Time) as Start_Time ,max(Start_Time)  as End_Time    from SaleManRounds    where Tr_data='" + currentDateandTime + "'";

        c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_StratTime.setText(c1.getString(c1.getColumnIndex("Start_Time")));
                tv_EndTime.setText(c1.getString(c1.getColumnIndex("End_Time")));
            }
            c1.close();
        } else {
            tv_StratTime.setText("00:00:00");
            tv_EndTime.setText("00:00:00");
        }


       // TextView tv_SalesWithoutPayment = (TextView)   findViewById(R.id.tv_SalesWithoutPayment);
      //  tv_SalesWithoutPayment.setText("0");


        q = "Select  ifnull(count(Seq),0)  as Seq  from  Sal_invoice_Hdr s   where  UserID='" + sharedPreferences.getString("UserID", "") + "' " +
                "  and  s.date ='" + currentDateandTime + "'" +"and inovice_type='-1'"  ;

        c1 = sqlHandler.selectQuery(q);

        TextView tv_SalesWithoutPayment=(TextView)findViewById(R.id.tv_SalesWithoutPayment);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesWithoutPayment.setText(c1.getString(c1.getColumnIndex("Seq")));

            }
            c1.close();
        } else {
            tv_SalesCount.setText("0");
        }


       /* q = " select     ifnull(count( distinct  acc),0) as acc from   Sal_invoice_Hdr   " +
                "    where date ='" + currentDateandTime + "' and   acc not in  ( select CustAcc from RecVoucher where TrDate='" + currentDateandTime + "'  ) ";

        c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesWithoutPayment.setText(c1.getString(c1.getColumnIndex("acc")));

            }
            c1.close();
        } else {
            tv_SalesWithoutPayment.setText("0");

        }
*/
        q = " select  distinct  c.name , c.no from Sal_invoice_Hdr     left join Customers c on c.no =Sal_invoice_Hdr.acc   " +
                "    where date ='" + currentDateandTime + "' and   acc not in  ( select CustAcc from RecVoucher where TrDate='" + currentDateandTime + "'  ) ";

        ListView LstView = (ListView)  findViewById(R.id.LstView);
        c1 = sqlHandler.selectQuery(q);
        Cls_CustomerWithoutPayment cls_customerWithoutPayment;
        ArrayList<Cls_CustomerWithoutPayment> ItemsList;
        ItemsList = new ArrayList<Cls_CustomerWithoutPayment>();
        ItemsList.clear();
        int m = 0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    m++;
                    cls_customerWithoutPayment = new Cls_CustomerWithoutPayment(c1.getString(c1.getColumnIndex("name")), c1.getString(c1.getColumnIndex("no")),"");
                    ItemsList.add(cls_customerWithoutPayment);
                } while (c1.moveToNext());

            }

            c1.close();
        }
        // Toast.makeText(getActivity(),i+"",Toast.LENGTH_LONG).show();
        m = 30 + (m * 60);

        LinearLayout LytListView = (LinearLayout) findViewById(R.id.LytListView);
        LytListView.setLayoutParams(new LinearLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, m));

        Cls_CustWithoutPayment_Adapter cls_unitItems_adapter = new Cls_CustWithoutPayment_Adapter(
               this, ItemsList);

        LstView.setAdapter(cls_unitItems_adapter);



        TextView tv_NewCustomer = (TextView)findViewById(R.id.tv_NewCustomer);
        TextView tv_CustomerVisted = (TextView) findViewById(R.id.tv_CustomerVisted);
        TextView tv_CustomerNotVisited = (TextView) findViewById(R.id.tv_CustomerNotVisited);
        TextView tv_PassVisited = (TextView) findViewById(R.id.tv_PassVisited);

        tv_NewCustomer.setText(GetNewCustomerCount());
        tv_CustomerVisted.setText(GetCustomerVisted());
        tv_CustomerNotVisited.setText(GetCustomerNotVisited());
        tv_PassVisited.setText(GetPassVisited());



    }
///////////////////////////////////////////////////////////////////////
private String GetNewCustomerCount() {
    String c = "0";
    String q = "select ifnull(count(no),0) as no  from  CusfCard where Tr_Date='" + currentDateandTime + "'";
    Cursor c1 = sqlHandler.selectQuery(q);
    if (c1 != null && c1.getCount() != 0) {
        if (c1.moveToFirst()) {
            c = c1.getString(c1.getColumnIndex("no"));
        }
        c1.close();
    }
    return c;
}
    private String GetPassVisited() {
        String c = "0";

        return c;
    }
    private String GetCustomerVisted() {
        String c = "0";
        String q = "select ifnull(count(no),0) as no  from  SaleManRounds where Tr_Data='" + currentDateandTime + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                c = c1.getString(c1.getColumnIndex("no"));
            }
            c1.close();
        }
        return c;
    }
    private String GetCustomerNotVisited () {

        String Week_Num = "1";
        int dayOfWeek = 0;
        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        Week_Num= DB.GetValue(this,"ComanyInfo","VisitWeekNo","1=1");
        String w="";
        if (Week_Num.equalsIgnoreCase("1")) {

            if (dayOfWeek == 7)
                w = " sat = 1 ";

            if (dayOfWeek == 1)
                w = " sun = 1 ";

            if (dayOfWeek == 2)
                w = " mon = 1 ";


            if (dayOfWeek == 3)
                w = " tues = 1 ";

            if (dayOfWeek == 4)
                w = " wens = 1 ";

            if (dayOfWeek == 5)
                w = " thurs = 1 ";
        }
        if (Week_Num.equalsIgnoreCase("2")) {
            if (dayOfWeek == 7)
                w = " sat1 = 1 ";

            if (dayOfWeek == 1)
                w = " sun1 = 1 ";

            if (dayOfWeek == 2)
                w = " mon1 = 1 ";


            if (dayOfWeek == 3)
                w = " tues1 = 1 ";

            if (dayOfWeek == 4)
                w = " wens1 = 1 ";

            if (dayOfWeek == 5)
                w= " thurs1 = 1 ";

        }


        String count = "0";
        q = "Select  ifnull(count(c.no),0) as no   from Customers  c" +
                "  where  c." + w  +" AND  c.no not in ( select CusNo from SaleManRounds where Tr_Data='"+currentDateandTime+"'  )";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                count = c1.getString(c1.getColumnIndex("no"));
            }
            c1.close();
        }
        return count;
    }

///////////////////////////////////////////////////////////////////////
    private void connetBle(){
        try {
            BTCon.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
            BTCon.setTextColor(getResources().getColor(R.color.Blue));
            if (BPrinter_MAC_ID.equals(null) || BPrinter_MAC_ID.equals("")) {
                showSnackbar(getString(R.string.bleselect));
            } else {
                binder.connectBtPort(BPrinter_MAC_ID, new UiExecute() {
                    @Override
                    public void onsucess() {
                        ISCONNECT = true;
                        showSnackbar(getString(R.string.con_success));
                        BTCon.setText("جاري العمل على الطباعة ");
                        CallPrint();
                        binder.write(DataForSendToPrinterPos80.openOrCloseAutoReturnPrintState(0x1f), new UiExecute() {
                            @Override
                            public void onsucess() {
                                binder.acceptdatafromprinter(new UiExecute() {
                                    @Override
                                    public void onsucess() {

                                    }

                                    @Override
                                    public void onfailed() {
                                        ISCONNECT = false;
                                        showSnackbar(getString(R.string.con_has_discon));
                                    }
                                });
                            }

                            @Override
                            public void onfailed() {

                            }
                        });


                    }

                    @Override
                    public void onfailed() {

                        ISCONNECT = false;
                        showSnackbar(getString(R.string.con_failed));
                    }
                });
            }

        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void btn_back(View view) {
        finish();
        Intent i = new Intent(this , OrdersItems.class);
       // startActivity(i);
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(this , OrdersItems.class);
     //   startActivity(i);


    }
    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (action.equals(Xprinter_ManSummeryTo_img.DISCONNECT)){
                Message message=new Message();
                message.what=4;
                handler.handleMessage(message);
            }
        }
    }
    private void CallPrint(){
        StoreImage();
        Bitmap myBitmap = null;
         myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");
         Printz(myBitmap);
        // finish();
     }
    private  void StoreImage(){
        LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);
        ReportView = lay;
        Bitmap b = loadBitmapFromView(ReportView);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "z1.jpg";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            b.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();
            //  bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Bitmap loadBitmapFromView(View v) {

        v.measure(View.MeasureSpec.makeMeasureSpec(v.getLayoutParams().width,
                View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                v.getLayoutParams().height, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
    protected void Printz (final Bitmap bitmap) {



        Log.e("test","test2");

        try{


            b1=convertGreyImg(bitmap);
            Message message=new Message();
            message.what=1;
            handler.handleMessage(message);


            Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
            Tiny.getInstance().source(b1).asBitmap().withOptions(options).compress(new BitmapCallback() {
                @Override
                public void callback(boolean isSuccess, Bitmap bitmap) {
                    if (isSuccess){
//                            Toast.makeText(PosActivity.this,"bitmap: "+bitmap.getByteCount(),Toast.LENGTH_LONG).show();
                        b2=bitmap;
//                            b2=resizeImage(b1,380,false);
                        Message message=new Message();
                        message.what=2;
                        handler.handleMessage(message);
                    }


                }
            });
//                b2=resizeImage(b1,576,386,false);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                    break;
                case 2:
                    //usb connection need special deal with
                    if (PosPrinterDev.PortType.USB!= Xprinter_ManSummeryTo_img.portType){
                        printpicCode(b1);
                    }else {
                        showSnackbar("bimap  "+b2.getWidth()+"  height: "+b1.getHeight());
                        b2=resizeImage(b1,576,false);
                        printUSBbitamp(b1);

                    }




                    break;
                case 3://disconnect

                    break;
                case 4:

                    break;


            }

        }
    };
    private List<Bitmap> cutBitmap(int h, Bitmap bitmap){
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        boolean full=height%h==0;
        int n=height%h==0?height/h:(height/h)+1;
        Bitmap b;
        List<Bitmap> bitmaps=new ArrayList<>();
        for (int i=0;i<n;i++){
            if (full){
                b= Bitmap.createBitmap(bitmap,0,i*h,width,h);
            }else {
                if (i==n-1){
                    b= Bitmap.createBitmap(bitmap,0,i*h,width,height-i*h);
                }else {
                    b= Bitmap.createBitmap(bitmap,0,i*h,width,h);
                }
            }

            bitmaps.add(b);
        }

        return bitmaps;
    }
    private void printUSBbitamp(final Bitmap printBmp){

        int height=printBmp.getHeight();
        // if height > 200 cut the bitmap
        if (height>200){

            Xprinter_ManSummeryTo_img.binder.writeDataByYouself(new UiExecute() {
                @Override
                public void onsucess() {

                }

                @Override
                public void onfailed() {

                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list=new ArrayList<byte[]>();
                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    List<Bitmap> bitmaplist=new ArrayList<>();
                    bitmaplist=cutBitmap(200,printBmp);//cut bitmap
                    if(bitmaplist.size()!=0){
                        for (int i=0;i<bitmaplist.size();i++){
                            list.add(DataForSendToPrinterPos80.printRasterBmp(0,bitmaplist.get(i), BitmapToByteData.BmpType.Threshold, BitmapToByteData.AlignType.Center,576));
                        }
                    }
                    list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66,1));
                    return list;
                }
            });
        }else {
            Xprinter_ManSummeryTo_img.binder.writeDataByYouself(new UiExecute() {
                @Override
                public void onsucess() {

                }

                @Override
                public void onfailed() {

                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list=new ArrayList<byte[]>();
                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    list.add(DataForSendToPrinterPos80.printRasterBmp(
                            0,printBmp, BitmapToByteData.BmpType.Threshold, BitmapToByteData.AlignType.Center,576));
                    list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66,1));
                    return list;
                }
            });
        }

    }
    private void printpicCode(final Bitmap printBmp){


        Xprinter_ManSummeryTo_img.binder.writeDataByYouself(new UiExecute() {
            @Override
            public void onsucess() {

            }

            @Override
            public void onfailed() {
                showSnackbar("failed");
            }
        }, new ProcessData() {
            @Override
            public List<byte[]> processDataBeforeSend() {
                List<byte[]> list=new ArrayList<byte[]>();
                list.add(DataForSendToPrinterPos80.initializePrinter());
                list.add(DataForSendToPrinterPos80.printRasterBmp(
                        0,printBmp, BitmapToByteData.BmpType.Threshold, BitmapToByteData.AlignType.Left,576));
//                list.add(DataForSendToPrinterPos80.printAndFeedForward(3));
                list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66,1));
                return list;
            }
        });




    }
    public Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();
        int height = img.getHeight();

        int[] pixels = new int[width * height];

        img.getPixels(pixels, 0, width, 0, 0, width, height);


        //The arithmetic average of a grayscale image; a threshold
        double redSum=0,greenSum=0,blueSun=0;
        double total=width*height;

        for(int i = 0; i < height; i++)  {
            for(int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey  & 0x00FF0000 ) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);



                redSum+=red;
                greenSum+=green;
                blueSun+=blue;


            }
        }
        int m=(int) (redSum/total);

        //Conversion monochrome diagram
        for(int i = 0; i < height; i++)  {
            for(int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int alpha1 = 0xFF << 24;
                int red = ((grey  & 0x00FF0000 ) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);


                if (red>=m) {
                    red=green=blue=255;
                }else{
                    red=green=blue=0;
                }
                grey = alpha1 | (red << 16) | (green << 8) | blue;
                pixels[width*i+j]=grey;


            }
        }
        Bitmap mBitmap= Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        mBitmap.setPixels(pixels, 0, width, 0, 0, width, height);



        return mBitmap;
    }
    private void showSnackbar(String showstring){ }
    /* @Override
    protected void onDestroy() {
       try {
            super.onDestroy();
            binder.disconnectCurrentPort(new UiExecute() {
                @Override
                public void onsucess() {

                }

                @Override
                public void onfailed() {

                }
            });
            unbindService(conn);
        }catch ( Exception ex){

        }
    }*/
    public static PosPrinterDev.PortType portType;//connect type
    public static Bitmap resizeImage(Bitmap bitmap, int w, boolean ischecked)
    {

        Bitmap BitmapOrg = bitmap;
        Bitmap resizedBitmap = null;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        if (width<=w) {
            return bitmap;
        }
        if (!ischecked) {
            int newWidth = w;
            int newHeight = height*w/width;

            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // if you want to rotate the Bitmap
            // matrix.postRotate(45);
            resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                    height, matrix, true);
        }else {
            resizedBitmap= Bitmap.createBitmap(BitmapOrg, 0, 0, w, height);
        }

        return resizedBitmap;
    }

}
