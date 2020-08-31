package com.cds_jo.GalaxySalesApp.assist;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.Acc_Report;
import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.Companies;
import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.itextpdf.text.PageSize.A4;

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
    PrintReport_TSC_Ipad printReport_tsc_ipad;
    Bitmap LargeBitmap = null;
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

try {
    mBluetoothAdapter.enable();
}catch (Exception r){}
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
            LinearLayout Sal_ItemSLayout2 = (LinearLayout) findViewById(R.id.Sal_ItemSLayout2);
            LinearLayout Sal_ItemSLayout3 = (LinearLayout) findViewById(R.id.Sal_ItemSLayout3);
            LinearLayout Sal_ItemSLayout4 = (LinearLayout) findViewById(R.id.Sal_ItemSLayout4);

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;

            TextView tv_Des;
            TextView Cred;
            TextView Dept;
            TextView Tot;
           int index = 0;
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

                if(  index<=11) {
                    Sal_ItemSLayout.addView(view);
                }else if(index<=21){
                    Sal_ItemSLayout2.addView(view);
                }else if(index<=31){
                    Sal_ItemSLayout3.addView(view);
                 }else if(index<=41){
                    Sal_ItemSLayout4.addView(view);
                }
                index=index+1;
            }






    }
    public void btn_share(View view) {
        TextView ed_Mobile =(EditText)findViewById(R.id.ed_Mobile);
        TextView tv_cusnm =(TextView)findViewById(R.id.tv_cusname);
        String MsgHeader ="السيد";
        MsgHeader = MsgHeader +" "+tv_cusnm.getText().toString() +" المحترم ";
        MsgHeader = MsgHeader+"\n";
        MsgHeader=MsgHeader+" تمت الطباعة من خلال نظام المبيعات المحمول " ;
        MsgHeader = MsgHeader+"\n ";
        MsgHeader = MsgHeader+"وكل عام وانتم بالف خير  ";
         String PhoneNo =ed_Mobile.getText().toString();
        LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);


        PrintReport_Zepra520 obj = new PrintReport_Zepra520(Convert_ccReportTo_ImgActivity.this,
                Convert_ccReportTo_ImgActivity.this, lay, 550, 1);
        obj.StoreImage();



        PackageManager pm = this.getPackageManager();
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            File imageFileToShare = new File("//sdcard/z1.jpg");
            Uri uri2 = Uri.fromFile(imageFileToShare);
            @SuppressWarnings("unused")
            PackageInfo info = pm.getPackageInfo(this.getPackageName(), PackageManager.GET_META_DATA);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/*");
            waIntent.setPackage("com.whatsapp");
           // waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
           //waIntent.putExtra("jid", "962786185c 997" + "@s.whatsapp.net"); //phone number witho
            waIntent.putExtra("jid", "962"+PhoneNo + "@s.whatsapp.net"); //phone number witho
            waIntent.putExtra(Intent.EXTRA_TEXT, MsgHeader);
            waIntent.putExtra(Intent.EXTRA_STREAM, uri2);
            this.startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
            Toast.makeText(this, "الرجاء تثبيت الواتس اب من المتجر", Toast.LENGTH_SHORT).show();
        }
    }
    public void btn_back(View view) {
        Intent i =  new Intent(this, Acc_Report.class);
         startActivity(i);
    }
    private void pdf1()  {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(A4,0,0,0,0);
        try {
            String targetPdf = "/sdcard/Statement_of_Account.pdf";

            PdfWriter.getInstance(document, new FileOutputStream(targetPdf)); //  Change pdf's name.
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();
        com.itextpdf.text.Image img1 = null;
        try {
            img1 = com.itextpdf.text.Image.getInstance("//sdcard//z1.jpg");

        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if(img1!=null  ) {
                float scaler1 = ((document.getPageSize().getWidth() - 0
                        - document.rightMargin() - 0) / img1.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                img1.scalePercent(60);
                img1.setAlignment(com.itextpdf.text.Image.BOTTOM | com.itextpdf.text.Image.ALIGN_CENTER);
                //  img1.scaleToFit(700,3000);
                document.add(img1);

            }




        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();


    }
    public void Send_Email(View view) {
        try {
            LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);


            PrintReport_Zepra520 obj = new PrintReport_Zepra520(Convert_ccReportTo_ImgActivity.this,
                    Convert_ccReportTo_ImgActivity.this, lay, 550, 1);
            obj.StoreImage();
            pdf1();
            String targetPdf = "/sdcard/Statement_of_Account.pdf";
            ArrayList<Uri> uris = new ArrayList<Uri>();
            File fileIn = new File(targetPdf);
            Uri u = Uri.fromFile(fileIn);
            uris.add(u);
            String email, subject, message, attachmentFile;
            // Customer_email = "maen.naamneh@yahoo.com";
            email = "";//"m.naamneh@gi-group.com";
            subject ="كشف حساب العميل";
            message = "";// tv_sig.getText().toString();
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putExtra(android.content.Intent.EXTRA_EMAIL, email);
            intent.putExtra(android.content.Intent.EXTRA_EMAIL,
                    new String[]{email});
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(android.content.Intent.EXTRA_TEXT, message);

            intent.setType("text/plain");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            final PackageManager pm = getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
            ResolveInfo best = null;
            for (final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") ||
                        info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
            if (best != null)
                intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

            //   startActivity(intent);
            startActivity(Intent.createChooser(intent, "الرجاء اختيار البرنامج "));

        } catch (Throwable t) {
            Toast.makeText(this,
                    "Request failed try again: " + t.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void Print_Report(View view) {
        try{
        LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);

        if (ComInfo.ComNo== Companies.Arabian.getValue()) {
            PrintReport_TSC obj = new PrintReport_TSC(Convert_ccReportTo_ImgActivity.this,
                    Convert_ccReportTo_ImgActivity.this, lay, 550, 1);
            obj.DoPrint();

        }else if (ComInfo.ComNo== Companies.Ukrania.getValue()) {

            LinearLayout PageLyt = (LinearLayout) findViewById(R.id.MainHeader);

            printReport_tsc_ipad = new PrintReport_TSC_Ipad(Convert_ccReportTo_ImgActivity.this,
                    Convert_ccReportTo_ImgActivity.this, PageLyt, 550, 1);


            try {
                printReport_tsc_ipad.ConvertLayToImg(PageLyt, "z1");
                Bitmap myBitmap = null;
                myBitmap = BitmapFactory.decodeFile("//sdcard//z1.jpg");
                //Toast.makeText(Convert_ccReportTo_ImgActivity.this,myBitmap.getHeight()+","+myBitmap.getDensity(),Toast.LENGTH_SHORT).show();
                printReport_tsc_ipad.SendSmallImage(myBitmap);
            }catch (Exception f){}

             try {
                 PageLyt = (LinearLayout) findViewById(R.id.Details);
                 printReport_tsc_ipad.ConvertLayToImg(PageLyt, "z2");
                 LargeBitmap = null;
                 LargeBitmap = BitmapFactory.decodeFile("//sdcard//z2.jpg");
                 Log.i("dt1", "dt1 " + LargeBitmap.getHeight() + "");
                 if (LargeBitmap != null && LargeBitmap.getHeight() > 0) {
                     printReport_tsc_ipad.SendLongImage(LargeBitmap);
                 }
             }catch (Exception s){}

                 try {
                     PageLyt = (LinearLayout) findViewById(R.id.Details2);
                     printReport_tsc_ipad.ConvertLayToImg(PageLyt, "z31");
                     LargeBitmap = null;
                     LargeBitmap = BitmapFactory.decodeFile("//sdcard//z31.jpg");
                     Log.i("dt2", "dt2 " + LargeBitmap.getHeight() + "");
                     if (LargeBitmap != null && LargeBitmap.getHeight() > 0) {
                         printReport_tsc_ipad.SendLongImage(LargeBitmap);
                     }
                 }catch ( Exception O){}

            try {
                PageLyt = (LinearLayout) findViewById(R.id.Details3);
                printReport_tsc_ipad.ConvertLayToImg(PageLyt, "z4");
                LargeBitmap = null;
                LargeBitmap = BitmapFactory.decodeFile("//sdcard//z4.jpg");

                Log.i("dt3", "dt3 " + LargeBitmap.getHeight() + "");
                if (LargeBitmap != null && LargeBitmap.getHeight() > 0) {
                    printReport_tsc_ipad.SendLongImage(LargeBitmap);
                }
            }catch (Exception d) { }


            try {
                PageLyt = (LinearLayout) findViewById(R.id.Details4);
                printReport_tsc_ipad.ConvertLayToImg(PageLyt, "z5");
                LargeBitmap = null;
                LargeBitmap = BitmapFactory.decodeFile("//sdcard//z5.jpg");

                Log.i("dt4", "dt4 " + LargeBitmap.getHeight() + "");
                if (LargeBitmap != null && LargeBitmap.getHeight() > 0) {
                    printReport_tsc_ipad.SendLongImage(LargeBitmap);
                }
            }catch (Exception d) { }

            try {
                PageLyt = (LinearLayout) findViewById(R.id.MainDetails);
                printReport_tsc_ipad.ConvertLayToImg(PageLyt, "z6");
                LargeBitmap = null;
                LargeBitmap = BitmapFactory.decodeFile("//sdcard//z6.jpg");
                printReport_tsc_ipad.SendSmallImage(LargeBitmap);
            }catch ( Exception d){}

        }

    }catch(Exception f){

        }




                  /*  PrintReport_Zepra520 obj =  new PrintReport_Zepra520(Convert_ccReportTo_ImgActivity.this,
                            Convert_ccReportTo_ImgActivity.this,lay,560,1);
                    obj.DoPrint();*/
        //  }


    }
}
