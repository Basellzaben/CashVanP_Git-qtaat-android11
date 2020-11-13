package com.cds_jo.GalaxySalesApp.Pos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.cls_Search_po;

import java.util.ArrayList;

public class cls_Search_Inv_POS_adapter extends BaseAdapter {

    Context context;
    ArrayList<cls_Search_po> cls_search_pos;

    public cls_Search_Inv_POS_adapter(Context context, ArrayList<cls_Search_po> list) {

        this.context = context;
        cls_search_pos = list;
    }

    @Override
    public int getCount() {

        return cls_search_pos.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_search_pos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        cls_Search_po cls_search_po = cls_search_pos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sale_pos_row_search, null);

        }
        TextView tv_custno = (TextView) convertView.findViewById(R.id.tv_custno);
        tv_custno.setText(  cls_search_po.getAcc());

        TextView tvCustNm = (TextView) convertView.findViewById(R.id.tv_nm);
        tvCustNm.setText(cls_search_po.getCustNm());

        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_Date);
        tvDate.setText(cls_search_po.getDate());

         TextView tv_tot = (TextView) convertView.findViewById(R.id.tv_tot);
        tv_tot.setText(cls_search_po.getTot());


        TextView tv_BillNo = (TextView) convertView.findViewById(R.id.tv_BillNo);
        tv_BillNo.setText(cls_search_po.getCustNo());


       /* TextView tv_invoType = (TextView) convertView.findViewById(R.id.tv_invoType);

       if(cls_search_po.getType().equals("0")) {
           tv_invoType.setText("نقدية");
       }
       else {
           tv_invoType.setText("ذمــم");
       }
*/

        return convertView;
    }

}

