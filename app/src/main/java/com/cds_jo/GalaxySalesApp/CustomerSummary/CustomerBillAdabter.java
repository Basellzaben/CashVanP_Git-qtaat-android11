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

public class CustomerBillAdabter extends BaseExpandableListAdapter {

    private Context _context;
    private List<cls_Bill> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<List<cls_Bill>, List<cls_BillC>> _listDataChild;

    public CustomerBillAdabter(Context _context, List<cls_Bill> _listDataHeader, HashMap<List<cls_Bill>, List<cls_BillC>> _listDataChild) {
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
        cls_Bill cls_bill= (cls_Bill) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.m, null);
        }


        //  LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView B_CustName = (MyTextView) convertView.findViewById(R.id.B_CustName);
        B_CustName.setText(cls_bill.getName());


        MyTextView B_BillNo = (MyTextView) convertView.findViewById(R.id.B_BillNo);
        B_BillNo.setText(cls_bill.getbill());

        MyTextView B_Note = (MyTextView) convertView.findViewById(R.id.B_Note);
        B_Note.setText(cls_bill.getDec());
        MyTextView B_Buy = (MyTextView) convertView.findViewById(R.id.B_Buy);
        int x=Integer.parseInt(cls_bill.getCluse());
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


        MyTextView B_Date = (MyTextView) convertView.findViewById(R.id.B_Date);
        B_Date.setText(cls_bill.getTot());
        MyTextView B_SUM = (MyTextView) convertView.findViewById(R.id.B_SUM);
        B_SUM.setText(cls_bill.getTot());

        MyTextView B_BillF = (MyTextView) convertView.findViewById(R.id.B_BillF);
        B_BillF.setText(cls_bill.getTotalwithtax());






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

        cls_BillC cls_billC= (cls_BillC) getChild(groupPosition,childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.desginbillc, null);
        }


        //  LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView SR_CustName = (MyTextView) convertView.findViewById(R.id.B_ItemName);
        SR_CustName.setText(cls_billC.getItem_Name());


        MyTextView SR_Qty = (MyTextView) convertView.findViewById(R.id.B_Qty);
        SR_Qty.setText(cls_billC.getA_Qty());

        MyTextView SR_Buonas = (MyTextView) convertView.findViewById(R.id.B_Buonas);
        SR_Buonas.setText(cls_billC.getBonus());
        MyTextView SR_price = (MyTextView) convertView.findViewById(R.id.B_price);
        SR_price.setText(cls_billC.getPrice());

        MyTextView SR_SUMC = (MyTextView) convertView.findViewById(R.id.B_SUMC);
        SR_SUMC.setText(cls_billC.gettotalwithtax());




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
