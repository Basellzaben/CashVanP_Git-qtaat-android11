package com.cds_jo.GalaxySalesApp.assist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.tscdll.TSCActivity;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.example.tscdll.TSCActivity;

/**
 * Created by Hp on 30/05/2016.
 */
public class PrintReport_TSC {
    Context context;
    Activity Activity;
    String BPrinter_MAC_ID;
    View ReportView;
    float ImageCountFactor;
    Bitmap bm2=null, bm1=null,bm3 ;
    TSCActivity TscDll = new TSCActivity();
    int h;
    Bitmap Empty_bitmap = null;
    Connection connection;
    PrintReport_TSC(Context _context, Activity _Activity,
                    View _ReportView, int _PageWidth, float _ImageCountFactor) {
        context = _context;
        Activity = _Activity;

        BPrinter_MAC_ID = "00:19:0E:A3:5D:58";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");
        ReportView = _ReportView;
        //PageWidth = _PageWidth;
        ImageCountFactor = _ImageCountFactor;

    }

    public  void DoPrint(){
        StoreImage();
        Bitmap myBitmap = null;
        myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");

        Toast.makeText(context  ,"العمل جاري على طباعة الملف",Toast.LENGTH_SHORT ).show();

       PrintImage(myBitmap);
       // PrintImage_new(myBitmap);

    }
    public  void DoPrint1(){
        StoreImage1();
        Bitmap myBitmap1 = null;

        myBitmap1= BitmapFactory.decodeFile("//sdcard//Download/Zain.jpg");

        Toast.makeText(context  ,"جاري تحميل الملف  000",Toast.LENGTH_SHORT ).show();

       PrintImage_new( myBitmap1);


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
    private  void StoreImage1(){


        Bitmap b = loadBitmapFromView(ReportView);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "Zain.jpg";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd.getPath()+"/Download", filename);

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

    public void PrintImage_new(final Bitmap bitmap) {

 try {
     File sd = Environment.getExternalStorageDirectory();
     File dest = new File(sd.getPath()+"/Download", "Zain.jpg");
     TscDll.openport(BPrinter_MAC_ID);
     //TscDll.downloadpcx("UL.PCX");
       TscDll.downloadbmp("Zain.jpg");
     // TscDll.sendcommand("PUTBMP 100,520,\"Zain.jpg\"\n");
     TscDll.setup(70, 110, 4, 4, 0, 0, 0);

     TscDll.clearbuffer();
    // TscDll.sendcommand("PUTBMP 19,15,\"Zain.BMP\"");

     // TscDll.sendcommand("SET TEAR ON\n");
     // TscDll.sendcommand("SET COUNTER @1 1\n");
     // TscDll.sendcommand("@1 = \"0001\"\n");
     // TscDll.sendcommand("TEXT 100,300,\"3\",0,1,1,@1\n");
     // TscDll.sendcommand("PUTPCX 100,300,\"UL.PCX\"\n");
     // TscDll.sendcommand("PUTBMP 100,520,\"Zain.jpg\"\n");
       TscDll.sendbitmap(0, 0, TscDll.bitmap2Gray(bitmap));

  //   TscDll.sendbitmap(0, 0, bitmap);

     // TscDll.sendpicture(0,0,"//sdcard//z1.jpg");

      String status = TscDll.printerstatus();
     TscDll.printlabel(1, 1);
     TscDll.closeport(5000);

 }catch ( Exception ex){
     Toast.makeText(context,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
 }
    }
  private void PrintImage(final Bitmap bitmap) {

      if ( bitmap.getHeight()>1707) {
          bm1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), 1707);
          bm2 = Bitmap.createBitmap(bitmap, 0, 1707, bitmap.getWidth(), bitmap.getHeight()-1707  );
          try {

          String filename = "z1.jpg";
          File sd = Environment.getExternalStorageDirectory();
          File dest = new File(sd, filename);


          if(bm1!=null) {
              FileOutputStream   out = new FileOutputStream(dest);
              filename = "z1.jpg";
              dest = new File(sd, filename);
              out = new FileOutputStream(dest);
              bm1.compress(Bitmap.CompressFormat.JPEG, 100, out);

          }

          if(bm2!=null) {
              FileOutputStream   out = new FileOutputStream(dest);
                  filename = "z1.jpg";
                  dest = new File(sd, filename);
                  out = new FileOutputStream(dest);
                  bm2.compress(Bitmap.CompressFormat.JPEG, 100, out);

           }
          } catch (Exception e) {
              Toast.makeText(context, e.getMessage() + "  Exception   ", Toast.LENGTH_SHORT).show();
          }


          new Thread(new Runnable() {
              public void run() {
                  try {


                      Looper.prepare();
                      String BPrinter_MAC_ID;
                      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                      BPrinter_MAC_ID = sharedPreferences.getString("AddressBT", "");

                      Connection connection = new BluetoothConnection(BPrinter_MAC_ID);
                      connection.open();

                     // ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, connection);

                      TscDll.openport(BPrinter_MAC_ID);
                      TscDll.downloadpcx("UL.PCX");
                      TscDll.closeport(5000);
                     /* printer.printImage(new ZebraImageAndroid(bm1), 20, 0, 550, bm1.getHeight(), false);
                      printer.printImage(new ZebraImageAndroid(bm2), 20, 0, 550, bm2.getHeight(), false);*/
                      connection.close();

                  } catch (ConnectionException e) {
                      e.printStackTrace();
                  } finally {
                      Looper.myLooper().quit();
                  }
              }
          }).start();

      }else {

          h = bitmap.getHeight();
          if (h > 1800) {
              h = 1800;
          }
          Toast.makeText(context, h + "  h   ", Toast.LENGTH_SHORT).show();
          new Thread(new Runnable() {
              public void run() {
                  try {


                      Looper.prepare();
                      String BPrinter_MAC_ID;
                      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                      BPrinter_MAC_ID = sharedPreferences.getString("AddressBT", "");

                      Connection connection = new BluetoothConnection(BPrinter_MAC_ID);
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
