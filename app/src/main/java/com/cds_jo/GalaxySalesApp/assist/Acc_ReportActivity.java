package com.cds_jo.GalaxySalesApp.assist;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.Companies;
import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SCR_ACTIONS;
import com.cds_jo.GalaxySalesApp.Select_Customer;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.Logtrans.InsertLogTrans;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import header.Header_Frag;

//import com.loopj.android.http.HttpGet;// temp

public class Acc_ReportActivity extends AppCompatActivity {
    ListView items_Lsit;
    String SCR_NO="11005";
    public ProgressDialog loadingdialog;
    SqlHandler sqlHandler;
    TextView CheqBal;
    TextView Ball ;
    TextView CusTop;
    TextView NetBall;
    EditText FromDate;
    EditText ToDate ;
    String   CUST_NET_BAL ="";
    ArrayList<Cls_Acc_Report> cls_acc_reportsList;
    DecimalFormat Per;
    Button btn_print,btn_print2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_ee);
        InsertLogTrans obj=new InsertLogTrans(Acc_ReportActivity.this,SCR_NO ,SCR_ACTIONS.open.getValue(),"","-1","");

        cls_acc_reportsList = new ArrayList<Cls_Acc_Report>();
         SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        items_Lsit=(ListView)findViewById(R.id.lst_acc);
        acc.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));


        CustNm.setError(null);





        btn_print = (Button)findViewById(R.id.btn_print);
        btn_print2 = (Button)findViewById(R.id.btn_print2);

        if(ComInfo.ComNo== Companies.Arabian.getValue() ){
            btn_print.setVisibility(View.INVISIBLE);
            btn_print2.setVisibility(View.INVISIBLE);
        }


        FromDate = (EditText)findViewById(R.id.ed_FromDate);
        ToDate = (EditText)findViewById(R.id.ed_ToDate);
        final   EditText et_focuse = (EditText)findViewById(R.id.et_focuse);
        et_focuse.requestFocus();
        FromDate.setText("01/01/2017");
        et_focuse.requestFocus();
        String ss = FromDate.getText().toString();

        ToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToDate.setText("", TextView.BufferType.EDITABLE);
            }
        });

        FromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FromDate.setText("", TextView.BufferType.EDITABLE);
            }
        });



        et_focuse.requestFocus();
        et_focuse.setText("1");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String currentYear = sdf.format(new Date());

        FromDate.setText("01/01/"+currentYear);
        ToDate.setText("31/12/"+currentYear);



        SimpleDateFormat sdf_currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String currentDate = sdf_currentDate.format(new Date());
        ToDate.setText(currentDate);
        Fragment frag = new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();
       items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener(){
           @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Cls_Acc_Report cls_acc_report1 = new Cls_Acc_Report();
               cls_acc_report1 = cls_acc_reportsList.get(position);
               String doctype = String.valueOf(cls_acc_report1.getDoctype()).toString();
               String des1 = String.valueOf(cls_acc_report1.getDes()).toString();
               String doc_num1 = String.valueOf(cls_acc_report1.getDoc_num()).toString();
               String v_type1 = String.valueOf(cls_acc_report1.getV_type()).toString();
               String myear1 = String.valueOf(cls_acc_report1.getMyear()).toString();

               if (doctype != "") {
                   int doctype1 = Integer.parseInt(doctype);
                   int doc_num = Integer.parseInt(doc_num1);
                   int v_type = Integer.parseInt(v_type1);
                   int myear = Integer.parseInt(myear1);
                   if (doctype1 != 2) {
                       GVal.bill = doc_num;
                       GVal.dis = des1;
                       GVal.v_type = v_type;
                       GVal.myear = myear;
                       GVal.doctype = doctype1;
                       Acc_Report_D exampleDialog = new Acc_Report_D();
                       exampleDialog.show(getFragmentManager(), "MyCustomDialog");
//                android.app.FragmentManager fragmentManager = getFragmentManager();
//                Acc_Report_D exampleDialog = new Acc_Report_D();
//                exampleDialog.show( fragmentManager,null);
                   }


               }

           }
       });

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
    private boolean isValidDate(String dateOfBirth) {
        boolean valid = true;

        if(dateOfBirth.trim().length()<10) {
            return false;
        }
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");


        } catch (Exception e) {
            valid = false;
            return valid;
        }


        String[] parts = dateOfBirth.split("/");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        if ( SToD(part1) <0  || SToD(part1)>31 ){
            valid = false;
            return valid;
        }
        if ( SToD(part2) <0  || SToD(part2)>12 ){
            valid = false;
            return valid;
        }

        if ( SToD(part3) <1990  || SToD(part3)>2050 ){
            valid = false;
            return valid;
        }

        return valid;
    }
    private static String arabicToenglish(String number)
    {
        number=number.replace(",",".");
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }

        return new String(chars);
    }
    public void onProgressUpdate( ){



        final List<String> items_ls = new ArrayList<String>();
        items_Lsit=(ListView)findViewById(R.id.lst_acc);
        items_Lsit.setAdapter(null);

        CheqBal = (TextView)findViewById(R.id.tv_CheqBal);
        Ball = (TextView)findViewById(R.id.tv_Ball);
        CusTop = (TextView)findViewById(R.id.tv_CusTop);
        NetBall = (TextView)findViewById(R.id.tv_NetBall);

        final   TextView acc = (TextView)findViewById(R.id.tv_acc);
          FromDate = (EditText)findViewById(R.id.ed_FromDate);
          ToDate = (EditText)findViewById(R.id.ed_ToDate);

        FromDate.setError(null);
        ToDate.setError(null);

        if ( FromDate.getText().toString().length()   <= 0) {
            FromDate.setError("*");
            FromDate.requestFocus();
            return;
        }

        if ( ToDate.getText().toString().length()   <= 0) {
            ToDate.setError("*");
            ToDate.requestFocus();
            return;
        }


        AlertDialog alertDialog  ;
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("تاريخ بداية الفترة");
        alertDialog.setMessage( "هناك خطأ في طريقة ادخال بداية الفترة ، الرجاء ادخال التاريخ كالتالي dd/mm/yyyy");
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setButton("نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FromDate.setText("");
                FromDate.requestFocus();
            }
        });

        if(!isValidDate(FromDate.getText().toString())){
            alertDialog.show();
            return;
        }



        alertDialog.setTitle("تاريخ نهاية الفترة");
        alertDialog.setMessage( "هناك خطأ في طريقة ادخال نهاية الفترة ، الرجاء ادخال التاريخ كالتالي dd/mm/yyyy");
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setButton("نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ToDate.setText("");
                ToDate.requestFocus();

            }
        });

        if(!isValidDate(ToDate.getText().toString())){

            alertDialog.show();
            return;
        }
        cls_acc_reportsList.clear();
        CheqBal.setText("");
        Ball.setText("");
        CusTop.setText("");
        NetBall.setText("");

        final Handler _handler = new Handler();
             CUST_NET_BAL =  DB.GetValue(Acc_ReportActivity.this, "Customers", "CUST_NET_BAL", "no ='" + acc.getText().toString() + "' ");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(Acc_ReportActivity.this);

        custDialog.setTitle("الرجاء الانتظار");
        custDialog.setMessage("العمل جاري على نسخ البيانات");
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.show();

        Locale locale ;
        locale   = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
          Per= new DecimalFormat("0.000");

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(Acc_ReportActivity.this);
               ws.CallReport(acc.getText().toString(), FromDate.getText().toString(),ToDate.getText().toString(),UserID);


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
                    JSONArray js_v_type= js.getJSONArray("v_type");
                    JSONArray js_myear= js.getJSONArray("myear");

                    Cls_Acc_Report cls_acc_report = new Cls_Acc_Report();

                    cls_acc_report.setTot("الرصيد");
                    cls_acc_report.setRate("العملة المقابلة");
                    cls_acc_report.setCred("الدائن");
                    cls_acc_report.setDept("المدين");
                    cls_acc_report.setBb("رصيد إفتتاحي");
                    cls_acc_report.setDes(" البيــــــــان");
                    cls_acc_report.setDate("التاريــخ");
                    cls_acc_report.setCur_no("العملة");
                    cls_acc_report.setDoctype("نوع المستند ");
                    cls_acc_report.setDoc_num("رقم المستند");
                    cls_acc_reportsList.add(cls_acc_report);

                    // date,fromstore,tostore,des,docno,itemno,qty,UnitNo,UnitRate,myear





                    for( i =0 ; i<js_doc_num.length();i++)
                    {
                        cls_acc_report = new Cls_Acc_Report();

                        cls_acc_report.setDoc_num(js_doc_num.get(i).toString());
                        cls_acc_report.setDoctype(js_doctype.get(i).toString());
                        cls_acc_report.setCur_no(js_cur_no.get(i).toString());
                        cls_acc_report.setDate(js_date.get(i).toString());
                        cls_acc_report.setDes(js_des.get(i).toString());
                        cls_acc_report.setMyear(js_myear.get(i).toString());
                        cls_acc_report.setV_type(js_v_type.get(i).toString());
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


                        custDialog.setMax(js_doc_num.length());
                        custDialog.incrementProgressBy(1);

                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();
                        }

                    }



                    Locale locale ;
                    locale   = new Locale("ar");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

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

                            CheqBal.setText(txtCheqBal);
                            Ball.setText(txtBall);
                            CusTop.setText(txtCusTop);
                            NetBall.setText(S_tot);

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
                                    Acc_ReportActivity.this, cls_acc_reportsList);

                            items_Lsit.setAdapter(cls_acc_report_adapter);
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    Acc_ReportActivity.this).create();
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

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    Acc_ReportActivity.this).create();
                            alertDialog.setTitle("كشف حساب تفصيلي");
                            if (We_Result.ID == -404) {
                                alertDialog.setMessage(We_Result.Msg);
                            } else {
                                alertDialog.setMessage("لا يوجد بيانات" );
                            }
                            alertDialog.setIcon(R.drawable.tick);

                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();

                        }
                    });
                }


            }
        }).start();




    }

    public void btnPrint(View view) {

        TextView tv_acc = (TextView)findViewById(R.id.tv_acc);
        TextView tv_cusnm = (TextView)findViewById(R.id.tv_cusnm);
        EditText ed_ToDate = (EditText)findViewById(R.id.ed_ToDate);
        EditText ed_FromDate = (EditText)findViewById(R.id.ed_FromDate);


        sqlHandler = new SqlHandler(this);

        String q ="Delete from  ACC_REPORT where Cust_No ='"+ tv_acc.getText().toString()+"'";
         q ="Delete from  ACC_REPORT ";
        sqlHandler.executeQuery(q);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        Long i;
        ContentValues cv =new ContentValues();
        for (int x = 0; x < cls_acc_reportsList.size(); x++) {
            Cls_Acc_Report contactListItems = new Cls_Acc_Report();
            contactListItems = cls_acc_reportsList.get(x);


            cv = new ContentValues();



            cv.put("Cust_No", tv_acc.getText().toString());
            cv.put("Cust_Nm", tv_cusnm.getText().toString());
            cv.put("FDate",ed_FromDate.getText().toString());
            cv.put("TDate", ed_ToDate.getText().toString());
            cv.put("TrDate", currentDateandTime);
            cv.put("Tot", contactListItems.getTot().toString().replace(",", ""));
            cv.put("Rate", contactListItems.getRate().toString().replace(",", ""));
            cv.put("Cred", contactListItems.getCred().toString().replace(",", ""));
            cv.put("Dept", contactListItems.getDept().toString().replace(",", ""));
            cv.put("Bb", contactListItems.getBb().toString().replace(",", ""));
            cv.put("Des", contactListItems.getDes().toString().replace(",", ""));
            cv.put("Date", contactListItems.getDate().toString()==null ?"" :  contactListItems.getDate().toString());
            cv.put("Cur_no", contactListItems.getCur_no().toString().replace(",", ""));
            cv.put("Doctype", contactListItems.getDoctype().toString().replace(",", ""));
            cv.put("Doc_num", contactListItems.getDoc_num().toString().replace(",", ""));
            cv.put("CheqBal", CheqBal.getText().toString());
            cv.put("Ball",  Ball.getText().toString());
            cv.put("CusTop",  CusTop.getText().toString());
            cv.put("NetBall",  NetBall.getText().toString());
            cv.put("Notes", "");
            i = sqlHandler.Insert("ACC_REPORT", null, cv);
        }

        Intent k = new Intent(this,Convert_ccReportTo_ImgActivity.class);


        k.putExtra("Scr", "po");
        TextView   CustNm =(TextView)findViewById(R.id.tv_cusnm);

        TextView   accno = (TextView)findViewById(R.id.tv_acc);
        k.putExtra("cusnm", CustNm.getText().toString());
        k.putExtra("OrderNo","");
        k.putExtra("accno", accno.getText().toString());

        startActivity(k);

    }
    public void btnPrint2(View view) {

        Intent k = new Intent(this,Convert_ccReport2To_ImgActivity.class);
        k.putExtra("Scr", "po");
        TextView   CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView   accno = (TextView)findViewById(R.id.tv_acc);
        k.putExtra("cusnm", CustNm.getText().toString());
        k.putExtra("OrderNo","");
        k.putExtra("accno", accno.getText().toString());
        k.putExtra("Amt", accno.getText().toString());

        startActivity(k);

    }
    public void btn_searchCustomer(View view) {


        TextView acc = (TextView)findViewById(R.id.tv_acc);
        TextView tv_cusnm = (TextView)findViewById(R.id.tv_cusnm);
        acc.setText("");
        tv_cusnm.setText("");

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "AccReport");
        FragmentManager Manager =  getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    public void Set_Cust(final String No, String Nm) {
        final    ProgressDialog progressDialog;
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
        HidKeybad();
         //onProgressUpdate();

    }
    private  void HidKeybad(){
        try{
            if (this.getCurrentFocus() != null) {
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }catch (Exception ex){}
    }
    public void btn_back(View view) {
        Intent k = new Intent(this,JalMasterActivity.class);
        startActivity(k);
    }

    public void btn_GetData(View view) {
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        InsertLogTrans obj=new InsertLogTrans(Acc_ReportActivity.this,SCR_NO , SCR_ACTIONS.Report.getValue(),"",acc.getText().toString(),"");

        onProgressUpdate();
    }


    public void Print_Report(View view) {
    }
}




