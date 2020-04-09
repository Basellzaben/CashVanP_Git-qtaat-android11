package com.cds_jo.GalaxySalesApp.Reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.CustomerSummary.cls_Bill;
import com.cds_jo.GalaxySalesApp.CustomerSummary.cls_BillC;
import com.cds_jo.GalaxySalesApp.R;

import java.util.HashMap;
import java.util.List;

import Methdes.MyTextView;

public class SalesBillAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<cls_sales> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<List<cls_sales>, List<cls_salesC>> _listDataChild;

    public SalesBillAdapter(Context _context, List<cls_sales> _listDataHeader, HashMap<List<cls_sales>, List<cls_salesC>> _listDataChild) {
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
        cls_sales cls_bill= (cls_sales) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.desginreportbill, null);
        }

        if(ReportId.Id==4) {
            LinearLayout RR = (LinearLayout) convertView.findViewById(R.id.RR);
            RR.setWeightSum((float) 10);
            MyTextView Is_damge = (MyTextView) convertView.findViewById(R.id.Is_damge);
            Is_damge.setVisibility(View.VISIBLE);
            Is_damge.setText(cls_bill.getIs_Damage());
        }
        MyTextView B_CustName = (MyTextView) convertView.findViewById(R.id.B_CustName);
        B_CustName.setText(cls_bill.getCustname());


        MyTextView Man_Name = (MyTextView) convertView.findViewById(R.id.Man_Name);
        Man_Name.setText(cls_bill.getManName());

        MyTextView orderno = (MyTextView) convertView.findViewById(R.id.orderno);
        orderno.setText(cls_bill.getOrderNo());
        MyTextView TransDate = (MyTextView) convertView.findViewById(R.id.TransDate);
        TransDate.setText(cls_bill.getTransDate());



        MyTextView NetTotal = (MyTextView) convertView.findViewById(R.id.NetTotal);
        NetTotal.setText(cls_bill.getNetTotal());
        MyTextView TaxTotal = (MyTextView) convertView.findViewById(R.id.TaxTotal);
        TaxTotal.setText(cls_bill.getTaxTotal());

        MyTextView Dis_Amt = (MyTextView) convertView.findViewById(R.id.Dis_Amt);
        Dis_Amt.setText(cls_bill.getDis_Amt());
        MyTextView Total = (MyTextView) convertView.findViewById(R.id.Total);
        Total.setText(cls_bill.getTotal());





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

        cls_salesC cls_billC= (cls_salesC) getChild(groupPosition,childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.desginreportbillc, null);
        }


        //  LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView B_ItemName = (MyTextView) convertView.findViewById(R.id.B_ItemName);
        B_ItemName.setText(cls_billC.getItem_Name());


        MyTextView SR_Qty = (MyTextView) convertView.findViewById(R.id.B_Qty);
        SR_Qty.setText(cls_billC.getQty());

        MyTextView SR_Buonas = (MyTextView) convertView.findViewById(R.id.B_Buonas);
        SR_Buonas.setText(cls_billC.getBounce());
        MyTextView SR_price = (MyTextView) convertView.findViewById(R.id.B_price);
        SR_price.setText(cls_billC.getPrice());

        MyTextView uint = (MyTextView) convertView.findViewById(R.id.uint);
        uint.setText(cls_billC.getUnitName());
        MyTextView OrgPrice = (MyTextView) convertView.findViewById(R.id.OrgPrice);
        OrgPrice.setText(cls_billC.getOrgPrice());





        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
