
package com.cds_jo.GalaxySalesApp.Reports;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Methdes.MethodToUse;
import Methdes.MyTextView;

public class PopShowInvoiceDtl extends DialogFragment implements View.OnClickListener  {
    ListView items_Lsit;
    public ProgressDialog loadingdialog;
    private int year, month, day;
    String DocNo,Amt,OrderType ,  TransDate,ToDate ;
    MyTextView msg ;
    View form ;
    NumberFormat nf_out;
    Button Back;//,GetData;
    Context context;
      MyTextView tv_InvoiceNo,tv_amtNo;

    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    @Override
    public void onStart()
    {
        super.onStart();


        if (getDialog() == null)
            return;


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom( getDialog().getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(lp);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setWindowAnimations(0);

    }

    @Override
    public View onCreateView( LayoutInflater inflater,ViewGroup container,Bundle savestate){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        form =inflater.inflate(R.layout.show_cust_invoice_dtl,container,false);
        context=getActivity();
        msg = (MyTextView)form.findViewById(R.id.msg);
    /*    GetData = (Button)form.findViewById(R.id.GetData);
        GetData.setTypeface(MethodToUse.SetTFace(context));
        GetData.setOnClickListener(this);*/

        Back = (Button)form.findViewById(R.id.Back);
        Back.setTypeface(MethodToUse.SetTFace(context));
        Back.setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        DocNo=getArguments().getString("DocNo");
        OrderType=getArguments().getString("OrderType");
        Amt=getArguments().getString("Amt");



        tv_InvoiceNo = (MyTextView)form.findViewById(R.id.tv_InvoiceNo);
        tv_InvoiceNo.setText(DocNo);


        tv_amtNo = (MyTextView)form.findViewById(R.id.tv_amtNo);
        tv_amtNo.setText(Amt);

        msg.setVisibility(View.VISIBLE);
        onProgressUpdate();
        return  form;
    }
    public void onClick(View v) {
        if (v == Back) {
            this.dismiss();
        }
       /* else if (v == GetData) {
        onProgressUpdate();
    }*/
    }
    private  Double SToD(String str){
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat)nf;
        str = str.replace(",","");
        Double d = 0.0;
        if (str.length()==0) {
            str = "0";
        }
        if (str.length()>0)
            try {
                d =  Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            }
            catch (Exception ex)
            {
                str="0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }

    public void onProgressUpdate( ){


        nf_out = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf_out.setMaximumFractionDigits(3);
        nf_out.setMinimumFractionDigits(3);


        final List<String> items_ls = new ArrayList<String>();
        items_Lsit=(ListView)form.findViewById(R.id.lst_acc);
        items_Lsit.setAdapter(null);



        final MyTextView tv_InvoiceDate = (MyTextView)form.findViewById(R.id.tv_InvoiceDate);

        tv_InvoiceDate.setText("");



        final Handler _handler = new Handler();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(context);

        custDialog.setTitle("التفاصيل");
        custDialog.setMessage("جاري العمل على استرجاع البيانات");
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);


        msg.setVisibility(View.VISIBLE);
        msg.setText("  الرجاء الانتظار ..." + "  العمل جاري على عرض البيانات  ");
       // GetData.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(context);

                    ws.GET_Report_Home_Sales_Details("-1", "-1", OrderType, "2", "01-01-2020", "01-01-2021", "-1", DocNo, "-1");


                try {

                    Integer i;
                    String q = "";

                    if (We_Result.ID>0) {
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Item_Name = js.getJSONArray("Item_Name");
                        JSONArray js_OrgPrice = js.getJSONArray("OrgPrice");
                        JSONArray js_price = js.getJSONArray("price");
                        JSONArray js_Qty = js.getJSONArray("Qty");
                        JSONArray js_Bounce= js.getJSONArray("Bounce");
                        JSONArray js_Dis_Amt = js.getJSONArray("Dis_Amt");
                        JSONArray js_OrderNo = js.getJSONArray("OrderNo");
                        JSONArray js_UnitName = js.getJSONArray("UnitName");
                        JSONArray js_TransDate = js.getJSONArray("TransDate");
                        JSONArray js_Item_No = js.getJSONArray("Item_No");
                        JSONArray js_LineTotal = js.getJSONArray("LineTotal");




                        final ArrayList<cls_salesC> cls_acc_reportsList = new ArrayList<cls_salesC>();

                        cls_salesC cls_acc_report;// = new Cls_CustLastInvoice();



                        TransDate=js_TransDate.get(0).toString();

                        cls_acc_report = new cls_salesC();

                        cls_acc_report.setItem_Name("وصف المادة");
                        cls_acc_report.setPrice("السعر");
                        cls_acc_report.setQty("الكمية");
                        cls_acc_report.setOrgPrice("السعر قبل الضريبة");
                        cls_acc_report.setBounce("البونص");
                        cls_acc_report.setUnitName("الوحدة");
                        cls_acc_report.setItemno("رقم المادة");
                        cls_acc_report.setLineTotal("المجموع");
                        cls_acc_report.setBounce("البونص");
                        cls_acc_report.setDiscount("الخصم");
                        cls_acc_reportsList.add(cls_acc_report);

                        for (i = 0; i < js_Item_Name.length(); i++) {
                            cls_acc_report = new cls_salesC();
                            cls_acc_report.setItemno(js_Item_No.get(i).toString());
                            cls_acc_report.setLineTotal(js_LineTotal.get(i).toString());
                            cls_acc_report.setItem_Name(js_Item_Name.get(i).toString());
                            cls_acc_report.setPrice(js_price.get(i).toString());
                            cls_acc_report.setOrgPrice(js_OrgPrice.get(i).toString());
                            cls_acc_report.setBounce(js_Bounce.get(i).toString());
                            cls_acc_report.setUnitName(js_UnitName.get(i).toString());
                            cls_acc_report.setQty(js_Qty.get(i).toString());
                            cls_acc_report.setDiscount(js_Dis_Amt.get(i).toString());
                            cls_acc_reportsList.add(cls_acc_report);

                            custDialog.setMax(js_Item_Name.length());
                            custDialog.incrementProgressBy(1);

                            if (custDialog.getProgress() == custDialog.getMax()) {
                                custDialog.dismiss();
                                msg.setText( "");
                                msg.setVisibility(View.GONE);
                             //   GetData.setVisibility(View.VISIBLE);
                            }
                        }
                        final int total = i;
                       _handler.post(new Runnable() {

                            public void run() {
                                msg.setText( "");
                                msg.setVisibility(View.GONE);
                              //  GetData.setVisibility(View.VISIBLE);

                                tv_InvoiceDate.setText(TransDate);


                                TarnsDetailsAdapter cls_acc_report_adapter = new TarnsDetailsAdapter(
                                        context, cls_acc_reportsList);

                                items_Lsit.setAdapter(cls_acc_report_adapter);
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        context).create();
                                alertDialog.setTitle("تحديث البيانات");

                                alertDialog.setMessage("تمت عملية استرجاع  البيانات بنجاح ");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                // alertDialog.show();

                                custDialog.dismiss();
                            }
                        });
                    }else {
                        msg.setText( "لا يوجد بيانات");
                        msg.setVisibility(View.GONE);
                      //  GetData.setVisibility(View.VISIBLE);
                        custDialog.dismiss();
                    }
                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            msg.setText( "لا يوجد بيانات");
                            msg.setVisibility(View.VISIBLE);
                         //   GetData.setVisibility(View.VISIBLE);

                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    context).create();
                            alertDialog.setTitle("اخر فاتورة للعميل");
                            if (We_Result.ID == -404) {
                                alertDialog.setMessage(We_Result.Msg);
                            } else {
                                alertDialog.setMessage("لا يوجد بيانات" );
                            }
                            alertDialog.setIcon(R.drawable.error_new );

                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                          //  alertDialog.show();

                        }
                    });
                }


            }
        }).start();





    }








}




