package com.cds_jo.GalaxySalesApp.assist;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.Companies;
import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Convert_ccReportTo_ImgActivity extends AppCompatActivity {
    private ESCPOSPrinter posPtr;
    SqlHandler sqlHandler;
    ListView lvCustomList;
    private Button mButton;
    private View mView;
    String ShowTax = "0";
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVERABLE_BT = 0;
    private static final DecimalFormat oneDecimal = new DecimalFormat("#,##0.0");
    ImageView img_Logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String Company = sharedPreferences.getString("CompanyID", "1") ;
        final String PrinterType = sharedPreferences.getString("PrinterType", "1") ;
            setContentView(R.layout.covert_acc_report);
       img_Logo = (ImageView) findViewById(R.id.img_Logo);
        File imgFile = new  File("//sdcard/Android/Cv_Images/logo.jpg");
        try {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img_Logo.setImageBitmap(myBitmap);
            }
        }
        catch (Exception ex){}

        Intent i = getIntent();
        mView = findViewById(R.id.f_view);



        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.
                getDefaultAdapter();




        String u =  sharedPreferences.getString("UserName", "");
        String UserID =  sharedPreferences.getString("UserID", "");




        String  Mobile = "";
        Mobile= DB.GetValue(Convert_ccReportTo_ImgActivity.this,"manf","Mobile1","man='"+UserID+"'");
        if(Mobile.equalsIgnoreCase("")){
            Mobile="";
        }






        TextView tv_CompName =(TextView)findViewById(R.id.tv_CompName);
        tv_CompName.setText(sharedPreferences.getString("CompanyNm", ""));

        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());


        TextView tv_footer =(TextView)findViewById(R.id.tv_footer);
        TextView tv_footer1 =(TextView)findViewById(R.id.tv_footer1);












        sqlHandler = new SqlHandler(this);
        ShowRecord ("");
        mButton = (Button) findViewById(R.id.btn_Print);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);
                /*if (PrinterType.equals("1")) {

                    if (Company.equals("1") || Company.equals("2")) {
                        PrintReport_SEWOO_ESCPOS ObjPrint = new PrintReport_SEWOO_ESCPOS(Convert_ccReportTo_ImgActivity.this,
                                Convert_ccReportTo_ImgActivity.this, lay, 570, 1);
                        ObjPrint.ConnectToPrinter();

                    }

                }

                if (PrinterType.equals("2")) {*/

                if (ComInfo.ComNo== Companies.Arabian.getValue()) {
                    PrintReport_TSC obj = new PrintReport_TSC(Convert_ccReportTo_ImgActivity.this,
                            Convert_ccReportTo_ImgActivity.this, lay, 550, 1);
                    obj.DoPrint();

                }else {
                    PrintReport_Zepra520 obj = new PrintReport_Zepra520(Convert_ccReportTo_ImgActivity.this,
                            Convert_ccReportTo_ImgActivity.this, lay, 570, 1);
                    obj.DoPrint();
                }






                  /*  PrintReport_Zepra520 obj =  new PrintReport_Zepra520(Convert_ccReportTo_ImgActivity.this,
                            Convert_ccReportTo_ImgActivity.this,lay,560,1);
                    obj.DoPrint();*/
              //  }



        }
    });
        mBluetoothAdapter.enable();
 }

    public  void ShowRecord( String OrdNo){


       /* ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setColor(Color.BLACK);
        shape.getPaint().setStyle(Paint.Style.STROKE);
        shape.getPaint().setStrokeWidth(2);





        TextView tv_cusnm =(TextView)findViewById(R.id.tv_cusname);
        TextView et_Date =(TextView)findViewById(R.id.ed_date);
        TextView tv_Disc =(TextView)findViewById(R.id.tv_Disc);
        TextView tv_NetTotal =(TextView)findViewById(R.id.tv_NetTotal);
        TextView tv_TotalTax =(TextView)findViewById(R.id.tv_TotalTax);


        String q = "Select distinct s.include_Tax, s.inovice_type , s.Total , s.Nm,  s.disc_Total, s.OrderNo,s.Net_Total,s.Tax_Total ,s.acc ,s.date , c.name  " +
                "    from  Sal_invoice_Hdr s left join Customers c on c.no =s.acc   where s.OrderNo = '"+OrdNo+"'";

        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                et_Date.setText(c1.getString(c1.getColumnIndex("date")));
                tv_cusnm.setText(c1.getString(c1.getColumnIndex("name")));
                             tv_cusnm.setText(c1.getString(c1.getColumnIndex("name")));

                tv_Disc.setText(c1.getString(c1.getColumnIndex("disc_Total")));
                tv_NetTotal.setText(c1.getString(c1.getColumnIndex("Net_Total")));
                tv_TotalTax.setText(c1.getString(c1.getColumnIndex("Tax_Total")));





            }
            c1.close();  }*/
      showList(OrdNo);

    }

    private  Double SToD(String str){
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat)nf;
        str = str.replace(",","");
        Double d = 0.0;
        if (str.length()==0) {
            str = "0";
        }
        if (str.length()>0)
            try {
                d =  Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            }
            catch (Exception ex)
            {
                str="0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }
    private void showList(String OrderNo) {
        Intent i = getIntent();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String Company = sharedPreferences.getString("CompanyID", "1") ;

        ArrayList<Cls_Acc_Report> contactList = new ArrayList<Cls_Acc_Report>();
        contactList.clear();
        sqlHandler = new SqlHandler(this);
        String query = "SELECT   Tot, Dept,Des,Date, ifnull(Cred,0) as Cred ,FDate,TDate  ,Cust_Nm from ACC_REPORT    " ;



               // "Left Join invf on   invf.Item_No=sid.itemNo  where sid.OrderNo =  '"+  i.getStringExtra("OrderNo").toString()+"'";
        TextView tv_cusnm =(TextView)findViewById(R.id.tv_cusname);
        TextView tv_fdate =(TextView)findViewById(R.id.tv_fdate);
        TextView tv_tdate =(TextView)findViewById(R.id.tv_tdate);

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                    tv_cusnm.setText(c1.getString(c1
                            .getColumnIndex("Cust_Nm")));
                    tv_fdate.setText(c1.getString(c1
                            .getColumnIndex("FDate")));
                    tv_tdate.setText(c1.getString(c1
                            .getColumnIndex("TDate")));
                do {
                    Cls_Acc_Report contactListItems = new Cls_Acc_Report();

                    if(c1.getString(c1.getColumnIndex("Date")).equalsIgnoreCase("عدد الحركات")) {
                        contactListItems.setDes(c1.getString(c1.getColumnIndex("Des")) );
                    }else{
                        contactListItems.setDes(c1.getString(c1
                                .getColumnIndex("Des")) +"\n\r   " + c1.getString(c1.getColumnIndex("Date")));
                    }


                    contactListItems.setCred(c1.getString(c1
                            .getColumnIndex("Cred")));
                    contactListItems.setDept(c1.getString(c1
                            .getColumnIndex("Dept")));
                    contactListItems.setTot(c1.getString(c1
                            .getColumnIndex("Tot")));

                    contactList.add(contactListItems);


                } while (c1.moveToNext());


            }



            c1.close();
        }





            LinearLayout Sal_ItemSLayout = (LinearLayout) findViewById(R.id.Sal_ItemSLayout);

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;

            TextView tv_Des;
            TextView Cred;
            TextView Dept;
            TextView Tot;

            for (Cls_Acc_Report Obj : contactList){

                    view = inflater.inflate(R.layout.report_img_row, null);

                tv_Des = (TextView) view.findViewById(R.id.tv_Des);
                Cred = (TextView) view.findViewById(R.id.tv_Cred);
                Dept = (TextView) view.findViewById(R.id.tv_Dept);
                Tot = (TextView) view.findViewById(R.id.tv_Tot);


                tv_Des.setText(Obj.getDes());
                Cred.setText(Obj.getCred());
                Dept.setText(Obj.getDept());
                Tot.setText(Obj.getTot());

                Sal_ItemSLayout.addView(view);

            }






    }

    public void btn_back(View view) {
        Intent i =  new Intent(this,Sale_InvoiceActivity.class);
         startActivity(i);
    }
}
