package com.cds_jo.GalaxySalesApp.assist;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.BtService;
import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.Companies;
import com.cds_jo.GalaxySalesApp.ContactListItems;
import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.Item_Package_Items;
import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.cls_Tab_Sales;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Methdes.MyTextView;

public class Convert_ManSummery_To_Img extends Fragment {
    public static BtService pl = null;
    SqlHandler sqlHandler;
    Button btn_Print;

    private View mView;
    String q = "";
    private ESCPOSPrinter posPtr;
    View v;
    String currentDateandTime;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.convert_man_summery_to_img, container, false);
        mView = v.findViewById(R.id.f_view);
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.
                getDefaultAdapter();



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u = sharedPreferences.getString("UserName", "");
        MyTextView tv_UserNm = (MyTextView) v.findViewById(R.id.tv_UserNm);
        tv_UserNm.setText(u);
        String UserID = sharedPreferences.getString("UserID", "");
        MyTextView ed_date = (MyTextView) v.findViewById(R.id.ed_date);

        MyTextView tv_CompName = (MyTextView) v.findViewById(R.id.tv_CompName);
        tv_CompName.setText(sharedPreferences.getString("CompanyNm", ""));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        currentDateandTime = sdf.format(new Date());
        ed_date.setText(" التاريـخ  : " + currentDateandTime);

        TextView tv_SalesCount = (TextView) v.findViewById(R.id.tv_SalesCount);
        TextView tv_SalesCount_Cash = (TextView) v.findViewById(R.id.tv_SalesCount_Cash);
        TextView tv_PayCashCount = (TextView) v.findViewById(R.id.tv_PayCashCount);
        TextView tv_SalesOrdersCount = (TextView) v.findViewById(R.id.tv_SalesOrdersCount);
        TextView tv_PayCheckCount = (TextView) v.findViewById(R.id.tv_PayCheckCount);
        btn_Print = (Button) v.findViewById(R.id.btn_Print);

        btn_Print.setBackgroundColor(getResources().getColor(R.color.Blue));
        btn_Print.setTextColor(Color.WHITE);

        btn_Print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Do_P();


            }

        });
        sqlHandler = new SqlHandler(getActivity());

        q = "Select    ifnull( sum(ifnull(s.Net_Total,0.000)),0.000) as Amt  from  Sal_invoice_Hdr s   where  UserID='" + sharedPreferences.getString("UserID", "") + "' " +
                " and s.inovice_type = '-1'  and  s.date ='" + currentDateandTime + "'";
        TextView tv_Sales = (TextView) v.findViewById(R.id.tv_Sales);
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_Sales.setText(c1.getString(c1.getColumnIndex("Amt")));
            }
            c1.close();
        } else {
            tv_Sales.setText("0.000");
        }


        q = "Select  ifnull( sum(ifnull(s.Net_Total,0.000)),0.000) as Amt  from  Sal_invoice_Hdr s   where  UserID='" + sharedPreferences.getString("UserID", "") + "' " +
                " and s.inovice_type != '-1'  and  s.date ='" + currentDateandTime + "'";

        c1 = sqlHandler.selectQuery(q);

        TextView tv_SalesCash = (TextView) v.findViewById(R.id.tv_SalesCash);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesCash.setText(c1.getString(c1.getColumnIndex("Amt")));
            }
            c1.close();
        } else {
            tv_SalesCash.setText("0.000");
        }


        q = "Select  ifnull(count(Seq),0)  as Seq  from  Sal_invoice_Hdr s   where  UserID='" + sharedPreferences.getString("UserID", "") + "'  and inovice_type='-1'" +
                "  and  s.date ='" + currentDateandTime + "'";

        c1 = sqlHandler.selectQuery(q);


        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesCount.setText(c1.getString(c1.getColumnIndex("Seq")));
            }
            c1.close();
        } else {
            tv_SalesCount.setText("0");
        }


      q = "Select  ifnull(count(Seq),0)  as Seq  from  Sal_invoice_Hdr s   where  UserID='" + sharedPreferences.getString("UserID", "") + "'  and inovice_type='0'" +
                "  and  s.date ='" + currentDateandTime + "'";

        c1 = sqlHandler.selectQuery(q);


        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesCount_Cash.setText(c1.getString(c1.getColumnIndex("Seq")));
            }
            c1.close();
        } else {
            tv_SalesCount_Cash.setText("0");
        }









        q = "Select      ifnull( sum(ifnull( RecVoucher.Cash,0.000)),0.000) as Cash , ifnull( sum(ifnull( RecVoucher.CheckTotal,0.000)),0.000) as CheckTotal            from RecVoucher   " +
                " where  RecVoucher.UserID ='" + UserID + "' and  RecVoucher.TrDate ='" + currentDateandTime + "'";


        c1 = sqlHandler.selectQuery(q);

        TextView tv_PayCash = (TextView) v.findViewById(R.id.tv_PayCash);
        TextView tv_Paycheck = (TextView) v.findViewById(R.id.tv_Paycheck);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_PayCash.setText(c1.getString(c1.getColumnIndex("Cash")));
                tv_Paycheck.setText(c1.getString(c1.getColumnIndex("CheckTotal")));
            }
            c1.close();
        } else {
            tv_PayCash.setText("0.000");
            tv_Paycheck.setText("0.000");
        }


        q = "Select  ifnull(count(Seq),0)  as Seq  from  RecVoucher s   where  (VouchType='1' or VouchType='3'    ) and   UserID='" + sharedPreferences.getString("UserID", "") + "' " +
                "  and  s.TrDate ='" + currentDateandTime + "'";

        c1 = sqlHandler.selectQuery(q);


        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_PayCashCount.setText(c1.getString(c1.getColumnIndex("Seq")));
            }
            c1.close();
        } else {
            tv_PayCashCount.setText("0");
        }


        q = "Select  ifnull(count(Seq),0)  as Seq  from  RecVoucher s   where  (VouchType='2'    ) and   UserID='" + sharedPreferences.getString("UserID", "") + "' " +
                "  and  s.TrDate ='" + currentDateandTime + "'";

        c1 = sqlHandler.selectQuery(q);


        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_PayCheckCount.setText(c1.getString(c1.getColumnIndex("Seq")));
            }
            c1.close();
        } else {
            tv_PayCheckCount.setText("0");
        }


        q = "Select  count(1) as s1 ,0 as s2,0 as  s3    from  Sal_invoice_Hdr s  where  s.UserID='" + sharedPreferences.getString("UserID", "") + "' and  s.Post ='-1'";


        q = q + "  UNION ALL Select  0 as1 ,count(1) as  s2,0 as  s3    from RecVoucher   where  RecVoucher.UserID ='" + sharedPreferences.getString("UserID", "") + "' and  RecVoucher.Post =-1";

        q = q + " UNION ALL  Select  0 as1 ,0 as   s2,count(1) as  s3   from Po_Hdr po   " +
                " where  userid='" + sharedPreferences.getString("UserID", "") + "' and po.posted =-1";


        c1 = sqlHandler.selectQuery(q);

        TextView tv_Unposted = (TextView) v.findViewById(R.id.tv_Unposted);
        int c = 0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {

                do {

                    c = c + (Integer.parseInt(c1.getString(c1.getColumnIndex("s1")))
                            + Integer.parseInt(c1.getString(c1.getColumnIndex("s2"))) + Integer.parseInt(c1.getString(c1.getColumnIndex("s3"))));


                } while (c1.moveToNext());
                tv_Unposted.setText(c + "");
            }
            c1.close();
        } else {

            tv_Unposted.setText("0");
        }


        q = " Select   ifnull( sum(ifnull( po.Net_Total,0.000)),0.000) as Net_Total from Po_Hdr po where  po.userid='" + sharedPreferences.getString("UserID", "") +
                "' and  po.date ='" + currentDateandTime + "'";


        c1 = sqlHandler.selectQuery(q);

        TextView tv_SalesOrders = (TextView) v.findViewById(R.id.tv_SalesOrders);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesOrders.setText(c1.getString(c1.getColumnIndex("Net_Total")));


            }
            c1.close();
        } else {

            tv_Unposted.setText("0");
        }


        q = "Select  ifnull(count(Seq),0)  as Seq  from  Po_Hdr s   where  userid='" + sharedPreferences.getString("UserID", "") + "' " +
                "  and  s.date ='" + currentDateandTime + "'";

        c1 = sqlHandler.selectQuery(q);


        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesOrdersCount.setText(c1.getString(c1.getColumnIndex("Seq")));
            }
            c1.close();
        } else {
            tv_SalesOrdersCount.setText("0");
        }


        TextView tv_EndTime = (TextView) v.findViewById(R.id.tv_EndTime);
        TextView tv_StratTime = (TextView) v.findViewById(R.id.tv_StratTime);


        q = " select    min(Start_Time) as Start_Time ,max(Start_Time)  as End_Time    from SaleManRounds    where Tr_data='" + currentDateandTime + "'";

        c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_StratTime.setText(c1.getString(c1.getColumnIndex("Start_Time")));
                tv_EndTime.setText(c1.getString(c1.getColumnIndex("End_Time")));
            }
            c1.close();
        } else {
            tv_StratTime.setText("00:00:00");
            tv_EndTime.setText("00:00:00");
        }


        TextView tv_SalesWithoutPayment = (TextView) v.findViewById(R.id.tv_SalesWithoutPayment);
        tv_SalesWithoutPayment.setText("0");


        q = " select     ifnull(count( distinct  acc),0) as acc from   Sal_invoice_Hdr   " +
                "    where date ='" + currentDateandTime + "' and   acc not in  ( select CustAcc from RecVoucher where TrDate='" + currentDateandTime + "'  ) ";

        c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_SalesWithoutPayment.setText(c1.getString(c1.getColumnIndex("acc")));

            }
            c1.close();
        } else {
            tv_SalesWithoutPayment.setText("0");

        }

        q = " select  distinct  c.name , c.no from Sal_invoice_Hdr     left join Customers c on c.no =Sal_invoice_Hdr.acc   " +
                "    where date ='" + currentDateandTime + "' and   acc not in  ( select CustAcc from RecVoucher where TrDate='" + currentDateandTime + "'  ) ";

        ListView LstView = (ListView) v.findViewById(R.id.LstView);
        c1 = sqlHandler.selectQuery(q);
        Cls_CustomerWithoutPayment cls_customerWithoutPayment;
        ArrayList<Cls_CustomerWithoutPayment> ItemsList;
        ItemsList = new ArrayList<Cls_CustomerWithoutPayment>();
        ItemsList.clear();
        int i = 0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    i++;
                    cls_customerWithoutPayment = new Cls_CustomerWithoutPayment(c1.getString(c1.getColumnIndex("name")), c1.getString(c1.getColumnIndex("no")),"");
                    ItemsList.add(cls_customerWithoutPayment);
                } while (c1.moveToNext());

            }

            c1.close();
        }
        // Toast.makeText(getActivity(),i+"",Toast.LENGTH_LONG).show();
        i = 30 + (i * 60);

        LinearLayout LytListView = (LinearLayout) v.findViewById(R.id.LytListView);
        LytListView.setLayoutParams(new LinearLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, i));

        Cls_CustWithoutPayment_Adapter cls_unitItems_adapter = new Cls_CustWithoutPayment_Adapter(
                getActivity(), ItemsList);

        LstView.setAdapter(cls_unitItems_adapter);
        try {
            mBluetoothAdapter.enable();
        }catch ( Exception ex ){}

        TextView tv_NewCustomer = (TextView) v.findViewById(R.id.tv_NewCustomer);
        TextView tv_CustomerVisted = (TextView) v.findViewById(R.id.tv_CustomerVisted);
        TextView tv_CustomerNotVisited = (TextView) v.findViewById(R.id.tv_CustomerNotVisited);
        TextView tv_PassVisited = (TextView) v.findViewById(R.id.tv_PassVisited);

        tv_NewCustomer.setText(GetNewCustomerCount());
        tv_CustomerVisted.setText(GetCustomerVisted());
        tv_CustomerNotVisited.setText(GetCustomerNotVisited());
        tv_PassVisited.setText(GetPassVisited());
        return v;
    }
    private String GetNewCustomerCount() {
        String c = "0";
        String q = "select ifnull(count(no),0) as no  from  CusfCard where Tr_Date='" + currentDateandTime + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                c = c1.getString(c1.getColumnIndex("no"));
            }
            c1.close();
        }
        return c;
    }
    private String GetPassVisited() {
        String c = "0";

        return c;
    }
    private String GetCustomerVisted() {
        String c = "0";
        String q = "select ifnull(count(no),0) as no  from  SaleManRounds where Tr_Data='" + currentDateandTime + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                c = c1.getString(c1.getColumnIndex("no"));
            }
            c1.close();
        }
        return c;
    }
    private String GetCustomerNotVisited () {

        String Week_Num = "1";
        int dayOfWeek = 0;
        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        Week_Num=DB.GetValue(getActivity(),"ComanyInfo","VisitWeekNo","1=1");
       String w="";
        if (Week_Num.equalsIgnoreCase("1")) {

            if (dayOfWeek == 7)
                w = " sat = 1 ";

            if (dayOfWeek == 1)
                w = " sun = 1 ";

            if (dayOfWeek == 2)
                w = " mon = 1 ";


            if (dayOfWeek == 3)
                w = " tues = 1 ";

            if (dayOfWeek == 4)
                w = " wens = 1 ";

            if (dayOfWeek == 5)
                w = " thurs = 1 ";
        }
        if (Week_Num.equalsIgnoreCase("2")) {
            if (dayOfWeek == 7)
                w = " sat1 = 1 ";

            if (dayOfWeek == 1)
                w = " sun1 = 1 ";

            if (dayOfWeek == 2)
                w = " mon1 = 1 ";


            if (dayOfWeek == 3)
                w = " tues1 = 1 ";

            if (dayOfWeek == 4)
                w = " wens1 = 1 ";

            if (dayOfWeek == 5)
                w= " thurs1 = 1 ";

        }


        String count = "0";
        q = "Select  ifnull(count(c.no),0) as no   from Customers  c" +
                "  where  c." + w  +" AND  c.no not in ( select CusNo from SaleManRounds where Tr_Data='"+currentDateandTime+"'  )";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                count = c1.getString(c1.getColumnIndex("no"));
            }
            c1.close();
        }
        return count;
    }
    public void btn_back(View view) {    }
    private void Do_P() {
        ComInfo.ComNo = Integer.parseInt(DB.GetValue(getActivity(), "ComanyInfo", "CompanyID", "1=1"));

        btn_Print.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
        btn_Print.setTextColor(getResources().getColor(R.color.Blue));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        LinearLayout lay = (LinearLayout) v.findViewById(R.id.Mainlayout);

        if (ComInfo.ComNo == Companies.beutyLine.getValue()) {
            PrintReport_TSC obj = new PrintReport_TSC(getActivity(),
                    getActivity(), lay, 520, 1);
            // obj.DoPrint();
            obj.DoPrint();
        }  else  if (ComInfo.ComNo == Companies.Ukrania.getValue()) {
            PrintReport_TSC_Ipad obj = new PrintReport_TSC_Ipad(getActivity(),
                getActivity(), lay, 550, 1);
        // obj.DoPrint();
        obj.DoPrint();


    }   else  if (ComInfo.ComNo == Companies.Saad.getValue()) {
            PrintReport_TSC_Ipad obj = new PrintReport_TSC_Ipad(getActivity(),
                getActivity(), lay, 520, 1);
        // obj.DoPrint();
        obj.DoPrint();


    }else {
            PrintReport_Zepra520 obj = new PrintReport_Zepra520(getActivity(),
                    getActivity(), lay, 570, 1);
            obj.DoPrint();

        }




    }


}
