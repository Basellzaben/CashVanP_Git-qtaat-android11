package com.cds_jo.GalaxySalesApp.CustomerSummary;


import android.os.Bundle;
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
public class CustomerSellingRequestFraq extends Fragment {
    CustomerSellingRequestAdapter listAdapter;
    ExpandableListView expListView;
    cls_SelingRequest cls_selingRequest;
    cls_SelingRequestC cls_selingRequestC;
    List<cls_SelingRequest> listDataHeader;
    HashMap<List<cls_SelingRequest>, List<cls_SelingRequestC>> listDataChild;

    public CustomerSellingRequestFraq() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        {View v= inflater.inflate(R.layout.fragment_customer_selling_request_fraq, container, false);
        expListView = (ExpandableListView) v.findViewById(R.id.lst_acc);
        getData();
        listAdapter = new CustomerSellingRequestAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        return v;
    }

}
    private void getData() {
        listDataHeader = new ArrayList<cls_SelingRequest>();
        listDataChild = new HashMap<List<cls_SelingRequest>, List<cls_SelingRequestC>>();
        Thread thread = new Thread() {
            @Override
            public void run() {



                CallWebServices ws = new CallWebServices(getActivity());
                ws.GET_CustReportSellingRequest("11041000011");
                try {
                    Integer i;
                    Integer j;
                    String sn="";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Name = js.getJSONArray("name");
                    JSONArray js_Dec = js.getJSONArray("Dec");
                    JSONArray js_Date = js.getJSONArray("date");
                    JSONArray js_TaxNo = js.getJSONArray("TaxNo");
                    JSONArray js_TaxPerc = js.getJSONArray("TaxPerc");
                    JSONArray js_Tot = js.getJSONArray("Tot");
                    JSONArray js_Item_Name = js.getJSONArray("Item_Name");
                    JSONArray js_item_no = js.getJSONArray("item_no");
                    JSONArray js_A_Qty = js.getJSONArray("A_Qty");
                    JSONArray js_price = js.getJSONArray("price");
                    JSONArray js_Bonus = js.getJSONArray("Bonus");
                    JSONArray js_Sales_No = js.getJSONArray("Sales_No");
                    JSONArray js_day = js.getJSONArray("day");


                    //  cls_selingRequest = new cls_SelingRequest();
                    //  cls_selingRequestC = new cls_SelingRequestC();
                    for (i = 0; i < js_Name.length(); i++)
                    {
                        sn=js_Sales_No.get(i).toString();
                        List<cls_SelingRequestC> q = new ArrayList<cls_SelingRequestC>();
                        for (j = 0; j < js_Name.length(); j++)
                        {
                            if(js_Sales_No.get(j).toString()==sn)
                            {
                                q.add(new cls_SelingRequestC(js_price.get(j).toString(),js_Bonus.get(j).toString(),js_Tot.get(j).toString(),js_A_Qty.get(j).toString(),js_Item_Name.get(j).toString(),js_Sales_No.get(j).toString()));
                                if(j==0)
                                {
                                    listDataHeader.add(new cls_SelingRequest(js_Date.get(j).toString(),js_Dec.get(j).toString(),js_TaxNo.get(j).toString(),js_Name.get(j).toString(),js_Tot.get(j).toString(),js_item_no.get(j).toString(),js_TaxPerc.get(j).toString(),js_Sales_No.get(j).toString(),js_day.get(j).toString()));

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
