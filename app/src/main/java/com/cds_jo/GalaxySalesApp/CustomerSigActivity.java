package com.cds_jo.GalaxySalesApp;

import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.PrintReport_TSC;
import com.cds_jo.GalaxySalesApp.assist.PrintReport_Zepra520;
import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CustomerSigActivity extends DialogFragment implements View.OnClickListener {
    private ESCPOSPrinter posPtr;
    SqlHandler sqlHandler;
    ListView lvCustomList;

    private View mView;
    String ShowTax = "0";
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVERABLE_BT = 0;
    private static final DecimalFormat oneDecimal = new DecimalFormat("#,##0.0");

    Button btn_Save,btn_Clear,btn_Cancel;
    LinearLayout mContent;
    View ViewNotes;
    signature mSignature;
    Bitmap bitmap;
    ImageView img_Logo;
    ImageView imgSig;
    int SaveImg = 0 ;
    String StoredPath ;
    String DIRECTORY ;
    float STROKE_WIDTH = 5f;
    float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    LinearLayout Lyt_Erease,Lyt_Green,Lyt_Red,Lyt_Blue,Lyt_yallow,Lyt_Black;
    View form;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        form = inflater.inflate(R.layout.customer_sig, container, false);



try {

}catch (Exception ex)
{
    Toast.makeText(getActivity(),ex.getMessage(),Toast.LENGTH_SHORT).show();
}
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String Company = sharedPreferences.getString("CompanyID", "1") ;




        String folder_main = "/Android/Cv_Images/SalInv_Sig";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }

        mSignature = new  signature(getActivity(), null);
        mSignature.setBackgroundColor(Color.WHITE);

        mContent = (LinearLayout) form.findViewById(R.id.mContent);
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ViewNotes = mContent;

        Lyt_Erease= (LinearLayout)form.findViewById(R.id.Lyt_Erease);
        Lyt_Erease.setOnClickListener(this);

        Lyt_Green= (LinearLayout)form.findViewById(R.id.Lyt_Green);
        Lyt_Green.setOnClickListener(this);


        Lyt_Red= (LinearLayout)form.findViewById(R.id.Lyt_Red);
        Lyt_Red.setOnClickListener(this);


        Lyt_Blue= (LinearLayout)form.findViewById(R.id.Lyt_Blue);
        Lyt_Blue.setOnClickListener(this);

        Lyt_yallow= (LinearLayout)form.findViewById(R.id.Lyt_yallow);
        Lyt_yallow.setOnClickListener(this);


        Lyt_Black= (LinearLayout)form.findViewById(R.id.Lyt_Black);
        Lyt_Black.setOnClickListener(this);



        btn_Save=(Button)  form.findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(this);
      /*  btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSig();

            }
        });*/


        btn_Clear=(Button)  form.findViewById(R.id.btn_Clear);
        btn_Clear.setOnClickListener(this);
        /*btn_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewNotes.setDrawingCacheEnabled(true);
                mSignature.save(ViewNotes, StoredPath);
                mSignature.clear();
                mSignature.setBackgroundColor(Color.WHITE);
                SaveSig();
            }
        });*/



        img_Logo = (ImageView) form.findViewById(R.id.img_Logo);
        File imgFile = new  File("//sdcard/Android/Cv_Images/logo.jpg");
        try {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img_Logo.setImageBitmap(myBitmap);
            }
        }
        catch (Exception ex){}

        imgSig = (ImageView) form.findViewById(R.id.imgSig);
        loadimage();








        String u =  sharedPreferences.getString("UserName", "");
        String UserID =  sharedPreferences.getString("UserID", "");




        sqlHandler = new SqlHandler(getActivity());


        return form;
 }
    public void Clear() {
        ViewNotes.setDrawingCacheEnabled(true);
        mSignature.save(ViewNotes, StoredPath);
        mSignature.clear();
        mSignature.setBackgroundColor(Color.WHITE);
        SaveSig();
    }
    private  void ResetFoundSize(){
        mSignature.paint.setStrokeWidth(5f);
        ViewNotes.setDrawingCacheEnabled(true);
        STROKE_WIDTH = 5f;
        HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

    }

    public  void SaveSig(){
        String q = "";
        long i = 0 ;
        DIRECTORY = "//sdcard/Android/CashVan_Images/Customer_CardImages/"+getArguments().getString("OrderNo")+"/";
        StoredPath = DIRECTORY  +System.currentTimeMillis()+".png";
        ViewNotes.setDrawingCacheEnabled(true);
        mSignature.save(ViewNotes, StoredPath);
        ContentValues cv = new ContentValues();
        cv.put("OrderNo", getArguments().getString("OrderNo"));
        cv.put("ImgDesc", "توقيع العميل");
        cv.put("img", StoredPath);
        cv.put("Posted", "-1");
        i = sqlHandler.Insert("CusfCardAtt", null, cv);
        if(i>0){
            ((CusfCard) getActivity()).ShowList();
            this.dismiss();
        }
        loadimage();
    }
    public void Red( ) {

        mSignature.save(ViewNotes, StoredPath);
        loadimage();
      //  mSignature.clear();
        mSignature.paint.setColor(Color.RED);
        ResetFoundSize();
    }
    public void Erease() {
        ViewNotes.setDrawingCacheEnabled(true);
        mSignature.save(ViewNotes, StoredPath);
        loadimage();
        mSignature.clear();
        mSignature.paint.setColor(Color.WHITE);
        mSignature.paint.setStrokeWidth(40f);
        ViewNotes.setDrawingCacheEnabled(true);
        STROKE_WIDTH = 30f;
        HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

        /*float u = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,5000,getResources().getDisplayMetrics());*/


    }
    public void  yallow( ) {

        ViewNotes.setDrawingCacheEnabled(true);
        mSignature.save(ViewNotes, StoredPath);
        loadimage();
       // mSignature.clear();
        mSignature.paint.setColor(Color.YELLOW);
        ResetFoundSize();
    }

    public void  Blue( ) {

        ViewNotes.setDrawingCacheEnabled(true);
        mSignature.save(ViewNotes, StoredPath);
        loadimage();
      //  mSignature.clear();
        mSignature.paint.setColor(Color.BLUE);
        ResetFoundSize();
    }

    public void  Green(   ) {
        ViewNotes.setDrawingCacheEnabled(true);
        mSignature.save(ViewNotes, StoredPath);
        loadimage();
       // mSignature.clear();
        mSignature.paint.setColor(Color.GREEN);
        ResetFoundSize();
    }

    public void  Black(   ) {

        ViewNotes.setDrawingCacheEnabled(true);
        mSignature.save(ViewNotes, StoredPath);
        loadimage();
       // mSignature.clear();
        mSignature.paint.setColor(Color.BLACK);
        ResetFoundSize();
    }
    private  void loadimage (){
          TextView  textView129 = (TextView) form.findViewById(R.id.textView129);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        imgSig = (ImageView) form.findViewById(R.id.imgSig);
        File   imgFile = new  File("//sdcard//Android/Cv_Images/SalInv_Sig/" + getArguments().getString("OrderNo")  + ".png");
        if (!imgFile.exists()) {

            mSignature.setBackgroundColor(Color.WHITE);
        }else{
            imgFile = new  File("//sdcard//Android/Cv_Images/SalInv_Sig/"  + getArguments().getString("OrderNo")  + ".png");
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

    @Override
    public void onClick(View view) {
        if (view == Lyt_Erease) {
            Erease();
        }else if (view ==Lyt_Green){
            Green();
         }else if (view ==Lyt_Red){
            Red();
        }else if (view ==Lyt_Blue){
              Blue();
        }else if (view ==Lyt_yallow){
            yallow();
        }else if (view ==Lyt_Black){
            Black();
        } else if (view ==btn_Save){
            SaveSig();
        } else if (view ==btn_Clear) {
            Clear();
        }
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

}
