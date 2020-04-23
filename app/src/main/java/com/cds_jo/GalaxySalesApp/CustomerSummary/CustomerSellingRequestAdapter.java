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



public class CustomerSellingRequestAdapter extends BaseAdapter {
    private Context _context;
    private List<cls_SelingRequest> _listDataHeader; // header titles
    // child data in format of header title, child title
    String[] SellingRequest;
    public CustomerSellingRequestAdapter(Context _context, List<cls_SelingRequest> _listDataHeader) {
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
        cls_SelingRequest cls_selingRequest= _listDataHeader.get(position);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.desginsellingrequest, null);
        }


        //  LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        if(SellingRequest[position]!=cls_selingRequest.getSales_No()) {
            SellingRequest[position] = cls_selingRequest.getSales_No();
            MyTextView SR_CustName = (MyTextView) convertView.findViewById(R.id.SR_CustName);
            SR_CustName.setText(cls_selingRequest.getName());


            MyTextView SR_OrderNo = (MyTextView) convertView.findViewById(R.id.SR_OrderNo);
            SR_OrderNo.setText(cls_selingRequest.getSales_No());

            MyTextView SR_Note = (MyTextView) convertView.findViewById(R.id.SR_Note);
            SR_Note.setText(cls_selingRequest.getDec());
            MyTextView SR_DTime = (MyTextView) convertView.findViewById(R.id.SR_DTime);
            SR_DTime.setText(cls_selingRequest.getDay());

            MyTextView SR_Date = (MyTextView) convertView.findViewById(R.id.SR_Date);
            SR_Date.setText(cls_selingRequest.getTot());
            MyTextView SR_SUM = (MyTextView) convertView.findViewById(R.id.SR_SUM);
            SR_SUM.setText(cls_selingRequest.getTot());


            int t = Integer.parseInt(cls_selingRequest.getTaxNo());
            if (t != 0) {
                float b = Integer.parseInt(cls_selingRequest.getTaxPerc()) / 100;
                float bill = (Integer.parseInt(cls_selingRequest.getTot()) * b) + Integer.parseInt(cls_selingRequest.getTot());
                MyTextView SR_Bill = (MyTextView) convertView.findViewById(R.id.SR_Bill);
                SR_Bill.setText(String.valueOf(bill));

            } else {
                MyTextView SR_Bill = (MyTextView) convertView.findViewById(R.id.SR_Bill);
                SR_Bill.setText(cls_selingRequest.getTot());
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
        }
        return convertView;
    }
}
