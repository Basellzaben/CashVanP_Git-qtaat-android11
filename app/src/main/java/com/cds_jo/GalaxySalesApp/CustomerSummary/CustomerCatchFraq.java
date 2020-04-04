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
public class CustomerCatchFraq extends Fragment {
    cls_CustomerCatch cls_customerCatch;
    CustomerCatchAdabter adabter;
    ListView listView;
    ArrayList<cls_CustomerCatch> list;
    final Handler _handler = new Handler();


    public CustomerCatchFraq() {
        // Required empty public constructor
    }

    String CustAcc,UserID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        {
            View v = inflater.inflate(R.layout.fragment_customer_catch_fraq, container, false);
            listView = (ListView) v.findViewById(R.id.lst_acc);
            list = new ArrayList<>();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            CustAcc = sharedPreferences.getString("CustNo", "");
            UserID = sharedPreferences.getString("UserID", "");

            getData();
            return v;
        }

    }

    private void getData() {
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(getActivity());
                ws.GET_CustReportCatch(CustAcc);
                try {
                    Integer i;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Name = js.getJSONArray("name");
                    JSONArray js_Type = js.getJSONArray("type");
                    JSONArray js_Date = js.getJSONArray("date");
                    JSONArray js_Amt = js.getJSONArray("CustNmEn");
                    JSONArray js_Chaq = js.getJSONArray("amtchaq");

                    cls_customerCatch = new cls_CustomerCatch();
                    for (i = 0; i < js_Name.length(); i++) {
                        cls_customerCatch.setCustName(js_Name.get(i).toString());
                        cls_customerCatch.setVType(js_Type.get(i).toString());
                        cls_customerCatch.setAmt(js_Amt.get(i).toString());
                        cls_customerCatch.setChaq(js_Chaq.get(i).toString());
                        cls_customerCatch.setDate(js_Date.get(i).toString());
                        list.add(cls_customerCatch);
                    }
                    _handler.post(new Runnable() {
                        public void run() {

                            adabter = new CustomerCatchAdabter(getActivity(), list);
                            listView.setAdapter(adabter);
                        }
                    });

                } catch (final Exception e) {

                }

            }
        };
        thread.start();

    }
}