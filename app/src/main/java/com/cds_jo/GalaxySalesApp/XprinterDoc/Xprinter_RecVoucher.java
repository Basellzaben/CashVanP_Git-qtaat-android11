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
import android.graphics.Matrix;
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

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.RecvVoucherActivity;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.Cls_Check;
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
import java.util.Locale;



public class Xprinter_RecVoucher extends FragmentActivity {
    public static String DISCONNECT="com.posconsend.net.disconnetct";
    public static IMyBinder binder;
    private Bitmap b1;
    private Bitmap b2;
    SqlHandler sqlHandler ;
    ListView lvCustomList;
    private Button mButton;
    private Context context;
    View ReportView;
    ImageView img_Logo;
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
        setContentView(R.layout.xprinter_recvoucher);
        //bind service，get ImyBinder object
        Intent intent=new Intent(this,PosprinterService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");

        netReciever=new Receiver();
        registerReceiver(netReciever,new IntentFilter(Xprinter_RecVoucher.DISCONNECT));
        Tiny.getInstance().init(getApplication());

        BTCon= (Button) findViewById(R.id.btn_Print);
        BTCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connetBle();
            }
        });


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
        View test1View = findViewById(R.id.f_view);

        String u =  sharedPreferences.getString("UserName", "");
        TextView    tv_UserNm  = (TextView)findViewById(R.id.tv_UserNm);
        tv_UserNm.setText(u);


        TextView tv_CompName =(TextView)findViewById(R.id.tv_CompName);
        tv_CompName.setText(sharedPreferences.getString("CompanyNm", ""));

        final String PrinterType = sharedPreferences.getString("PrinterType", "1") ;
        String OrderNo = getIntent().getStringExtra("OrderNo");


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


        ShowRecord (OrderNo);


    }
///////////////////////////////////////////////////////////////////////
    public  void ShowRecord( String OrdNo){
    TextView OrderNo = (TextView)findViewById(R.id.et_OrdeNo);
    TextView amt =(TextView)findViewById(R.id.et_Amt);
    TextView etNote =(TextView)findViewById(R.id.et_notes);
    TextView et_Date =(TextView)findViewById(R.id.et_Date);
    TextView tv_acc =(TextView)findViewById(R.id.tv_acc);
    TextView tv_cusnm =(TextView)findViewById(R.id.tv_cusnm);
    TextView cash =(TextView)findViewById(R.id.tv_Cash);
    TextView tv_Cur =(TextView)findViewById(R.id.tv_Cur);
    TextView tv_type =(TextView)findViewById(R.id.tv_type);
    TextView tv_Checktotal =(TextView)findViewById(R.id.tv_Checktotal);
    TextView tv_Checktotal1 =(TextView)findViewById(R.id.tv_Checktotal1);

    TextView tv_Seq =(TextView)findViewById(R.id.tv_Seq);
    LinearLayout lytCheck1 = (LinearLayout) findViewById(R.id.lytCheck1);
    LinearLayout lytCheck2 = (LinearLayout) findViewById(R.id.lytCheck2);

    OrderNo.setText(OrdNo);


    String q ="Select  ifnull(rc.Seq,'') as  Seq,  ifnull(CheckTotal,0) as CheckTotal ,   ifnull( rc.cash ,0) as Cash, rc.VouchType,rc.curno  ,curf.cur    ,  rc.Desc,rc.Amnt,rc.TrDate,rc.CustAcc  ,c.name from RecVoucher rc   " +
            "left join Customers c on c.no = rc.CustAcc   Left join curf on curf.cur_no = rc.curno " +
            "where rc.DocNo = '"+ OrdNo.replaceAll("[^\\d.]", "") +"'";
    SqlHandler sqlHandler = new SqlHandler(this);

    Cursor c1 = sqlHandler.selectQuery(q);
    if (c1 != null && c1.getCount() != 0) {
        if (c1.moveToFirst()) {
            etNote.setText(c1.getString(c1.getColumnIndex("Desc")));
            amt.setText(c1.getString(c1.getColumnIndex("Amnt")));
            et_Date.setText(c1.getString(c1.getColumnIndex("TrDate")));
            tv_acc.setText(c1.getString(c1.getColumnIndex("CustAcc")));
            tv_cusnm.setText(c1.getString(c1.getColumnIndex("name")));
            cash.setText(c1.getString(c1.getColumnIndex("Cash")));
            tv_Cur.setText(c1.getString(c1.getColumnIndex("cur")));
            tv_Checktotal.setText(c1.getString(c1.getColumnIndex("CheckTotal")));

            LinearLayout.LayoutParams lp =  new  LinearLayout.LayoutParams(0,0);

            if (c1.getString(c1.getColumnIndex("VouchType")).equals("1")){
                tv_type.setText("نقدي");
                lytCheck1.setVisibility(View.INVISIBLE);
                lytCheck1.setLayoutParams(lp);
                lytCheck2.setVisibility(View.INVISIBLE);
                lytCheck2.setLayoutParams(lp);
                tv_Checktotal.setVisibility(View.INVISIBLE);
                tv_Checktotal1.setVisibility(View.INVISIBLE);
            }
            else if (c1.getString(c1.getColumnIndex("VouchType")).equals("2")){
                tv_type.setText("شيكات");
            }

            else if (c1.getString(c1.getColumnIndex("VouchType")).equals("3")){
                tv_type.setText(" نقدي + شيكات");
            }

            tv_Seq.setText(c1.getString(c1.getColumnIndex("Seq")));
        }


        c1.close();
    }
    showList();

}
    private void showList( ) {

        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
        ArrayList<Cls_Check> contactList = new ArrayList<Cls_Check>();
        contactList.clear();
        sqlHandler = new SqlHandler(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String Company = sharedPreferences.getString("CompanyID", "1") ;


        String query ="Select rc.CheckNo,rc.CheckDate,rc.BankNo, rc.Amnt  , b.Bank from  RecCheck rc  left join banks b on b.bank_num = rc.BankNo where DocNo ='"+ DocNo.getText().toString().replaceAll("[^\\d.]", "")+"'";
        Integer i = 1;
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Check cls_check_obj = new Cls_Check();
                    cls_check_obj.setSer(i);
                    cls_check_obj.setCheckNo(c1.getString(c1
                            .getColumnIndex("CheckNo")));
                    cls_check_obj.setCheckDate(c1.getString(c1
                            .getColumnIndex("CheckDate")));
                    cls_check_obj.setBankName(c1.getString(c1
                            .getColumnIndex("Bank")));
                    cls_check_obj.setAmnt(c1.getString(c1
                            .getColumnIndex("Amnt")));
                    contactList.add(cls_check_obj);
                    i++;

                } while (c1.moveToNext());


            }


            c1.close();
        }

        LinearLayout ChecksLayout = (LinearLayout) findViewById(R.id.ChecksLayout);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;

        TextView tv_Amnt;
        TextView tv_BankNm;
        TextView tv_CheckDate;
        TextView tv_Checkno;
        TextView Chkser;

        for (Cls_Check Obj : contactList){


            view = inflater.inflate(R.layout.check_print_list_row ,null);
            if  (Company.equals("2")) {
                view = inflater.inflate(R.layout.check_print_list_row_1, null);
            }



            tv_Amnt = (TextView) view.findViewById(R.id.tv_Amnt);
            tv_BankNm = (TextView) view.findViewById(R.id.tv_BankNm);
            tv_CheckDate = (TextView) view.findViewById(R.id.tv_CheckDate);
            tv_Checkno = (TextView) view.findViewById(R.id.tv_Checkno);
            Chkser = (TextView) view.findViewById(R.id.ser);
            try {
                tv_Amnt.setText(Obj.getAmnt());
                tv_BankNm.setText(Obj.getBankName());
                tv_CheckDate.setText(Obj.getCheckDate());
                tv_Checkno.setText(Obj.getCheckNo());
                Chkser.setText(Obj.getSer() + "");
                ChecksLayout.addView(view);
            }

            catch (Exception ex){

            }

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
        Intent i = new Intent(this , RecvVoucherActivity.class);
      //  startActivity(i);
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(this , RecvVoucherActivity.class);
     //   startActivity(i);
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (action.equals(Xprinter_RecVoucher.DISCONNECT)){
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
        BTCon.setText("طباعة");
        finish();
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
                    if (PosPrinterDev.PortType.USB!= Xprinter_RecVoucher.portType){
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

            Xprinter_RecVoucher.binder.writeDataByYouself(new UiExecute() {
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
            Xprinter_RecVoucher.binder.writeDataByYouself(new UiExecute() {
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


        Xprinter_RecVoucher.binder.writeDataByYouself(new UiExecute() {
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
    protected void onDestroy() {        super.onDestroy();
    binder.disconnectCurrentPort(new UiExecute() {
            @Override
            public void onsucess() {

            }

            @Override
            public void onfailed() {

            }
        });
        unbindService(conn);
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
