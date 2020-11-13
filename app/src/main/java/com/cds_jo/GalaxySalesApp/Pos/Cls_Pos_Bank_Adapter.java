package com.cds_jo.GalaxySalesApp.Pos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.Cls_Bank_Search;
import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

/**
 * Created by Hp on 09/03/2016.
 */
public class Cls_Pos_Bank_Adapter extends BaseAdapter   {

        Context context;
        ArrayList<Cls_Bank_Search> cls_Bank_searchs;

        public Cls_Pos_Bank_Adapter(Context context, ArrayList<Cls_Bank_Search> list) {

        this.context = context;
            cls_Bank_searchs = list;
    }

        @Override
        public int getCount() {

        return cls_Bank_searchs.size();
    }

        @Override
        public Object getItem(int position) {

        return cls_Bank_searchs.get(position);
    }

        @Override
        public long getItemId(int position) {

        return position;
    }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            Cls_Bank_Search cls_Bank_search_obj = cls_Bank_searchs.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bank_pos_row, null);

        }
        TextView bankNo = (TextView) convertView.findViewById(R.id.tv_bankNo);
            bankNo.setText(cls_Bank_search_obj.getNo());

        TextView bankNm = (TextView) convertView.findViewById(R.id.tv_bankNm);
            bankNm.setText(cls_Bank_search_obj.getName());



        return convertView;
    }

    }

