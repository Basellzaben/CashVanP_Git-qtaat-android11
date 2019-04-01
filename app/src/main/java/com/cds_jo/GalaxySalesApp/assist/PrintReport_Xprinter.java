package com.cds_jo.GalaxySalesApp.assist;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import net.posprinter.posprinterface.BackgroundInit;
import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;

import com.cds_jo.GalaxySalesApp.ConnectXPrinterActivity;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.BitmapCallback;
import android.content.ServiceConnection;
import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.DataForSendToPrinterPos80;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static android.content.Context.BIND_AUTO_CREATE;


/**
 * Created by Hp on 30/05/2016.
 */
public class PrintReport_Xprinter {
    Bitmap b1;//grey-scale bitmap
    Bitmap b2;//grey-scale bitmap
    Context context;
    Activity Activity;
    String BPrinter_MAC_ID;
    View ReportView;
    float ImageCountFactor;
    Bitmap Empty_bitmap = null;
    Connection connection;
    public static IMyBinder binder;
    public static boolean ISCONNECT;
    PrintReport_Xprinter(Context _context, Activity _Activity,
                         View _ReportView, int _PageWidth, float _ImageCountFactor) {
        context = _context;
        Activity = _Activity;

        Intent intent=new Intent(context,ConnectXPrinterActivity.class);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");
        BPrinter_MAC_ID = "00:19:0E:A3:5D:58";
        ReportView = _ReportView;
        //PageWidth = _PageWidth;
        ImageCountFactor = _ImageCountFactor;
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
        connetBle();
        DoPrint();
    }

    private void connetBle(){
        String bleAdrress="DC:0D:30:25:8C:74";
        if (bleAdrress.equals(null)||bleAdrress.equals("")){

        }else {
            binder.connectBtPort(bleAdrress, new UiExecute() {
                @Override
                public void onsucess() {
                    ISCONNECT=true;
                    binder.write(DataForSendToPrinterPos80.openOrCloseAutoReturnPrintState(0x1f), new UiExecute() {
                        @Override
                        public void onsucess() {
                            binder.acceptdatafromprinter(new UiExecute() {
                                @Override
                                public void onsucess() {

                                }

                                @Override
                                public void onfailed() {
                                    ISCONNECT=false;

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

                    ISCONNECT=false;

                }
            });
        }


    }
    public  void DoPrint(){
        StoreImage();
        Bitmap myBitmap = null;
        myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");

        Toast.makeText(context  ,"العمل جاري على طباعة الملف",Toast.LENGTH_SHORT ).show();
       // connetNet();
        PrintImage(myBitmap);


    }
    private void connetNet(){

            //ipAddress :ip address; portal:9100

            binder.connectNetPort(BPrinter_MAC_ID,9100, new UiExecute() {
                @Override
                public void onsucess() {


                    binder.acceptdatafromprinter(new UiExecute() {
                        @Override
                        public void onsucess() {

                        }

                        @Override
                        public void onfailed() {


                        }
                    });
                }

                @Override
                public void onfailed() {
                    //Execution of the connection in the UI thread after the failure of the connection



                }
            });



    }
    public  void DoPrint1(){
        StoreEmptyImage();
        Bitmap myBitmap1 = null;
        myBitmap1= BitmapFactory.decodeFile("//sdcard//e1.jpg");

        Toast.makeText(context  ,"جاري تحميل الملف  000",Toast.LENGTH_SHORT ).show();

         PrintImageEmpty( myBitmap1);


    }
    private  void StoreEmptyImage(){
        // LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);

        Bitmap b = loadBitmapFromView(ReportView);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "e1.jpg";
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
    private  void StoreImage(){
       // LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);

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
  /*  private void PrintImage(final Bitmap bitmap) {




         new Thread(new Runnable() {
           public void run() {
                 try {
                     Looper.prepare();
                     String BPrinter_MAC_ID;
                     SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                     BPrinter_MAC_ID = sharedPreferences.getString("AddressBT", "");
                     connection = new BluetoothConnection(BPrinter_MAC_ID);

                     connection.open();
                     ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, connection);
                     printer.printImage(new ZebraImageAndroid(bitmap), 20, 0, 550, bitmap.getHeight(), false);
                     connection.close();
                 }catch (ConnectionException eq) {
                     Toast.makeText(context  ,eq.getMessage().toString(),Toast.LENGTH_SHORT ).show();

                } catch (Exception e) {
                    Toast.makeText(context  ,e.getMessage().toString(),Toast.LENGTH_SHORT ).show();
                } finally {

                     Looper.myLooper().quit();
                }
            }
        }).start();

    }*/
  private void PrintImaged(final Bitmap bitmap) {



      new Thread(new Runnable() {
          public void run() {
              try {


                   Looper.prepare();
                  String BPrinter_MAC_ID ;
                  SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                  BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");

                  Connection connection =  new BluetoothConnection(BPrinter_MAC_ID);
                  connection.open();

                  ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, connection);
                  connection.write("! U1 JOURNAL\r\n! U1 SETFF 20 2\r\n".getBytes());
                  printer.printImage(new ZebraImageAndroid(bitmap), 20, 0, 550, bitmap.getHeight(), false);
                   connection.close();

              } catch (ConnectionException e) {
                  e.printStackTrace();
              } finally {
                   Looper.myLooper().quit();
              }
          }
      }).start();

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
        Bitmap mBitmap=Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        mBitmap.setPixels(pixels, 0, width, 0, 0, width, height);



        return mBitmap;
    }

    protected void PrintImage(final Bitmap bitmap) {


            try{

                b1=convertGreyImg(bitmap);
                Message message=new Message();
                message.what=1;
              //  handler.handleMessage(message);

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
                            //handler.handleMessage(message);
                        }


                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }


    }

    public void PrintImageEmpty(final Bitmap bitmap ) {

       new Thread(new Runnable() {

            public void run() {
                try {

                     Looper.prepare();
                    String BPrinter_MAC_ID;
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    BPrinter_MAC_ID = sharedPreferences.getString("AddressBT", "");

                    connection = new BluetoothConnection(BPrinter_MAC_ID);

                    connection.open();
                    connection.write("! U1 JOURNAL\r\n! U1 SETFF 20 2\r\n".getBytes());
                    connection.write("! U1 JOURNAL\r\n! U1 SETFF 20 2\r\n".getBytes());
                    ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, connection);
                    printer.printImage(new ZebraImageAndroid(bitmap), 20, 0, 550, bitmap.getHeight(), false);
                     connection.close();
                }catch (ConnectionException eq) {
                    Toast.makeText(context  ,eq.getMessage().toString(),Toast.LENGTH_SHORT ).show();

                } catch (Exception e) {
                    Toast.makeText(context  ,e.getMessage().toString(),Toast.LENGTH_SHORT ).show();
                } finally {

                   Looper.myLooper().quit();
                }
            }
       }).start();

    }
}
