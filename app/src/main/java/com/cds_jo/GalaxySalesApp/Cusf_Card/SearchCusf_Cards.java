package com.cds_jo.GalaxySalesApp.Cusf_Card;

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

import com.cds_jo.GalaxySalesApp.Cls_Search_CusCard;
import com.cds_jo.GalaxySalesApp.Cls_Visit_images_Header;
import com.cds_jo.GalaxySalesApp.CusfCard;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.VisitImges;
import com.cds_jo.GalaxySalesApp.cls_Search_VisitImages_adapter;

import java.util.ArrayList;

public class SearchCusf_Cards extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
     TextView editText4;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        form =inflater.inflate(R.layout.visit_imges_search,container,false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //itemnm = (TextView)form.findViewById(R.id.ed_item_nm);
         items_Lsit=(ListView) form.findViewById(R.id.listView2);
        editText4 =(TextView)form.findViewById(R.id.editText4);
        editText4.clearFocus();
        ArrayList<Cls_Visit_images_Header> cls_search_pos_list  = new ArrayList<Cls_Visit_images_Header>();
        cls_search_pos_list.clear();
        String q = "Select distinct OrderNo,Nm,Tr_Date,Acc      from CusfCard   ";

        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Visit_images_Header obj= new Cls_Visit_images_Header();
                    obj.setOrderNo(c1.getString(c1.getColumnIndex("OrderNo")));
                    obj.setCustName(c1.getString(c1.getColumnIndex("Nm")));
                    obj.setTr_Date(c1.getString(c1.getColumnIndex("Tr_Date")));
                    obj.setCustNo(c1.getString(c1.getColumnIndex("Acc")));

                    cls_search_pos_list.add(obj);
                }while (c1.moveToNext());
            }
        }
        c1.close();


        Cls_Search_CusCard cls_search_po_adapter = new Cls_Search_CusCard(
                this.getActivity(),cls_search_pos_list);
        items_Lsit.setAdapter(cls_search_po_adapter);






        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Cls_Visit_images_Header po_obj = (Cls_Visit_images_Header) arg0.getItemAtPosition(position);
                String nm = po_obj.getCustNo();
               ((CusfCard)getActivity()).Set_Order(po_obj.getOrderNo(), po_obj.getCustName(),po_obj.getCustNo());
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


        // Set the item as checked to be highlighted
        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);

        //conversationAdapter.notifyDataSetChanged();

    }
/*@Override
    public  void OnClick(View view  ){

}*/
}
