package com.cds_jo.GalaxySalesApp.CustomerSummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;

import com.cds_jo.GalaxySalesApp.R;

import java.util.HashMap;
import java.util.List;

import Methdes.MyTextView;

public class CustomerBillAdabter   extends BaseAdapter {
    String[] bill;

    private Context _context;
    private List<cls_Bill> _listDataHeader; // header titles
    // child data in format of header title, child title


    public CustomerBillAdabter(Context _context, List<cls_Bill> _listDataHeader) {
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
        cls_Bill cls_bill= _listDataHeader.get(position);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.m, null);
        }


        //  LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);
        if(bill[position]!=cls_bill.getBill()) {
            bill[position]=cls_bill.getBill();
            MyTextView B_CustName = (MyTextView) convertView.findViewById(R.id.B_CustName);
            B_CustName.setText(cls_bill.getName());


            MyTextView B_BillNo = (MyTextView) convertView.findViewById(R.id.B_BillNo);
            B_BillNo.setText(cls_bill.getBill());

            MyTextView B_Note = (MyTextView) convertView.findViewById(R.id.B_Note);
            B_Note.setText(cls_bill.getDec());
            MyTextView B_Buy = (MyTextView) convertView.findViewById(R.id.B_Buy);
            int x = Integer.parseInt(cls_bill.getCluse());
            if (x == 1) {
                B_Buy.setText("نقدا");
            } else if (x == 2) {
                B_Buy.setText("ذمم");
            } else if (x == 3) {
                B_Buy.setText("شيك");
            } else if (x == 4) {
                B_Buy.setText("فاتورة بفاتورة");
            }


            MyTextView B_Date = (MyTextView) convertView.findViewById(R.id.B_Date);
            B_Date.setText(cls_bill.getTot());
            MyTextView B_SUM = (MyTextView) convertView.findViewById(R.id.B_SUM);
            B_SUM.setText(cls_bill.getTot());

            MyTextView B_BillF = (MyTextView) convertView.findViewById(R.id.B_BillF);
            B_BillF.setText(cls_bill.getTotalwithtax());


        }


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
