package com.cds_jo.GalaxySalesApp;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.Acc_ReportActivity;
import com.cds_jo.GalaxySalesApp.assist.Cls_Stores;
import com.cds_jo.GalaxySalesApp.assist.Cls_Stores_Adapter;
import com.cds_jo.GalaxySalesApp.assist.CustomerReturnQtyActivity;
import com.cds_jo.GalaxySalesApp.assist.Customer_List;
import com.cds_jo.GalaxySalesApp.assist.Customers;
import com.cds_jo.GalaxySalesApp.assist.OrdersItems;
import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;

import java.util.ArrayList;
import java.util.List;


public class Select_StoreNo extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
    TextView itemnm;
    private SearchView mSearchView;
    EditText filter   ;
    ImageView btn_filter_search ;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form = inflater.inflate(R.layout.select_store, container, false);


        // Get the SearchView and set the searchable configuration
        btn_filter_search =(ImageView) form.findViewById(R.id.brn_seachAcc);
        filter =    (EditText) form.findViewById(R.id.et_Search_filter);


        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Toast.makeText(getActivity(),s.toString(),Toast.LENGTH_SHORT).show();
                onProgressUpdate(s.toString());

            }
        });
        onProgressUpdate("");

        btn_filter_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                onProgressUpdate(filter.getText().toString());
            }
        });



        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            Cls_Stores cc = (Cls_Stores) arg0.getItemAtPosition(position);
                String nm = cc.getSname();
                Exist_Pop();


                if (getArguments().getString("Scr") == "FromStore") {
                    ((TransferQty) getActivity()).Set_Store(cc.getSno(), cc.getSname(),1);
                }

                else if (getArguments().getString("Scr") == "ToStore") {
                    ((TransferQty) getActivity()).Set_Store(cc.getSno(), cc.getSname() ,2);

                }





            }


        });
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return  form;
    }




    public void onProgressUpdate(String t ){
        final List<String> items_ls = new ArrayList<String>();
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        items_Lsit.setAdapter(null);

        String query ;



        if (t.toString().equals("")){
              query = "Select * from stores  ";//   Customers.Cust_type='"+Cust_type+"'";
        }
        else {
            query = "Select * from stores where sname like '%" + t + "%' or  sno like '%" + t + "%'";//  And Customers.Cust_type='"+Cust_type+"'";
        }
        Cursor c = sqlHandler.selectQuery(query);
        ArrayList<Cls_Stores> customersesList = new ArrayList<Cls_Stores>();
        if (c!=null && c.getCount()!=0 ){
            if(c.moveToFirst()){
             do{
                 Cls_Stores s = new Cls_Stores();


                 s.setSno(c.getString(c.getColumnIndex("sno")));
                 s.setSname(c.getString(c.getColumnIndex("sname")));

                 customersesList.add(s);

             }while (c.moveToNext());
            }
            c.close();
        }



            Cls_Stores_Adapter List = new Cls_Stores_Adapter(
                    this.getActivity(), customersesList);

            items_Lsit.setAdapter(List);

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
        }
    }


    public void onListItemClick(ListView l, View v, int position, long id) {



        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);



    }

}
