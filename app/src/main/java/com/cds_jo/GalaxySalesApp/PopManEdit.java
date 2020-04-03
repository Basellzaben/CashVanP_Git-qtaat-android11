package com.cds_jo.GalaxySalesApp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class PopManEdit extends android.app.DialogFragment  implements View.OnClickListener {

    EditText Ed_manname, Ed_mobileno, Ed_pass, Ed_storeno, Ed_address, Ed_Email;
    Button btn_save, btn_Cancel, btnAddNewRecord;
    View form;
    SqlHandler sqlHandler, sql_Handler;
    String UserID;
    ImageView iv_man;
    int REQUEST_CAMERA = 0;
    EditText OrderNo;
    SwipeMenuListView lv_Items;
    Cls_VisitImages obj;
    String StoredPath;
    String DIRECTORY;
    TextView CustNo, CustNm;
    public ProgressDialog loadingdialog;
    SharedPreferences sharedPreferences;
    Bitmap myBitmap = null;
    String Desc = "";
    String currentDateandTime;
    SimpleDateFormat sdf;
    File imgFile;
    String ImgUrl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        form = inflater.inflate(R.layout.fragment_pop_man_edit, container, false);
        getDialog().setTitle("معلومات المندوب");

        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        UserID = sharedPreferences.getString("UserID", "");

        Ed_manname = (EditText) form.findViewById(R.id.Ed_manname);
        Ed_mobileno = (EditText) form.findViewById(R.id.Ed_mobileno);
        Ed_pass = (EditText) form.findViewById(R.id.Ed_pass);
        Ed_storeno = (EditText) form.findViewById(R.id.Ed_storeno);
        Ed_address = (EditText) form.findViewById(R.id.Ed_address);
        Ed_Email = (EditText) form.findViewById(R.id.Ed_Email);
        iv_man = (ImageView) form.findViewById(R.id.iv_man);

        btn_save = (Button) form.findViewById(R.id.btn_Save);
        btn_save.setOnClickListener(this);

        btn_Cancel = (Button) form.findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(this);

        btnAddNewRecord = (Button) form.findViewById(R.id.btnAddNewRecord);
        btnAddNewRecord.setOnClickListener(this);

        sql_Handler = new SqlHandler(getActivity());
        String q = " SELECT name,password,Mobile1,img From manf where man ='" + UserID + "' ";
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                obj = new Cls_VisitImages();
                Ed_manname.setText(c1.getString(c1.getColumnIndex("name")));
                Ed_mobileno.setText(c1.getString(c1.getColumnIndex("Mobile1")));
                Ed_pass.setText(c1.getString(c1.getColumnIndex("password")));
                obj.setImgUrl(c1.getString(c1.getColumnIndex("img")));
            }

            c1.close();
        }

        return form;
    }

    private void Saveman() {
        ContentValues cv = new ContentValues();
      cv.put("name", Ed_manname.getText().toString());
       cv.put("Mobile1", Ed_mobileno.getText().toString());
       cv.put("password", Ed_pass.getText().toString());
        // cv.put("store", Ed_storeno.getText().toString());
        // cv.put("Address", Ed_address.getText().toString());
        //cv.put("Email", Ed_Email.getText().toString());
        cv.put("img","sdcard/Android/Cv_Images/InfoMan/ManImage.jpg");
        long i;
        i = sqlHandler.Insert("manf", "img", cv );
        if (i > 0) {
            Toast.makeText(getActivity(), "تم التخزين بنجاح", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "لم تتم عملية التخزين", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnAddNewRecord){
            selectImage();
       }
       else if (v == btn_Cancel) {

            this.dismiss();
        } else if (v == btn_save) {
            Ed_manname=(EditText) form.findViewById(R.id.Ed_manname);
            Ed_mobileno=(EditText) form.findViewById(R.id.Ed_mobileno);
            Ed_pass=(EditText) form.findViewById(R.id.Ed_pass);
            iv_man = (ImageView) form.findViewById(R.id.iv_man);
            Saveman();

        }

        }

    public void btn_Add_New_Record(View view) {
        selectImage();
    }
    String image_path;
    private void selectImage() {

        image_path=  "//sdcard/Android/Cv_Images/InfoMan";
        File img = new File(image_path);
        Uri uri = Uri.fromFile(img);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== RESULT_OK) {
            onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        thumbnail = Bitmap.createScaledBitmap(thumbnail, 400, 400, false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        DIRECTORY = "//sdcard/Android/Cv_Images/InfoMan/";
        StoredPath = DIRECTORY  +"ManImage.jpg";
        String folder_main = "/Android/Cv_Images/InfoMan/";
        File f ;
        f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File destination = new  File(StoredPath);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            LoadManImage();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    private  void LoadManImage(){

        imgFile = new File("//sdcard/Android/Cv_Images/InfoMan/ManImage.jpg");
        try {
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 0, 0, mpaint);// Round Image Corner 100 100 100 100
                iv_man.setImageBitmap(imageRounded);


            }

        } catch (Exception ex) {
            iv_man.setImageDrawable(null);
            iv_man.setImageResource(0);
        }

        //Saveman();


    }



}
