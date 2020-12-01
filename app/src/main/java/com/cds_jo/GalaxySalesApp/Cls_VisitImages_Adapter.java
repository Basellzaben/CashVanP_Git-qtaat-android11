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

import com.cds_jo.GalaxySalesApp.assist.Cls_Deptf;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Hp on 17/03/2016.
 */
public class Cls_VisitImages_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_VisitImages> cls_Records;


    public Cls_VisitImages_Adapter(Context context, ArrayList<Cls_VisitImages> list) {

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
            convertView = inflater.inflate(R.layout.visit_row, null);

        }
        ImageView img =(ImageView) convertView.findViewById(R.id.img);
        Methdes.MyTextView tv_Desc = ( Methdes.MyTextView) convertView.findViewById(R.id.tv_Desc);
        tv_Desc.setText(obj.getDesc());
try {

        Methdes.MyTextView tv_Date = ( Methdes.MyTextView) convertView.findViewById(R.id.tv_fromdate);
        tv_Date.setText(obj.getTr_Date());

        Methdes.MyTextView tv_time = ( Methdes.MyTextView) convertView.findViewById(R.id.tv_time);
        tv_time.setText(obj.getTr_Time());

}catch (Exception sd){}

        File imgFile = new  File(obj.getImgUrl());
        try {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img.setImageBitmap(myBitmap);
            }
        }
        catch (Exception ex){}




        return convertView;
    }

}


