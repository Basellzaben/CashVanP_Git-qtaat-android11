package com.cds_jo.GalaxySalesApp.warehouse;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;

import java.util.ArrayList;

import Methdes.MyTextView;


public class SearchLookup extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
     TextView Search_filter;
     MyTextView tv_Title ;
    @Override
    public View onCreateView(final LayoutInflater inflater   , ViewGroup container  , Bundle savestate){
        try {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            form = inflater.inflate(R.layout.search_lookup, container, false);
            String TABLE_Nm = (DB.GetValue(getActivity(), "Lookup", "ArDesc", " ItemNo ='0' " +
                    " AND  TableNo ='" + getArguments().getString("TABLE_NO")+"'"));


            tv_Title = (MyTextView) form.findViewById(R.id.tv_Title);
            tv_Title.setText(TABLE_Nm);
            items_Lsit = (ListView) form.findViewById(R.id.listView2);
            Search_filter = (TextView) form.findViewById(R.id.et_Search_filter);


            Search_filter.clearFocus();
            Search_filter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    FillList(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    Cls_Lookup po_obj = (Cls_Lookup) arg0.getItemAtPosition(position);

                    if (getArguments().getString("Scr") == "ItemRecepitSerial") {
                        ((ItemsRecepit) getActivity()).Set_SetSerail(po_obj.getITEM_NO(), po_obj.getDESC_A(), getArguments().getString("ID"));
                    }else if (getArguments().getString("Scr") == "ItemRecepitVendor") {
                        ((ItemsRecepit) getActivity()).Set_SetVendor(po_obj.getITEM_NO(), po_obj.getDESC_A(), getArguments().getString("ID"));
                    }

                    Exist_Pop();

                }


            });

        }
        catch (Exception ex){
            Toast.makeText(getActivity(),ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FillList("");
        return  form;
    }
private  void  FillList(String f){
    ArrayList<Cls_Lookup> cls_maint_card_searches = new ArrayList<Cls_Lookup>();
    cls_maint_card_searches.clear();


    String q = " Select distinct  ArDesc  , ItemNo  From  Lookup " +
            "  Where TableNo ='" + getArguments().getString("TABLE_NO") + "'  AND  ItemNo !='0' " +
            " And  (ArDesc like '%"+f+"%'  or ItemNo like '%"+f+"%')   " +
            " Order By Cast( ItemNo as integer ) asc";

    SqlHandler sqlHandler = new SqlHandler(getActivity());
    Cursor c1 = sqlHandler.selectQuery(q);

    if (c1 != null && c1.getCount() != 0) {
        if (c1.moveToFirst()) {
            do {
                Cls_Lookup obj = new Cls_Lookup();
                obj.setITEM_NO(c1.getString(c1.getColumnIndex("ItemNo")));
                obj.setDESC_A(c1.getString(c1.getColumnIndex("ArDesc")));
                cls_maint_card_searches.add(obj);
            } while (c1.moveToNext());
        }

        c1.close();
    }

    Lookup_Adapter cls_search_po_adapter = new Lookup_Adapter(
            this.getActivity(), cls_maint_card_searches);
    items_Lsit.setAdapter(cls_search_po_adapter);



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



}
