package com.cds_jo.GalaxySalesApp.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

import Methdes.MyTextView;

/**
 * Created by Hp on 08/03/2016.
 */
public class CheckAdapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_Check> cls_checks;


    public CheckAdapter(Context context, ArrayList<Cls_Check> list) {

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
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Check  cls_check = cls_checks.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.check_list_row, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);
        MyTextView ser = (MyTextView) convertView.findViewById(R.id.ser);
        ser.setText(cls_check.getSer().toString());

        MyTextView tv_Checkno = (MyTextView) convertView.findViewById(R.id.tv_Checkno);
        tv_Checkno.setText(cls_check.getCheckNo());

        MyTextView cur_no = (MyTextView) convertView.findViewById(R.id.tv_CheckDate);
        cur_no.setText(cls_check.getCheckDate());

        MyTextView BankNm = (MyTextView) convertView.findViewById(R.id.tv_BankNm);
        BankNm.setText(cls_check.getBankName());


        MyTextView tvdes = (MyTextView) convertView.findViewById(R.id.tv_Amnt);
        tvdes.setText(cls_check.getAmnt());


        if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));

        }

       /* TextView tvbb = (TextView) convertView.findViewById(R.id.tv_bb);
        tvbb.setText(cls_check.getUserID());


        TextView tvdept = (TextView) convertView.findViewById(R.id.tv_dept);
        tvdept.setText(cls_check.getPost());
*/

        return convertView;
    }

}
