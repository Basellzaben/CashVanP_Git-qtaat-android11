package com.cds_jo.GalaxySalesApp.CustomerSummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.R;

import java.util.List;

import Methdes.MyTextView;

public class ItemSalesPayoffAdapter extends BaseAdapter {
    private Context _context;
    private List<cls_SalesPayoffC> _listDataHeader; // header titles

    public ItemSalesPayoffAdapter(Context _context, List<cls_SalesPayoffC> _listDataHeader) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
    }


    @Override
    public int getCount() {
        return _listDataHeader.size();
    }

    @Override
    public Object getItem(int position) {
        return _listDataHeader.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        cls_SalesPayoffC clsSalesPayoffC= _listDataHeader.get(position);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.desginsalespayoffc, null);
        }


        //  LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView SR_CustName = (MyTextView) convertView.findViewById(R.id.SP_ItemName);
        SR_CustName.setText(clsSalesPayoffC.getItem_Name());


        MyTextView SR_Qty = (MyTextView) convertView.findViewById(R.id.SP_Qty);
        SR_Qty.setText(clsSalesPayoffC.getA_Qty());

        MyTextView SR_Buonas = (MyTextView) convertView.findViewById(R.id.SP_Buonas);
        SR_Buonas.setText(clsSalesPayoffC.getBonus());
        MyTextView SR_price = (MyTextView) convertView.findViewById(R.id.SP_price);
        SR_price.setText(clsSalesPayoffC.getPrice());

        MyTextView SR_SUMC = (MyTextView) convertView.findViewById(R.id.SP_SUMC);
        SR_SUMC.setText(clsSalesPayoffC.getTotalwithtax());
        return convertView;

    }
}
