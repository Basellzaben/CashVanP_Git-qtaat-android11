package com.cds_jo.GalaxySalesApp.AbuHaltam;

import android.app.DialogFragment;
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

import com.cds_jo.GalaxySalesApp.Delivery_VoucherAct;
import com.cds_jo.GalaxySalesApp.EditeTransActivity;
import com.cds_jo.GalaxySalesApp.Pos.Pos_Activity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.CustomerReturnQtyActivity;
import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;
import com.cds_jo.GalaxySalesApp.assist.cls_Search_Inv_Sale_adapter;
import com.cds_jo.GalaxySalesApp.assist.cls_Search_po;

import java.util.ArrayList;

public class Sal_Dev_SearchActivity  extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
    // TextView itemnm;

    String DocType ="1";
    ArrayList<cls_Search_Dev> cls_search_pos_list  ;
    @Override
    public View onCreateView(final LayoutInflater inflater   , ViewGroup container  , Bundle savestate){
        form =inflater.inflate(R.layout.activity_del__inv__search,container,false);
        DocType = getArguments().getString("doctype") ;


            getDialog().setTitle("البحث في أمر التسليم");



        final TextView Search = (TextView)form.findViewById(R.id.tv_Search)  ;
        //  Search.setFocusable(false);
        // Search.setEnabled(false);
        //  Search.setCursorVisible(false);

       /* add =(Button)form.findViewById(R.id.btn_add);
        cancel=(Button)form.findViewById(R.id.btn_cancel);
        add.setOnClickListener(this);
        cancel.setOnClickListener(this);
*/
        DocType = getArguments().getString("doctype") ;


        //itemnm = (TextView)form.findViewById(R.id.ed_item_nm);
        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        cls_search_pos_list  = new ArrayList<cls_Search_Dev>();
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
                /*arg1.setBackgroundColor(Color.GREEN);
                Object o = items_Lsit.getItemAtPosition(position);
                String str=(String)o;//As you are using Default String Adapter*/
                cls_Search_Dev po_obj = (cls_Search_Dev) arg0.getItemAtPosition(position);
                String nm = po_obj.getCustNo();

                if (getArguments().getString("Scr") == "Dev_inv") {

                            ((Delivery_VoucherAct) getActivity()).Set_Order(po_obj.getDoc_no().toString());
                    Exist_Pop();

                }
            }


        });
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return  form;
    }


    private void FillList(String Filter){
        String q ="";
/*        if (getArguments().getString("typ") == "0") {
            q = "Select s.OrderNo ,s.acc ,s.date , s.Nm as  name  from  Sal_invoice_Hdr s  where s.inovice_type='0'";
        }
        else
        {
              q = "Select s.OrderNo ,s.acc ,s.date ,   CASE s.inovice_type WHEN '-1' THEN  c.name ELSE s.Nm END as  name   from  Sal_invoice_Hdr s inner join Customers c on c.no =s.acc where s.inovice_type='-1' ";

        }*/
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        cls_search_pos_list.clear();
        items_Lsit.setAdapter(null);
        if (Filter.length()==0) {
            q = "Select distinct Doc_No,cus_name ,cus " +
                    "from Inv_Delivery";
        }else {
            q = "Select distinct Doc_No,cus_name ,cus " +
                    "from Inv_Delivery"+
            " where cus like '%" + Filter + "%'   or   cus_name  like '%" + Filter + "%'  or   Doc_No  like '%" + Filter + "%' " ;
         /*   q = "Select  distinct s.Net_Total ,s.OrderNo ,s.acc ,s.date , s.inovice_type,  CASE s.inovice_type WHEN '-1' THEN  c.name ELSE s.Nm END as  name   from  Sal_invoice_Hdr s left join Customers c on c.no =s.acc" +
                    " Where  ifnull(s.doctype,'1')='"+DocType + "' UserID='"+sharedPreferences.getString("UserID","") +"'" +
                   */
        }

        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_Search_Dev cls_searchpos= new cls_Search_Dev();

                    cls_searchpos.setDoc_no(c1.getString(c1.getColumnIndex("Doc_No")));
                    cls_searchpos.setCustNm(c1.getString(c1.getColumnIndex("cus_name")));

                    cls_searchpos.setCustNo(c1.getString(c1.getColumnIndex("cus")));


                    cls_search_pos_list.add(cls_searchpos);
                }while (c1.moveToNext());
            }
        }
        c1.close();


        TextView count = (TextView)form.findViewById(R.id.tv_Count);
        count.setText(  cls_search_pos_list.size() +"");
        cls_Search_Del_Sale_adapter cls_Search_invSale_adapter_obj = new cls_Search_Del_Sale_adapter(
                this.getActivity(),cls_search_pos_list);


        items_Lsit.setAdapter(cls_Search_invSale_adapter_obj);
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
            Toast.makeText(getActivity(),
                    "Your Message", Toast.LENGTH_SHORT).show();
            //   ((OrdersItems)getActivity()).Save_Method("maen");


        }


    }


    public void onListItemClick(ListView l, View v, int position, long id) {


        // Set the item as checked to be highlighted
        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);

        //conversationAdapter.notifyDataSetChanged();

    }

}
