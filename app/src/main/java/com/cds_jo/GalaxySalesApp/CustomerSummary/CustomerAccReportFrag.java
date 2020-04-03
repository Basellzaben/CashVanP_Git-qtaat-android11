package com.cds_jo.GalaxySalesApp.CustomerSummary;


import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_Acc_Report;
import com.cds_jo.GalaxySalesApp.assist.Cls_Acc_Report_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerAccReportFrag extends Fragment {
    TextView Ball ;
    TextView CusTop;
    TextView NetBall;
    ListView items_Lsit;
    DecimalFormat Per;
    String   CUST_NET_BAL ="";
    ArrayList<Cls_Acc_Report> cls_acc_reportsList;

    public CustomerAccReportFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_customer_acc_report, container, false);
        Ball = (TextView)v.findViewById(R.id.Ball);
        CusTop = (TextView)v.findViewById(R.id.tv_CusTop);
        NetBall = (TextView)v.findViewById(R.id.NetBall);
        items_Lsit = (ListView) v.findViewById(R.id.lst_acc);
        cls_acc_reportsList = new ArrayList<Cls_Acc_Report>();
        return v;
    }
    public void showData(){



        final List<String> items_ls = new ArrayList<String>();
        items_Lsit.setAdapter(null);



        final Handler _handler = new Handler();



        cls_acc_reportsList.clear();

        Ball.setText("");
        CusTop.setText("");
        NetBall.setText("");


        Locale locale ;
        locale   = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        Per= new DecimalFormat("0.000");

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws  = new CallWebServices(getActivity());
                ws.GetAccReportSummary("123");


                try {
                    float t_dept , t_cred,t_bb ,tot ,t_tot,temp,t_rate;
                    t_dept= t_cred=t_bb =tot =t_tot =temp=t_rate =  0 ;
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);


                    JSONArray js_doc_num= js.getJSONArray("doc_num");
                    JSONArray js_doctype= js.getJSONArray("doctype");
                    JSONArray js_cur_no= js.getJSONArray("cur_no");
                    JSONArray js_date= js.getJSONArray("date");
                    JSONArray js_des= js.getJSONArray("des");
                    JSONArray js_bb= js.getJSONArray("bb");
                    JSONArray js_dept= js.getJSONArray("dept");
                    JSONArray js_cred= js.getJSONArray("cred");
                    JSONArray js_rate= js.getJSONArray("rate");



                    Cls_Acc_Report cls_acc_report ;



                    // date,fromstore,tostore,des,docno,itemno,qty,UnitNo,UnitRate,myear





                    for( i =0 ; i<js_doc_num.length();i++)
                    {
                        cls_acc_report = new Cls_Acc_Report();

                        cls_acc_report.setDoc_num(js_doc_num.get(i).toString());
                        cls_acc_report.setDoctype(js_doctype.get(i).toString());
                        cls_acc_report.setCur_no(js_cur_no.get(i).toString());
                        cls_acc_report.setDate(js_date.get(i).toString());
                        cls_acc_report.setDes(js_des.get(i).toString());
                        if(i==0)
                            cls_acc_report.setBb(js_bb.get(i).toString());
                        else
                            cls_acc_report.setBb("0.000");
                        cls_acc_report.setDept(js_dept.get(i).toString());
                        cls_acc_report.setCred(js_cred.get(i).toString());
                        if( Float.parseFloat( js_dept.get(i).toString())>0) {
                            temp = Float.parseFloat(js_dept.get(i).toString()) * Float.parseFloat(js_rate.get(i).toString());
                        } else {
                            temp = Float.parseFloat(js_cred.get(i).toString()) * Float.parseFloat(js_rate.get(i).toString());
                        }




                        cls_acc_report.setRate(( Per.format(temp)  + ""));
                        t_rate = t_rate + temp;
                        t_dept  = t_dept +  Float.parseFloat( js_dept.get(i).toString());
                        t_cred  = t_cred +  Float.parseFloat( js_cred.get(i).toString());
                        t_bb  =  Float.parseFloat( js_bb.get(i).toString());


                        if(i==0)
                            tot= tot +   (Float.parseFloat( js_dept.get(i).toString()) +   Float.parseFloat( js_bb.get(0).toString())  -   Float.parseFloat( js_cred.get(i).toString()));
                        else
                            tot= tot +   (Float.parseFloat( js_dept.get(i).toString())  -   Float.parseFloat( js_cred.get(i).toString()));


                        cls_acc_report.setTot(String.valueOf(Per.format(tot)));

                        cls_acc_reportsList.add(cls_acc_report);




                    }



                    Locale locale ;
                    locale   = new Locale("ar");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());

                    final int total = i;
                    final  String   txtCheqBal = js.getString("CheqBal");
                    final  String   txtBall = js.getString("Ball");
                    final  String  txtCusTop = js.getString("CusTop");
                    final  String   txtNetBall = js.getString("NetBall");




                    final  String   S_t_bb =  String.valueOf(t_bb);
                    final  String   S_dept =  String.valueOf(t_dept);
                    final  String   S_cred =  String.valueOf(t_cred);
                    final  String   S_rate =  String.valueOf(t_rate);
                    final  String   S_tot =  String.valueOf(tot);


                    _handler.post(new Runnable() {

                        public void run() {


                            Ball.setText(txtBall);
                            CusTop.setText(txtCusTop);
                            NetBall.setText(txtNetBall);

                            Cls_Acc_Report cls_acc_report1 = new Cls_Acc_Report();
                            cls_acc_report1 = new Cls_Acc_Report();
                            cls_acc_report1.setDate("عدد الحركات");
                            cls_acc_report1.setCur_no((cls_acc_reportsList.size()-1)+"");
                            cls_acc_report1.setDoctype("");
                            cls_acc_report1.setDoc_num("");
                            cls_acc_report1.setDes("المجموع");
                            cls_acc_report1.setBb(Per.format(Double.parseDouble( S_t_bb))+"" );
                            cls_acc_report1.setDept( Per.format(Double.parseDouble( S_dept))+"");
                            cls_acc_report1.setCred(Per.format(Double.parseDouble(S_cred))+"");
                            cls_acc_report1.setRate(Per.format(Double.parseDouble( S_rate))+"");
                            cls_acc_report1.setTot(CUST_NET_BAL);


                            cls_acc_reportsList.add(cls_acc_report1);

                            Cls_Acc_Report_Adapter cls_acc_report_adapter = new Cls_Acc_Report_Adapter(
                                    getActivity(), cls_acc_reportsList);

                            items_Lsit.setAdapter(cls_acc_report_adapter);

                            // alertDialog.show();


                        }
                    });

                } catch (final Exception e) {


                }


            }
        }).start();




    }
}
