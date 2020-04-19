package com.cds_jo.GalaxySalesApp.CustomerSummary;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import Methdes.MyTextView;

public class CustomerInformationFragment extends Fragment {
    MyTextView Customer_No,Custumer_Name,Custumer_Phone,Custumer_Fax,Custumer_Email,Custumer_Id,Custumer_Type,Custumer_Address;
    MyTextView Man_Name,Custumer_category,Custumer_Date_Start,Custumer_Connect_with,Facilities_ceiling,Ceiling_checks,Custumer_status,Custumer_Buy;
    MyTextView Custumer_collector,Custumer_away_Buy,Custumer,Custumer_No_Tax,Custumer_Photo,Custumer_up_order,Custumer_discount,discount_percentage;

    String Latitude,Longitude,CustAcc;
    SqlHandler sqlHandler;
    public CustomerInformationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        {View v =inflater.inflate(R.layout.fragment_customer_information, container, false);

            Customer_No=(MyTextView)v.findViewById(R.id.Customer_No);
            Custumer_Name=(MyTextView)v.findViewById(R.id.Custumer_Name);
            Custumer_Phone=(MyTextView)v.findViewById(R.id.Custumer_Phone);
            Custumer_Email=(MyTextView)v.findViewById(R.id.Custumer_ُُEmail);
            Custumer_Type=(MyTextView)v.findViewById(R.id.Custumer_Type);
            Custumer_Fax=(MyTextView)v.findViewById(R.id.Custumer_Fax);
            Custumer_Id=(MyTextView)v.findViewById(R.id.Custumer_Id);
            Custumer_Address=(MyTextView)v.findViewById(R.id.Custumer_ِِAddress);


            Man_Name=(MyTextView)v.findViewById(R.id.Man_Name);
            Custumer_category=(MyTextView)v.findViewById(R.id.Custumer_category);
            Custumer_Date_Start=(MyTextView)v.findViewById(R.id.Custumer_Date_Start);
            Custumer_Connect_with=(MyTextView)v.findViewById(R.id.Custumer_Connect_with);
            Facilities_ceiling=(MyTextView)v.findViewById(R.id.Facilities_ceiling);
            Ceiling_checks=(MyTextView)v.findViewById(R.id.Ceiling_checks);
            Custumer_status=(MyTextView)v.findViewById(R.id.Custumer_status);
            Custumer_Buy=(MyTextView)v.findViewById(R.id.Custumer_Buy);


            Custumer_collector=(MyTextView)v.findViewById(R.id.Custumer_collector);
            Custumer_away_Buy=(MyTextView)v.findViewById(R.id.Custumer_away_Buy);
            Custumer=(MyTextView)v.findViewById(R.id.Custumer);
            Custumer_No_Tax=(MyTextView)v.findViewById(R.id.Custumer_No_Tax);
            Custumer_Photo=(MyTextView)v.findViewById(R.id.Custumer_Photo);
            Custumer_up_order=(MyTextView)v.findViewById(R.id.Custumer_up_order);
            Custumer_discount=(MyTextView)v.findViewById(R.id.Custumer_discount);
            discount_percentage=(MyTextView)v.findViewById(R.id.discount_percentage);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            CustAcc = sharedPreferences.getString("CustNo", "");
            sqlHandler =new SqlHandler(getActivity());
            showData();
        return v;
    }}
        private void showData() {
            final int i=0;

            final Handler _handler = new Handler();

            final Thread thread = new Thread() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(getActivity());
                    ws.GET_CustReportInformationCust(CustAcc);
                    try {

                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Name = js.getJSONArray("name");
                        JSONArray js_email = js.getJSONArray("email");
                        JSONArray js_Date = js.getJSONArray("date");
                        JSONArray js_mob = js.getJSONArray("mob");
                        JSONArray js_Man = js.getJSONArray("SMan");
                        JSONArray js_No = js.getJSONArray("No");
                        JSONArray js_AllowinvoiceWithFlag= js.getJSONArray("AllowinvoiceWithFlag");
                        JSONArray js_TaxSts = js.getJSONArray("TaxSts");
                        JSONArray js_State = js.getJSONArray("State");
                        JSONArray js_Is_Approved = js.getJSONArray("Is_Approved");
                        JSONArray js_Tel = js.getJSONArray("Tel");
                        JSONArray js_Fax = js.getJSONArray("Fax");
                        JSONArray js_NaNo = js.getJSONArray("NaNo");
                        JSONArray js_CHECK_IMG= js.getJSONArray("CHECK_IMG");
                        JSONArray js_Address = js.getJSONArray("Address");
                        JSONArray js_clename = js.getJSONArray("clename");
                        JSONArray js_CType_name = js.getJSONArray("CType_name");
                        JSONArray js_Opt_Name = js.getJSONArray("Opt_Name");
                        JSONArray js_CatName = js.getJSONArray("CatName");
                        JSONArray js_Person = js.getJSONArray("Person");
                        JSONArray js_Discount_Percent= js.getJSONArray("Discount_Percent");
                        JSONArray js_Pay_How= js.getJSONArray("Pay_How");
                        JSONArray js_Celing= js.getJSONArray("Celing");
                        JSONArray js_Chqceling= js.getJSONArray("Chqceling");
                        JSONArray Js_Latitude= js.getJSONArray("Latitude");
                        JSONArray js_Longitude= js.getJSONArray("Longitude");




                        Customer_No.setText(js_No.get(0).toString());
                        Custumer_Name.setText(js_Name.get(0).toString());
                        Custumer_Phone.setText(js_mob.get(0).toString()+"/"+js_Tel.get(0).toString());
                        Custumer_Type.setText(js_CType_name.get(0).toString());
                        Custumer_Email.setText(js_email.get(0).toString());
                        Custumer_Fax.setText(js_Fax.get(0).toString());
                        Custumer_Id.setText(js_NaNo.get(0).toString());
                        Custumer_Address.setText(js_Address.get(0).toString());


                        Man_Name.setText(js_Man.get(0).toString());
                        Custumer_category.setText(js_CatName.get(0).toString());
                        Custumer_Date_Start.setText(js_Date.get(0).toString());
                        Custumer_Connect_with.setText(js_Person.get(0).toString());
                        Facilities_ceiling.setText(js_Celing.get(0).toString());
                        Ceiling_checks.setText(js_Chqceling.get(0).toString());
                        if(Integer.parseInt(js_State.get(0).toString())==1)
                        {
                            Custumer_status.setText("مفتوح");

                        }
                        else if(Integer.parseInt(js_State.get(0).toString())==2)
                        {
                            Custumer_status.setText("معلق");

                        }
                        else if(Integer.parseInt(js_State.get(0).toString())==3)
                        {
                            Custumer_status.setText("ملغي");

                        }
                        if(Integer.parseInt(js_Pay_How.get(0).toString())==1)
                        {
                            Custumer_Buy.setText("نقدا");

                        }
                        else if(Integer.parseInt(js_Pay_How.get(0).toString())==2)
                        {
                            Custumer_Buy.setText("ذمم");

                        }

                        // Custumer_collector.setText(js_No.get(0).toString());
                        //  Custumer_away_Buy.setText(js_Name.get(0).toString());
                        if(Integer.parseInt(js_Is_Approved.get(0).toString())==1)
                        {
                            Custumer.setText("نعم");

                        }
                        else if(Integer.parseInt(js_Is_Approved.get(0).toString())==0)
                        {
                            Custumer.setText("لا");

                        }
                        if(Integer.parseInt(js_TaxSts.get(0).toString())==1)
                        {
                            Custumer_No_Tax.setText("نعم");

                        }
                        else if(Integer.parseInt(js_TaxSts.get(0).toString())==0)
                        {
                            Custumer_No_Tax.setText("لا");

                        }
                        if(Integer.parseInt(js_CHECK_IMG.get(0).toString())==1)
                        {
                            Custumer_Photo.setText("نعم");

                        }
                        else if(Integer.parseInt(js_CHECK_IMG.get(0).toString())==0)
                        {
                            Custumer_Photo.setText("لا");

                        }
                        if(Integer.parseInt(js_AllowinvoiceWithFlag.get(0).toString())==1)
                        {
                            Custumer_up_order.setText("نعم");

                        }
                        else if(Integer.parseInt(js_AllowinvoiceWithFlag.get(0).toString())==0)
                        {
                            Custumer_up_order.setText("لا");

                        }
                        //  Custumer_discount.setText(js_NaNo.get(0).toString());
                        discount_percentage.setText(js_Discount_Percent.get(0).toString());

                        sqlHandler.executeQuery("Update Customers set =Latitude'" + Js_Latitude + "' ,Longitude='" + js_Longitude + "' where no='" + CustAcc + "'");

                    } catch (final Exception e) {


                    }

                }
            };
            thread.start();


        }

}
