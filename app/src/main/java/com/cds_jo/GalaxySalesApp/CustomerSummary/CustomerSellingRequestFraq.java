package com.cds_jo.GalaxySalesApp.CustomerSummary;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
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
public class CustomerSellingRequestFraq extends Fragment {
    CustomerSellingRequestAdapter listAdapter;
    ListView expListView;
    cls_SelingRequest cls_selingRequest;
    cls_SelingRequestC cls_selingRequestC;
    List<cls_SelingRequest> listDataHeader;
    final Handler _handler = new Handler();
    String[] SellingRequest;
    public CustomerSellingRequestFraq() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        {View v= inflater.inflate(R.layout.fragment_customer_selling_request_fraq, container, false);
            expListView = (ListView) v.findViewById(R.id.lst_acc);
            getData();
            expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cls_selingRequest=listDataHeader.get(position);
                    SellingRequest[0]=cls_selingRequest.getSales_No();
                    cls_selingRequestC =new cls_SelingRequestC();
                    sendData.listDatac1=new ArrayList<>();
                    for(int i=0;i<listDataHeader.size();i++)
                    { cls_selingRequest=listDataHeader.get(i);
                        if(SellingRequest[0]==cls_selingRequest.getDec())
                        {
                            cls_selingRequestC.setA_Qty(cls_selingRequest.getA_Qty());
                            cls_selingRequestC.setBonus(cls_selingRequest.getBonus());
                            cls_selingRequestC.setItem_Name(cls_selingRequest.getItem_Name());
                            cls_selingRequestC.setPrice(cls_selingRequest.getPrice());
                            cls_selingRequestC.setSales_No(cls_selingRequest.getSales_No());
                            cls_selingRequestC.setTot(cls_selingRequest.getTot());
                            sendData.listDatac2.add(cls_selingRequestC);
                        }
                    }

                    ItemSalesPayoff exampleDialog = new ItemSalesPayoff();
                    exampleDialog.show((getActivity()).getSupportFragmentManager(), " dialog");

                }
            });
            return v;
        }

    }
    private void getData() {
        listDataHeader = new ArrayList<cls_SelingRequest>();

        Thread thread = new Thread() {
            @Override
            public void run() {



                CallWebServices ws = new CallWebServices(getActivity());
                ws.GET_CustReportSellingRequest("11041000011");
                try{
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


                    cls_selingRequest = new cls_SelingRequest();
                    //  cls_selingRequestC = new cls_SelingRequestC();
                    for (i = 0; i < js_Name.length(); i++)
                    {
                        cls_selingRequest.setName(js_Name.get(i).toString());
                        cls_selingRequest.setDec(js_Dec.get(i).toString());
                        cls_selingRequest.setTaxPerc(js_TaxPerc.get(i).toString());
                        cls_selingRequest.setTot(js_Tot.get(i).toString());
                        cls_selingRequest.setItem_Name(js_Item_Name.get(i).toString());

                        cls_selingRequest.setDate(js_Date.get(i).toString());
                        cls_selingRequest.setItem_no(js_item_no.get(i).toString());
                        cls_selingRequest.setA_Qty(js_A_Qty.get(i).toString());
                        cls_selingRequest.setPrice(js_price.get(i).toString());
                        cls_selingRequest.setBonus(js_Bonus.get(i).toString());
                        cls_selingRequest.setSales_No(js_Sales_No.get(i).toString());
                        cls_selingRequest.setTaxNo(js_TaxNo.get(i).toString());

                        cls_selingRequest.setDay(js_day.get(i).toString());
                        listDataHeader.add(cls_selingRequest);
                    }
                    _handler.post(new Runnable() {
                        public void run() {

                            listAdapter = new CustomerSellingRequestAdapter(getActivity(), listDataHeader);
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