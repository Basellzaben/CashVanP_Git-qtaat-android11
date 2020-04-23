package com.cds_jo.GalaxySalesApp.CustomerSummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.R;

import java.util.List;

import Methdes.MyTextView;

public class cls_ItemBillAdapter extends BaseAdapter {
    private Context _context;
    private List<cls_BillC> _listDataHeader;

    public cls_ItemBillAdapter(Context _context, List<cls_BillC> _listDataHeader) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
    }

    @Override
    public int getCount() {
        return _listDataHeader.size();    }

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
         cls_BillC cls_bill= _listDataHeader.get(position);
        if(convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.desginbillc, null);
        }


        //  LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView SR_CustName = (MyTextView) convertView.findViewById(R.id.B_ItemName);
        SR_CustName.setText(cls_bill.getItem_Name());


        MyTextView SR_Qty = (MyTextView) convertView.findViewById(R.id.B_Qty);
        SR_Qty.setText(cls_bill.getA_Qty());

        MyTextView SR_Buonas = (MyTextView) convertView.findViewById(R.id.B_Buonas);
        SR_Buonas.setText(cls_bill.getBonus());
        MyTextView SR_price = (MyTextView) convertView.findViewById(R.id.B_price);
        SR_price.setText(cls_bill.getPrice());

        MyTextView SR_SUMC = (MyTextView) convertView.findViewById(R.id.B_SUMC);
        SR_SUMC.setText(cls_bill.gettotalwithtax());




//        if(position%2==0)
//        {
//            RR.setBackgroundColor(Color.WHITE);
//        }
//        else
//        {
//            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
//
//        }

        return convertView;
    }
}
