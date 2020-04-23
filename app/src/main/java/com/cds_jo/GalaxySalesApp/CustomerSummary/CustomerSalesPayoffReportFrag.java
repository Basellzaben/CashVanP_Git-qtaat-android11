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
public class CustomerSalesPayoffReportFrag extends Fragment {

    CustomerSalesPayoffAdabter listAdapter;
    ListView expListView;
    cls_SalesPayoff cls_salesPayoff;
    cls_SalesPayoffC cls_salesPayoffC;
    List<cls_SalesPayoff> listDataHeader;
    String[] salesPayoff;
    final Handler _handler = new Handler();
    public CustomerSalesPayoffReportFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        {View v= inflater.inflate(R.layout.fragment_customer_sales_payoff_report, container, false);

            expListView = (ListView) v.findViewById(R.id.lst_acc);
            getData();
            expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cls_salesPayoff=listDataHeader.get(position);
                    salesPayoff[0]=cls_salesPayoff.getDec();
                    cls_salesPayoffC =new cls_SalesPayoffC();
                    sendData.listDatac1=new ArrayList<>();
                    for(int i=0;i<listDataHeader.size();i++)
                    { cls_salesPayoff=listDataHeader.get(i);
                        if(salesPayoff[0]==cls_salesPayoff.getDec())
                        {
                            cls_salesPayoffC.setA_Qty(cls_salesPayoff.getA_Qty());
                            cls_salesPayoffC.setBonus(cls_salesPayoff.getBonus());
                            cls_salesPayoffC.setItem_Name(cls_salesPayoff.getItem_Name());
                            cls_salesPayoffC.setPrice(cls_salesPayoff.getPrice());
                            cls_salesPayoffC.setDec(cls_salesPayoff.getDec());
                            cls_salesPayoffC.setTotalwithtax(cls_salesPayoff.getTotalwithtax());
                            sendData.listDatac1.add(cls_salesPayoffC);
                        }
                    }

                    ItemSalesPayoff exampleDialog = new ItemSalesPayoff();
                    exampleDialog.show((getActivity()).getSupportFragmentManager(), " dialog");

                }
            });
            return v;
        }}
    private void getData() {
        listDataHeader = new ArrayList<cls_SalesPayoff>();

        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(getActivity());
                ws.GET_CustReportSalesPayoff("11041000011");
                try {
                    Integer i;
                    Integer j;
                    String sn="";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Name = js.getJSONArray("name");
                    JSONArray js_Dec = js.getJSONArray("Dec");
                    JSONArray js_Date = js.getJSONArray("date");

                    JSONArray js_totalwithtax = js.getJSONArray("totalwithtax");
                    JSONArray js_Tot = js.getJSONArray("dtotal");
                    JSONArray js_Item_Name = js.getJSONArray("Item_Name");
                    JSONArray js_item_no = js.getJSONArray("item_no");
                    JSONArray js_A_Qty = js.getJSONArray("Qty");
                    JSONArray js_price = js.getJSONArray("price");
                    JSONArray js_Bonus = js.getJSONArray("Bonus");
                    JSONArray js_cluse = js.getJSONArray("cluse");
                    JSONArray note = js.getJSONArray("des");

                    //  cls_selingRequest = new cls_SelingRequest();
                    //  cls_selingRequestC = new cls_SelingRequestC();
                    cls_salesPayoff = new cls_SalesPayoff();
                    for (i = 0; i < js_Name.length(); i++) {
                        cls_salesPayoff = new cls_SalesPayoff();
                        cls_salesPayoff.setName(js_Name.get(i).toString());
                        cls_salesPayoff.setDec(js_Dec.get(i).toString());
                        cls_salesPayoff.setTotalwithtax(js_totalwithtax.get(i).toString());
                        cls_salesPayoff.setTot(js_Tot.get(i).toString());
                        cls_salesPayoff.setItem_Name(js_Item_Name.get(i).toString());
                        cls_salesPayoff.setDate(js_Date.get(i).toString());
                        cls_salesPayoff.setItem_no(js_item_no.get(i).toString());
                        cls_salesPayoff.setA_Qty(js_A_Qty.get(i).toString());
                        cls_salesPayoff.setPrice(js_price.get(i).toString());
                        cls_salesPayoff.setBonus(js_Bonus.get(i).toString());
                        cls_salesPayoff.setDes(note.get(i).toString());
                        cls_salesPayoff.setDate(js_cluse.get(i).toString());
                        listDataHeader.add(cls_salesPayoff);
                    }
                    _handler.post(new Runnable() {
                        public void run() {

                            listAdapter = new CustomerSalesPayoffAdabter(getActivity(), listDataHeader);
                            expListView.setAdapter(listAdapter);
                        }
                    });



                } catch (final Exception e) {

                }

            }
        };
        thread.start();


    }}
