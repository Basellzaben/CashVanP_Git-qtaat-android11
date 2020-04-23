package com.cds_jo.GalaxySalesApp.CustomerSummary;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.R;

import java.util.List;

import Methdes.MyTextView;

public class ItemSellingRequestAdapter extends BaseAdapter {
    private Context _context;
    private List<cls_SelingRequestC> _listDataHeader; // header titles

    public ItemSellingRequestAdapter(Context _context, List<cls_SelingRequestC> _listDataHeader) {
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

        cls_SelingRequestC cls_selingRequestC= _listDataHeader.get(position);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.desginsellingrequestchild, null);
        }


        //  LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView SR_CustName = (MyTextView) convertView.findViewById(R.id.SR_CustName);
        SR_CustName.setText(cls_selingRequestC.getItem_Name());


        MyTextView SR_Qty = (MyTextView) convertView.findViewById(R.id.SR_Qty);
        SR_Qty.setText(cls_selingRequestC.getA_Qty());

        MyTextView SR_Buonas = (MyTextView) convertView.findViewById(R.id.SR_Buonas);
        SR_Buonas.setText(cls_selingRequestC.getBonus());
        MyTextView SR_price = (MyTextView) convertView.findViewById(R.id.SR_price);
        SR_price.setText(cls_selingRequestC.getPrice());

        MyTextView SR_SUMC = (MyTextView) convertView.findViewById(R.id.SR_SUMC);
        SR_SUMC.setText(cls_selingRequestC.getTot());




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
