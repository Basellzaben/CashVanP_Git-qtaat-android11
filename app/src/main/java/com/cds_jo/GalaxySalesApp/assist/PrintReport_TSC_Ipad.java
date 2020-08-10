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
import android.util.Log;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Hp on 30/05/2016.
 */
public class PrintReport_TSC_Ipad {
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
    public PrintReport_TSC_Ipad(Context _context, Activity _Activity,
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
    public  void StoreContent(View v,String file_name){
        Bitmap b = loadBitmapFromView(v);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = file_name ;
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

    public  void StoreFooter(View v){
        Bitmap b = loadBitmapFromView(v);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "z3.jpg";
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
    public  void DoLongPage(){
        StoreImage();
        Bitmap myBitmap = null;
        myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");
        Toast.makeText(context  ,"العمل جاري على طباعة الملف",Toast.LENGTH_SHORT ).show();
        PrintLongImage(myBitmap);
    }
    public  void DoPrint(){
        StoreImage();
        Bitmap myBitmap = null;
        myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");
        Toast.makeText(context  ,"العمل جاري على طباعة الملف",Toast.LENGTH_SHORT ).show();
         PrintImage(myBitmap);
    }

    public  void SendSmallImage(final Bitmap bitmap){
        try {
            double INCH;
            TscDll.clearbuffer();
            INCH = (bitmap.getHeight() / 200);
            TscDll.openport(BPrinter_MAC_ID);
            TscDll.sendcommand("SIZE 3," + String.valueOf((int) INCH) + "\n");
            TscDll.clearbuffer();
            //TscDll.sendpicture(0, 0, Environment.getExternalStorageDirectory().getPath() + "/z3.jpg");
            TscDll.sendbitmap(0, 0, bitmap);

            TscDll.printlabel(1, 1);
            TscDll.closeport(5000);
        }catch (Exception d){}
    }

    public  void SendLongImage(final Bitmap bitmap){
        double  INCH,Margin;
        TscDll.clearbuffer();

        Margin=0.0;

        if (bitmap.getHeight()>700)  {//10 Rows
              Margin=0.9;
        }else if(bitmap.getHeight()>630)   {//9 Rows
            Margin = 0.2;//0.35;
        } else if (bitmap.getHeight()>550) {//8 Rows
            Margin = 0.9;
        }else if (bitmap.getHeight()>470)  {//7 Rows
            Margin = 0.5;
        } else if (bitmap.getHeight()>400) {//6 Rows
            Margin = 0.130;
        } else if (bitmap.getHeight()>310) {//5 Rows
            Margin = 0.77;
        } else if (bitmap.getHeight()>275) {//4 Rows
                 Margin = 0.5;
        } else if (bitmap.getHeight()>200) {//3 Rows
                 Margin = 0.3;
        } else if (bitmap.getHeight()>175) {//2 Rows
                 Margin =0.9;
        } else if (bitmap.getHeight()>125) {//1 Row
                 Margin =0.7;
        }else if (bitmap.getHeight()>50) {//1 Row Sal
                 Margin =-0.2;
        } else if (bitmap.getHeight()>20) {//0 Row
           Margin =0;
        }else{
                 Margin =0.5;
             }
        INCH = (bitmap.getHeight()/200)+Margin;
        Log.i("cv", "Height: " + bitmap.getHeight() + "");
        Log.i("cv", "INCH : " + INCH + "");

        TscDll.openport(BPrinter_MAC_ID);
        TscDll.sendcommand("SIZE 3,"+ String.valueOf(INCH) + "\n");
        //  TscDll.sendcommand("SIZE 3,1.8"+ "\n");
        TscDll.clearbuffer();

         TscDll.sendbitmap(0,0,bitmap);

        TscDll.printlabel(1, 1);
        TscDll.closeport(5000);
    }

    public  void SendInvoiceImage(final Bitmap bitmap){
        double  INCH,Margin;
        TscDll.clearbuffer();

        Margin=0.0;

        if (bitmap.getHeight()>700)  {//10 Rows
            Margin=0.9;
        }else if(bitmap.getHeight()>630)   {//9 Rows
            Margin = 0.35;//0.35;
        } else if (bitmap.getHeight()>550) {//8 Rows
            Margin = 0.9;
        }else if (bitmap.getHeight()>470)  {//7 Rows
            Margin = 0.5;
        } else if (bitmap.getHeight()>400) {//6 Rows
            Margin = 0.130;
        } else if (bitmap.getHeight()>310) {//5 Rows
            Margin = 0.77;
        } else if (bitmap.getHeight()>275) {//4 Rows
            Margin = 0.5;
        } else if (bitmap.getHeight()>200) {//3 Rows
            Margin = 0.3;
        } else if (bitmap.getHeight()>175) {//2 Rows
            Margin =0.9;
        } else if (bitmap.getHeight()>125) {//1 Row
            Margin =0.7;
        }else if (bitmap.getHeight()>50) {//1 Row Sal
            Margin =-0.2;
        } else if (bitmap.getHeight()>20) {//0 Row
            Margin =0;
        }else{
            Margin =0.5;
        }
        INCH = (bitmap.getHeight()/200)+Margin;
        Log.i("cv", "Height: " + bitmap.getHeight() + "");
        Log.i("cv", "INCH : " + INCH + "");

        TscDll.openport(BPrinter_MAC_ID);
        TscDll.sendcommand("SIZE 3,"+ String.valueOf(INCH) + "\n");
        //  TscDll.sendcommand("SIZE 3,1.8"+ "\n");
        TscDll.clearbuffer();

        TscDll.sendbitmap(0,0,bitmap);

        TscDll.printlabel(1, 1);
        TscDll.closeport(5000);
    }
    public  void SendReciveVoucherImage(final Bitmap bitmap){
        double  INCH,Margin;
        TscDll.clearbuffer();

        Margin=0.0;

        if (bitmap.getHeight()>700)  {//10 Rows
            Margin=0.9;
        }else if(bitmap.getHeight()>630)   {//9 Rows
            Margin = 0.35;//0.35;
        } else if (bitmap.getHeight()>550) {//8 Rows
            Margin = 0.9;
        }else if (bitmap.getHeight()>470)  {//7 Rows
            Margin = 0.5;
        } else if (bitmap.getHeight()>400) {//6 Rows
            Margin = 0.130;
        } else if (bitmap.getHeight()>310) {//5 Rows
            Margin = 0.77;
        } else if (bitmap.getHeight()>275) {//4 Rows
            Margin = 0.5;
        } else if (bitmap.getHeight()>200) {//3 Rows
            Margin = 0.3;
        } else if (bitmap.getHeight()>175) {//2 Rows
            Margin =0.9;
        } else if (bitmap.getHeight()>125) {//1 Row
            Margin =0.7;
        }else if (bitmap.getHeight()>50) {//1 Row Sal
            Margin =-0.2;
        } else if (bitmap.getHeight()>20) {//0 Row
            Margin =0;
        }else{
            Margin =0.5;
        }
        INCH = (bitmap.getHeight()/200)+Margin;
        Log.i("cv", "Height: " + bitmap.getHeight() + "");
        Log.i("cv", "INCH : " + INCH + "");

        TscDll.openport(BPrinter_MAC_ID);
        TscDll.sendcommand("SIZE 3,"+ String.valueOf(INCH) + "\n");
        //  TscDll.sendcommand("SIZE 3,1.8"+ "\n");
        TscDll.clearbuffer();

        TscDll.sendbitmap(0,0,bitmap);

        TscDll.printlabel(1, 1);
        TscDll.closeport(5000);
    }
    public  void DoPrintSmallImg(){
        StoreImage();
        Bitmap myBitmap = null;
        myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");
        Toast.makeText(context  ,"العمل جاري على طباعة الملف",Toast.LENGTH_SHORT ).show();
        PrintImage(myBitmap);
    }


    public  void StoreHeader(View v){
        Bitmap b = loadBitmapFromView(v);
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
    public  void DoPrint2Img(){


        StoreImage();
        Bitmap myBitmap = null;
        myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");



        Bitmap myBitmap2 = null;
        myBitmap2= BitmapFactory.decodeFile("//sdcard//z2.jpg");

        Bitmap myBitmap21 = null;
        myBitmap21= BitmapFactory.decodeFile("//sdcard//z21.jpg");

         Bitmap myBitmap22 = null;
        myBitmap22= BitmapFactory.decodeFile("//sdcard//z22.jpg");


        Bitmap myBitmap3 = null;
        myBitmap3= BitmapFactory.decodeFile("//sdcard//z3.jpg");

        Toast.makeText(context  ,"العمل جاري على طباعة الملف",Toast.LENGTH_SHORT ).show();


        PrintImage2(myBitmap, myBitmap2,myBitmap21,myBitmap22,myBitmap3);


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
    private  void StoreImage3(){


        Bitmap b = loadBitmapFromView(ReportView);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "z3.jpg";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            b.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private  void StoreImage2(){


        Bitmap b = loadBitmapFromView(ReportView);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "z2.jpg";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            b.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void storeImage(View View,String ImgName) {
        try {
            Bitmap image = loadBitmapFromView(View);
            String filename = ImgName +".jpg";
            File sd = Environment.getExternalStorageDirectory();

            File pictureFile = new File(sd, filename);

            FileOutputStream fos = new FileOutputStream(pictureFile);


            ByteArrayOutputStream imageByteArray = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, imageByteArray);
            byte[] imageData = imageByteArray.toByteArray();

            //300 will be the dpi of the bitmap
            setDpi(imageData, 300);

            fos.write(imageData);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public   void ConvertLayToImg( View View,String ImgName){
        Bitmap b = loadBitmapFromView(View);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = ImgName +".jpg";
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

    }
    private static void setDpi(byte[] imageData, int dpi) {
        imageData[13] = 1;
        imageData[14] = (byte) (dpi >> 8);
        imageData[15] = (byte) (dpi & 0xff);
        imageData[16] = (byte) (dpi >> 8);
        imageData[17] = (byte) (dpi & 0xff);
    }
    public   void StoreImage(){
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
        Bitmap b = null;
           try {

               v.measure(View.MeasureSpec.makeMeasureSpec(v.getLayoutParams().width,
                       View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                       v.getLayoutParams().height, View.MeasureSpec.UNSPECIFIED));
               v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
                b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                       Bitmap.Config.ARGB_8888);
               Canvas c = new Canvas(b);
               v.draw(c);
           }catch (Exception es){

           }
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
  public void SendCommand(String txt) {

     /* try {

          TscDll.openport(BPrinter_MAC_ID);

          TscDll.setup(70, 110, 4, 4, 0, 0, 0);
          TscDll.clearbuffer();
          TscDll.sendcommand("SET TEAR ON\n");
          TscDll.sendcommand("SET COUNTER @1 1\n");
          TscDll.sendcommand("@1 = \"0001\"\n");
          TscDll.sendcommand("TEXT 100,300,\"3\",0,1,1,@1\n");

          TscDll.printerfont(100, 250, "3", 0, 1, 1, "معن");
          TscDll.printlabel(1, 1);
          TscDll.closeport(5000);

      }catch ( Exception ex){
          Toast.makeText(context,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
      }*/
  }
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
       //TscDll.sendbitmap(0, 0, TscDll.bitmap2Gray(bitmap));
       TscDll.sendpicture(0, 0, dest+"");

  //   TscDll.sendbitmap(0, 0, bitmap);

     // TscDll.sendpicture(0,0,"//sdcard//z1.jpg");

      //String status = TscDll.printerstatus();
     TscDll.printlabel(1, 1);
     TscDll.closeport(5000);

 }catch ( Exception ex){
     Toast.makeText(context,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
 }
    }
    private void PrintImage2(final Bitmap bitmap,final Bitmap bitmap2,final Bitmap bitmap21,final Bitmap bitmap22,final Bitmap bitmap3) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Looper.prepare();
                        String BPrinter_MAC_ID;
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        BPrinter_MAC_ID = sharedPreferences.getString("AddressBT", "");
                        Connection c2 = new BluetoothConnection(BPrinter_MAC_ID);
                        c2.open();
                        ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, c2);
                        c2.write("! U1 JOURNAL\r\n! U1 SETFF 20 2\r\n".getBytes());
                        printer.printImage(new ZebraImageAndroid(bitmap), 20, 0, 550, bitmap.getHeight(), false);
                        printer.printImage(new ZebraImageAndroid(bitmap2), 20, 0, 550, bitmap2.getHeight(), false);
                        printer.printImage(new ZebraImageAndroid(bitmap21), 20, 0, 550, bitmap21.getHeight(), false);
                        printer.printImage(new ZebraImageAndroid(bitmap22), 20, 0, 550, bitmap22.getHeight(), false);
                        printer.printImage(new ZebraImageAndroid(bitmap3), 20, 0, 550, bitmap3.getHeight(), false);
                        c2.close();

                    } catch (ConnectionException e) {
                        e.printStackTrace();

                    } finally {
                        Looper.myLooper().quit();
                    }
                }
            }).start();

    }
    private void PrintLongImage(final Bitmap bitmap) {
        double  INCH;

            bm1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), 750);
            TscDll.clearbuffer();
            INCH = (bm1.getHeight()/ 194);
            TscDll.openport(BPrinter_MAC_ID);

            TscDll.sendcommand("SIZE 3,"+ String.valueOf((int)INCH) + "");
            TscDll.clearbuffer();
            //TscDll.sendpicture(0, 0, Environment.getExternalStorageDirectory().getPath() + "/z3.jpg");
            TscDll.sendbitmap(0,0,bm1);
            TscDll.printlabel(1, 1);
            TscDll.closeport(5000);



        bm2 = Bitmap.createBitmap(bitmap, 0,750, bitmap.getWidth(), 10);
        TscDll.clearbuffer();
        INCH = (bm2.getHeight()/ 194);
        TscDll.openport(BPrinter_MAC_ID);

        TscDll.sendcommand("SIZE 3,"+ String.valueOf((int)INCH) + "");
        TscDll.clearbuffer();
        //TscDll.sendpicture(0, 0, Environment.getExternalStorageDirectory().getPath() + "/z3.jpg");
        TscDll.sendbitmap(0,0,bm2);
        TscDll.printlabel(1, 1);
        TscDll.closeport(5000);


    }


  private void PrintImage(final Bitmap bitmap) {

         double  INCH;
          TscDll.clearbuffer();
          INCH = (bitmap.getHeight()/ 200);
          TscDll.openport(BPrinter_MAC_ID);
           TscDll.sendcommand("SIZE 3,"+ String.valueOf((int)INCH) + "\n");
          TscDll.clearbuffer();
          //TscDll.sendpicture(0, 0, Environment.getExternalStorageDirectory().getPath() + "/z3.jpg");
          TscDll.sendbitmap(0,0,bitmap);
          TscDll.printlabel(1, 1);
          TscDll.closeport(5000);


  }
    public void Print_Ipad()
    {




      //  Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/z1.jpg");///small-icon-images-11.jpg
        Bitmap bmp = BitmapFactory.decodeFile("//sdcard//z1.jpg");
        double INCH =  bmp.getHeight()/ 9000;

        TscDll.openport(BPrinter_MAC_ID); //"00:0C:BF:2E:2D:C8" , "00:0C:BF:2E:2D:33"
        //TscDll.setup(80,1000,4,8,0,0,1);
         TscDll.sendcommand("SIZE 3,"+ String.valueOf((int)INCH) + "\n");
        TscDll.clearbuffer();



        TscDll.clearbuffer();
      /*  try {
            TscDll.sendbitmap_gray_resize(0, 20, bm1, 100, bm1.getHeight());
        }catch (Exception fd){
            Toast.makeText(context,fd.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }*/


         TscDll.sendpicture(0, 0, Environment.getExternalStorageDirectory().getPath() + "/z1.jpg");
        TscDll.printlabel(1, 1);




//    TscDll.setup(78, 51, 4, 8, 0, 0, 0);
//    TscDll.clearbuffer();
//    TscDll.sendcommand("SET TEAR ON\n");
//    TscDll.sendcommand("SET COUNTER @1 1\n");
//    TscDll.sendcommand("@1 = \"0001\"\n");
//    TscDll.sendcommand("SIZE 4,10 \n");
//    TscDll.sendcommand("GAP 0,0\n");
//    TscDll.sendcommand("DIRECTION 1\n");
//    TscDll.sendcommand("CLS r\n");
//    TscDll.sendcommand("CODEPAGE UTF-8\n");


        //TscDll.printerfont(50,50,"arial.TTF",0,10,10,"TEST");


        //String imageZPLString = Utils.getZplCode(bmp,true);

        //TscDll.sendcommand(imageZPLString);
        //TscDll.printlabel(1, 1);
        TscDll.closeport(5000);

        //TscDll.setup(78, 51, 4, 8, 0, 0, 0);
        //TscDll.clearbuffer();
        //TscDll.sendcommand("SET TEAR ON\n");
        //TscDll.sendcommand("SET COUNTER @1 1\n");
        //TscDll.sendcommand("@1 = \"0001\"\n");
        //TscDll.sendcommand("SIZE 4,10 \n");
        //TscDll.sendcommand("GAP 0,0\n");
        //TscDll.sendcommand("DIRECTION 1\n");
        //TscDll.sendcommand("CLS r\n");
        //TscDll.sendcommand("CODEPAGE UTF-8\n");


        //TscDll.printerfont(50,50,"arial.TTF",0,10,10,"TEST");



        //sendpicture(100, 50, Environment.getExternalStorageDirectory().getPath() + "/ImageAfterAddingText.jpg", TscDll);

//    Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/cafe2Var2Default.jpg");
//    String x_axis = "100";
//    String y_axis = "50";
//    String mode = Integer.toString(0);
//    String command = "BITMAP " + x_axis + "," + y_axis + "," + bmp.getWidth() + "," + bmp.getHeight() + "," + mode + ",";
//    byte[] stream = Utils.decodeBitmap(bmp);
//    TscDll.sendcommand(command);
//    TscDll.sendcommand(stream);
//    TscDll.sendcommand("\r\n");


        //endregion

        //region ZPL
/*    try {

        EditText txt = (EditText) findViewById(R.id.editText);

            TscDll.openport_without_security("8C:DE:52:BD:15:43");//mac address

            //----------------------------------------------------------
            TscDll.sendcommand("^XA^LRN^CI28^CW1,E:TT0003M_.TTF^LL293^FS");
            TscDll.sendcommand("^PA0,1,1,1");
            TscDll.sendcommand("^FO600,80");
            TscDll.sendcommand("^A1N,30,30");
            TscDll.sendcommand("^TBN,400,145");
            TscDll.sendcommand("^FH");
            TscDll.sendcommand("^FWN,1");
            TscDll.sendcommand("^FD");

            TscDll.sendcommand(txt.getText().toString());
            TscDll.sendcommand("^FS");
            //----------------------------------------------------------
            TscDll.sendcommand("^FO670,240");
            TscDll.sendcommand("^A0N,60,40");
            TscDll.sendcommand("^FH");
            TscDll.sendcommand("^FD");
            TscDll.sendcommand("5 JD");
            TscDll.sendcommand("^FS");
            //----------------------------------------------------------
            TscDll.sendcommand("^FO500,200^BY3");
            TscDll.sendcommand("^BEN,60,Y,N");
            TscDll.sendcommand("^FD628541254542^FS");
            //----------------------------------------------------------
            TscDll.sendcommand("^PQ1");
            TscDll.sendcommand("^XZ");


            TscDll.closeport();

    } catch (Exception e) {
        e.printStackTrace();
    }*/
        //endregion


/*    TscDll.openport_without_security("00:19:0E:A0:86:A1");//mac address
    EditText txt = (EditText) findViewById(R.id.editText);
    TscDll.sendcommand(new byte[] { 0x1B, 0x4D });
    TscDll.sendcommand(new byte[] { 0x1B, 0x78, 0x01 });
    TscDll.sendcommand(new byte[] { 0x1B, 0x28, 0x74, 0x03, 0x00, 0x00, 0x0D, 0x00 });

    TscDll.sendcommand("--------------------------------------------------" + "\n");
    TscDll.sendcommand("---------------Test Arabic Printring--------------" + "\n");
    TscDll.sendcommand("                                                  " + "\n");

    //String stringToConvert = "    ýèÇÑôÔå ÏåÍÃ     ";
    //String stringToConvert = "    أحمد مشهراوي     ";
    //byte[] theByteArray = stringToConvert.getBytes();
    //TscDll.sendcommand(theByteArray);

    TscDll.sendcommand(new byte[] { (byte)0xFD,(byte)0xE8,(byte)0xC7,(byte)0xD1,(byte)0xF4,(byte)0xD4,(byte)0xE5,(byte)0x20,(byte)0xCF, (byte)0xE5, (byte)0xCD, (byte)0xC3  });
    TscDll.sendcommand("                                                  " + "\n");
    TscDll.sendcommand("--------------------------------------------------" + "\n");
    TscDll.closeport();*/
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
