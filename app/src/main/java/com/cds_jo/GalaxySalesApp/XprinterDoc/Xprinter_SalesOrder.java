package com.cds_jo.GalaxySalesApp.XprinterDoc;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.ContactListItems;
import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.Cls_Check;
import com.cds_jo.GalaxySalesApp.assist.ESCPSample3;
import com.cds_jo.GalaxySalesApp.assist.OrdersItems;
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
import java.util.Date;
import java.util.List;
public class Xprinter_SalesOrder extends FragmentActivity {
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
        setContentView(R.layout.xprinter_po);
        //bind service，get ImyBinder object
        Intent intent=new Intent(this,PosprinterService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");

        netReciever=new Receiver();
        registerReceiver(netReciever,new IntentFilter(Xprinter_SalesOrder.DISCONNECT));
        Tiny.getInstance().init(getApplication());

        BTCon= (Button) findViewById(R.id.btn_Print);
        BTCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connetBle();
            }
        });
        Intent i = getIntent();
        File imgFile = new  File("//sdcard/Android/Cv_Images/logo.jpg");
        img_Logo = (ImageView) findViewById(R.id.img_Logo);
        imgSig = (ImageView) findViewById(R.id.imgSig);
        imgFile = new  File("//sdcard//Android/Cv_Images/Po_Sig/"+i.getStringExtra("OrderNo").toString() +".png");
        try {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgSig.setImageBitmap(myBitmap);
            }
        }
        catch (Exception ex){}
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
///////////////////////////////////////////////////////////////////////
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

    TextView textView58 =(TextView)findViewById(R.id.textView58);



    textView5.setBackground(shape);
    textView7.setBackground(shape);
    textView8.setBackground(shape);
    textView9.setBackground(shape);
    textView43.setBackground(shape);



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
      //  TextView tv_tax;
        TextView tv_total;

        for (ContactListItems Obj : contactList) {
            view = inflater.inflate(R.layout.po_print_row_x, null);
            tv_no = (TextView) view.findViewById(R.id.tv_no);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_Price = (TextView) view.findViewById(R.id.tv_Price);
            tv_Qty = (TextView) view.findViewById(R.id.tv_Qty);
            tv_Unit = (TextView) view.findViewById(R.id.tv_Unit);
           // tv_tax = (TextView) view.findViewById(R.id.tv_tax);
            tv_total = (TextView) view.findViewById(R.id.tv_total);


            tv_no.setText(Obj.getno());
            tv_name.setText(Obj.getName());
            tv_Price.setText(Obj.getprice());
            tv_Qty.setText(Obj.getQty());
            tv_Unit.setText(Obj.getUniteNm() + "");
           // tv_tax.setText(Obj.getTax() + "");
            tv_total.setText(Obj.getTotal());
            ChecksLayout.addView(view);

        }
    }
///////////////////////////////////////////////////////////////////////
    private void connetBle(){
        try {

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
            if (action.equals(Xprinter_SalesOrder.DISCONNECT)){
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
                    if (PosPrinterDev.PortType.USB!= Xprinter_SalesOrder.portType){
                        printpicCode(b2);
                    }else {
                        showSnackbar("bimap  "+b2.getWidth()+"  height: "+b2.getHeight());
                        b2=resizeImage(b2,576,false);
                        printUSBbitamp(b2);

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

            Xprinter_SalesOrder.binder.writeDataByYouself(new UiExecute() {
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
            Xprinter_SalesOrder.binder.writeDataByYouself(new UiExecute() {
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


        Xprinter_SalesOrder.binder.writeDataByYouself(new UiExecute() {
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
