package com.cds_jo.GalaxySalesApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Hp on 17/03/2016.
 */
public class Cls_Cusf_Card_Image_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_VisitImages> cls_Records;


    public Cls_Cusf_Card_Image_Adapter(Context context, ArrayList<Cls_VisitImages> list) {

        this.context = context;
        cls_Records = list;
    }@Override
    public int getCount() {

        return cls_Records.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_Records.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_VisitImages  obj = cls_Records.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cust_imag_row, null);

        }
        ImageView img =(ImageView) convertView.findViewById(R.id.img);
        Methdes.MyTextView tv_Desc = ( Methdes.MyTextView) convertView.findViewById(R.id.tv_Desc);
        tv_Desc.setText(obj.getDesc());

        Methdes.MyTextView tv_Ab_Reference = ( Methdes.MyTextView) convertView.findViewById(R.id.tv_Ab_Reference);

        if(obj.getPosted().toString().equalsIgnoreCase("-1")){
            tv_Ab_Reference.setText("غير مرحلة");
            tv_Ab_Reference.setTextColor(Color.RED);
        }else{
            tv_Ab_Reference.setText("مرحلة برقم :" + obj.getPosted().toString());
            tv_Ab_Reference.setTextColor(Color.GREEN);
        }


       /* Methdes.MyTextView tv_time = ( Methdes.MyTextView) convertView.findViewById(R.id.tv_time);
        tv_time.setText(obj.getTr_Time());*/


        File imgFile = new  File(obj.getImgUrl());
        try {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap thumb = Bitmap.createScaledBitmap(myBitmap, 100, 100, true);
                img.setImageBitmap(thumb);
            }
        }
        catch (Exception ex){
            Toast.makeText(context,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }




        return convertView;
    }

}


