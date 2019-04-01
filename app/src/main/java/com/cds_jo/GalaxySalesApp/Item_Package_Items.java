package com.cds_jo.GalaxySalesApp;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.Cls_Select_Item_From_Pakage;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class Item_Package_Items extends BaseAdapter {
    Methdes.MyTextView GiftQty;
    Context context;
    ArrayList<Cls_Select_Item_From_Pakage> contactList;
    int CalcSum=0;
    PopSelectItemFromPackage popSelectItemFromPackage ;
    public Item_Package_Items(Context context, ArrayList<Cls_Select_Item_From_Pakage> list) {
        popSelectItemFromPackage=new PopSelectItemFromPackage();
        this.context = context;
        contactList = list;
    }

    @Override
    public int getCount() {

        return contactList.size();
    }

    @Override
    public Object getItem(int position) {

        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final Cls_Select_Item_From_Pakage contactListItems = contactList.get(position);
        final Cls_Select_Item_From_Pakage  ItemsSum = contactList.get(getCount()-1);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.select_item_package, null);

        }

        Methdes.MyTextView ItemNo = (Methdes.MyTextView) convertView.findViewById(R.id.ItemNo);
        ItemNo.setText(contactListItems.getItemNo());

        Methdes.MyTextView ItemNm = (Methdes.MyTextView) convertView.findViewById(R.id.ItemNm);
        ItemNm.setText(contactListItems.getItemNm());

        Methdes.MyTextView Qty = (Methdes.MyTextView) convertView.findViewById(R.id.MinQty);
        Qty.setText(contactListItems.getMinQty());

        Methdes.MyTextView UnitNm = (Methdes.MyTextView) convertView.findViewById(R.id.UnitNm);
        UnitNm.setText(contactListItems.getUnitNm());




        GiftQty = (Methdes.MyTextView) convertView.findViewById(R.id.GiftQty);
        GiftQty.setText(contactListItems.getGiftQty());


        ItemNo.setTextColor(Color.BLACK);
        ItemNm.setTextColor(Color.BLACK);
        Qty.setTextColor(Color.BLACK);


        UnitNm.setTextColor(Color.BLACK);

        ItemNo.setBackgroundColor(Color.WHITE);
        ItemNm.setBackgroundColor(Color.WHITE);
        Qty.setBackgroundColor(Color.WHITE);


        UnitNm.setBackgroundColor(Color.WHITE);


        GiftQty.setBackgroundColor(Color.WHITE);

        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);

        Button btn_Inc = (Button) convertView.findViewById(R.id.btn_Inc);
        Button btn_Dec = (Button) convertView.findViewById(R.id.btn_Dec);

        btn_Inc.setVisibility(View.VISIBLE);
        btn_Dec.setVisibility(View.VISIBLE);

        if(contactListItems.getFlg().equalsIgnoreCase("-1")) {
            btn_Inc.setVisibility(View.INVISIBLE);
            btn_Dec.setVisibility(View.INVISIBLE);
        }
        if(position%2==0 && position >0 )
        {
            RR.setBackgroundColor(Color.WHITE);
            ItemNo.setTextColor(Color.BLACK);
            ItemNm.setTextColor(Color.BLACK);
            Qty.setTextColor(Color.BLACK);



            ItemNo.setTextColor(Color.BLACK);
            ItemNm.setTextColor(Color.BLACK);

            GiftQty.setTextColor(Color.BLACK);

        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            ItemNo.setTextColor(Color.BLACK);
            ItemNm.setTextColor(Color.BLACK);
            Qty.setTextColor(Color.BLACK);



            ItemNo.setTextColor(Color.BLACK);
            ItemNm.setTextColor(Color.BLACK);
            GiftQty.setTextColor(Color.BLACK);



        }


        btn_Inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcSum=CalcSum()+1;
              if(CalcSum<= Integer.parseInt(contactListItems.getSumQty())) {
                    contactListItems.setGiftQty((Integer.parseInt(contactListItems.getGiftQty()) + 1) + "");
                    ItemsSum.setGiftQty(CalcSum + "");
                   ItemsSum.setItemNo( TotalItemCount() +"");

                }else {
                  ShowMsg("لقد تجاوزت الكميات المسموح  بها");
                }

                if(  Integer.parseInt(contactListItems.getTotalItems())>0 &&  (TotalItemCount()> Integer.parseInt(contactListItems.getTotalItems()))){
                    contactListItems.setGiftQty(contactListItems.getMinQty());
                    ItemsSum.setGiftQty(CalcSum + "");
                    ItemsSum.setItemNo( TotalItemCount() +"");
                    ShowMsg("لقد تجاوزت عدد المواد المسموح  به");
                }
                notifyDataSetChanged();
            }

        });


        btn_Dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactListItems.setGiftQty( (Integer.parseInt(contactListItems.getGiftQty())-1)+"");
                if (Integer.parseInt(contactListItems.getGiftQty())< Integer.parseInt(contactListItems.getMinQty())){
                    ShowMsg("لا يمكن تجاوز الحد الأدنى للكمية");
                }
                if (Integer.parseInt(contactListItems.getGiftQty())< Integer.parseInt(contactListItems.getMinQty())) {
                    contactListItems.setGiftQty(contactListItems.getMinQty());
                    ItemsSum.setItemNo( TotalItemCount() +"");
                }


                ItemsSum.setGiftQty( CalcSum() +"");
                ItemsSum.setItemNo( TotalItemCount() +"");
                notifyDataSetChanged();

            }
        });


        return convertView;
    }

    public Integer CalcSum() {
        Integer qty = 0;
        for (int i = 0; i < getCount()-1; i++) {
            qty = qty + Integer.parseInt(contactList.get(i).getGiftQty());
        }
        return qty;
    }

    public Integer  TotalItemCount() {
        Integer Count = 0;
        for (int i = 0; i < getCount()-1; i++) {
            if(Integer.parseInt(contactList.get(i).getGiftQty())>0){
            Count = Count + 1;
            }
        }
        return Count;
    }

    private   void ShowMsg(String msg){

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("تحدد مواد وكمية الهدية");
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.error_new);
        alertDialog.setButton("رجوع", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        alertDialog.show();

    }
}
