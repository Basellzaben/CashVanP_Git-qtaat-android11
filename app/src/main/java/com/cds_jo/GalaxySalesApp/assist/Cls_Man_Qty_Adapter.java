package com.cds_jo.GalaxySalesApp.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.Cls_Man_Balanc;
import com.cds_jo.GalaxySalesApp.ManBalanceQtyActivity;
import com.cds_jo.GalaxySalesApp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Cls_Man_Qty_Adapter extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<Cls_Man_Balanc> clsTransQties;
    ArrayList<Cls_Man_Balanc> mDisplayedValues;
    public Cls_Man_Qty_Adapter(Context context, ArrayList<Cls_Man_Balanc> list) {

        this.context = context;
        clsTransQties = list;
    }

    @Override
    public int getCount() {

        return clsTransQties.size();
    }

    @Override
    public Object getItem(int position) {

        return clsTransQties.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final Cls_Man_Balanc cls_trans_qty = clsTransQties.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.man_balanc_qty_row, null);

        }
        DecimalFormat precision = new DecimalFormat("0.00");
        TextView Item_No = (TextView) convertView.findViewById(R.id.tv_Item_No);
        Item_No.setText(cls_trans_qty.getItemno());

        TextView ItemNm = (TextView) convertView.findViewById(R.id.tv_ItemNm);
        ItemNm.setText(cls_trans_qty.getItem_Name());

        TextView UnitName = (TextView) convertView.findViewById(R.id.tv_UnitName);
        UnitName.setText(cls_trans_qty.getUnitName());


        TextView Qty = (TextView) convertView.findViewById(R.id.tv_Qty);
        Qty.setText(cls_trans_qty.getQty());

        TextView QtyAcc = (TextView) convertView.findViewById(R.id.tv_QtyAcc);
         QtyAcc.setText(cls_trans_qty.getQtyAcc());


        TextView QtySaled = (TextView) convertView.findViewById(R.id.tv_qtysaled);
        QtySaled.setText(cls_trans_qty.getQtySaled());


        TextView tv_ac_qty = (TextView) convertView.findViewById(R.id.tv_ac_qty);
        tv_ac_qty.setText(cls_trans_qty.getAct_Aty());


        TextView tv_diff = (TextView) convertView.findViewById(R.id.tv_diff);
        tv_diff.setText(cls_trans_qty.getDiff());


        if(cls_trans_qty.getAct_Aty() == cls_trans_qty.getQty()){

            // convertView.setBackgroundColor(Color.GREEN);
        }
       /* else
        {
            tv_CustNm.setTextColor(Color.RED);
        }*/

        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);

          if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
        }

        return convertView;


    }
@Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<Cls_Man_Balanc>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Cls_Man_Balanc> FilteredArrList = new ArrayList<Cls_Man_Balanc>();

                if (clsTransQties == null) {
                    clsTransQties = new ArrayList<Cls_Man_Balanc>(mDisplayedValues); // saves the original data in mOriginalValues
                }


                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = clsTransQties.size();
                    results.values = clsTransQties;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < clsTransQties.size(); i++) {
                        String data = clsTransQties.get(i).getItem_Name();
                        if (data.toLowerCase().startsWith(constraint.toString())) {

                            Cls_Man_Balanc  cls_trans_qty = new Cls_Man_Balanc();


                            cls_trans_qty.setItemno(clsTransQties.get(i).getItemno());
                            cls_trans_qty.setItem_Name(  clsTransQties.get(i).getItem_Name());
                            cls_trans_qty.setUnitNo( clsTransQties.get(i).getUnitNo());
                            cls_trans_qty.setQtyAcc(clsTransQties.get(i).getQtyAcc());
                            cls_trans_qty.setUnitName( clsTransQties.get(i).getUnitName());
                               cls_trans_qty.setQtySaled(clsTransQties.get(i).getQtySaled());


                               cls_trans_qty.setQty(clsTransQties.get(i).getQty());


                            cls_trans_qty.setAct_Aty(clsTransQties.get(i).getAct_Aty());

                                cls_trans_qty.setDiff(clsTransQties.get(i).getDiff());


                            FilteredArrList.add(cls_trans_qty);




                           /* FilteredArrList.add(new Cls_Man_Balanc(
                                    clsTransQties.get(i).getItemno(),
                                    clsTransQties.get(i).getItem_Name(),
                                    clsTransQties.get(i).getUnitNo(),clsTransQties.get(i).getQtyAcc(),
                                    clsTransQties.get(i).getUnitName(),clsTransQties.get(i).getQtySaled(),
                                    clsTransQties.get(i).getQty(),clsTransQties.get(i).getAct_Aty(),
                                    clsTransQties.get(i).getDiff()
,
                            ));*/
                        }
                    }


                    ((ManBalanceQtyActivity)context).c(FilteredArrList);
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    public Cls_Man_Qty_Adapter(Context context) {
        this.context = context;
    }
}




