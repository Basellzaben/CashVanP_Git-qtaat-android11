package com.cds_jo.GalaxySalesApp.CustomerSummary;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

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
public class CustomerSalesPayoffReportFrag extends Fragment {

    CustomerSalesPayoffAdabter listAdapter;
    ExpandableListView expListView;
   String CustAcc;
    List<cls_SalesPayoff> listDataHeader;

    HashMap<List<cls_SalesPayoff>, List<cls_SalesPayoffC>> listDataChild;
    public CustomerSalesPayoffReportFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        {View v= inflater.inflate(R.layout.fragment_customer_sales_payoff_report, container, false);

            expListView = (ExpandableListView) v.findViewById(R.id.lst_acc);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            CustAcc = sharedPreferences.getString("CustNo", "");
            getData();
            listAdapter = new CustomerSalesPayoffAdabter(getActivity(), listDataHeader, listDataChild);

            // setting list adapter
            expListView.setAdapter(listAdapter);
            return v;
    }}
        private void getData() {
            listDataHeader = new ArrayList<cls_SalesPayoff>();
            listDataChild = new HashMap<List<cls_SalesPayoff>, List<cls_SalesPayoffC>>();
            Thread thread = new Thread() {
                @Override
                public void run() {


                    CallWebServices ws = new CallWebServices(getActivity());
                    ws.GET_CustReportSalesPayoff(CustAcc);
                    try {
                        Integer i;
                        Integer j;
                        String sn="";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Name = js.getJSONArray("name");
                        JSONArray js_Dec = js.getJSONArray("Dec");
                        JSONArray js_Date = js.getJSONArray("date");
                        JSONArray js_doc = js.getJSONArray("doc");
                        JSONArray js_totalwithtax = js.getJSONArray("totalwithtax");
                        JSONArray js_Tot = js.getJSONArray("dtotal");
                        JSONArray js_Item_Name = js.getJSONArray("Item_Name");
                        JSONArray js_item_no = js.getJSONArray("item_no");
                        JSONArray js_A_Qty = js.getJSONArray("Qty");
                        JSONArray js_price = js.getJSONArray("price");
                        JSONArray js_Bonus = js.getJSONArray("Bonus");
                        JSONArray js_Sales_No = js.getJSONArray("TotalTax");
                        JSONArray js_cluse = js.getJSONArray("cluse");

                        //  cls_selingRequest = new cls_SelingRequest();
                        //  cls_selingRequestC = new cls_SelingRequestC();
                        for (i = 0; i < js_Name.length(); i++)
                        {
                            sn=js_Sales_No.get(i).toString();
                            List<cls_SalesPayoffC> q = new ArrayList<cls_SalesPayoffC>();
                            for (j = 0; j < js_Name.length(); j++)
                            {
                                if(js_Sales_No.get(j).toString()==sn)
                                {
                                    q.add(new cls_SalesPayoffC(js_price.get(j).toString(),js_Bonus.get(j).toString(),js_totalwithtax.get(j).toString(),js_A_Qty.get(j).toString(),js_Item_Name.get(j).toString()));
                                    if(j==0)
                                    {
                                        listDataHeader.add(new cls_SalesPayoff(js_Date.get(j).toString(),js_Dec.get(j).toString(),js_doc.get(j).toString(),js_Name.get(j).toString(),js_Tot.get(j).toString(),js_item_no.get(j).toString(),js_totalwithtax.get(j).toString(),js_cluse.get(j).toString(),js_Sales_No.get(j).toString()));
                                    }

                                }
                            }
                            listDataChild.put(listDataHeader, q);
                        }

                    } catch (final Exception e) {

                    }

                }
            };
            thread.start();


}}//5454545
