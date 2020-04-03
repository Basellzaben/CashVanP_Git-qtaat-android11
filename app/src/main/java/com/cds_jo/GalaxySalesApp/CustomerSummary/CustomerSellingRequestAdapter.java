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

public class CustomerSellingRequestAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<cls_SelingRequest> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<List<cls_SelingRequest>, List<cls_SelingRequestC>> _listDataChild;

    public CustomerSellingRequestAdapter(Context _context, List<cls_SelingRequest> _listDataHeader, HashMap<List<cls_SelingRequest>, List<cls_SelingRequestC>> _listDataChild) {
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
        cls_SelingRequest cls_selingRequest= (cls_SelingRequest) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.desginsellingrequest, null);
        }


        //  LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

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


        int t=Integer.parseInt(cls_selingRequest.getTaxNo());
        if(t!=0)
        {
            float b=Integer.parseInt(cls_selingRequest.getTaxPerc())/100;
            float bill=(Integer.parseInt(cls_selingRequest.getTot())*b)+Integer.parseInt(cls_selingRequest.getTot());
            MyTextView SR_Bill = (MyTextView) convertView.findViewById(R.id.SR_Bill);
            SR_Bill.setText(String.valueOf(bill));

        }else
        {
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

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        cls_SelingRequestC cls_selingRequestC= (cls_SelingRequestC) getChild(groupPosition,childPosition);
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

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
