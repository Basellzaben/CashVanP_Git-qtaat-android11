package com.cds_jo.GalaxySalesApp.assist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;


import com.honeywell.mobility.print.LinePrinter;
import com.honeywell.mobility.print.LinePrinterException;
import com.honeywell.mobility.print.PrintProgressEvent;
import com.honeywell.mobility.print.PrintProgressListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.widget.Toast.makeText;


/**
 * this
 */
public class PrintReport_PR3 {
    Context context;
    Activity Activity;
    String BPrinter_MAC_ID;
    View ReportView;
    float ImageCountFactor;
    String sResult = null;
    PrintReport_PR3(Context _context, Activity _Activity,
                    View _ReportView, int _PageWidth, float _ImageCountFactor) {
        context = _context;
        Activity = _Activity;
//hjj
        BPrinter_MAC_ID = "B8:69:C2:25:49:42";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");
       // BPrinter_MAC_ID = "b8:69:c2:25:49:42";
        ReportView = _ReportView;
        //PageWidth = _PageWidth;
        ImageCountFactor = _ImageCountFactor;
        copyAssetFiles();
    }

    public  void DoPrint(){

       StoreImage();

        Bitmap myBitmap = null;
         myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");
        PrintImage(myBitmap);


    }

    private  void StoreImage(){
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

    private void PrintImage(final Bitmap bitmap) {


          PrintTask task = new PrintTask();

        makeText(context,BPrinter_MAC_ID,Toast.LENGTH_SHORT).show();
        task.execute("RR3", BPrinter_MAC_ID);


    }
    private void copyAssetFiles()    {
        InputStream input = null;
        OutputStream output = null;


        try
        {
            AssetManager assetManager = context.getAssets();
            String[] files = { "printer_profiles.JSON" };

            for (String filename : files)
            {
                input = assetManager.open(filename);
                File outputFile = new File(context.getExternalFilesDir(null), filename);

                output = new FileOutputStream(outputFile);

                byte[] buf = new byte[1024];
                int len;
                while ((len = input.read(buf)) > 0)
                {
                    output.write(buf, 0, len);
                }
                input.close();
                input = null;

                output.flush();
                output.close();
                output = null;
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(context, "Error copying asset files.", Toast.LENGTH_SHORT).show();

        }
        finally
        {
            try
            {
                if (input != null)
                {
                    input.close();
                    input = null;
                }

                if (output != null)
                {
                    output.close();
                    output = null;
                }
            }
            catch (IOException e){}
        }
    }
    public class PrintTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute()
        {}
        @Override
        protected String doInBackground(String... args)
        {
            LinePrinter lp = null;

            String sPrinterID = "PR3";
            String sMacAddr  = BPrinter_MAC_ID;//"00:07:80:3D:BD:C4";
            String sDocNumber = "1234567890";

            String sPrinterURI = "bt://" + sMacAddr;

            LinePrinter.ExtraSettings exSettings = new LinePrinter.ExtraSettings();
            exSettings.setContext(context);
            PrintProgressListener progressListener =
                    new PrintProgressListener()
                    {
                        public void receivedStatus(PrintProgressEvent aEvent)
                        {
                            // Publishes updates on the UI thread.
                            publishProgress(aEvent.getMessageType());
                        }
                    };

            try
            {

                File profiles = new File (context.getExternalFilesDir(null), "printer_profiles.JSON");

                lp = new LinePrinter(
                        profiles.getAbsolutePath(),
                        sPrinterID,
                        sPrinterURI,
                        exSettings);
                lp.addPrintProgressListener(progressListener);
                int numtries = 0;
                int maxretry = 2;
                while(numtries < maxretry)
                {
                    try{
                        lp.connect();  // Connects to the printer
                        break;
                    }
                    catch(LinePrinterException ex){
                        numtries++;
                        Thread.sleep(1000);
                    }
                }
                if (numtries == maxretry)
                    lp.connect();//Final retry

              //  int[] results = lp.getStatus();
               // if (results != null)
               // {

               // }

                Bitmap myBitmap = null;
                myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");
                File graphicFile = new  File("//sdcard//z1.jpg");
                if (graphicFile.exists())
                {
                    lp.writeGraphic(graphicFile.getAbsolutePath(),
                            LinePrinter.GraphicRotationDegrees.DEGREE_0,
                            0,  // Offset in printhead dots from the left of the page
                            580, // Desired graphic width on paper in printhead dots
                            myBitmap.getHeight()); // Desired graphic height on paper in printhead dots
                }

            }

            catch (LinePrinterException ex)
            {
                sResult = "LinePrinterException: " + ex.getMessage();
            }
            catch (Exception ex)
            {
                if (ex.getMessage() != null)
                    sResult = "Unexpected exception: " + ex.getMessage();
                else
                    sResult = "Unexpected exception.";
            }
            finally
            {
                if (lp != null)
                {
                    try
                    {
                      lp.disconnect();  // Disconnects from the printer
                        lp.close();  // Releases resources
                    }
                    catch (Exception ex) {}
                }
            }

            return sResult;
        }
        @Override
        protected void onProgressUpdate(Integer... values)
        {}
        @Override
        protected void onPostExecute(String result)
        {}
    }
}
