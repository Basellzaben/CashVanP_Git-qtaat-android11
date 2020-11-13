package com.cds_jo.GalaxySalesApp.warehouse;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.ContactListItems;
import com.cds_jo.GalaxySalesApp.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ItemRecepitListAdapter extends BaseAdapter {

    Context context;
    ArrayList<ContactListItems> contactList;

    public ItemRecepitListAdapter(Context context, ArrayList<ContactListItems> list) {

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
    private Double SToD(String str) {
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat) nf;
        str = str.replace(",", "");
        Double d = 0.0;
        if (str.length() == 0) {
            str = "0";
        }
        if (str.length() > 0)
            try {
                d = Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            } catch (Exception ex) {
                str = "0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ContactListItems contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_recepit_list_row, null);

        }
        Methdes.MyTextView tvSlNo = (Methdes.MyTextView) convertView.findViewById(R.id.tv_no);
        tvSlNo.setText(contactListItems.getno());
        Methdes.MyTextView tvName = (Methdes.MyTextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(contactListItems.getName());
        Methdes.MyTextView tvPrice = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Price);
        tvPrice.setText(contactListItems.getItemOrgPrice());
        Methdes.MyTextView tvQTY = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Qty);
        tvQTY.setText(contactListItems.getQty());


        Methdes.MyTextView Unit = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Unit);
        Unit.setText(contactListItems.getUniteNm());


        Methdes.MyTextView Bounce = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Bounce);
        Bounce.setText( String.valueOf(Double.valueOf(contactListItems.getBounce())  ));







        Methdes.MyTextView Total = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Total);
        Total.setText(contactListItems.getTotal());


        Methdes.MyTextView tv_StoreNm = (Methdes.MyTextView) convertView.findViewById(R.id.tv_StoreNm);
        tv_StoreNm.setText(contactListItems.getStoreNm());


        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);


      if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
            tvSlNo.setTextColor(Color.BLACK);
            tvName.setTextColor(Color.BLACK);
            tvPrice.setTextColor(Color.BLACK);
            tvQTY.setTextColor(Color.BLACK);

            Unit.setTextColor(Color.BLACK);
            Bounce.setTextColor(Color.BLACK);


            Total.setTextColor(Color.BLACK);
            tv_StoreNm.setTextColor(Color.BLACK);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tvSlNo.setTextColor(Color.BLACK);
            tvName.setTextColor(Color.BLACK);
            tvPrice.setTextColor(Color.BLACK);
            tvQTY.setTextColor(Color.BLACK);

            Unit.setTextColor(Color.BLACK);
            Bounce.setTextColor(Color.BLACK);

            Total.setTextColor(Color.BLACK);
            tv_StoreNm.setTextColor(Color.BLACK);
        }







        return convertView;
    }

}
