package com.cds_jo.GalaxySalesApp;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class ContactListAdapter extends BaseAdapter {

    Context context;
    ArrayList<ContactListItems> contactList;

    public ContactListAdapter(Context context, ArrayList<ContactListItems> list) {

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
            convertView = inflater.inflate(R.layout.contact_list_row, null);

        }
        Methdes.MyTextView tvSlNo = (Methdes.MyTextView) convertView.findViewById(R.id.tv_no);
        tvSlNo.setText(contactListItems.getno());
        Methdes.MyTextView tvName = (Methdes.MyTextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(contactListItems.getName());
        Methdes.MyTextView tvPrice = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Price);
        tvPrice.setText(contactListItems.getItemOrgPrice());
        Methdes.MyTextView tvQTY = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Qty);
        tvQTY.setText(contactListItems.getQty());
        Methdes.MyTextView tvTAX = (Methdes.MyTextView) convertView.findViewById(R.id.tv_tax);
        tvTAX.setText(contactListItems.getTax());

        Methdes.MyTextView Unit = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Unit);
        Unit.setText(contactListItems.getUniteNm());


        Methdes.MyTextView Bounce = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Bounce);
        Bounce.setText( String.valueOf(Double.valueOf(contactListItems.getBounce()) + Double.valueOf(contactListItems.getPro_bounce())));


        Methdes.MyTextView Disc = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Disc_Per);
        Disc.setText(String.valueOf (Double.valueOf( contactListItems.getDiscount() ) + Double.valueOf(contactListItems.getDisPerFromHdr()) +  Double.valueOf(contactListItems.getPro_dis_Per())));




        Methdes.MyTextView Disc_Amt = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Disc_Amt);
        Disc_Amt.setText( String.valueOf(  Double.valueOf(contactListItems.getDis_Amt()) +  Double.valueOf(contactListItems.getPro_amt()) +  Double.valueOf(contactListItems.getPro_amt())   )) ;


        Methdes.MyTextView Total = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Total);
        Total.setText(contactListItems.getTotal());


        Methdes.MyTextView Tax_Amt = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Tax_Amt);
        Tax_Amt.setText(contactListItems.getTax_Amt());


        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);


      if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
            tvSlNo.setTextColor(Color.BLACK);
            tvName.setTextColor(Color.BLACK);
            tvPrice.setTextColor(Color.BLACK);
            tvQTY.setTextColor(Color.BLACK);
            tvTAX.setTextColor(Color.BLACK);
            Unit.setTextColor(Color.BLACK);
            Bounce.setTextColor(Color.BLACK);
            Disc.setTextColor(Color.BLACK);
            Disc_Amt.setTextColor(Color.BLACK);
            Total.setTextColor(Color.BLACK);
            Tax_Amt.setTextColor(Color.BLACK);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tvSlNo.setTextColor(Color.BLACK);
            tvName.setTextColor(Color.BLACK);
            tvPrice.setTextColor(Color.BLACK);
            tvQTY.setTextColor(Color.BLACK);
            tvTAX.setTextColor(Color.BLACK);
            Unit.setTextColor(Color.BLACK);
            Bounce.setTextColor(Color.BLACK);
            Disc.setTextColor(Color.BLACK);
            Disc_Amt.setTextColor(Color.BLACK);
            Total.setTextColor(Color.BLACK);
            Tax_Amt.setTextColor(Color.BLACK);
        }


        if(  contactListItems.getProType().equalsIgnoreCase("3"))
        {
            RR.setBackgroundColor(Color.MAGENTA);
            tvSlNo.setTextColor(Color.BLACK);
            tvName.setTextColor(Color.BLACK);
            tvPrice.setTextColor(Color.BLACK);
            tvQTY.setTextColor(Color.BLACK);
            tvTAX.setTextColor(Color.BLACK);
            Unit.setTextColor(Color.BLACK);
            Bounce.setTextColor(Color.BLACK);
            Disc.setTextColor(Color.BLACK);
            Disc_Amt.setTextColor(Color.BLACK);
            Total.setTextColor(Color.BLACK);
            Tax_Amt.setTextColor(Color.BLACK);
        }

        else if(  Integer.parseInt( contactListItems.getProID().toString())>0)
        {
            RR.setBackgroundColor(Color.GREEN);
            tvSlNo.setTextColor(Color.BLACK);
            tvName.setTextColor(Color.BLACK);
            tvPrice.setTextColor(Color.BLACK);
            tvQTY.setTextColor(Color.BLACK);
            tvTAX.setTextColor(Color.BLACK);
            Unit.setTextColor(Color.BLACK);
            Bounce.setTextColor(Color.BLACK);
            Disc.setTextColor(Color.BLACK);
            Disc_Amt.setTextColor(Color.BLACK);
            Total.setTextColor(Color.BLACK);
            Tax_Amt.setTextColor(Color.BLACK);
        }
        else if(  (  Integer.parseInt( contactListItems.getItemInOffer().toString())>=1 )&&  SToD( contactListItems.getProID().toString())<=0)
        {
            RR.setBackgroundColor(Color.RED);
            tvSlNo.setTextColor(Color.BLACK);
            tvName.setTextColor(Color.BLACK);
            tvPrice.setTextColor(Color.BLACK);
            tvQTY.setTextColor(Color.BLACK);
            tvTAX.setTextColor(Color.BLACK);
            Unit.setTextColor(Color.BLACK);
            Bounce.setTextColor(Color.BLACK);
            Disc.setTextColor(Color.BLACK);
            Disc_Amt.setTextColor(Color.BLACK);
            Total.setTextColor(Color.BLACK);
            Tax_Amt.setTextColor(Color.BLACK);
        }


        return convertView;
    }

}
