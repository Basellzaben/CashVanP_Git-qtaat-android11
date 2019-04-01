
package com.cds_jo.GalaxySalesApp;

import android.app.DialogFragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.ImageView;


import java.io.File;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopItemImagePreview extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button  cancel;
    File imgFile;
    SqlHandler sqlHandler;

    ImageView img ;
    String  ItemNo,str;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog2);
    }
    @Override
    public void onStart(){
        super.onStart();

// safety check
        if (getDialog() == null)
            return;

        int dialogWidth = 1000;//WindowManager.LayoutParams.MATCH_PARENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here
        if (ComInfo.ComNo == 4) {
            dialogWidth = 500;
       }
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }
    @Override
    public View onCreateView( LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        form =inflater.inflate(R.layout.imageimagepreview,container,false);


        cancel=(Button) form.findViewById(R.id.btn_Back);
        cancel.setOnClickListener(this);

        img = (ImageView)form.findViewById(R.id.img);
        sqlHandler = new SqlHandler(getActivity());

        Methdes.MyTextView tvItemNm= ( Methdes.MyTextView)form.findViewById(R.id.ItemNm);

        ItemNo= getArguments().getString("ItemNo") ;
        str= getArguments().getString("ItemNm") ;
        tvItemNm.setText(str);


        showImage(ItemNo);
        return  form;
    }
    private void showImage(String ItemNo) {
        imgFile = new File("//sdcard/Android/Cv_Images/" + ItemNo + ".jpg");
        try {
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 0, 0, mpaint);// Round Image Corner 100 100 100 100
                img.setImageBitmap(imageRounded);


            } else {

                Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.no_image);
                Bitmap imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 0, 0, mpaint);// Round Image Corner 100 100 100 100
                img.setImageBitmap(imageRounded);
            }

        } catch (Exception ex) {
            img.setImageDrawable(null);
            img.setImageResource(0);
        }


    }


    public void onClick(View v ) {
        if (v == cancel) {
          this.dismiss();
        }
    }
}


