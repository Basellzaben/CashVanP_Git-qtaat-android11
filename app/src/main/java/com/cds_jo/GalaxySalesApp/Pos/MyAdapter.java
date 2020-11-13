package com.cds_jo.GalaxySalesApp.Pos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.cds_jo.GalaxySalesApp.MainActivity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.Cls_Deptf;
import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;


import java.io.File;
import java.util.ArrayList;



   public class MyAdapter extends RecyclerView.Adapter <MyAdapter.ViewHolder>{

       ArrayList<Cls_Deptf>  deptfs;
        private Context context;
        MyAdapter(Context context,  ArrayList<Cls_Deptf> cls_deptfs ) {
            super();
            this.context = context;
            deptfs=cls_deptfs;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.gridlayout, viewGroup, false);
            return new ViewHolder(v);
        }
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.textView.setText(deptfs.get(i).getType_Name());
         //   viewHolder.imgThumbnail.setImageResource(deptfs.get(i).getImg());
            File imgFile = new File("//sdcard/Android/Cv_Images/" + deptfs.get(i).getImg() + ".jpg");
            try {
                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Bitmap imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                    Canvas canvas = new Canvas(imageRounded);
                    Paint mpaint = new Paint();
                    mpaint.setAntiAlias(true);
                    mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                    canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 0, 0, mpaint);// Round Image Corner 100 100 100 100
                    viewHolder.imgThumbnail.setImageBitmap(imageRounded);


                } else {

                    Bitmap myBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.no_image);
                    Bitmap imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                    Canvas canvas = new Canvas(imageRounded);
                    Paint mpaint = new Paint();
                    mpaint.setAntiAlias(true);
                    mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                    canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 0, 0, mpaint);// Round Image Corner 100 100 100 100
                    viewHolder.imgThumbnail.setImageBitmap(imageRounded);
                }

            } catch (Exception ex) {
                viewHolder.imgThumbnail.setImageDrawable(null);
                viewHolder.imgThumbnail.setImageResource(0);
            }
            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {
                        Toast.makeText(context, "#" + position + " - " + deptfs.get(position).getType_No() ,
                                Toast.LENGTH_SHORT).show();

                    } else { ((Pos_Activity) context).FillItems(deptfs.get(position).getType_No());

                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return deptfs.size();
        }
        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
                View.OnLongClickListener {
            ImageView imgThumbnail;
            TextView textView;
            TextView tv_Price;
            private ItemClickListener clickListener;
            ViewHolder(View itemView) {
                super(itemView);
                imgThumbnail =(ImageView) itemView.findViewById(R.id.imgThumbnail);
                textView =(TextView) itemView.findViewById(R.id.textView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }
            void setClickListener(ItemClickListener itemClickListener) {
                this.clickListener = itemClickListener;
            }
            @Override
            public void onClick(View view) {
                clickListener.onClick(view, getPosition(), false);
            }
            @Override
            public boolean onLongClick(View view) {
                clickListener.onClick(view, getPosition(), true);
                return true;
            }
        }
}
