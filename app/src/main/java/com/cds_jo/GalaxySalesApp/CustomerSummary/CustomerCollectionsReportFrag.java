package com.cds_jo.GalaxySalesApp.CustomerSummary;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerCollectionsReportFrag extends Fragment {
    ArrayList<cls_CustomerOfCollection1> TList;
    CustomerCollectoinAdapter adapter;
    cls_CustomerOfCollection1 cls_customerOfCollection1;
    ListView lv;
    String CustAcc ;
    final Handler _handler = new Handler();


    public CustomerCollectionsReportFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_customer_collections_report, container, false);

        lv=(ListView)v.findViewById(R.id.k) ;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        CustAcc = sharedPreferences.getString("CustNo", "");
        getData();

        return v;
    }
    private void getData() {

        TList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(getActivity());
                ws.GET_CustReportCollections(CustAcc);
                try {
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray NameCust = js.getJSONArray("NameCust");
                    JSONArray  Date = js.getJSONArray("Date");
                    JSONArray new_Tr_date = js.getJSONArray("NewTr_date");
                    JSONArray custNo = js.getJSONArray("CustNo");
                    JSONArray orderNo = js.getJSONArray("orderNo");
                    JSONArray Amt = js.getJSONArray("Amt");
                    JSONArray InoviceAmt = js.getJSONArray("InoviceAmt");
                    JSONArray Notes = js.getJSONArray("Notes");
                    JSONArray SupervisorNutes = js.getJSONArray("SupervisorNutes");
                    JSONArray newAmt = js.getJSONArray("NewAmt");//
                    JSONArray orderDate = js.getJSONArray("Order_date");
                    cls_customerOfCollection1=new cls_CustomerOfCollection1();
                    for (int i = 0; i < NameCust.length(); i++) {
//**
                        cls_customerOfCollection1.setAmt(Amt.get(i).toString());
                        cls_customerOfCollection1.setNameCust(NameCust.get(i).toString());
                        cls_customerOfCollection1.setDate(Date.get(i).toString());
                        cls_customerOfCollection1.setNewTr_date(new_Tr_date.get(i).toString());
                        cls_customerOfCollection1.setCustNo(custNo.get(i).toString());
                        cls_customerOfCollection1.setOrderNo(orderNo.get(i).toString());
                        cls_customerOfCollection1.setInoviceAmt(InoviceAmt.get(i).toString());
                        cls_customerOfCollection1.setNotes(Notes.get(i).toString());
                        cls_customerOfCollection1.setSupervisorNutes(SupervisorNutes.get(i).toString());
                        cls_customerOfCollection1.setNewAmt(newAmt.get(i).toString());
                        cls_customerOfCollection1.setOrder_date(orderDate.get(i).toString());
                        TList.add(cls_customerOfCollection1);
//**
                    }//
                    _handler.post(new Runnable() {
                        public void run() {

                            adapter = new CustomerCollectoinAdapter(getActivity(), TList);
                            lv.setAdapter(adapter);
                        }
                    });


                    //  mo(TList);
                } catch (Exception e) {

                }


            }

        }).start();
    }


}
