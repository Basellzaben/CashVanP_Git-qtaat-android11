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
public class CustomerBillFrag extends Fragment {
    CustomerBillAdabter listAdapter;
    ExpandableListView expListView;
    cls_Bill cls_bill;
    cls_BillC cls_billC;
    List<cls_Bill> listDataHeader;
    HashMap<List<cls_Bill>, List<cls_BillC>> listDataChild;
String CustAcc,UserID;
    public CustomerBillFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_bill, container, false);

        expListView = (ExpandableListView) v.findViewById(R.id.lst_acc);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        CustAcc = sharedPreferences.getString("CustNo", "");
        UserID = sharedPreferences.getString("UserID", "");
        getData();
        listAdapter = new CustomerBillAdabter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        return v;
    }
    private void getData() {
        listDataHeader = new ArrayList<cls_Bill>();
        listDataChild = new HashMap<List<cls_Bill>, List<cls_BillC>>();
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
                    JSONArray js_Name = js.getJSONArray("cusname");
                    JSONArray js_Dec = js.getJSONArray("Dec");
                    JSONArray js_Date = js.getJSONArray("date");
                    JSONArray js_sumWithOutTax = js.getJSONArray("SumWithOutTax");
                    JSONArray js_totalwithtax = js.getJSONArray("totalwithtax");
                    JSONArray js_Tot = js.getJSONArray("Tot");
                    JSONArray js_Item_Name = js.getJSONArray("Item_Name");
                    JSONArray js_item_no = js.getJSONArray("item_no");
                    JSONArray js_A_Qty = js.getJSONArray("A_Qty");
                    JSONArray js_price = js.getJSONArray("price");
                    JSONArray js_Bonus = js.getJSONArray("Bonus");
                    JSONArray js_bill = js.getJSONArray("bill");
                    JSONArray js_cluse = js.getJSONArray("cluse");


                    //  cls_selingRequest = new cls_SelingRequest();
                    //  cls_selingRequestC = new cls_SelingRequestC();
                    for (i = 0; i < js_Name.length(); i++)
                    {
                        sn=js_bill.get(i).toString();
                        List<cls_BillC> q = new ArrayList<cls_BillC>();
                        for (j = 0; j < js_Name.length(); j++)
                        {
                            if(js_bill.get(j).toString()==sn)
                            {
                                q.add(new cls_BillC(js_price.get(j).toString(),js_Bonus.get(j).toString(),js_sumWithOutTax.get(j).toString(),js_A_Qty.get(j).toString(),js_Item_Name.get(j).toString(),js_bill.get(j).toString()));
                                if(j==0)
                                {
                                    listDataHeader.add(new cls_Bill(js_Date.get(j).toString(),js_Dec.get(j).toString(),js_sumWithOutTax.get(j).toString(),js_Name.get(j).toString(),js_Tot.get(j).toString(),js_item_no.get(j).toString(),js_totalwithtax.get(j).toString(),js_bill.get(j).toString(),js_cluse.get(j).toString()));

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
    }

}
