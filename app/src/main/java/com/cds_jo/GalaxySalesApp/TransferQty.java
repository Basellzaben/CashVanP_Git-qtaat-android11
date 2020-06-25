package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_TransQtyItems;

import com.cds_jo.GalaxySalesApp.assist.Store_Trans_Qty_Img;
import com.cds_jo.GalaxySalesApp.assist.TransIteQtyAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import header.Header_Frag;


public class TransferQty extends FragmentActivity {
    SqlHandler sqlHandler;
    ListView lv_Items;
    ArrayList<Cls_TransQtyItems> contactList ;

    String UserID= "";
    public ProgressDialog loadingdialog;
    public   String json;
    Boolean IsNew;
    String CatNo = "-1";

    String ScrKind = "1";
    String Operand = "";
    String ScrTitle = "";
    TextView tv_fromStore  ,tv_ToStore,tv_fromStoreNm  ,tv_ToStoreNm ,et_OrdeNo ;
    EditText et_Desc;
    ImageView imgFromStore;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setContentView(R.layout.trans_qty  );
        lv_Items = (ListView) findViewById(R.id.LstvItems);
        sqlHandler = new SqlHandler(this);
        IsNew = true;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID= sharedPreferences.getString("UserID", "");

        Bundle extras = getIntent().getExtras();
        ScrKind       = "1";;// extras.getString("ScrKind");

        GetMaxPONo();
        contactList = new ArrayList<Cls_TransQtyItems>();
        contactList.clear();
        showList(0);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Home_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent k = new Intent(TransferQty.this, JalMasterActivity.class);
                startActivity(k);
            }
        });
        tv_fromStore =(TextView)findViewById(R.id.tv_fromStore);
        tv_ToStore = (TextView)findViewById(R.id.tv_ToStore);

        tv_fromStoreNm =(TextView)findViewById(R.id.tv_fromStoreNm);
        tv_ToStoreNm = (TextView)findViewById(R.id.tv_ToStoreNm);
        et_Desc= (EditText) findViewById(R.id.et_Desc);


        et_OrdeNo = (TextView) findViewById(R.id.et_OrdeNo);
        SetStoreNo();
        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();


        imgFromStore = (ImageView) findViewById(R.id.imgFromStore);
        imgFromStore.setVisibility(View.VISIBLE);
        if(ScrKind.equalsIgnoreCase("1")) {
            ScrTitle ="سند نقل مواد";
        }else{

            ScrTitle = "سند إرجاع مواد";
        }

    }
    private void SetStoreNo() {

        SqlHandler sqlHandler = new SqlHandler(this);

        tv_fromStore.setText("");
        tv_ToStore.setText("");
        tv_fromStoreNm.setText("");
        tv_ToStoreNm.setText("");

        et_Desc.setText("");

        String query = "Select  distinct ss.FromStore,ss.ToStore ,s1.sname as nm1,s2.sname  as nm2" +
                "      from storesSetting  ss left join stores s1 on s1.sno =ss.FromStore " +
                "      left join stores s2 on s2.sno =ss.ToStore where ss.ScrKind ='"+ScrKind+"'";


        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_fromStore.setText(c1.getString(c1.getColumnIndex("FromStore")));
                tv_ToStore.setText(c1.getString(c1.getColumnIndex("ToStore")));
                tv_fromStoreNm.setText(c1.getString(c1.getColumnIndex("nm1")));
                tv_ToStoreNm.setText(c1.getString(c1.getColumnIndex("nm2")));
          c1.close();
            }
        }

        if (ScrKind.equalsIgnoreCase("1")){
            et_Desc.setText(" سند نقل مواد من مستودع  "+tv_fromStore.getText().toString() + " إلى "  + tv_ToStore.getText().toString() + "  من خلال الموبايل" );
        }else
        {
            et_Desc.setText(" سند ارجاع  مواد من مستودع  "+tv_fromStore.getText().toString() + " إلى "  + tv_ToStore.getText().toString() + "  من خلال الموبايل" );

        }

    }
    public  void GetMaxPONo(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");
        String Login = sharedPreferences.getString("Login", "No");
        if(Login.toString().equals("No")){
            Intent i = new Intent(this,GalaxyLoginActivity.class);
            startActivity(i);
        }

        String query = "SELECT  Distinct  COALESCE(MAX(OrderNo), 0) +1 AS no FROM TransferQtyhdr where UserNo ='"+UserID+"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";
        TextView Maxpo = (TextView) findViewById(R.id.et_OrdeNo);
        if (c1!=null &&c1.getCount() > 0  ) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

        String max1="0";

        max1 =   DB.GetValue(TransferQty.this, "OrdersSitting", "ifnull(TransQtySerial,0)", "1=1");

        if (max1==""){
            max1 ="0";
        }
        max1 =String.valueOf(Integer.parseInt(max1) + 1);
        if (SToD(max1) > SToD(max))
        {
            max = max1 ;
        }

        if (max.length()==1) {
            Maxpo.setText(intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5));

        }
        else {
            Maxpo.setText(intToString(Integer.valueOf(max), 7)  );

        }


        Maxpo.setFocusable(false);
        Maxpo.setEnabled(false);
        Maxpo.setCursorVisible(false);

    // Maxpo.setText(max);

}
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    private  void  UpDateMaxOrderNo() {

        String query = "SELECT  Distinct COALESCE(MAX(orderno), 0)   AS no FROM Po_Hdr";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
        }

        max=(intToString(Integer.valueOf(max), 7)  );

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("m4",max);
        editor.commit();
    }
    private void showList( Integer f  ) {

        lv_Items.setAdapter(null);


       // etTotal.setText(String.valueOf(Total));
       // et_Tottal_Tax.setText(String.valueOf(Total_Tax));

        TransIteQtyAdapter contactListAdapter = new TransIteQtyAdapter(
                TransferQty.this, contactList);
        TextView Total = (TextView)findViewById(R.id.et_Total);
        Total.setText(contactList.size() + "");
        lv_Items.setAdapter(contactListAdapter);
        //  json = new Gson().toJson(contactList);
    }
    public  void showPop(){


      Bundle bundle = new Bundle();
      bundle.putString("Scr", "po");
      bundle.putString("CatNo", CatNo);
      bundle.putString("Kind", ScrKind);
      FragmentManager Manager =  getFragmentManager();
        Pop_TransQty_Select_Items obj = new Pop_TransQty_Select_Items();
      obj.setArguments(bundle);
      obj.show(Manager, null);



  }
    public void btn_back(View view) {
         Intent k= new Intent(this, JalMasterActivity.class);
        startActivity(k);

    }
    public void Save_Method(String ItemNo , String p , String q ,String t,String u ,String dis ,String bounce) {

        TextView OrderNo = (TextView)findViewById(R.id.et_OrdeNo);
        String bounce_unitno,bounce_qty,dis_per,dis_value;
        bounce_unitno=bounce_qty=dis_per=dis_value="";
        String  query = "INSERT INTO Po_dtl(orderno,itemno,price,qty,tax,unitNo,dis_Amy,dis_per,bounce_qty,bounce_unitno) values " +
                "('"+ OrderNo.getText().toString()+"','"+ItemNo +"',"+p+","+q+","+t+",'"+u+"','"+dis_value+"','"+dis_value+"','"+bounce_qty+"','"+bounce_unitno+"')";

        sqlHandler.executeQuery(query);
        showList(1);
    }
    public void Save_List(String ItemNo ,String ItemNm , String q ,String UnitNo ,String UnitNm,String oprand ){

        if (q.toString().equals(""))
            q = "0";




        for (int x = 0; x < contactList.size(); x++) {
            Cls_TransQtyItems obj = new Cls_TransQtyItems();
            obj = contactList.get(x);

         if ( obj.getItemNo().equals(ItemNo)) {
             AlertDialog alertDialog = new AlertDialog.Builder(

                     this).create();

             alertDialog.setTitle("Galaxy");
             alertDialog.setMessage("المادة موجودة");
             alertDialog.setIcon(R.drawable.tick);
             alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {

                 }
             });

             alertDialog.show();
             return;
         }

        }



        Cls_TransQtyItems ItemsList = new Cls_TransQtyItems();
        ItemsList.setItemNo(ItemNo);
        ItemsList.setItemNm(ItemNm);
        ItemsList.setQty(q);
        ItemsList.setUnit(UnitNo);
        ItemsList.setUnit_Rate(oprand);
        ItemsList.setUnitNm(UnitNm);
        contactList.add(ItemsList);
        showList(1);
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
    public void Set_Cust(String No, String Nm) {
       TextView CustNm =(TextView)findViewById(R.id.tv_fromStore);
        TextView acc = (TextView)findViewById(R.id.tv_ToStore);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
    }
    public void Set_Store(String No, String Nm , int f) {

        if (f==1){
            tv_fromStore.setText(No);
            tv_fromStoreNm.setText(Nm);
        }else{
            tv_ToStore.setText(No);
            tv_ToStoreNm.setText(Nm);

        }

        String str = "";
        str = "سند نقل مواد من ";
        str=str+  tv_fromStoreNm.getText();
        str=str+"  إلى  ";
        str=str+  tv_ToStoreNm.getText();
        et_Desc.setText(  str );
    }
    public void Set_Order(  String No) { // FillList
        et_OrdeNo.setText(No);
       contactList.clear();
      //  AddEmptyRow();
        showList(0);
       sqlHandler = new SqlHandler(this);


        String query = "  select   Distinct  ifnull (h.Notes,'') Notes , ifnull(h.fromstore,0) as fromstore  ,ifnull(h.tostore,0) as tostore ,s1.sname as nm1,s2.sname  as nm2  from TransferQtyhdr h" +
                        " left join stores s1 on s1.sno =h.fromstore  " +
                        " left join stores s2 on s2.sno =h.tostore "  +
                      "   where   OrderNo ='" + et_OrdeNo.getText().toString() + "' And Kind ='"+ScrKind+"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_fromStore.setText(c1.getString(c1.getColumnIndex("fromstore")).toString());
                tv_ToStore.setText(c1.getString(c1.getColumnIndex("tostore")).toString());
                tv_fromStoreNm.setText(c1.getString(c1.getColumnIndex("nm1")).toString());
                tv_ToStoreNm.setText(c1.getString(c1.getColumnIndex("nm2")).toString());
                et_Desc.setText(c1.getString(c1.getColumnIndex("Notes")).toString());
            }

            c1.close();
        }
         query = "  select Distinct Unites.UnitName,  invf.Item_Name ,d.itemno,d.UnitNo,d.qty ,d.UnitRate " +
                "   from TransferQtydtl d left join invf on invf.Item_No =  d.itemno    " +
                 " left join Unites on Unites.Unitno=  d.UnitNo " +
                 "  Where d.OrderNo='" + et_OrdeNo.getText().toString() + "' and Kind ='"+ScrKind+"'";
        c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_TransQtyItems  contactListItems = new Cls_TransQtyItems();

                    contactListItems.setItemNo(c1.getString(c1
                            .getColumnIndex("itemno")));
                    contactListItems.setItemNm(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setUnit(c1.getString(c1
                            .getColumnIndex("UnitNo")));
                    contactListItems.setUnitNm(c1.getString(c1
                           .getColumnIndex("UnitName")));
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setUnit_Rate(c1.getString(c1
                            .getColumnIndex("UnitRate")));
                    contactList.add(contactListItems);

                } while (c1.moveToNext());

            }

            c1.close();
        }

        showList(0);
        IsNew=false;
    }
    public void btn_searchCustomer(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");
        if (u=="-1") {
            /*Bundle bundle = new Bundle();
            bundle.putString("Scr", "po");
            FragmentManager Manager = getFragmentManager();
            Select_Customer obj = new Select_Customer();
            obj.setArguments(bundle);
            obj.show(Manager, null);*/
        }
    }
    public void btn_show_Pop(View view) {
        showPop();
    }
    public void btn_print(View view) {

       /* TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
        String q = " SELECT  Distinct COALESCE (orderno, 0)  AS no FROM Po_Hdr   where orderno = '" +  DocNo.getText().toString() + "'";
        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 == null || c1.getCount() == 0) {
            btn_save_po(view);
            return;
        } else
        {
            c1.close();
        }*/

        Intent k = new Intent(this, Store_Trans_Qty_Img.class);

        k.putExtra("Scr", "po");
        TextView   CustNm =(TextView)findViewById(R.id.tv_fromStore);
        TextView   OrdeNo = (TextView)findViewById(R.id.et_OrdeNo);
        TextView   tv_ToStore = (TextView)findViewById(R.id.tv_ToStore);
        k.putExtra("cusnm", CustNm.getText().toString());
        k.putExtra("OrderNo", OrdeNo.getText().toString());
        k.putExtra("tv_ToStore", tv_ToStore.getText().toString());
        startActivity(k);
    }
    public void Save_Recod_Po()    {
        TextView pono = (TextView)findViewById(R.id.et_OrdeNo);

        String q1 = "Select * From TransferQtyhdr Where OrderNo='" + pono.getText().toString() + "'";
        Cursor c1;
        c1 = sqlHandler.selectQuery(q1);

        if (c1 != null && c1.getCount() != 0) {
            IsNew = false;
            c1.close();
        } else {
            IsNew = true;
        }



        TextView custNm = (TextView)findViewById(R.id.tv_fromStore);

        TextView acc = (TextView)findViewById(R.id.tv_ToStore);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());


        Long i;
        ContentValues cv =new ContentValues();
        cv.put("OrderNo", pono.getText().toString());
        cv.put("fromstore",tv_fromStore.getText().toString());
        cv.put("tostore",tv_ToStore.getText().toString());
        cv.put("Tr_Date",currentDateandTime);
        cv.put("Tr_Time",StringTime);
        cv.put("UserNo",UserID);
        cv.put("Posted","-1");
        cv.put("Notes",et_Desc.getText().toString());
        cv.put("Kind",ScrKind+"");


        if (IsNew==true) {
            i = sqlHandler.Insert("TransferQtyhdr", null, cv);
        }
        else {
            i = sqlHandler.Update("TransferQtyhdr", cv, "OrderNo ='"+pono.getText().toString()+"'");
        }

        if(i>0){
            String q ="Delete from  TransferQtydtl where OrderNo ='"+ pono.getText().toString()+"' AND Kind  = '" + ScrKind +"'";
            sqlHandler.executeQuery(q);

            for (int x = 0; x < contactList.size(); x++) {
                Cls_TransQtyItems contactListItems = new Cls_TransQtyItems();
                contactListItems = contactList.get(x);


                cv = new ContentValues();
                cv.put("OrderNo", pono.getText().toString());
                cv.put("itemno",contactListItems.getItemNo());
                cv.put("qty", contactListItems.getQty().toString().replace(",", ""));
                cv.put("UnitNo", contactListItems.getUnit().toString().replace(",", ""));
                cv.put("UnitRate", contactListItems.getUnit_Rate().toString().replace(",", ""));
                cv.put("Kind",ScrKind+"");
                i = sqlHandler.Insert("TransferQtydtl", null, cv);
            }
        }

            if (i> 0 ) {
               // GetMaxPONo();
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle(ScrTitle);
                alertDialog.setMessage("تمت عملية الحفظ بنجاح ");



                alertDialog.setIcon(R.drawable.tick);

                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        View view = null;
                        UpDateMaxOrderNo();
                        IsNew=false;
                      //  btn_print(view);
                    }
                });


                alertDialog.show();
            }
    }
    public void btn_save_po(View view) {
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        String q = "SELECT Distinct *  from  TransferQtyhdr where   Posted > 0 AND   OrderNo ='" + pono.getText().toString() + "' and Kind='" + ScrKind+ "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle(ScrTitle);
            alertDialog.setMessage("لا يمكن التعديل ، لقد تم ترحيل السند");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            c1.close();
           // return;
        }



        if (  contactList.size()==0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle(ScrTitle);
            alertDialog.setMessage("لا يمكن تخزين المستند بدون مواد");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();

            return;
        }

        TextView fromStore = (TextView) findViewById(R.id.tv_fromStore);

        TextView ToStore = (TextView) findViewById(R.id.tv_ToStore);


        if (ToStore.getText().toString().length() == 0) {
            ToStore.setError("required!");
            ToStore.requestFocus();
            return;
        }



        if (fromStore.getText().toString().length() == 0) {
            fromStore.setError("required!");
            fromStore.requestFocus();
            return;
        }



        if (fromStore.getText().toString().equalsIgnoreCase(ToStore.getText().toString())) {
            fromStore.setError("required!");
            fromStore.requestFocus();


            ToStore.setError("required!");
            ToStore.requestFocus();
            return;
        }

        if (pono.getText().toString().length() == 0) {
            pono.setError("required!");
            pono.requestFocus();
            return;
        }


        ToStore.setError(null);
        fromStore.setError(null);
        pono.setError(null);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("اضافة مادة");
        alertDialog.setMessage("هل تريد الاستمرار بعملية الحفظ");
        alertDialog.setIcon(R.drawable.save);
        alertDialog.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Save_Recod_Po();
            }
        });


        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });


        alertDialog.show();



    }
    public void btn_delete(View view) {
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        String q = "SELECT Distinct *  from  Po_Hdr where   posted >0 AND   orderno ='" + pono.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle(ScrTitle);
            alertDialog.setMessage("لا يمكن التعديل ، المستند تم ترحيله");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            c1.close();
            return;
        } else {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(ScrTitle);
            alertDialog.setMessage("هل انت متاكد من عملية الحذف");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Delete_Record_PO();

                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }
    public void Delete_Record_PO(){



        String query ="Delete from  TransferQtyhdr where OrderNo ='"+ et_OrdeNo.getText().toString()+"' and Kind='" +ScrKind +"'";
        sqlHandler.executeQuery(query);

        query ="Delete from  TransferQtydtl where OrderNo ='"+ et_OrdeNo.getText().toString()+"' and Kind ='" + ScrKind + "'";
        sqlHandler.executeQuery(query);



        contactList.clear();
        GetMaxPONo();
        showList(0);
        IsNew = true;

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle(ScrTitle);
        alertDialog.setMessage("تمت عملية الحذف بنجاح");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
       alertDialog.show();
    }
    public void btn_Search_Orders(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        bundle.putString("Kind", ScrKind+"");
        FragmentManager Manager =  getFragmentManager();
        SearchQtyTransOrders obj = new SearchQtyTransOrders();
        obj.setArguments(bundle);
        obj.show(Manager, null);
       }
    public void btn_share(View view) {

        if(IsNew==true){
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle( "سند نقل مواد");
            alertDialog.setMessage("يجب تخزين المستند اولاَ");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialog.show();
            return;
        }


        final  SqlHandler sql_Handler = new SqlHandler(this);

        final  String str;


            String  query = "SELECT Distinct  OrderNo,fromstore,tostore  ,Tr_Date,ifnull(Tr_Time,'') as Tr_Time  ,UserNo  , Posted , Kind,Notes " +
                "  FROM TransferQtyhdr where OrderNo  ='" +et_OrdeNo.getText().toString()+"' and Kind ='" + ScrKind +"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        JSONObject  jsonObject = new JSONObject();

        if (c1.getCount() > 0 && c1!=null) {
            c1.moveToFirst();


            try {
                jsonObject.put("OrderNo",c1.getString(c1.getColumnIndex("OrderNo")));
                jsonObject.put("fromstore",c1.getString(c1.getColumnIndex("fromstore")));
                jsonObject.put("tostore",c1.getString(c1.getColumnIndex("tostore")));
                jsonObject.put("Tr_Date",c1.getString(c1.getColumnIndex("Tr_Date")));
                jsonObject.put("Tr_Time",c1.getString(c1.getColumnIndex("Tr_Time")));
                jsonObject.put("UserNo",c1.getString(c1.getColumnIndex("UserNo")));
                jsonObject.put("Posted",c1.getString(c1.getColumnIndex("Posted")));
                jsonObject.put("Kind",c1.getString(c1.getColumnIndex("Kind")));
                jsonObject.put("Notes",c1.getString(c1.getColumnIndex("Notes")));

            }
            catch ( JSONException ex){
                ex.printStackTrace();
            }


            c1.close();
        }

        String json = new Gson().toJson(contactList);
        str = jsonObject.toString()+ json;


        loadingdialog = ProgressDialog.show(TransferQty.this, "الرجاء الانتظار ...", "العمل جاري على المستند", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(TransferQty.this);
                ws.SaveTrandferStoreQty(str);
                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        cv.put("Posted", We_Result.ID);
                        long i;
                        i = sql_Handler.Update("TransferQtyhdr", cv, "OrderNo='"+ et_OrdeNo.getText().toString()+"' and Kind ='"+ ScrKind+"'");

                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        TransferQty.this).create();
                                alertDialog.setTitle(ScrTitle);
                                alertDialog.setMessage("تمت عملية اعتماد المستند بنجاح" + We_Result.ID +"");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                                alertDialog.show();
                                contactList.clear();
                                GetMaxPONo();
                                SetStoreNo();
                                showList(0);

                            }
                        });
                    } else {

                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        TransferQty.this).create();
                                alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " +We_Result.ID+"" );
                                alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();

                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setMessage("عملية الاعتماد لم تتم بنجاح" +"    " );
                            }
                        });
                    }

                } catch (final Exception e) {
                    loadingdialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    TransferQty.this).create();
                            alertDialog.setTitle("فشل في عمليه الاتصال");
                            alertDialog.setMessage(e.getMessage().toString());
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
    public void btn_new(View view) {
        GetMaxPONo();
        SetStoreNo();
        contactList.clear();
        showList(0);
        IsNew=false;
        imgFromStore.setVisibility(View.VISIBLE);
        tv_fromStore.setText("");
        tv_fromStoreNm.setText("");


    }
    public void btn_Delete_Item( final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("حذف مادة");
        alertDialog.setMessage("هل انت متاكد من عملية الحذف");
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int position =lv_Items.getPositionForView(view);
                contactList.remove(position);

                showList(0);
            }
        });

        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Intent intent = new Intent(getApplicationContext(), JalMasterActivity.class);
         Intent intent = new Intent(getApplicationContext(), JalMasterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void Select_FromSrore(View view) {


            Bundle bundle = new Bundle();
            bundle.putString("Scr", "FromStore");
            FragmentManager Manager = getFragmentManager();
            Select_StoreNo obj = new Select_StoreNo()  ;
            obj.setArguments(bundle);
            obj.show(Manager, null);






    }
    public void Select_ToSrore(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "ToStore");
        FragmentManager Manager = getFragmentManager();
        Select_StoreNo obj = new Select_StoreNo()  ;
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void btn_UpDateStore(View view) {

        Drawable greenProgressbar;
        greenProgressbar = this.getResources().getDrawable(R.drawable.progrees_states);

        TextView tv  ;
        RelativeLayout.LayoutParams lp;

        tv = new TextView(getApplicationContext());
        lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setLayoutParams(lp);
        tv.setPadding(10, 15, 10, 15);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.BLUE);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

        final Handler _handler = new Handler();

        final String Store = tv_fromStore.getText().toString();

        if (tv_fromStore.getText().toString().length() == 0) {
            tv_fromStore.setError("required!");
            tv_fromStore.requestFocus();
            return;
        }




        final ProgressDialog custDialog = new ProgressDialog(TransferQty.this);
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("تحديت كميات مستودع النقل");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();
        String MaxSeer = "1";
           /* query = "SELECT  COALESCE(MAX(ser), 0) +1 AS no from ManStore";
             Cursor c1 = sqlHandler.selectQuery(query);
            String Operand = "0";
            Double Order_qty = 0.0;
            if (c1 != null && c1.getCount() != 0) {
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    MaxSeer = String.valueOf(c1.getInt(0));
                }
                c1.close();
            }*/

        String q;
        final String Ser = "1";
        q = "Delete from ManStore";
        sqlHandler.executeQuery(q);
        q = "delete from sqlite_sequence where name='ManStore'";
        sqlHandler.executeQuery(q);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(TransferQty.this);
                ws.TrnsferQtyFromMobile(Store, "0", "");
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_date = js.getJSONArray("date");
                    JSONArray js_fromstore = js.getJSONArray("fromstore");
                    JSONArray js_tostore = js.getJSONArray("tostore");
                    JSONArray js_des = js.getJSONArray("des");
                    JSONArray js_docno = js.getJSONArray("docno");
                    JSONArray js_itemno = js.getJSONArray("itemno");
                    JSONArray js_qty = js.getJSONArray("qty");
                    JSONArray js_UnitNo = js.getJSONArray("UnitNo");
                    JSONArray js_UnitRate = js.getJSONArray("UnitRate");
                    JSONArray js_myear = js.getJSONArray("myear");
                    JSONArray js_StoreName = js.getJSONArray("StoreName");
                    JSONArray js_RetailPrice = js.getJSONArray("RetailPrice");

                    for (i = 0; i < js_docno.length(); i++) {
                        q = "Insert INTO ManStore(SManNo,date,fromstore,tostore,des,docno,itemno,qty,UnitNo,UnitRate,myear,RetailPrice ,StoreName ,ser) " +
                                " values ("
                                + Store.toString()
                                + ",'" + js_date.get(i).toString()
                                + "','" + js_fromstore.get(i).toString()
                                + "','" + js_tostore.get(i).toString()
                                + "','" + js_des.get(i).toString()
                                + "','" + js_docno.get(i).toString()
                                + "','" + js_itemno.get(i).toString()
                                + "','" + js_qty.get(i).toString()
                                + "','" + js_UnitNo.get(i).toString()
                                + "','" + js_UnitRate.get(i).toString()
                                + "','" + js_myear.get(i).toString()
                                + "','" + js_RetailPrice.get(i).toString()
                                + "','" + js_StoreName.get(i).toString()
                                + "'," + Ser.toString()
                                + " )";
                        sqlHandler.executeQuery(q);
                        custDialog.setMax(js_docno.length());
                        custDialog.incrementProgressBy(1);
                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();
                            imgFromStore.setVisibility(View.INVISIBLE);

                        }
                    }
                    final int total = i;
                    _handler.post(new Runnable() {

                        public void run() {

                            custDialog.dismiss();
                            imgFromStore.setVisibility(View.INVISIBLE);

                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            imgFromStore.setVisibility(View.INVISIBLE);

                            custDialog.dismiss();
                        }
                    });
                }
            }
        }).start();
    }

}
