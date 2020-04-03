package com.cds_jo.GalaxySalesApp.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

import Methdes.MyTextView;

public class TableOfCollectoinAdapter extends BaseAdapter {

    Context context;
    ArrayList<cls_TableOfCollection> cls_checks;


    public TableOfCollectoinAdapter(Context context, ArrayList<cls_TableOfCollection> list) {

        this.context = context;
        cls_checks = list;
    }

    @Override
    public int getCount() {

        return cls_checks.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_checks.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final cls_TableOfCollection  clsTableOfCollection = cls_checks.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tablecollectionslayout, null);

        }
        MyTextView tv_custNo = (MyTextView) convertView.findViewById(R.id.tv_new_date123);
        tv_custNo.setText(clsTableOfCollection.getNewTr_date());

        MyTextView tv_CustName = (MyTextView) convertView.findViewById(R.id.tv_custName123);
        tv_CustName.setText(clsTableOfCollection.getNameCust());

        MyTextView tv_orderno = (MyTextView) convertView.findViewById(R.id.tv_orderno123);
        tv_orderno.setText(clsTableOfCollection.getOrderNo1());

        MyTextView tv_AmtI123 = (MyTextView) convertView.findViewById(R.id.tv_totInv);
        tv_AmtI123.setText(clsTableOfCollection.getInoviceAmt());
        MyTextView new_Amt = (MyTextView) convertView.findViewById(R.id.newAmt);
        new_Amt.setText(clsTableOfCollection.getNewAmt());

        MyTextView tv_Amt = (MyTextView) convertView.findViewById(R.id.tv_Amt);
        tv_Amt.setText(clsTableOfCollection.getAmt());

        MyTextView tv_Notes = (MyTextView) convertView.findViewById(R.id.tv_Notes123);
        tv_Notes.setText(clsTableOfCollection.getNotes());

        MyTextView tv_TR_date = (MyTextView) convertView.findViewById(R.id.tv_TR_date123);
        tv_TR_date.setText(clsTableOfCollection.getOrderDate());

        MyTextView tv_old_date = (MyTextView) convertView.findViewById(R.id.tv_old_date123);
        tv_old_date.setText(clsTableOfCollection.getTr_date());

        MyTextView SupervisorNutes = (MyTextView) convertView.findViewById(R.id.SupervisorNutes123);
        SupervisorNutes.setText(clsTableOfCollection.getSupervisorNutes());


        return convertView;
    }}

/* extends BaseAdapter {
    Context context;
    ArrayList<cls_TableOfCollection> TList;

    public TableOfCollectoinAdapter(Context context, ArrayList<cls_TableOfCollection> TList) {
        this.context = context;
        this.TList = TList;
    }

    @Override
    public int getCount() {
        return TList.size();
    }

    @Override
    public Object getItem(int i) {
        return TList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
         cls_TableOfCollection  clsTableOfCollection = TList.get(i);

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.tablecollectionslayout, null);

        }

        MyTextView tv_custNo = (MyTextView) view.findViewById(R.id.tv_CustNo);
        tv_custNo.setText(clsTableOfCollection.getCustNo1());

        MyTextView tv_CustName = (MyTextView) view.findViewById(R.id.tv_custName);
        tv_CustName.setText(clsTableOfCollection.getNameCust());

        MyTextView tv_orderno = (MyTextView) view.findViewById(R.id.tv_orderno);
        tv_orderno.setText(clsTableOfCollection.getOrderNo1());
        MyTextView tv_AmtI = (MyTextView) view.findViewById(R.id.tv_AmtI);
        tv_AmtI.setText(clsTableOfCollection.getAmt());

        MyTextView tv_Amt = (MyTextView) view.findViewById(R.id.tv_Amt);
        tv_Amt.setText(clsTableOfCollection.getInoviceAmt());

        MyTextView tv_Notes = (MyTextView) view.findViewById(R.id.tv_Notes);
        tv_Notes.setText(clsTableOfCollection.getNotes());
        MyTextView tv_TR_date = (MyTextView) view.findViewById(R.id.tv_TR_date);
        tv_TR_date.setText(clsTableOfCollection.getTr_date());

        MyTextView tv_old_date = (MyTextView) view.findViewById(R.id.tv_old_date);
        tv_old_date.setText(clsTableOfCollection.getOld_date());

        MyTextView SupervisorNutes = (MyTextView) view.findViewById(R.id.SupervisorNutes);
        SupervisorNutes.setText(clsTableOfCollection.getSupervisorNutes());





        return view;
    }
}*/
