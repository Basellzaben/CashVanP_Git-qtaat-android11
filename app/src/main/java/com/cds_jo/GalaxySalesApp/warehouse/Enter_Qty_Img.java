package com.cds_jo.GalaxySalesApp.warehouse;

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
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.Companies;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.Cls_TransQtyItems;
import com.cds_jo.GalaxySalesApp.assist.ESCPSample3;
import com.cds_jo.GalaxySalesApp.assist.OrdersItems;
import com.cds_jo.GalaxySalesApp.assist.PrintReport_SEWOO_ESCPOS;
import com.cds_jo.GalaxySalesApp.assist.PrintReport_Zepra520;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Enter_Qty_Img extends AppCompatActivity {

    SqlHandler sqlHandler;
    ListView lvCustomList;
    private Button mButton;
    private View mView;
    private Button mButton2;
    private ESCPOSPrinter posPtr;
    ESCPSample3 obj_print=new ESCPSample3();
    Bitmap bitmap;
    ImageView img_Logo,imgSig;
    String Order_No ;
    String DIRECTORY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_qty_img);
        mView = findViewById(R.id.f_view);

        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.
                getDefaultAdapter();



        img_Logo = (ImageView) findViewById(R.id.img_Logo);
        File imgFile = new  File("//sdcard/Android/Cv_Images/logo.jpg");
        try {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img_Logo.setImageBitmap(myBitmap);
            }
        }
        catch (Exception ex){}

        String folder_main = "/CV_TransQty";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/CV_TransQty/";
        Intent i = getIntent();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserName", "");
        TextView    tv_UserNm  = (TextView)findViewById(R.id.tv_UserNm);
        tv_UserNm.setText(u);

        sqlHandler = new SqlHandler(this);
        TextView tv_OrderNo=(TextView)findViewById(R.id.tv_OrderNo);
        tv_OrderNo.setText(i.getStringExtra("OrderNo"));
        Order_No=i.getStringExtra("OrderNo");
        ShowRecord(Order_No);


        showList(Order_No);
        try {
            mBluetoothAdapter.enable();
        }catch (Exception d){}
        StoreImage();
    }

   private void showList(String OrderNo ) {



        ArrayList<Cls_TransQtyItems> contactList = new ArrayList<Cls_TransQtyItems>();
        contactList.clear();
        Intent i = getIntent();
         String   query = "  select Distinct Unites.UnitName,  invf.Item_Name ,d.itemno,d.UnitNo,d.qty ,d.UnitRate " +
               "   from EnterQtydtl d left join invf on invf.Item_No =  d.itemno    " +
               " left join Unites on Unites.Unitno=  d.UnitNo " +
               "  Where d.OrderNo='" +OrderNo + "' and Kind ='1'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_TransQtyItems contactListItems = new Cls_TransQtyItems();
                    contactListItems.setItemNo(c1.getString(c1
                            .getColumnIndex("itemno")));
                    contactListItems.setItemNm(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setUnit(c1.getString(c1
                            .getColumnIndex("UnitNo")));
                    contactListItems.setUnitNm(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setUnit_Rate(c1.getString(c1
                            .getColumnIndex("UnitRate")));
                    contactList.add(contactListItems);
                } while (c1.moveToNext());


               // etTotal.setText(String.valueOf(Total));
             //   et_Tottal_Tax.setText(String.valueOf(Total_Tax));
            }

        c1.close();
   }

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

        for (Cls_TransQtyItems Obj : contactList) {
            view = inflater.inflate(R.layout.store_qty_row, null);
            tv_no = (TextView) view.findViewById(R.id.tv_no);
            tv_name = (TextView) view.findViewById(R.id.tv_name);

            tv_Qty = (TextView) view.findViewById(R.id.tv_Qty);
            tv_Unit = (TextView) view.findViewById(R.id.tv_Unit);



            tv_no.setText(Obj.getItemNo());
            tv_name.setText(Obj.getItemNm());

            tv_Qty.setText(Obj.getQty());
            tv_Unit.setText(Obj.getUnitNm() + "");

            ChecksLayout.addView(view);

        }
    }


    public void btn_back(View view) {

        Intent k = new Intent(Enter_Qty_Img.this, OrdersItems.class);
        startActivity(k);
    }

    public  void ShowRecord( String OrdNo){

        TextView ed_date =(TextView)findViewById(R.id.ed_date);
        TextView tv_FromStore =(TextView)findViewById(R.id.tv_FromStore);
        TextView tv_ToStore =(TextView)findViewById(R.id.tv_ToStore);
        TextView tv_Des =(TextView)findViewById(R.id.tv_Des);



        String query = "  select   Distinct  ifnull (h.Notes,'') Notes , ifnull(h.fromstore,0) as fromstore  ,ifnull(h.tostore,0) as tostore ,s1.sname as nm1  ,h.Tr_Date  from EnterQtyhdr h" +
                " left join stores s1 on s1.sno =h.fromstore  " +

                "   where   OrderNo ='" + OrdNo + "' And Kind ='1'";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_Des.setText(c1.getString(c1.getColumnIndex("Notes")));
                tv_FromStore.setText(c1.getString(c1.getColumnIndex("nm1")).toString());
                tv_ToStore.setText("");
                ed_date.setText(c1.getString(c1.getColumnIndex("Tr_Date")).toString());
            }

            c1.close();
        }

    }
    public void Do_P(View view) {
        if(ComInfo.ComNo== Companies.Ukrania.getValue()){
            btn_Open_Img_A4();
        }else {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            final String PrinterType = sharedPreferences.getString("PrinterType", "1");
            LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);

        }

    }
    private  void StoreImage(){
        LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);

        Bitmap b = loadBitmapFromView(lay);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "z1.png";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //  bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
         createPdf();
    }
    private void createPdf() {

        try {
            WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics displaymetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            float hight = displaymetrics.heightPixels;
            float width = displaymetrics.widthPixels;

            int convertHighet = (int) hight, convertWidth = (int) width;


            File imgFile = new File("//sdcard/Android/Cv_Images/logo.jpg");

            imgFile = new File("//sdcard//z1.png");


            if (imgFile.exists()) {
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            }


            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            Canvas canvas = page.getCanvas();


            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#ffffff"));
            canvas.drawPaint(paint);


            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            document.finishPage(page);


            // write the document content
            String targetPdf = "/sdcard/CV_TransQty/" + Order_No + ".pdf";
            File filePath = new File(targetPdf);
            try {
                document.writeTo(new FileOutputStream(filePath));

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            }

            // close the document
            document.close();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
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

    public void btn_Open_Img_A4( ) {

        try {
            String targetPdf = "/sdcard/CV_TransQty/" + Order_No + ".pdf";
            // String targetPdf = "/sdcard/g1.png";

            File imgFile = new File(targetPdf);

            Uri path = Uri.fromFile(imgFile);
            // Uri path=FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider", imgFile);


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setDataAndType(path, "application/pdf");

            startActivity(Intent.createChooser(intent, "الرجاء اختيار البرنامج "));
            // startActivity(intent);
        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

}