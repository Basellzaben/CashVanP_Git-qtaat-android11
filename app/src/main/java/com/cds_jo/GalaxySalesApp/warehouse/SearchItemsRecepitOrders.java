package com.cds_jo.GalaxySalesApp.warehouse;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;


import java.util.ArrayList;

public class SearchItemsRecepitOrders extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
     TextView editText4;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        form =inflater.inflate(R.layout.activity_item_recepit_orders,container,false);
        getDialog().setTitle("البحث في سندات استلام المواد");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

         items_Lsit=(ListView) form.findViewById(R.id.listView2);
        editText4 =(TextView)form.findViewById(R.id.editText4);
        editText4.clearFocus();
        ArrayList<Cls_ItemRecepit_Search_Row> cls_search_pos_list  = new ArrayList<Cls_ItemRecepit_Search_Row>();
        cls_search_pos_list.clear();
        String q = "Select distinct orderno,date ,purches_year_no   , purches_serial_nm ,purches_order_no " +
                "  from ItemsReceipthdr ";
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_ItemRecepit_Search_Row cls_searchpos= new Cls_ItemRecepit_Search_Row();

                    cls_searchpos.setOrderno(c1.getString(c1.getColumnIndex("orderno")));
                    cls_searchpos.setDate(c1.getString(c1.getColumnIndex("date")));
                    cls_searchpos.setPurches_serial_nm(c1.getString(c1.getColumnIndex("purches_serial_nm")));
                    cls_searchpos.setPurches_order_no(c1.getString(c1.getColumnIndex("purches_order_no")));
                    cls_searchpos.setMYear(c1.getString(c1.getColumnIndex("purches_year_no")));

                    cls_search_pos_list.add(cls_searchpos);
                }while (c1.moveToNext());
            }
            c1.close();
    }


        Cls_Search_Item_Recepit_adapter cls_search_po_adapter = new Cls_Search_Item_Recepit_adapter(
                this.getActivity(),cls_search_pos_list);
        items_Lsit.setAdapter(cls_search_po_adapter);






        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Cls_ItemRecepit_Search_Row po_obj = (Cls_ItemRecepit_Search_Row) arg0.getItemAtPosition(position);
                String nm = po_obj.getOrderno();



                ((ItemsRecepit)getActivity()).Set_Order(po_obj.getOrderno(), po_obj.getPurches_order_no(),po_obj.getPurches_serial_nm());
                 Exist_Pop();

            }


        });
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return  form;
    }

    public void Exist_Pop ()
    {
        this.dismiss();
    }
    @Override
    public void onClick(View v) {
        Button bu = (Button) v ;
        if (bu.getText().toString().equals("Cancel")){
            this.dismiss();
        }
        else  if (bu.getText().toString().equals("Add")){

        }


    }


    public void onListItemClick(ListView l, View v, int position, long id) {



        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);



    }

}
