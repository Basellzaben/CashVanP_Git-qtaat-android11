package com.cds_jo.GalaxySalesApp;

import android.annotation.SuppressLint;
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
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

    public class Convert_Sal_Return_To_ImgActivity_Line extends AppCompatActivity {
        private ESCPOSPrinter posPtr;
        SqlHandler sqlHandler;
        ListView lvCustomList;
        private Button mButton;
        private View mView;
        String ShowTax = "0";
        private static final int REQUEST_ENABLE_BT = 0;
        private static final int REQUEST_DISCOVERABLE_BT = 0;
        private static final DecimalFormat oneDecimal = new DecimalFormat("#,##0.0");

        Button btn_Save, btn_Clear, btn_Cancel;
        LinearLayout mContent;
        View ViewNotes;
        signature mSignature;
        Bitmap bitmap;
        ImageView img_Logo;
        ImageView imgSig;
        int SaveImg = 0;
        String StoredPath;
        String DIRECTORY;
        float STROKE_WIDTH = 5f;
        float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            try {
                setContentView(R.layout.activity_convert__sal__return__to__img__line);
            }
        catch(Exception ex)

        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String Company = sharedPreferences.getString("CompanyID", "1");


        String folder_main = "/Android/Cv_Images/Salreturn_Sig";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
            if(!f.exists())

        {
            f.mkdirs();
        }

        DIRECTORY =Environment.getExternalStorageDirectory().

        getPath() +"/Android/Cv_Images/Salreturn_Sig/";
        StoredPath =DIRECTORY +

        getIntent().

        getStringExtra("OrderNo")+".png";


        mSignature =new

        signature(Convert_Sal_Return_To_ImgActivity_Line .this, null);
            mSignature.setBackgroundColor(Color.WHITE);

        mContent =(LinearLayout)

        findViewById(R.id.mContent);
            mContent.addView(mSignature,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        ViewNotes =mContent;


        btn_Save=(Button)

        findViewById(R.id.btn_Save);
            btn_Save.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
            SaveSig();

        }
        });


        btn_Clear=(Button)

        findViewById(R.id.btn_Clear);
            btn_Clear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
            ViewNotes.setDrawingCacheEnabled(true);
            mSignature.save(ViewNotes, StoredPath);
            mSignature.clear();
            mSignature.setBackgroundColor(Color.WHITE);
            SaveSig();
        }
        });

        // Dynamically generating Layout through java code


        img_Logo =(ImageView)

        findViewById(R.id.img_Logo);

        File imgFile = new File("//sdcard/Android/Cv_Images/logo.jpg");
            try

        {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img_Logo.setImageBitmap(myBitmap);
            }
        }
            catch(
        Exception ex)

        {
        }

        imgSig =(ImageView)

        findViewById(R.id.imgSig);

        loadimage();


        Intent i = getIntent();
        mView =

        findViewById(R.id.f_view);

        TextView OrderNo = (TextView) findViewById(R.id.tv_OrderNo);
            OrderNo.setText(

        getIntent().

        getStringExtra("Orderno"));


        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.
                getDefaultAdapter();


        String u = sharedPreferences.getString("UserName", "");
        String UserID = sharedPreferences.getString("UserID", "");
        TextView tv_TaxAcc = (TextView) findViewById(R.id.tv_TaxAcc);
            tv_TaxAcc.setText(sharedPreferences.getString("TaxAcc1",""));


        TextView ManManMobile = (TextView) findViewById(R.id.tv_ManMobile);

        String Mobile = "";
        Mobile=DB.GetValue(Convert_Sal_Return_To_ImgActivity_Line .this,"manf","Mobile1","man='"+UserID+"'");
            if(Mobile.equalsIgnoreCase(""))

        {
            Mobile = "";
        }
            ManManMobile.setText(Mobile);


        TextView tv_SupervisorMobile = (TextView) findViewById(R.id.tv_SupervisorMobile);
            tv_SupervisorMobile.setText(sharedPreferences.getString("SuperVisorMobile",""));


        TextView tv_CompName = (TextView) findViewById(R.id.tv_CompName);
            tv_CompName.setText(sharedPreferences.getString("CompanyNm",""));

        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        TextView Time = (TextView) findViewById(R.id.tv_time);
            Time.setText(StringTime);

        TextView tv_footer = (TextView) findViewById(R.id.tv_footer);
        TextView tv_footer1 = (TextView) findViewById(R.id.tv_footer1);

        TextView tv_footer3 = (TextView) findViewById(R.id.tv_footer3);

            tv_footer.setText(sharedPreferences.getString("CompanyNm","")+" - "+sharedPreferences.getString("Address","")+" - "+sharedPreferences.getString("Notes",""));
            tv_footer1.setText(" هاتف الشركة  "+(sharedPreferences.getString("CompanyMobile","")));


        String invoice_type = "";
        invoice_type =DB.GetValue(Convert_Sal_Return_To_ImgActivity_Line .this,"RecVoucher","VouchType","SalesOrderNo='"+OrderNo.getText().

        toString() +"'");
            if(invoice_type.equalsIgnoreCase("1"))

        {

            TextView tv_RecVoucher = (TextView) findViewById(R.id.tv_RecVoucher);


            TextView tv_custacc = (TextView) findViewById(R.id.tv_custacc);
            String custacc = "";
            custacc = DB.GetValue(Convert_Sal_Return_To_ImgActivity_Line.this, "RecVoucher", "CustAcc", "Docno='" + tv_RecVoucher.getText().toString() + "'");
            if (custacc.equalsIgnoreCase("")) {
                custacc = "";
            }
            tv_custacc.setText(custacc);


            TextView tv_custnameV = (TextView) findViewById(R.id.tv_custnameV);
            String custnameV = "";
            custnameV = DB.GetValue(Convert_Sal_Return_To_ImgActivity_Line.this, "Customers", "name", "no='" + tv_custacc.getText().toString() + "'");
            if (custnameV.equalsIgnoreCase("")) {
                custnameV = "";
            }
            tv_custnameV.setText(custnameV);


            TextView tv_cashv = (TextView) findViewById(R.id.tv_cashv);
            String cashv = "";
            cashv = DB.GetValue(Convert_Sal_Return_To_ImgActivity_Line.this, "RecVoucher", "Cash", "Docno='" + tv_RecVoucher.getText().toString() + "'");
            if (cashv.equalsIgnoreCase("")) {
                cashv = "";
            }
            tv_cashv.setText(cashv);


            TextView tv_cashVoucher = (TextView) findViewById(R.id.tv_cashVoucher);
            String cashVoucher = "";
            cashVoucher = DB.GetValue(Convert_Sal_Return_To_ImgActivity_Line.this, "RecVoucher", "Cash", "Docno='" + tv_RecVoucher.getText().toString() + "'");
            if (cashVoucher.equalsIgnoreCase("")) {
                cashv = "";
            }
            tv_cashVoucher.setText(cashVoucher);


            TextView tv_DescVoucher = (TextView) findViewById(R.id.tv_DescVoucher);
            String DescVoucher = "";
            DescVoucher = DB.GetValue(Convert_Sal_Return_To_ImgActivity_Line.this, "RecVoucher", "Desc", "Docno='" + tv_RecVoucher.getText().toString() + "'");
            if (DescVoucher.equalsIgnoreCase("")) {
                DescVoucher = "";
            }
            tv_DescVoucher.setText(DescVoucher);

        }
            else

        {
            LinearLayout Voucher = (LinearLayout) findViewById(R.id.voucher);
            Voucher.setVisibility(View.GONE);


        }

        String footer3 = "";


            tv_footer3.setText(footer3);
        ShowTax=

        getIntent().

        getStringExtra("ShowTax");


        TextView tv_UserNm = (TextView) findViewById(R.id.tv_UserNm);
            tv_UserNm.setText(u);

        //TextView    textView10  = (TextView)findViewById(R.id.textView10);

        TextView textView12 = (TextView) findViewById(R.id.textView12);
        TextView tv_TotalTax = (TextView) findViewById(R.id.tv_TotalTax);

        //TextView    tv_Tax_Caption  = (TextView)findViewById(R.id.tv_Tax_Caption);

        //TextView    tv_DvNm  = (TextView)findViewById(R.id.tv_DvNm);

        TextView textView11 = (TextView) findViewById(R.id.textView11);
        TextView tv_Total = (TextView) findViewById(R.id.tv_Total);
        TextView textView86 = (TextView) findViewById(R.id.textView86);
        TextView textView861 = (TextView) findViewById(R.id.textView861);
        /*TextView tv_Disc =(TextView)findViewById(R.id.tv_Disc);
        TextView tv_Disc_Percent =(TextView)findViewById(R.id.tv_Disc_Percent);*/


            textView12.setVisibility(View.VISIBLE);
            tv_TotalTax.setVisibility(View.VISIBLE);
            textView11.setVisibility(View.VISIBLE);
            tv_Total.setVisibility(View.VISIBLE);
        /* textView86.setVisibility(View.VISIBLE);*/
        //textView861.setVisibility(View.VISIBLE);
       /* tv_Disc.setVisibility(View.VISIBLE);
        tv_Disc_Percent.setVisibility(View.VISIBLE);*/


        sqlHandler =new

        SqlHandler(this);

        ShowRecord(getIntent().

        getStringExtra("Orderno"));
        mButton =(Button)

        findViewById(R.id.btn_Print);


    }


        private  void ResetFoundSize(){
            mSignature.paint.setStrokeWidth(5f);
            ViewNotes.setDrawingCacheEnabled(true);
            STROKE_WIDTH = 5f;
            HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

        }

        public  void SaveSig(){
            ViewNotes.setDrawingCacheEnabled(true);
            mSignature.save(ViewNotes, StoredPath);
            loadimage();
        }
        public void btn_Red(View view) {

            mSignature.save(ViewNotes, StoredPath);
            loadimage();
            mSignature.clear();
            mSignature.paint.setColor(Color.RED);
            ResetFoundSize();
        }
        public void btn_Erease(View view) {
            ViewNotes.setDrawingCacheEnabled(true);
            mSignature.save(ViewNotes, StoredPath);
            loadimage();
            mSignature.clear();
            mSignature.paint.setColor(Color.WHITE);
            mSignature.paint.setStrokeWidth(40f);
            ViewNotes.setDrawingCacheEnabled(true);
            STROKE_WIDTH = 30f;
            HALF_STROKE_WIDTH = STROKE_WIDTH / 2;



        }
        public void btn_yallow(View view) {

            ViewNotes.setDrawingCacheEnabled(true);
            mSignature.save(ViewNotes, StoredPath);
            loadimage();
            mSignature.clear();
            mSignature.paint.setColor(Color.YELLOW);
            ResetFoundSize();
        }

        public void btn_Blue(View view) {

            ViewNotes.setDrawingCacheEnabled(true);
            mSignature.save(ViewNotes, StoredPath);
            loadimage();
            mSignature.clear();
            mSignature.paint.setColor(Color.BLUE);
            ResetFoundSize();
        }

        public void btn_Green(View view) {
            ViewNotes.setDrawingCacheEnabled(true);
            mSignature.save(ViewNotes, StoredPath);
            loadimage();
            mSignature.clear();
            mSignature.paint.setColor(Color.GREEN);
            ResetFoundSize();
        }

        public void btn_Black(View view) {

            ViewNotes.setDrawingCacheEnabled(true);
            mSignature.save(ViewNotes, StoredPath);
            loadimage();
            mSignature.clear();
            mSignature.paint.setColor(Color.BLACK);
            ResetFoundSize();
        }
        private  void loadimage (){
            TextView  textView129 = (TextView)findViewById(R.id.textView129);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            imgSig = (ImageView) findViewById(R.id.imgSig);
            File   imgFile = new  File("//sdcard//Android/Cv_Images/Salreturn_Sig/" +getIntent().getStringExtra("OrderNo") + ".png");
            if (!imgFile.exists()) {

                mSignature.setBackgroundColor(Color.WHITE);
            }else{
                imgFile = new  File("//sdcard//Android/Cv_Images/Salreturn_Sig/"   +getIntent().getStringExtra("OrderNo") + ".png");
                try {
                    mSignature.setBackgroundColor(Color.WHITE);

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Drawable backgroundImage = new BitmapDrawable(myBitmap);
                    ViewNotes.setBackgroundDrawable(backgroundImage);
                    mSignature.setBackgroundDrawable(backgroundImage);
                    imgSig.setImageBitmap(myBitmap);
                    textView129.setVisibility(View.INVISIBLE);

                }
                catch (Exception ex){
                    mSignature.setBackgroundColor(Color.WHITE);
                }
            }


        }

        public void Do_PRINT(View view) {
            mButton.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
            mButton.setTextColor(getResources().getColor(R.color.Blue));
            LinearLayout lay = (LinearLayout) findViewById(R.id.Invoice_Header);
            PrintReport_TSC obj = new PrintReport_TSC(Convert_Sal_Return_To_ImgActivity_Line.this,
                    Convert_Sal_Return_To_ImgActivity_Line.this,lay, 570, 1);



            lay = (LinearLayout) findViewById(R.id.Invoice_Header);
            obj.StoreHeader(lay);

            lay  = (LinearLayout) findViewById(R.id.Sal_ItemSLayout);
            if(lay.getHeight()==0) {
                lay  = (LinearLayout) findViewById(R.id.Sal_ItemSLayout_emp);
            }
            obj.StoreContent(lay, "z2.jpg");


            lay  = (LinearLayout) findViewById(R.id.Sal_ItemSLayout1);
            if(lay.getHeight()==0) {
                lay  = (LinearLayout) findViewById(R.id.Sal_ItemSLayout_emp);
            }
            obj.StoreContent(lay,"z21.jpg");


            lay  = (LinearLayout) findViewById(R.id.Sal_ItemSLayout2);
            if(lay.getHeight()==0) {
                lay  = (LinearLayout) findViewById(R.id.Sal_ItemSLayout_emp);
            }
            obj.StoreContent(lay,"z22.jpg");


            lay    = (LinearLayout) findViewById(R.id.Invoice_Footer);
            obj.StoreFooter(lay);


            obj.DoPrint2Img();


        }

        public class signature extends View {

            private static final float STROKE_WIDTH = 5f;
            private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
            private Paint paint = new Paint();
            private Path path = new Path();

            private float lastTouchX;
            private float lastTouchY;
            private final RectF dirtyRect = new RectF();

            public signature(Context context, AttributeSet attrs) {
                super(context, attrs);
                paint.setAntiAlias(true);
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeWidth(STROKE_WIDTH);
            }

            @SuppressLint("WrongThread")
            public void save(View v, String StoredPath) {
                Log.v("log_tag", "Width: " + v.getWidth());
                Log.v("log_tag", "Height: " + v.getHeight());
                if (bitmap == null) {
                    bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
                }
                Canvas canvas = new Canvas(bitmap);
                try {
                    // Output the file
                    FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                    v.draw(canvas);

                    // Convert the output file to Image such as .png
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                    mFileOutStream.flush();
                    mFileOutStream.close();

                } catch (Exception e) {
                    Log.v("log_tag", e.toString());
                }

            }

            public void clear() {
                path.reset();
                invalidate();
            }

            @Override
            protected void onDraw(Canvas canvas) {
                canvas.drawPath(path, paint);
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                float eventX = event.getX();
                float eventY = event.getY();
                SaveImg = 1 ;
                //mGetSign.setEnabled(true);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        path.moveTo(eventX, eventY);
                        lastTouchX = eventX;
                        lastTouchY = eventY;
                        return true;

                    case MotionEvent.ACTION_MOVE:

                    case MotionEvent.ACTION_UP:

                        resetDirtyRect(eventX, eventY);
                        int historySize = event.getHistorySize();
                        for (int i = 0; i < historySize; i++) {
                            float historicalX = event.getHistoricalX(i);
                            float historicalY = event.getHistoricalY(i);
                            expandDirtyRect(historicalX, historicalY);
                            path.lineTo(historicalX, historicalY);
                        }
                        path.lineTo(eventX, eventY);
                        break;

                    default:
                        debug("Ignored touch event: " + event.toString());
                        return false;
                }

                invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                        (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                        (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                        (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

                lastTouchX = eventX;
                lastTouchY = eventY;

                return true;
            }

            private void debug(String string) {

                Log.v("log_tag", string);

            }

            private void expandDirtyRect(float historicalX, float historicalY) {
                if (historicalX < dirtyRect.left) {
                    dirtyRect.left = historicalX;
                } else if (historicalX > dirtyRect.right) {
                    dirtyRect.right = historicalX;
                }

                if (historicalY < dirtyRect.top) {
                    dirtyRect.top = historicalY;
                } else if (historicalY > dirtyRect.bottom) {
                    dirtyRect.bottom = historicalY;
                }
            }

            private void resetDirtyRect(float eventX, float eventY) {
                dirtyRect.left = Math.min(lastTouchX, eventX);
                dirtyRect.right = Math.max(lastTouchX, eventX);
                dirtyRect.top = Math.min(lastTouchY, eventY);
                dirtyRect.bottom = Math.max(lastTouchY, eventY);
            }
        }
        public  void ShowRecord( String OrdNo){


            TextView tv_cusnm =(TextView)findViewById(R.id.tv_cusname);
            TextView et_Date =(TextView)findViewById(R.id.ed_date);
            TextView tv_Disc_Percent =(TextView)findViewById(R.id.tv_Disc_Percent);
            TextView tv_NetTotal =(TextView)findViewById(R.id.tv_NetTotal);
            TextView tv_TotalTax =(TextView)findViewById(R.id.tv_TotalTax);
            TextView tv_Total =(TextView)findViewById(R.id.tv_Total);
            TextView tv_CusBal =(TextView)findViewById(R.id.tv_CusBal);
            TextView tv_Seq =(TextView)findViewById(R.id.tv_Seq);




            String accno="";



            String q = "Select distinct ifnull(s.Seq,'') as  Seq,  s.Total , s.Nm, s.Orderno,s.Net_Total,s.Tax_Total ,s.acc ,s.date , c.name  " +
                    "    from  Sal_return_Hdr s left join Customers c on c.no =s.acc   where s.Orderno = '"+OrdNo+"'";

            SqlHandler sqlHandler = new SqlHandler(this);

            Cursor c1 = sqlHandler.selectQuery(q);
            if (c1 != null && c1.getCount() != 0) {
                if (c1.moveToFirst()) {
                    et_Date.setText(c1.getString(c1.getColumnIndex("date")));
                    tv_cusnm.setText(c1.getString(c1.getColumnIndex("name")));
                    tv_NetTotal.setText(c1.getString(c1.getColumnIndex("Net_Total")));
                    tv_TotalTax.setText(c1.getString(c1.getColumnIndex("Tax_Total")));
                    //tv_Seq.setText(c1.getString(c1.getColumnIndex("Seq")));
                    accno = c1.getString(c1.getColumnIndex("acc"));
                    tv_Total.setText(c1.getString(c1.getColumnIndex("Total")));
                }
                c1.close();
            }
            String DisPerFromHdr="-1";

            DisPerFromHdr = DB.GetValue(this,"Sal_return_Det","DisPerFromHdr","Orderno='"+OrdNo+"'");
            if(DisPerFromHdr.equalsIgnoreCase("-1")){
                DisPerFromHdr="0.0";
            }
            //tv_Disc_Percent.setText(  DisPerFromHdr+" %");



            showList(OrdNo);


            String NetTotal =   DB.GetValue(Convert_Sal_Return_To_ImgActivity_Line.this, "Customers", "CUST_NET_BAL", "no ='" +accno + "' ");


            if (NetTotal.equalsIgnoreCase("-1"))
            {
                NetTotal="غير مدخله";
            }


            q = "  Select distinct    ifnull( sum(ifnull(RecVoucher.Amnt,0.000)),0.000)     as Amt   from RecVoucher " +
                    " where  RecVoucher.CustAcc ='" + accno + "' and  RecVoucher.Post ='-1'";

            String UnpostedRecVoucher ="0.000";;
            c1 = sqlHandler.selectQuery(q);
            if (c1 != null && c1.getCount() != 0) {
                if (c1.moveToFirst()) {
                    UnpostedRecVoucher = c1.getString(c1.getColumnIndex("Amt"));
                }
                c1.close();
            }



            q = "Select distinct   ifnull( sum(ifnull(s.Net_Total,0.000)),0.000)   as Amt    " +
                    "  from  Sal_return_Hdr s where " +
                    "  where s.Orderno = '"+OrdNo+"'";

            String UnpostedSales="0.000";
            Cursor cc = sqlHandler.selectQuery(q);
            if (cc != null && cc.getCount() != 0) {
                if (cc.moveToFirst()) {
                    UnpostedSales = cc.getString(cc.getColumnIndex("Amt"));
                }
                cc.close();
            }
            Double Total =  SToD(NetTotal)+ SToD(UnpostedSales)-SToD(UnpostedRecVoucher ) ;
            //   Toast.makeText(this,NetTotal +"   "   + UnpostedRecVoucher +"       " +UnpostedSales +" ", Toast.LENGTH_SHORT).show();


            tv_CusBal.setText( SToD(Total+"")+"");

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
        private void showList(String Orderno) {
            Intent i = getIntent();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            final String Company = sharedPreferences.getString("CompanyID", "1") ;

            ArrayList<ContactListItems> contactList = new ArrayList<ContactListItems>();
            contactList.clear();
            sqlHandler = new SqlHandler(this);
            String query = "SELECT  distinct sid.itemNo,sid.OrgPrice ,sid.price, u.UnitName  ,sid.Tax_Amt ,   ( ifnull(sid.qty,0)) as qty,  invf.Item_Name  ,  sid.total    " +
                    "  FROM Sal_return_Det  sid  Left Join Unites u on u.Unitno =sid.unitNo " +
                    "Left Join invf on  invf.Item_No=sid.itemNo  where sid.Orderno =  '"+  i.getStringExtra("Orderno").toString()+"'";

            Double Pro = 0.0 ;
            Double Dis_Amt = 0.0 ;
            Cursor c1 = sqlHandler.selectQuery(query);
            if (c1 != null && c1.getCount() != 0) {
                if (c1.moveToFirst()) {
                    do {
                        ContactListItems contactListItems = new ContactListItems();

                        contactListItems.setno(c1.getString(c1
                                .getColumnIndex("ItemNo")));
                        contactListItems.setName(c1.getString(c1
                                .getColumnIndex("Item_Name")));
                        contactListItems.setprice(c1.getString(c1
                                .getColumnIndex("price")));
                        contactListItems.setQty(c1.getString(c1
                                .getColumnIndex("qty")));
                        contactListItems.setUnite(c1.getString(c1
                                .getColumnIndex("UnitName")));
                        contactListItems.setTax(c1.getString(c1
                                .getColumnIndex("Tax_Amt")));
                        contactListItems.setTotal(c1.getString(c1
                                .getColumnIndex("Total")));
                        contactList.add(contactListItems);



                    } while (c1.moveToNext());


                }
                c1.close();
            }

            LinearLayout Sal_ItemSLayout = (LinearLayout) findViewById(R.id.Sal_ItemSLayout);
            LinearLayout Sal_ItemSLayout1 = (LinearLayout) findViewById(R.id.Sal_ItemSLayout1);
            LinearLayout Sal_ItemSLayout2 = (LinearLayout) findViewById(R.id.Sal_ItemSLayout2);

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;

            TextView tv_no;
            TextView tv_name;
            TextView tv_Price;
            TextView tv_Qty;
            TextView tv_Unit;
            TextView tv_total;
            TextView tv_tax;

            TextView tv_itemCount =(TextView)findViewById(R.id.tv_itemCount);
            tv_itemCount.setText(contactList.size()+"");

            int count = 0;

            for (ContactListItems Obj : contactList){
                count =count +1 ;
                view = inflater.inflate(R.layout.sal_return_row_tab_10, null);

                tv_no = (TextView) view.findViewById(R.id.tv_no);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_Qty = (TextView) view.findViewById(R.id.tv_Qty);
                tv_Price = (TextView) view.findViewById(R.id.tv_Price2);
                tv_Unit = (TextView) view.findViewById(R.id.tv_Unit);
                tv_total = (TextView) view.findViewById(R.id.tv_total);
                tv_tax = (TextView) view.findViewById(R.id.tv_tax);

                tv_no.setText(Obj.getno());
                tv_name.setText(Obj.getName());
                tv_Price.setText(Obj.getprice());
                tv_Qty.setText(Obj.getQty());
                tv_Unit.setText(Obj.getUnite() );
                tv_total.setText(Obj.getTotal());
                tv_tax.setText(Obj.getTax());

                if( count < 20) {
                    Sal_ItemSLayout.addView(view);
                }else if(count <40){
                    Sal_ItemSLayout1.addView(view);
                }else{
                    Sal_ItemSLayout2.addView(view);
                }
            }


            FillCustomerMsg();

        }

        private  void FillCustomerMsg(){
            String q ;
            q = " Select  distinct    msg   from  CustomersMsg Where SaleManFlg ='4' ";// Where  SaleManFlg =4 Cusno = '' ";
            Cursor c1 = sqlHandler.selectQuery(q);
            ArrayList<Cls_InvoiceMsg> MsgList = new ArrayList<Cls_InvoiceMsg>();
            MsgList.clear();
            if (c1 != null && c1.getCount() != 0) {

                if (c1.moveToFirst()) {
                    do {
                        Cls_InvoiceMsg obj = new Cls_InvoiceMsg();
                        obj.setMsg(c1.getString(c1
                                .getColumnIndex("msg")));
                        MsgList.add(obj);
                    } while (c1.moveToNext());
                }
                c1.close();
            }

            LinearLayout  MsgLayout = (LinearLayout) findViewById(R.id.MsgLayout);

            LayoutInflater Msg_inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View  Msg_view;
            TextView tv_Msg;
            for (Cls_InvoiceMsg Obj : MsgList){
                try {
                    Msg_view = Msg_inflater.inflate(R.layout.msg_row_new, null);
                    tv_Msg = (TextView) Msg_view.findViewById(R.id.tv_Msg);
                    tv_Msg.setText(Obj.getMsg());
                    MsgLayout.addView(Msg_view);
                }catch ( Exception ex   ){}
            }

        }

        public void btn_back(View view) {
            Intent i =  new Intent(this, Sale_ReturnActivity.class);
            startActivity(i);
        }
    }


