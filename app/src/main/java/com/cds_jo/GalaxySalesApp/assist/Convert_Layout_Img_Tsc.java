package com.cds_jo.GalaxySalesApp.assist;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.ContactListItems;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Convert_Layout_Img_Tsc extends AppCompatActivity {

    SqlHandler sqlHandler;
    ListView lvCustomList;
    private Button mButton;
    private View mView;
    private Button mButton2;
    private ESCPOSPrinter posPtr;
    ESCPSample3 obj_print=new ESCPSample3();
    ImageView img_Logo,imgSig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convert_po_img_home);
        mView = findViewById(R.id.f_view);




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
        imgSig = (ImageView) findViewById(R.id.imgSig);
        imgFile = new  File("//sdcard//Android/Cv_Images/Po_Sig/"+i.getStringExtra("OrderNo").toString() +".png");
        try {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgSig.setImageBitmap(myBitmap);
            }
        }
        catch (Exception ex){}





        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserName", "");
        TextView    tv_UserNm  = (TextView)findViewById(R.id.tv_UserNm);
        tv_UserNm.setText(u);
        TextView tv_cusname=(TextView)findViewById(R.id.tv_cusname);
        tv_cusname.setText(i.getStringExtra("cusnm"));

        TextView tv_OrderNo=(TextView)findViewById(R.id.tv_OrderNo);
        tv_OrderNo.setText(i.getStringExtra("OrderNo"));
        ShowRecord(i.getStringExtra("OrderNo"));


        TextView tv_CompName =(TextView)findViewById(R.id.tv_CompName);
        tv_CompName.setText(sharedPreferences.getString("CompanyNm", ""));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd" );
        String currentDateandTime = sdf.format(new Date());
        sqlHandler = new SqlHandler(this);
        showList();

    }

   private void showList() {
        //Intent i = getIntent();
        float Total = 0;
        float Total_Tax = 0;
        float TTemp = 0;
        float PTemp = 0;
        float PQty = 0;
        TextView etTotal, et_Tottal_Tax;

        ArrayList<ContactListItems> contactList = new ArrayList<ContactListItems>();
        contactList.clear();
        Intent i = getIntent();
          String query = "  select  DISTINCT Unites.UnitName,pod.total, pod.tax_Amt, invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo from Po_dtl pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.orderno='" + i.getStringExtra("OrderNo").toString() + "'";
       // String query = "  select Unites.UnitName,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo from Po_dtl pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.orderno=1";


        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemno")));
                    contactListItems.setName(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setprice(c1.getString(c1
                            .getColumnIndex("price")));
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setTax(c1.getString(c1
                            .getColumnIndex("tax_Amt")));
                    contactListItems.setUniteNm(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setUniteNm(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("total")));
                    contactList.add(contactListItems);

                    TTemp = Float.valueOf(c1.getString(c1.getColumnIndex("tax")));
                    PTemp = Float.valueOf(c1.getString(c1.getColumnIndex("price")));
                    PQty = Float.valueOf(c1.getString(c1.getColumnIndex("qty")));

                    PTemp = PTemp * PQty;

                    TTemp = TTemp / 100;
                    TTemp = TTemp * PTemp;

                    Total = Total + PTemp;
                    Total_Tax = Total_Tax + TTemp;
                    PTemp = 0;
                    PQty = 0;
                    TTemp = 0;
                } while (c1.moveToNext());


               // etTotal.setText(String.valueOf(Total));
             //   et_Tottal_Tax.setText(String.valueOf(Total_Tax));
            }
        }
        c1.close();


        // Cls_Po_Print_Adapter contactListAdapter = new Cls_Po_Print_Adapter(
        //         Convert_Layout_Img.this, contactList);
        // lvCustomList.setAdapter(contactListAdapter);

        LinearLayout ChecksLayout = (LinearLayout) findViewById(R.id.Sal_ItemSLayout);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;

        TextView tv_no;
        TextView tv_name;
        TextView tv_Price;
        TextView tv_Qty;
        TextView tv_Unit;
        TextView tv_tax;
       TextView tv_total;

        for (ContactListItems Obj : contactList) {
            view = inflater.inflate(R.layout.po_print_row, null);
            tv_no = (TextView) view.findViewById(R.id.tv_no);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_Price = (TextView) view.findViewById(R.id.tv_Price);
            tv_Qty = (TextView) view.findViewById(R.id.tv_Qty);
            tv_Unit = (TextView) view.findViewById(R.id.tv_Unit);
            tv_tax = (TextView) view.findViewById(R.id.tv_tax);
            tv_total = (TextView) view.findViewById(R.id.tv_total);


            tv_no.setText(Obj.getno());
            tv_name.setText(Obj.getName());
            tv_Price.setText(Obj.getprice());
            tv_Qty.setText(Obj.getQty());
            tv_Unit.setText(Obj.getUniteNm() + "");
            tv_tax.setText(Obj.getTax() + "");
            tv_total.setText(Obj.getTotal());
            ChecksLayout.addView(view);

        }
    }


    public void btn_back(View view) {

        Intent k = new Intent(Convert_Layout_Img_Tsc.this, OrdersItems.class);
        startActivity(k);
    }

    public  void ShowRecord( String OrdNo){

        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setColor(Color.BLACK);
        shape.getPaint().setStyle(Paint.Style.STROKE);
        shape.getPaint().setStrokeWidth(2);


        // Assign the created border to EditText widget



        TextView et_Date =(TextView)findViewById(R.id.ed_date);
        TextView tv_Disc =(TextView)findViewById(R.id.tv_Disc);
        TextView tv_NetTotal =(TextView)findViewById(R.id.tv_NetTotal);
        TextView tv_TotalTax =(TextView)findViewById(R.id.tv_TotalTax);
        TextView tv_Total =(TextView)findViewById(R.id.tv_Total);

        TextView textView11 =(TextView)findViewById(R.id.textView11);
        TextView textView12 =(TextView)findViewById(R.id.textView12);
        TextView textView86 =(TextView)findViewById(R.id.textView86);
        TextView textView88 =(TextView)findViewById(R.id.textView88);




        TextView textView5 =(TextView)findViewById(R.id.textView5);
        TextView textView7 =(TextView)findViewById(R.id.textView7);
        TextView textView8 =(TextView)findViewById(R.id.textView8);
        TextView textView9 =(TextView)findViewById(R.id.textView9);
        TextView textView43 =(TextView)findViewById(R.id.textView43);
        TextView textView10 =(TextView)findViewById(R.id.textView10);
        TextView textView58 =(TextView)findViewById(R.id.textView58);



        textView5.setBackground(shape);
        textView7.setBackground(shape);
        textView8.setBackground(shape);
        textView9.setBackground(shape);
        textView43.setBackground(shape);
        textView10.setBackground(shape);


        textView11.setBackground(shape);
        textView12.setBackground(shape);
        textView86.setBackground(shape);
        textView88.setBackground(shape);
        textView58.setBackground(shape);

        tv_Total.setBackground(shape);
        tv_TotalTax.setBackground(shape);
        tv_NetTotal.setBackground(shape);
        tv_Disc.setBackground(shape);


        String q = "Select  distinct  * from Po_Hdr s  where s.orderno = '"+OrdNo+"'";

        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                et_Date.setText(c1.getString(c1.getColumnIndex("date")));
                tv_Disc.setText(c1.getString(c1.getColumnIndex("disc_Total")));
                tv_NetTotal.setText(c1.getString(c1.getColumnIndex("Net_Total")));
                tv_TotalTax.setText(c1.getString(c1.getColumnIndex("Tax_Total")));

                tv_Total.setText(c1.getString(c1.getColumnIndex("Total")));

            }
        }
        c1.close();
    }
    public void Do_P(View view) {
        LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);
        PrintReport_TSC obj = new PrintReport_TSC(Convert_Layout_Img_Tsc.this,
                Convert_Layout_Img_Tsc.this, lay, 570, 1);
        obj.DoPrint();

    }





}