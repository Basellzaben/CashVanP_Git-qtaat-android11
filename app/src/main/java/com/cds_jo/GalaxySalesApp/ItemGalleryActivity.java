package com.cds_jo.GalaxySalesApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.assist.Cls_Cur;
import com.cds_jo.GalaxySalesApp.assist.Cls_Cur_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Invf;
import com.cds_jo.GalaxySalesApp.assist.Cls_Trans_Qty_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_itms_Info;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ItemGalleryActivity extends Activity {

    ListView Lst_ItemImages ;
     ImageView ItemImage ;
    File imgFile ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_gallery);

           ItemImage =  (ImageView)findViewById(R.id.ItemImage);
          Lst_ItemImages  = (ListView)findViewById(R.id.Lst_ItemImages);
          imgFile = new File("//sdcard/Android/Cv_Images/logo.jpg");
        try {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ItemImage.setImageBitmap(myBitmap);
            }
        }
        catch (Exception ex){}
        FillItemImages();

        Lst_ItemImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Cls_itms_Info obj = (Cls_itms_Info) Lst_ItemImages.getItemAtPosition(position);
                imgFile = obj.getBitmapPath();
                try {
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        Bitmap imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                        Canvas canvas = new Canvas(imageRounded);
                        Paint mpaint = new Paint();
                        mpaint.setAntiAlias(true);
                        mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 100, 100, mpaint);// Round Image Corner 100 100 100 100
                        ItemImage.setImageBitmap(imageRounded);

                        //ItemImage.setImageBitmap(myBitmap);
                    }
                }finally {

                }


            }
        });

    }
    private void FillItemImages() {
        final ArrayList<Cls_itms_Info> Locations = new ArrayList<Cls_itms_Info>();
        final Handler _handler = new Handler();

        final SqlHandler sqlHandler = new SqlHandler(this);
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ItemGalleryActivity.this);
        progressDialog.setTitle("الرجاء الانتظار");
        progressDialog.setMessage("");
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
       /* progressDialog.show();*/

        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    String query = "Select  invf.Type_No, invf.Item_No , invf.Item_Name  from invf  LIMIT 20 " ;
                    ArrayList<Cls_Cur> cls_curs = new ArrayList<Cls_Cur>();
                    cls_curs.clear();

                    String Path = "";
                    Cursor c1 = sqlHandler.selectQuery(query);
                    if (c1 != null && c1.getCount() != 0) {
                        if (c1.moveToFirst()) {
                            do {
                                Cls_itms_Info cls_Loc = new Cls_itms_Info();

                                cls_Loc.setName(c1.getString(c1
                                        .getColumnIndex("Item_Name")));
                                cls_Loc.setNo(c1.getString(c1
                                        .getColumnIndex("Item_No")));

                                Path=(c1.getString(c1.getColumnIndex("Type_No")));
                                Path = Path + "/"+ c1.getString(c1.getColumnIndex("Item_No"));
                                Path ="/"+ c1.getString(c1.getColumnIndex("Item_No"));
                                imgFile = new  File("//sdcard/Android/Cv_Images/"+Path+".jpg");
                                try {
                                    if (imgFile.exists()) {
                                        cls_Loc.setBitmapPath(imgFile);
                                    }
                                    else
                                    {  imgFile = new  File("//sdcard/Android/Cv_Images/empty.jpg");
                                        cls_Loc.setBitmapPath(imgFile);
                                    }
                                }
                                catch (Exception ex){}

                                Locations.add(cls_Loc);

                            } while (c1.moveToNext());

                            c1.close();  }
                    }



                    _handler.post(new Runnable() {
                        public void run() {
                            Cls_ItemsImage_Adapter cls_trans_qty_adapter = new Cls_ItemsImage_Adapter(
                                    ItemGalleryActivity.this, Locations);
                            Lst_ItemImages.setAdapter(cls_trans_qty_adapter);
                        }
                    });


                    _handler.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    ItemGalleryActivity.this).create();

                            alertDialog.setMessage("تمت عملية تحديث البيانات بنجاح" + "   " + String.valueOf(""));
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            // alertDialog.show();

                        }
                    });
                } catch (final Exception e) {
                    progressDialog.dismiss();

                }
            }
        }).start();


    }
    public void btn_back(View view) {
        Intent k = new Intent(this, JalMasterActivity.class);
        startActivity(k);

    }


    public void btn_ShowVideo(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");

        bundle.putString("OrderNo","");
        bundle.putString("ID", "");
        FragmentManager Manager = getFragmentManager();
        PopVideoPreview obj = new PopVideoPreview();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
}