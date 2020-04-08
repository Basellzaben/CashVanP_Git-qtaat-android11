package com.cds_jo.GalaxySalesApp.Reports;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
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

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;

import java.util.ArrayList;
import java.util.List;

public class Select_Country  extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
    TextView itemnm;
    private SearchView mSearchView;
    EditText filter   ;
    ImageView btn_filter_search ;
    @Override
    public View onCreateView(final LayoutInflater inflater   , ViewGroup container  , Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form = inflater.inflate(R.layout.layout_select_country, container, false);

        getDialog().setTitle("Galaxy International Group");
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP|Gravity.CENTER);

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
                /*arg1.setBackgroundColor(Color.GREEN);
                Object o = items_Lsit.getItemAtPosition(position);
                String str=(String)o;//As you are using Default String Adapter*/
                cls_man customers = (cls_man) arg0.getItemAtPosition(position);
                String nm = customers.getName();
                Exist_Pop();
                //Toast.makeText(getActivity(), nm, Toast.LENGTH_LONG).show();

                if (getArguments().getString("Scr") == "Rep_Cout") {
                    ((Report_Home) getActivity()).Set_Country(customers.getNo(), customers.getName());

                }


                // Exist_Pop();
                //getDialog().setTitle(str);
                ////itemnm.setText(str);
            }


        });
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return  form;
    }


    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);

        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }


    public void onProgressUpdate(String t ){
        final List<String> items_ls = new ArrayList<String>();
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        items_Lsit.setAdapter(null);

        String query ;




        if (t.toString().equals("")){
            query = "Select * from Country  ";//   Customers.Cust_type='"+Cust_type+"'";
        }
        else {
            query = "Select * from Country where CountryName like '%" + t + "%' or  no like '%" + t + "%'";//  And Customers.Cust_type='"+Cust_type+"'";


        }

        Cursor c = sqlHandler.selectQuery(query);
        ArrayList<cls_Country> customersesList = new ArrayList<cls_Country>();
        if (c!=null && c.getCount()!=0 ){
            if(c.moveToFirst()){
                do{
                    cls_Country     customers = new cls_Country();

                    customers.setNo(c.getString(c.getColumnIndex("CountryNo")));
                    customers.setName(c.getString(c.getColumnIndex("CountryName")));
                    customersesList.add(customers);

                }while (c.moveToNext());
            }
            c.close();
        }




        Countrt_Adapter Customer_List_adapter = new Countrt_Adapter(
                this.getActivity(), customersesList);

        items_Lsit.setAdapter(Customer_List_adapter);


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