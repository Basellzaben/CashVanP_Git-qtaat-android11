package com.cds_jo.GalaxySalesApp.CustomerSummary;


import android.os.Bundle;
import android.os.Handler;
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
public class CustomerManVisitFraq extends Fragment {

    cls_ManVisit cls_manVisit;
    CustomerManVisitAdabter adabter;
    ListView listView;
    ArrayList<cls_ManVisit> list;
    final Handler _handler = new Handler();
    public CustomerManVisitFraq() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_customer_man_visit_fraq, container, false);


        listView =(ListView)v.findViewById(R.id.lst_acc);
        list=new ArrayList<>();
        getData();
        return v;
    }
    private void getData() {
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(getActivity());
                ws.GET_CustReportManVisit("11041000011");
                try {
                    Integer i;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Name = js.getJSONArray("name");
                    JSONArray js_Start_Time = js.getJSONArray("Start_Time");
                    JSONArray js_Date = js.getJSONArray("date");
                    JSONArray js_DayNum = js.getJSONArray("DayNum");
                    JSONArray js_End_Time = js.getJSONArray("End_Time");
                    JSONArray js_Note = js.getJSONArray("Note");
                    JSONArray js_no= js.getJSONArray("no");

                    cls_manVisit = new cls_ManVisit();
                    for (i = 0; i < js_Name.length(); i++) {
                        cls_manVisit.setName(js_Name.get(i).toString());
                        cls_manVisit.setStart(js_Start_Time.get(i).toString());
                        cls_manVisit.setDay(js_DayNum.get(i).toString());
                        cls_manVisit.setEnd(js_End_Time.get(i).toString());
                        cls_manVisit.setNo(js_no.get(i).toString());
                        cls_manVisit.setNote(js_Note.get(i).toString());

                        cls_manVisit.setDate(js_Date.get(i).toString());
                        list.add(cls_manVisit);
                    }
                    _handler.post(new Runnable() {
                        public void run() {

                            adabter = new CustomerManVisitAdabter(getActivity(), list);
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
