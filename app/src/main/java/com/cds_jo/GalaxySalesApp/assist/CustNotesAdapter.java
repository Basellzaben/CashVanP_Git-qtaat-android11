package com.cds_jo.GalaxySalesApp.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

import static com.cds_jo.GalaxySalesApp.R.id.RR1;

public class CustNotesAdapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_CustNotes> customerses;

    public CustNotesAdapter(Context context, ArrayList<Cls_CustNotes> list) {

        this.context = context;
        customerses = list;
    }

    @Override
    public int getCount() {

        return customerses.size();
    }

    @Override
    public Object getItem(int position) {

        return customerses.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        Cls_CustNotes customersesobj = customerses.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cust_notes_row, null);

        }

        LinearLayout RR=(LinearLayout)convertView.findViewById(RR1);

        TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
        tv_date.setText( " التاريخ والوقت :"+  customersesobj.getNotesDate());




         TextView tv_Mannm = (TextView) convertView.findViewById(R.id.tv_Mannm);
        tv_Mannm.setText( " المندوب :"+  customersesobj.getManNm());





        TextView tv_Notes = (TextView) convertView.findViewById(R.id.tv_Notes);
        tv_Notes.setText(customersesobj.getNotes());


        TextView tv_states = (TextView) convertView.findViewById(R.id.tv_states);
        if(customersesobj.getPosted().equalsIgnoreCase("-1")){
            tv_states.setText("غير مرحل");
        }else{
            tv_states.setText("تم الترحيل");
        }





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


}
