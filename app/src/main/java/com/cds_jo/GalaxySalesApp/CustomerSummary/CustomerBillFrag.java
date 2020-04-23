package com.cds_jo.GalaxySalesApp.CustomerSummary;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerBillFrag extends Fragment {
    CustomerBillAdabter listAdapter;
    ListView expListView;
    cls_Bill cls_bill;
    cls_BillC cls_billC;
    final Handler _handler = new Handler();
String[] bill;
    List<cls_Bill> listDataHeader;


String CustAcc,UserID;
    public CustomerBillFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_bill, container, false);
        expListView=(ListView)v.findViewById(R.id.lv);
        listDataHeader = new ArrayList<cls_Bill>();
        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cls_bill=listDataHeader.get(position);
                bill[0]=cls_bill.getBill();
                cls_billC =new cls_BillC();
                sendData.listDataC=new ArrayList<>();
                for(int i=0;i<listDataHeader.size();i++)
                { cls_bill=listDataHeader.get(i);
                    if(bill[0]==cls_bill.getBill())
                    {
                        cls_billC.setA_Qty(cls_bill.getA_Qty());
                        cls_billC.setBonus(cls_bill.getBonus());
                        cls_billC.setItem_Name(cls_bill.getItem_Name());
                        cls_billC.setPrice(cls_bill.getPrice());
                        cls_billC.setSales_No(cls_bill.getBill());
                        cls_billC.settotalwithtax(cls_bill.getTotalwithtax());
                        sendData.listDataC.add(cls_billC);
                    }
                }

                ItemBill exampleDialog = new ItemBill();
                exampleDialog.show((getActivity()).getSupportFragmentManager(), " dialog");

            }
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        CustAcc = sharedPreferences.getString("CustNo", "");
        UserID = sharedPreferences.getString("UserID", "");
        getData();

        // setting list adapter

        return v;
    }
    private void getData() {


        Thread thread = new Thread() {
            @Override
            public void run() {



                CallWebServices ws = new CallWebServices(getActivity());
                ws.GET_CustReportBill(CustAcc);
                try {
                    Integer i;
                    Integer j;
                    String sn="";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Name = js.getJSONArray("name");
                    JSONArray js_Dec = js.getJSONArray("Dec");
                    JSONArray js_Date = js.getJSONArray("date");
                    JSONArray js_sumWithOutTax = js.getJSONArray("sumWithOutTax");
                    JSONArray js_totalwithtax = js.getJSONArray("totalwithtax");
                    JSONArray js_Tot = js.getJSONArray("Tot");
                    JSONArray js_Item_Name = js.getJSONArray("Item_Name");
                    JSONArray js_item_no = js.getJSONArray("item_no");
                    JSONArray js_A_Qty = js.getJSONArray("A_Qty");
                    JSONArray js_price = js.getJSONArray("price");
                    JSONArray js_Bonus = js.getJSONArray("Bonus");
                    JSONArray js_bill = js.getJSONArray("bill");
                    JSONArray js_cluse = js.getJSONArray("cluse");


                    cls_bill = new cls_Bill();
                    for (i = 0; i < js_Name.length(); i++) {
                        cls_bill.setName(js_Name.get(i).toString());
                        cls_bill.setDec(js_Dec.get(i).toString());
                        cls_bill.setSumWithOutTax(js_sumWithOutTax.get(i).toString());
                        cls_bill.setTotalwithtax(js_totalwithtax.get(i).toString());
                        cls_bill.setTot(js_Tot.get(i).toString());
                        cls_bill.setItem_Name(js_Item_Name.get(i).toString());

                        cls_bill.setDate(js_Date.get(i).toString());
                        cls_bill.setItem_no(js_item_no.get(i).toString());
                        cls_bill.setA_Qty(js_A_Qty.get(i).toString());
                        cls_bill.setPrice(js_price.get(i).toString());
                        cls_bill.setBonus(js_Bonus.get(i).toString());
                        cls_bill.setBill(js_bill.get(i).toString());

                        cls_bill.setDate(js_cluse.get(i).toString());
                        listDataHeader.add(cls_bill);
                    }
                    _handler.post(new Runnable() {
                        public void run() {

                            listAdapter = new CustomerBillAdabter(getActivity(), listDataHeader);
                            expListView.setAdapter(listAdapter);
                        }
                    });


                } catch (final Exception e) {

                }

            }
        };
        thread.start();
    }

}
