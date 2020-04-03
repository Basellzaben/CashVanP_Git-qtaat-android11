package com.cds_jo.GalaxySalesApp.CustomerSummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.cds_jo.GalaxySalesApp.R;

import java.util.HashMap;
import java.util.List;

import Methdes.MyTextView;

public class CustomerSalesPayoffAdabter extends BaseExpandableListAdapter {

    private Context _context;
    private List<cls_SalesPayoff> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<List<cls_SalesPayoff>, List<cls_SalesPayoffC>> _listDataChild;

    public CustomerSalesPayoffAdabter(Context _context, List<cls_SalesPayoff> _listDataHeader, HashMap<List<cls_SalesPayoff>, List<cls_SalesPayoffC>> _listDataChild) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        cls_SalesPayoff clsSalesPayoff= (cls_SalesPayoff) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.desginsalespayoff, null);
        }


        //  LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView B_CustName = (MyTextView) convertView.findViewById(R.id.SP_CustName);
        B_CustName.setText(clsSalesPayoff.getName());


        MyTextView B_BillNo = (MyTextView) convertView.findViewById(R.id.SP_Doc);
        B_BillNo.setText(clsSalesPayoff.getDocNo());

        MyTextView B_Note = (MyTextView) convertView.findViewById(R.id.SP_Note);
        B_Note.setText(clsSalesPayoff.getDec());
        MyTextView B_Buy = (MyTextView) convertView.findViewById(R.id.SP_Buy);
        int x=Integer.parseInt(clsSalesPayoff.getCluse());
        if(x==1)
        {
            B_Buy.setText("نقدا");
        }
        else if(x==2)
        {
            B_Buy.setText("ذمم");
        }
        else if(x==3)
        {
            B_Buy.setText("شيك");
        }
        else if(x==4)
        {
            B_Buy.setText("فاتورة بفاتورة");
        }


        MyTextView B_Date = (MyTextView) convertView.findViewById(R.id.SP_Date);
        B_Date.setText(clsSalesPayoff.getDate());
        MyTextView B_SUM = (MyTextView) convertView.findViewById(R.id.SP_SUM);
        B_SUM.setText(clsSalesPayoff.getTot());

        MyTextView B_BillF = (MyTextView) convertView.findViewById(R.id.SP_BillF);
        Float su=Float.parseFloat(clsSalesPayoff.getTot())+Float.parseFloat(clsSalesPayoff.getTotalTax());
        B_BillF.setText(String.valueOf(su));






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

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        cls_SalesPayoffC clsSalesPayoffC= (cls_SalesPayoffC) getChild(groupPosition,childPosition);
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

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
