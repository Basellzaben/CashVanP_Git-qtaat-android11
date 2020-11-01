package com.cds_jo.GalaxySalesApp.NewHomePage;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.cds_jo.GalaxySalesApp.GalaxyLoginActivity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.OrdersItems;

import java.util.List;

public class Cls_Home_Show_Ditial_Adapter extends RecyclerView.Adapter<Cls_Home_Show_Ditial_Adapter.MyViewHolder> {

    private Context mContext ;
    private List<Cls_Home_Show_Ditial> mData ;


    public Cls_Home_Show_Ditial_Adapter(Context mContext, List<Cls_Home_Show_Ditial> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.home_show_ditial_design,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tv_title.setText(mData.get(position).getTitle());
        holder.img_book_thumbnail.setImageResource(mData.get(position).getImage());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mData.get(position).getID()==1)
                {
                    Intent intent = new Intent(mContext, OrdersItems.class);
                    mContext.startActivity(intent);
                }else if(mData.get(position).getID()==2)
                {
                    Intent intent = new Intent(mContext, OrdersItems.class);
                    mContext.startActivity(intent);
                }
                else if(mData.get(position).getID()==3)
                {
                  Intent intent = new Intent(mContext, OrdersItems.class);
                    mContext.startActivity(intent);
                }
                else if(mData.get(position).getID()==4)
                {
                    Intent intent = new Intent(mContext, OrdersItems.class);
                    mContext.startActivity(intent);
                }
                else if(mData.get(position).getID()==5)
                {
                    Intent intent = new Intent(mContext, OrdersItems.class);
                    mContext.startActivity(intent);
                } else if(mData.get(position).getID()==6)
                {
                    Intent intent = new Intent(mContext, OrdersItems.class);
                    mContext.startActivity(intent);
                }
                else if(mData.get(position).getID()==7)
                {
                    Intent intent = new Intent(mContext, GalaxyLoginActivity.class);
                    mContext.startActivity(intent);
                }


            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        ImageView img_book_thumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.title_id) ;
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);


        }
    }


}