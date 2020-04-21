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
    ArrayList<cls_CustomerOfCollection> TList;
    CustomerCollectoinAdapter adapter;
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
                    JSONArray name = js.getJSONArray("name");
                    JSONArray Tr_date = js.getJSONArray("Date");
                    JSONArray orderNo = js.getJSONArray("OrderNo");
                    JSONArray Amt = js.getJSONArray("Amt");
                    JSONArray Cash = js.getJSONArray("Cash");
                    JSONArray Notes = js.getJSONArray("notes");
                    JSONArray newAmt = js.getJSONArray("CheckTotal");
                    for (int i = 0; i < name.length(); i++) {
                        TList.add(new cls_CustomerOfCollection(name.get(i).toString(), Tr_date.get(i).toString(), orderNo.get(i).toString(), Amt.get(i).toString(),newAmt.get(i).toString(),Cash.get(i).toString(), Notes.get(i).toString()));
                    }
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
