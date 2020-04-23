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

public class CustomerSalesPayoffAdabter extends BaseAdapter {
    String[] SalesPayoff;
    private Context _context;
    private List<cls_SalesPayoff> _listDataHeader; // header titles
    // child data in format of header title, child title


    public CustomerSalesPayoffAdabter(Context _context, List<cls_SalesPayoff> _listDataHeader) {
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
        cls_SalesPayoff clsSalesPayoff= _listDataHeader.get(position);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.desginsalespayoff, null);
        }

        if(SalesPayoff[position]!=clsSalesPayoff.getDec()) {
            SalesPayoff[position] = clsSalesPayoff.getDec();
            //  LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

            MyTextView B_CustName = (MyTextView) convertView.findViewById(R.id.SP_CustName);
            B_CustName.setText(clsSalesPayoff.getName());


            MyTextView B_BillNo = (MyTextView) convertView.findViewById(R.id.SP_Doc);
            B_BillNo.setText(clsSalesPayoff.getDec());

            MyTextView B_Note = (MyTextView) convertView.findViewById(R.id.SP_Note);
            B_Note.setText(clsSalesPayoff.getDes());
            MyTextView B_Buy = (MyTextView) convertView.findViewById(R.id.SP_Buy);
            int x = Integer.parseInt(clsSalesPayoff.getCluse());
            if (x == 1) {
                B_Buy.setText("نقدا");
            } else if (x == 2) {
                B_Buy.setText("ذمم");
            } else if (x == 3) {
                B_Buy.setText("شيك");
            } else if (x == 4) {
                B_Buy.setText("فاتورة بفاتورة");
            }


            MyTextView B_Date = (MyTextView) convertView.findViewById(R.id.SP_Date);
            B_Date.setText(clsSalesPayoff.getDate());
            MyTextView B_SUM = (MyTextView) convertView.findViewById(R.id.SP_SUM);
            B_SUM.setText(clsSalesPayoff.getTot());

            MyTextView B_BillF = (MyTextView) convertView.findViewById(R.id.SP_BillF);
            Float su = Float.parseFloat(clsSalesPayoff.getTot()) + Float.parseFloat(clsSalesPayoff.getTotalwithtax());
            B_BillF.setText(String.valueOf(su));


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
