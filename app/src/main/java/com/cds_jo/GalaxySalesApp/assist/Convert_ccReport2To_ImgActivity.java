package com.cds_jo.GalaxySalesApp.assist;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.Companies;
import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.UpdateDataToMobileActivity;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Convert_ccReport2To_ImgActivity extends AppCompatActivity {
    private ESCPOSPrinter posPtr;
    SqlHandler sqlHandler;

    private Button mButton;
    private View mView;
    String AmtDesc ="";
    String NetTotal;
    ImageView img_Logo;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String Company = sharedPreferences.getString("CompanyID", "1") ;
        final String PrinterType = sharedPreferences.getString("PrinterType", "1") ;
            setContentView(R.layout.covert_acc_approval_report);
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



        String AccNo = getIntent().getStringExtra("accno");
        NetTotal = DB.GetValue(Convert_ccReport2To_ImgActivity.this, "Customers", "CUST_NET_BAL", "no ='" + AccNo + "' ");
        getTafkeed();



        mButton = (Button) findViewById(R.id.btn_Print);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);




                if (ComInfo.ComNo== Companies.beutyLine.getValue()) {
                    PrintReport_TSC obj = new PrintReport_TSC(Convert_ccReport2To_ImgActivity.this,
                            Convert_ccReport2To_ImgActivity.this, lay, 420, 1);
                    obj.DoPrint();
                }else {
                    PrintReport_Zepra520 obj = new PrintReport_Zepra520(Convert_ccReport2To_ImgActivity.this,
                            Convert_ccReport2To_ImgActivity.this, lay, 560, 1);
                    obj.DoPrint();

                }


        }
    });
        try {
            mBluetoothAdapter.enable();
        }catch (Exception d){}
 }
 private void  getTafkeed(){
     CallWebServices ws = new CallWebServices(Convert_ccReport2To_ImgActivity.this);
     ws.Tafkeet(NetTotal);
     JSONObject js = null;
     try {
         js = new JSONObject(We_Result.Msg);
         JSONArray js_Tafkeet= js.getJSONArray("Tafkeet");
         AmtDesc= js_Tafkeet.get(0).toString() ;
         if(AmtDesc.equalsIgnoreCase("null"))
             AmtDesc="";
     } catch (JSONException e) {
         e.printStackTrace();
     }

     final Handler _handler = new Handler();
     new Thread(new Runnable() {
         @Override
         public void run() {
             CallWebServices ws = new CallWebServices(Convert_ccReport2To_ImgActivity.this);
             ws.Tafkeet(NetTotal);
             try {
                 JSONObject js = new JSONObject(We_Result.Msg);
                 JSONArray js_Tafkeet= js.getJSONArray("Tafkeet");
                 AmtDesc= js_Tafkeet.get(0).toString() ;
                _handler.post(new Runnable() {
                     public void run() {
                         ShowRecord();
                     }
                 });

             } catch (final Exception e) {
                 ShowRecord();

             }
         }
     }).start();
 }
 private  void ShowRecord(){

     String u =  sharedPreferences.getString("UserName", "");
     String UserID =  sharedPreferences.getString("UserID", "");




     String  Mobile = "";
     Mobile= DB.GetValue(Convert_ccReport2To_ImgActivity.this,"manf","Mobile1","man='"+UserID+"'");

     String AccStatus = "";
     if (SToD(NetTotal)<0){
         AccStatus="دائن";
     }else{
         AccStatus="مدين";
     }
     // new Tafkeet().execute(NetTotal);






     TextView tv_CompName =(TextView)findViewById(R.id.tv_CompName);
     tv_CompName.setText(sharedPreferences.getString("CompanyNm", ""));



     TextView tv_CompName2 =(TextView)findViewById(R.id.tv_CompName2);
     tv_CompName2.setText(" السادة /"+sharedPreferences.getString("CompanyNm", "") +" المحترمين");

     SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
     String currentDate = sdf.format(new Date());

     TextView tv_cusnm =(TextView)findViewById(R.id.tv_cusname);
     TextView tv_fdate =(TextView)findViewById(R.id.tv_fdate);
     TextView tv_msg =(TextView)findViewById(R.id.tv_msg);
     TextView tv_msg2 =(TextView)findViewById(R.id.tv_msg2);
     tv_cusnm.setText("السادة /"+ getIntent().getStringExtra("cusnm")+" المحترمين  ");
     tv_fdate.setText(currentDate);
     String  msg = "";
     String  msg2 = "";
     msg= "نود أن نحيطكم علما بانة رصيد حسابكم حتي تاريخ";
     msg=msg+" " +currentDate+"م" +" ";
     msg=msg+"("+AccStatus+")";
     msg=msg+" مبلغ وقدره ( " +  NetTotal   + " )";
     msg=msg+" " + AmtDesc +" ";
     msg=msg+"نرجو التكرم بمراجعة الرصيد والمصادقة على صحته ، و في حالة عدم إرسال مطابقة خلال خمسة عشر يوما من تاريخ خطابنا هذا يعتبر هذا الرصيد مصدق عليه .";


     tv_msg.setText(msg);
     msg2=   "نحطيكم علما بأننا نصادق على صحة رصيدنا لديكم ";
     msg2=msg2+"("+AccStatus+")";
     msg2=  msg2+ " حتى تاريخ "+ currentDate + " ";


     msg2=msg2+ " بمبلغ وقدرة (                      ) " ;
     tv_msg2.setText(msg2);

     TextView tv_R1 =(TextView)findViewById(R.id.tv_R1);
     tv_R1.setText("1-__________________________________ ");


     TextView tv_R2 =(TextView)findViewById(R.id.tv_R2);
     tv_R2.setText("2-__________________________________ ");

     TextView tv_R3 =(TextView)findViewById(R.id.tv_R3);
     tv_R3.setText("3-__________________________________ ");
 }
    public void btn_back(View view) {
        Intent i =  new Intent(this,Acc_ReportActivity.class);
         startActivity(i);
    }

    private class Tafkeet extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {

            Get_Amt_Tafkeed obj = new Get_Amt_Tafkeed(Convert_ccReport2To_ImgActivity.this);
            try {
                AmtDesc= obj.DoTrans( params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


        }

    }
    private Double SToD(String str) {
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat) nf;
        str = str.replace(",", "");
        Double d = 0.0;
        if (str.length() == 0) {
            str = "0";
        }
        if (str.length() > 0)
            try {
                d = Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            } catch (Exception ex) {
                str = "0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }
}
