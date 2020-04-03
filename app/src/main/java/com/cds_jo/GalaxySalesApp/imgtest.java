package com.cds_jo.GalaxySalesApp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class imgtest extends AppCompatActivity {
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgtest);
        img=(ImageView)findViewById(R.id.img);
    }
    String image_path;
    public void OpenCare(View view) {
          image_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp_image.jpg";
        image_path = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/Android/tmp_image.jpg";


        File img = new File (image_path);
        Uri uri = Uri.fromFile(img);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 2);



    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if(resultCode==Activity.RESULT_OK) {
            onCaptureImageResult(data);
        }*/
        if (  resultCode == Activity.RESULT_OK) {
            Bitmap bmp = BitmapFactory.decodeFile(image_path);
            Bitmap thumb = Bitmap.createScaledBitmap(bmp, 100, 100, true);
            img.setImageBitmap(thumb);
            img.invalidate();



        }
    }

}
