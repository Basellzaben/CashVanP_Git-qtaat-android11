package com.cds_jo.GalaxySalesApp;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import Methdes.MyTextView;

public class Tab_SalesOrders extends   Fragment  implements View.OnClickListener {
    ArrayList<cls_Tab_Order_Sales> cls_Tab_Order_Sales  ;
    ListView items_Lsit;
    TextView count,tv_Summatin;
     Button btn_Details;
    String Order_no = "";
    MyTextView tv_lbl_Count ;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_sales_orders,container,false);
        items_Lsit=(ListView) v.findViewById(R.id.SalesSummery);

        cls_Tab_Order_Sales  = new ArrayList<cls_Tab_Order_Sales>();
        cls_Tab_Order_Sales.clear();
        count = (TextView)v.findViewById(R.id.tv_Count);
        tv_Summatin = (TextView)v.findViewById(R.id.tv_Summatin);
       /* btn_Details=(Button)v.findViewById(R.id.btn_Details);
        btn_Details.setOnClickListener(this);*/
        tv_lbl_Count = (MyTextView)v.findViewById(R.id.tv_lbl_Count);
        FillList();

     items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position > 0) {

                    try {

                        cls_Tab_Order_Sales cls_searchpos = new cls_Tab_Order_Sales();
                        cls_searchpos = (cls_Tab_Order_Sales) items_Lsit.getAdapter().getItem(position);
                        Order_no = cls_searchpos.getCustNo();
                        Toast.makeText(getActivity(), cls_searchpos.getCustNo(), Toast.LENGTH_SHORT).show();

                        FillListDetails();
                    }catch(Exception ex ){
                        FillList();
                    }

                }
            }
        });


        return v;
    }


    private  Double SToD(String str){
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat)nf;
        str = str.replace(",", "");
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
    private void FillList(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        String q ="";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        cls_Tab_Order_Sales.clear();
        items_Lsit.setAdapter(null);


        q =     " Select distinct po.orderno,po.date, c.name,po.acc ,po.posted  ,COALESCE(po.Net_Total,0) as total" +
                " from Po_Hdr po Left join Customers c on c.no = po.acc where  userid='"+sharedPreferences.getString("UserID", "") +
                "' and  po.date ='" + currentDateandTime + "'";

        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);
        cls_Tab_Order_Sales cls_searchpos;
        cls_searchpos= new cls_Tab_Order_Sales();
        cls_searchpos.setCustNo(" رقم الطلب");
        cls_searchpos.setCustNm("اسم العميل");
        cls_searchpos.setDate("التاريخ");
        cls_searchpos.setAcc("رقم العميل");
        cls_searchpos.setNotes("-3");
        cls_searchpos.setType("0");
        cls_searchpos.setTot("الإجمالي");

        cls_Tab_Order_Sales.add(cls_searchpos);

        tv_lbl_Count.setText("عدد الطلبات");

        Double sum = 0.0 ;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_searchpos= new cls_Tab_Order_Sales();
                    cls_searchpos.setCustNo(c1.getString(c1.getColumnIndex("orderno")));
                    cls_searchpos.setCustNm(c1.getString(c1.getColumnIndex("name")));
                    cls_searchpos.setDate(c1.getString(c1.getColumnIndex("date")));
                    cls_searchpos.setAcc(c1.getString(c1.getColumnIndex("acc")));
                    cls_searchpos.setNotes(c1.getString(c1.getColumnIndex("posted")));
                    cls_searchpos.setType("0");
                    cls_searchpos.setTot(c1.getString(c1.getColumnIndex("total")));
                    sum=sum+SToD(c1.getString(c1.getColumnIndex("total")));
                    cls_Tab_Order_Sales.add(cls_searchpos);
                }while (c1.moveToNext());
            }

            c1.close();
        }

        tv_Summatin.setText(sum +"");
        tv_Summatin.setText(SToD(tv_Summatin.getText().toString()).toString());

        count.setText((cls_Tab_Order_Sales.size()-1) +"");
        cls_Tab__Order_Sales_adapter SalesAdapter = new cls_Tab__Order_Sales_adapter(
                this.getActivity(),cls_Tab_Order_Sales);

        items_Lsit.setAdapter(SalesAdapter);
    }
    private void FillListDetails(){

        ArrayList<ContactListItems> contactList;
        contactList = new ArrayList<ContactListItems>();
        contactList.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        String q ="";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        cls_Tab_Order_Sales.clear();
        items_Lsit.setAdapter(null);
        tv_lbl_Count.setText("عدد المواد");

        ContactListItems contactListItems = new ContactListItems();
        contactListItems.setno("رقم المادة");
        contactListItems.setName("اسم المادة");
        contactListItems.setprice("السعر");
        contactListItems.setQty("الكمية");
        contactListItems.setTax("الضريبة");
        contactListItems.setUniteNm("الوحدة");
        contactListItems.setDiscount("الخصم %");
        contactListItems.setDis_Amt("قيمة الخصم");
        contactListItems.setBounce("البونص");
        contactListItems.setTotal("المجموع");
        contactListItems.setItemOrgPrice("");
        contactListItems.setDis_Amt("");
        contactListItems.setUnite("");
        contactListItems.setTax_Amt("");
        contactListItems.setProID("");
        contactListItems.setPro_bounce("0");
        contactListItems.setPro_dis_Per("0");
        contactListItems.setPro_amt("0");
        contactListItems.setPro_Total("0");
        contactListItems.setDisAmtFromHdr("0");
        contactListItems.setDisPerFromHdr("0");
        contactListItems.setBatch("");
        contactListItems.setExpDate("");
        contactListItems.setOperand("");
        contactList.add(contactListItems);



        q = "  select Distinct Unites.UnitName, pod.OrgPrice ,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo ,pod.dis_Amt,pod.dis_per,pod.bounce_qty ,  pod.tax_Amt   , pod.total  " +
                " , pod.pro_Total    , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt  " +
                ", ifnull( pod.Opraned,1) as Opraned  ,ifnull( pod.ExpDate,'') as ExpDate " +
                " ,ifnull( pod.Batch,'') as  Batch  from Po_dtl pod left join invf on invf.Item_No =  pod.itemno " +
                "   left join Unites on Unites.Unitno=  pod.unitNo  Where pod.orderno='" + Order_no+ "'";



        SqlHandler sqlHandler = new SqlHandler(getActivity());

        Cursor c1 = sqlHandler.selectQuery(q);
        int i = 0 ;
        Double sum = 0.0 ;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    contactListItems = new ContactListItems();
                    contactListItems.setno(c1.getString(c1.getColumnIndex("pod.itemno")));
                    contactListItems.setName(c1.getString(c1.getColumnIndex(" invf.Item_Name")));
                    contactListItems.setprice(c1.getString(c1.getColumnIndex(" pod.OrgPrice")));
                    contactListItems.setQty(c1.getString(c1.getColumnIndex("pod.qty")));
                    contactListItems.setTax(c1.getString(c1.getColumnIndex("pod.tax")));
                    contactListItems.setUniteNm(c1.getString(c1.getColumnIndex("Unites.UnitName")));
                    contactListItems.setDiscount(c1.getString(c1.getColumnIndex("pod.dis_per")));
                    contactListItems.setDis_Amt(c1.getString(c1.getColumnIndex("pod.dis_Amt")));
                    contactListItems.setBounce(c1.getString(c1.getColumnIndex("pod.bounce_qty")));
                    contactListItems.setTotal(c1.getString(c1.getColumnIndex("pod.total")));
                    sum=sum+SToD(c1.getString(c1.getColumnIndex("pod.total")));
                    contactList.add(contactListItems);
                    i=i+1;
                }while (c1.moveToNext());
            }
        c1.close();
        }

        tv_Summatin.setText(sum +"");
        tv_Summatin.setText(SToD(tv_Summatin.getText().toString()).toString());
        count.setText(i +"");
        PoSummeryListAdapter SalesAdapter = new PoSummeryListAdapter(
                this.getActivity(),contactList);

        items_Lsit.setAdapter(SalesAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}