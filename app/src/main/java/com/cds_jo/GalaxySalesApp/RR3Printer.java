package com.cds_jo.GalaxySalesApp;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.honeywell.mobility.print.LinePrinter;
import com.honeywell.mobility.print.LinePrinterException;
import com.honeywell.mobility.print.PrintProgressEvent;
import com.honeywell.mobility.print.PrintProgressListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RR3Printer extends AppCompatActivity {
    private String base64SignaturePng = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rr3_printer);
        copyAssetFiles();
    }

    public void Print(View view) {

        PrintTask task = new PrintTask();
        task.execute("RR3", "00:07:80:3D:BD:C4");
    }

    public class PrintTask extends AsyncTask<String, Integer, String> {
       @Override
        protected void onPreExecute()
        {}
        @Override
        protected String doInBackground(String... args)
        {
            LinePrinter lp = null;
            String sResult = null;
            String sPrinterID = "PR3";
            String sMacAddr  = "00:07:80:3D:BD:C4";
            String sDocNumber = "1234567890";

            String sPrinterURI = "bt://" + sMacAddr;
            LinePrinter.ExtraSettings exSettings = new LinePrinter.ExtraSettings();
            exSettings.setContext(RR3Printer.this);
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
                File profiles = new File (getExternalFilesDir(null), "printer_profiles.JSON");

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
                if (numtries == maxretry) lp.connect();//Final retry

               /* int[] results = lp.getStatus();
                if (results != null)
                {

                }
*/
                Bitmap myBitmap = null;
                myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");
                File graphicFile = new  File("//sdcard//z1.jpg");
                if (graphicFile.exists())
                {
                    lp.writeGraphic(graphicFile.getAbsolutePath(),
                            LinePrinter.GraphicRotationDegrees.DEGREE_0,
                            0,  // Offset in printhead dots from the left of the page
                            500, // Desired graphic width on paper in printhead dots
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
    private void copyAssetFiles()
    {
        InputStream input = null;
        OutputStream output = null;
        // Copy the asset files we delivered with the application to a location
        // where the LinePrinter can access them.
        try
        {
            AssetManager assetManager = getAssets();
            String[] files = { "printer_profiles.JSON", "honeywell_logo.bmp" };

            for (String filename : files)
            {
                input = assetManager.open(filename);
                File outputFile = new File(getExternalFilesDir(null), filename);

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

}
