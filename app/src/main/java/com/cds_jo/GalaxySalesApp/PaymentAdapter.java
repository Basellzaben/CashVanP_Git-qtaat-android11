package com.cds_jo.GalaxySalesApp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.assist.Cls_Check;

import java.util.ArrayList;

import Methdes.MyTextView;


public class PaymentAdapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Payment> cls_checks;


    public PaymentAdapter(Context context, ArrayList<Cls_Payment> list) {

        this.context = context;
        cls_checks = list;
    }

    @Override
    public int getCount() {

        return cls_checks.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_checks.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final Cls_Payment  cls_payment = cls_checks.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.payment_list_row, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView tv_Amt = (MyTextView) convertView.findViewById(R.id.tv_Amt);
        tv_Amt.setText(cls_payment.getAmt());


        MyTextView tv_Date = (MyTextView) convertView.findViewById(R.id.tv_Date);
        tv_Date.setText(cls_payment.getTr_date());

        MyTextView tv_Note = (MyTextView) convertView.findViewById(R.id.tv_Note);
        tv_Note.setText(cls_payment.getNotes());
        ImageButton imageButton= (ImageButton) convertView.findViewById(R.id.upd);
        ImageButton imageButton1= (ImageButton) convertView.findViewById(R.id.delet);

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SqlHandler sql_Handler = new SqlHandler(context);

                String  q = " delete from Pay_method where no='"+cls_payment.getNo() +"' And orderNo='"+cls_payment.getOrderNo()+"'" ;
                sql_Handler.executeQuery(q);
                cls_checks.remove(position);
                notifyDataSetChanged();


            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pop_Payments_method as=new Pop_Payments_method();

                // as.setData("mohammad");
                as.et_Amt.setText("asdasd");
                as.et_Note.setText("dghgjkh");
            }
        });

        if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));

        }

        return convertView;
    }
    private static class ViewHolder{
        MyTextView tv_Note,tv_Date,tv_Amt;
    }

}
