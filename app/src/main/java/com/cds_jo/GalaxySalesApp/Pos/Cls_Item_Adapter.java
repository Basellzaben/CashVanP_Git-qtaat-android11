package com.cds_jo.GalaxySalesApp.Pos;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.GalaxyLoginActivity;
import com.cds_jo.GalaxySalesApp.NewHomePage.Cls_Home_Show_Ditial;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.Cls_Invf;
import com.cds_jo.GalaxySalesApp.assist.OrdersItems;

import java.io.File;
import java.util.List;

public class Cls_Item_Adapter extends RecyclerView.Adapter<Cls_Item_Adapter.MyViewHolder> {

    private Context mContext ;
    private List<Cls_Invf> mData ;


    public Cls_Item_Adapter(Context mContext, List<Cls_Invf> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.pos_item_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.Price.setText(mData.get(position).getPrice());
        holder.tv_title.setText(mData.get(position).getItem_Name());
       // holder.img_book_thumbnail.setImageResource(mData.get(position).getImage());


        holder.img_book_thumbnail.setImageResource(R.drawable.img101);
        File imgFile = new File("//sdcard/Android/Cv_Images/" + mData.get(position).getImage() + ".jpg");
        try {
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 0, 0, mpaint);// Round Image Corner 100 100 100 100
                holder.img_book_thumbnail.setImageBitmap(imageRounded);


            } else {

                Bitmap myBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.no_image);
                Bitmap imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 0, 0, mpaint);// Round Image Corner 100 100 100 100
                holder.img_book_thumbnail.setImageBitmap(imageRounded);
            }

        } catch (Exception ex) {
            holder.img_book_thumbnail.setImageDrawable(null);
            holder.img_book_thumbnail.setImageResource(0);
        }



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Pos_Activity) mContext).Add_Item( mData.get(position).getItem_No());
            }
        });
       holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Pos_Activity) mContext).Add_Item( mData.get(position).getItem_No());
            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,Price;
        ImageView img_book_thumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.title_id) ;
            Price = (TextView) itemView.findViewById(R.id.Price) ;
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
           // ((Pos_Activity) mContext).Set_Order("11");

        }
    }


}