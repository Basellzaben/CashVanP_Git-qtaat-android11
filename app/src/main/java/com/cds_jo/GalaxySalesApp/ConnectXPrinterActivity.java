package com.cds_jo.GalaxySalesApp;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.BitmapCallback;
import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterPos80;
import net.posprinter.utils.PosPrinterDev;
import java.util.ArrayList;
import java.util.List;

public class ConnectXPrinterActivity extends FragmentActivity {
    public static String DISCONNECT="com.posconsend.net.disconnetct";
    public static IMyBinder binder;
    private Bitmap b1;
    private Bitmap b2;

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
        setContentView(R.layout.connectxprinteractivity);
        //bind service，get ImyBinder object
        Intent intent=new Intent(this,PosprinterService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");

        netReciever=new Receiver();
        registerReceiver(netReciever,new IntentFilter(ConnectXPrinterActivity.DISCONNECT));
        Tiny.getInstance().init(getApplication());

        BTCon= (Button) findViewById(R.id.buttonConnect);
        BTCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connetBle();
            }
        });

    }

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
                        BTCon.setText(getString(R.string.con_success));
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
    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (action.equals(ConnectXPrinterActivity.DISCONNECT)){
                Message message=new Message();
                message.what=4;
                handler.handleMessage(message);
            }
        }
    }
    private void CallPrint(){    Bitmap myBitmap = null;
         myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");
         Printz(myBitmap);
     }
    protected void Printz (final Bitmap bitmap) {



        Log.e("test","test2");

        try{


            b1=convertGreyImg(bitmap);
            Message message=new Message();
            message.what=1;
            handler.handleMessage(message);

            //compress the bitmap
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
                    if (PosPrinterDev.PortType.USB!=ConnectXPrinterActivity.portType){
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

            ConnectXPrinterActivity.binder.writeDataByYouself(new UiExecute() {
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
            ConnectXPrinterActivity.binder.writeDataByYouself(new UiExecute() {
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


        ConnectXPrinterActivity.binder.writeDataByYouself(new UiExecute() {
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
    @Override
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
    }
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
