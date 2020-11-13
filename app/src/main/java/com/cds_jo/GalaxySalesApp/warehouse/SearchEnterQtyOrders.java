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
import com.cds_jo.GalaxySalesApp.TransferQty;
import com.cds_jo.GalaxySalesApp.assist.cls_Search_TransQty_adapter;
import com.cds_jo.GalaxySalesApp.cls_Search_TransQty;

import java.util.ArrayList;

public class SearchEnterQtyOrders extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
     TextView editText4;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        form =inflater.inflate(R.layout.enter_qty__search,container,false);
        getDialog().setTitle("البحث في سندات إدخال المواد");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

         items_Lsit=(ListView) form.findViewById(R.id.listView2);
        editText4 =(TextView)form.findViewById(R.id.editText4);
        editText4.clearFocus();
        ArrayList<cls_Search_TransQty> cls_search_pos_list  = new ArrayList<cls_Search_TransQty>();
        cls_search_pos_list.clear();
        String q = "Select distinct po.OrderNo,po.Tr_Date , po.Notes ,s.sname as Fstore  from EnterQtyhdr " +
                "      po Left join stores s on s.sno = po.fromstore    " ;

        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_Search_TransQty obj= new cls_Search_TransQty();

                    obj.setOrderNo( "رقم المستند       : "+ c1.getString(c1.getColumnIndex("OrderNo")));
                    obj.setNo( c1.getString(c1.getColumnIndex("OrderNo")));
                    obj.setFromStore("  المستودع : "+c1.getString(c1.getColumnIndex("Fstore")));
                    obj.setToStore("");
                    obj.setOrderDate( " التاريخ :"+  c1.getString(c1.getColumnIndex("Tr_Date")));
                    obj.setOrderDesc("البيان : "+c1.getString(c1.getColumnIndex("Notes")));
                    cls_search_pos_list.add(obj);
                }while (c1.moveToNext());
            }

        c1.close();

    }
        cls_Search_EnterQty_adapter cls_search_po_adapter = new cls_Search_EnterQty_adapter(
                this.getActivity(),cls_search_pos_list);
        items_Lsit.setAdapter(cls_search_po_adapter);






        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                cls_Search_TransQty po_obj = (cls_Search_TransQty) arg0.getItemAtPosition(position);



                ((EnterQtyActivity)getActivity()).Set_Order(po_obj.getNo());
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
