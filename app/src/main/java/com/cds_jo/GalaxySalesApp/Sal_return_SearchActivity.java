package com.cds_jo.GalaxySalesApp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cds_jo.GalaxySalesApp.assist.Sale_ReturnActivity;
import com.cds_jo.GalaxySalesApp.assist.cls_Search_po_return;
import com.cds_jo.GalaxySalesApp.assist.cls_Search_return_Sale_adapter;


import java.util.ArrayList;
public class Sal_return_SearchActivity extends android.app.DialogFragment implements View.OnClickListener {

    View form;
    Button add, cancel;
    ListView items_Lsit;
    // TextView itemnm;

    ArrayList<cls_Search_po_return> cls_search_pos_list;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        form = inflater.inflate(R.layout.fragment_sal_return__search, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().setTitle("البحث في إرجاع المبيعات");


        final TextView Search = (TextView) form.findViewById(R.id.tv_Search);



        items_Lsit = (ListView) form.findViewById(R.id.listView2);
        cls_search_pos_list = new ArrayList<cls_Search_po_return>();
        cls_search_pos_list.clear();


        FillList("");


        Search.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Search.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });


        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//   if(s.toString().length()>0)
                FillList(s.toString());


            }
        });

        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                cls_Search_po_return po_obj = (cls_Search_po_return) arg0.getItemAtPosition(position);
                String nm = po_obj.getAcc();
                ((Sale_ReturnActivity) getActivity()).Set_Order(po_obj.getOrderNo().toString());
                Exist_Pop();
            }


        });
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return form;
    }
    private void FillList(String Filter) {
        String q = "";

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        cls_search_pos_list.clear();
        items_Lsit.setAdapter(null);
        if (Filter.length() == 0) {
            q = " Select distinct s.Net_Total, s.Orderno  ,s.acc ,s.date , s.doctype" +
                    ",   c.name    " +
                    "from  Sal_return_Hdr s  left join Customers c on c.no =s.acc ";
        } else {
            q = "Select  distinct s.Net_Total ,s.Orderno ,s.acc ,s.date, c.name   from  Sal_return _Hdr s left join Customers c on c.no =s.acc" +
                    " Where    c.name like '%" + Filter + "%'   or   s.Nm  like '%" + Filter + "%' ";
        }

        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);
        cls_Search_po_return   obj;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    obj   = new cls_Search_po_return();
                    obj.setOrderNo(c1.getString(c1.getColumnIndex("Orderno")));
                    obj.setname(c1.getString(c1.getColumnIndex("name")));
                    obj.setDate(c1.getString(c1.getColumnIndex("date")));
                    obj.setAcc(c1.getString(c1.getColumnIndex("acc")));
                    obj.setdocType("");
                    obj.setTot(c1.getString(c1.getColumnIndex("Net_Total")));
                    cls_search_pos_list.add(obj);
                } while (c1.moveToNext());
            }
            c1.close();
        }



        TextView count = (TextView) form.findViewById(R.id.tv_Count);
        count.setText(cls_search_pos_list.size() + "");
        cls_Search_return_Sale_adapter cls_Search_return_Sale_adapter_obj = new cls_Search_return_Sale_adapter(
                this.getActivity(), cls_search_pos_list);


        items_Lsit.setAdapter(cls_Search_return_Sale_adapter_obj);
    }

    public void Exist_Pop() {
        this.dismiss();
    }
    @Override
    public void onClick(View v) {
        Button bu = (Button) v;
        if (bu.getText().toString().equals("Cancel")) {
            this.dismiss();
        } else if (bu.getText().toString().equals("Add")) {
            Toast.makeText(getActivity(),
                    "Your Message", Toast.LENGTH_SHORT).show();
        }
    }
    public void onListItemClick(ListView l, View v, int position, long id) {

        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);
    }
}
