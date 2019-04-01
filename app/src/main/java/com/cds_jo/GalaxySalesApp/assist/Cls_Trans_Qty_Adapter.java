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


public class Cls_Trans_Qty_Adapter  extends BaseAdapter {
    Context context;
    ArrayList<Cls_Trans_Qty> clsTransQties;

    public Cls_Trans_Qty_Adapter(Context context, ArrayList<Cls_Trans_Qty> list) {

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
        Cls_Trans_Qty cls_trans_qty = clsTransQties.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.trans_qty_row, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);

        Methdes.MyTextView Item_No = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Item_No);
        Item_No.setText(cls_trans_qty.getItemno());

        Methdes.MyTextView ItemNm = (Methdes.MyTextView) convertView.findViewById(R.id.tv_ItemNm);
        ItemNm.setText(cls_trans_qty.getItem_Name());

        Methdes.MyTextView UnitName = (Methdes.MyTextView) convertView.findViewById(R.id.tv_UnitName);
        UnitName.setText(cls_trans_qty.getUnitNo());

        Methdes.MyTextView StroeNo = (Methdes.MyTextView) convertView.findViewById(R.id.tv_StroeNo);
        StroeNo.setText(cls_trans_qty.getStoreName());

        Methdes.MyTextView Qty = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Qty);
        Qty.setText(cls_trans_qty.getQty());

        Item_No.setTextColor(Color.BLACK);
        ItemNm.setTextColor(Color.BLACK);
        UnitName.setTextColor(Color.BLACK);
        StroeNo.setTextColor(Color.BLACK);
        Qty.setTextColor(Color.BLACK);
        /*  if(position==0)
      {
           RR.setBackgroundColor(context.getResources().getColor(R.color.Black11));

            Item_No.setTextColor(Color.WHITE);
            ItemNm.setTextColor(Color.WHITE);
            UnitName.setTextColor(Color.WHITE);
            StroeNo.setTextColor(Color.WHITE);
            Qty.setTextColor(Color.WHITE);

        }
        else  if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
        }*/

            if (position == 0)
            {
                RR.setBackgroundColor(Color.BLACK);

                Item_No.setTextColor(Color.WHITE);
                ItemNm.setTextColor(Color.WHITE);
                UnitName.setTextColor(Color.WHITE);
                StroeNo.setTextColor(Color.WHITE);
                Qty.setTextColor(Color.WHITE);

            }
            else  if(position%2==0)
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



