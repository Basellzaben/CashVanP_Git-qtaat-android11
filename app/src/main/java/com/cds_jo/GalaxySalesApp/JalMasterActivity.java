 package com.cds_jo.GalaxySalesApp;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.res.Configuration;
        import android.database.Cursor;
        import android.os.Build;
        import android.os.Handler;
        import android.preference.PreferenceManager;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.app.FragmentManager;
        import android.os.Bundle;

        import java.lang.reflect.Field;
        import java.text.DateFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.Locale;

        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.support.v7.widget.AppCompatTextView;
        import android.text.InputType;
        import android.text.method.PasswordTransformationMethod;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.AdapterView;
        import android.widget.EditText;
        import android.widget.GridView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.cds_jo.GalaxySalesApp.ManCard.DetailCardMan;
        import com.cds_jo.GalaxySalesApp.NewHomePage.NewHomePage;
        import com.cds_jo.GalaxySalesApp.assist.Acc_ReportActivity;
        import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
        import com.cds_jo.GalaxySalesApp.assist.OrdersItems;
        import com.cds_jo.GalaxySalesApp.Pos.Pos_Activity;
        import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;
        import com.cds_jo.GalaxySalesApp.assist.Sale_ReturnActivity;


        import org.json.JSONArray;
        import org.json.JSONObject;

        import Methdes.MyTextView;
        import header.Header_Frag;


 public class JalMasterActivity extends FragmentActivity {
    GridView gridView;
    ArrayList<Items> gridArray = new ArrayList<Items>();
    JalImageGridAdapter customGridAdapter;
       String UserID="";
    GetPermession obj;
    SqlHandler sqlHandler;
    private String getday() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat mdformatyear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String stryear = mdformatyear.format(calendar.getTime());

        SimpleDateFormat mdformatmonth = new SimpleDateFormat("MM", Locale.ENGLISH);
        String strmonth = mdformatmonth.format(calendar.getTime());

        SimpleDateFormat mdformatday = new SimpleDateFormat("dd", Locale.ENGLISH);
        String strday = mdformatday.format(calendar.getTime());

        String day=strday+"/"+strmonth+"/"+stryear;

        return day;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // PostLogTrans  postLogTrans = new PostLogTrans(JalMasterActivity.this);
        setContentView(R.layout.activity_jal_master);
        TextView Un = (TextView) findViewById(R.id.tv_UserName);

//startActivity(new Intent(JalMasterActivity.this, NewHomePage.class));



        String q=DB.GetValue(this,"cardMan","no","1=1");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      UserID = sharedPreferences.getString("UserID", "");

        String  companym= DB.GetValue(JalMasterActivity.this,"ComanyInfo","SuperVisorMobile","ID='"+UserID+"'");
        String mansupermobile= DB.GetValue(JalMasterActivity.this,"manf","SupervisorMobile","man='"+UserID+"'");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        //     editor.putString("SupervisorMobile", SupervisorMobile.get(i).toString());

        if(mansupermobile==null || mansupermobile.equals("0")||mansupermobile.equals("")) {
               editor.putString("SuperVisorMobile", companym);
            //editor.putString("SupervisorMobile", SupervisorMobile.get(i).toString());
            editor.apply();
        }else{
            editor.putString("SupervisorMobile",mansupermobile);
            editor.apply();
        }



        String query = "Select DateLogin From TimeLogin where manNo ='"+UserID +"'";
        sqlHandler = new SqlHandler(this);
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                try{
                    String day=getday();
                    String d=c1.getString(c1
                            .getColumnIndex("DateLogin"));
                    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date d1 = sdf.parse(d);
                    Date day2 = sdf.parse(day);
                    if(sdf.format(d1).equals(sdf.format( day2)))
                    {

                    }else
                    {
                        String qu="UPDATE TimeLogin SET DateLogin='"+String.valueOf(day)+"' WHERE  manNo ='"+UserID+"'" ;
                        sqlHandler.executeQuery(qu);
                       /* DetailCardMan exampleDialog = new DetailCardMan();
                        exampleDialog.show(getSupportFragmentManager(), "dialog");*/
                       android.app.FragmentManager fragmentManager = getFragmentManager();
                       DetailCardMan detailCardMan =new DetailCardMan();
                       detailCardMan.show(fragmentManager,null);



                    }



                }catch(ParseException ex){
                    // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
                }

            }}
        else
        {
            String day=getday();

            String qur = "insert into TimeLogin (manNo,DateLogin ) values ( '" +
                    UserID
                    + "','" + day
                    + "')";

            sqlHandler.executeQuery(qur);
           /* DetailCardMan exampleDialog = new DetailCardMan();
            exampleDialog.show(getSupportFragmentManager(), "dialog");*/

            android.app.FragmentManager fragmentManager = getFragmentManager();
            DetailCardMan detailCardMan = new DetailCardMan();
            detailCardMan.show( fragmentManager,null);
        }


try {
    ComInfo.ComNo = Integer.parseInt(DB.GetValue(this, "ComanyInfo", "CompanyID", "1=1"));
}catch (Exception t){}


        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, null);

       TextView  tv_News  =(TextView) findViewById(R.id.tv_News);
        Animation translatebu= AnimationUtils.loadAnimation(this, R.anim.animationfile);

        String msg="";
   //     msg =msg+" *** " +"كل عام وانتم بالف خير بمناسبة عيد الفطر السعيد  ";
        msg =msg+" *** " +"يوجد اجتماع اليوم على تمام الساعة التاسعة  ";
        msg =msg+" *** "  ;

        tv_News.setVisibility(View.INVISIBLE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        if(currentDateandTime.equalsIgnoreCase("2021/02/22") ){
            tv_News.setVisibility(View.VISIBLE);
            tv_News.setText(msg);
            tv_News.startAnimation(translatebu);
        }





        Call_marque();





        String Login = sharedPreferences.getString("Login", "No");
        if (Login.toString().equals("No")) {
            Intent i = new Intent(this, NewLoginActivity.class);
            startActivity(i);
        }


        Un.setText( ( ComInfo.ComNo + "")+   "      "+    sharedPreferences.getString("UserName", "") + "    "+ sharedPreferences.getString("CompanyNm", "") + "/" + sharedPreferences.getString("Address", ""));

        this.setTitle( ComInfo.ComNo + sharedPreferences.getString("CompanyNm", "") + "/" + sharedPreferences.getString("Address", ""));


        Bitmap rec_mony = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal4);
        Bitmap invoice = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal11);
        Bitmap trans_from_store = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel14);
        Bitmap po = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal1);
        Bitmap print = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal8);
        Bitmap logout = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal7);
        Bitmap acc = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal6);
        Bitmap gps = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal10);
        Bitmap Transfer = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal3);
        Bitmap ItemCostReport = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel15);
        Bitmap schedule = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel16);
        Bitmap inventory = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel17);
        Bitmap survey_icon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal9);
        Bitmap setting = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal2);
        Bitmap stockadd = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal12);
        Bitmap preapreqty = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel19);
        Bitmap updatetrans = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel18);
        Bitmap manbalance = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal13);
        Bitmap dailysummery = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel20);

        Bitmap img_visit = BitmapFactory.decodeResource(this.getResources(), R.mipmap.img_visit);
        Bitmap item_transfer = BitmapFactory.decodeResource(this.getResources(), R.mipmap.item_transfer);
        Bitmap recv_items = BitmapFactory.decodeResource(this.getResources(), R.mipmap.rec_items);
        obj = new GetPermession();
        Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.home);
        Bitmap userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.home);


        if(  ComInfo.ComNo==Companies.Arabian.getValue()) {
            if (obj.CheckAction(JalMasterActivity.this, "30002", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(gps, "خط السير", "30002"));
            if (obj.CheckAction(JalMasterActivity.this, "30003", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(invoice, "فاتورة ", "30003"));
           /* if (obj.CheckAction(JalMasterActivity.this, "30005", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(po, "طلب بيع", "30005"));
*/            if (obj.CheckAction(JalMasterActivity.this, "30004", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(rec_mony, "سند قبض", "30004"));
            if (obj.CheckAction(JalMasterActivity.this, "30001", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(acc, "كشف حساب", "30001"));
           /* if (obj.CheckAction(JalMasterActivity.this, "30007", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(print, "استعراض المواد", "30007"));
           if (obj.CheckAction(JalMasterActivity.this, "30022", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(trans_from_store, "استعراض كميات المستودع", "30022"));
       */      if (1 == 1)
                gridArray.add(new Items(Transfer, "تحديث البيانات", "30008"));
         /*   if (obj.CheckAction(JalMasterActivity.this, "30021", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(ItemCostReport, "استعلام كلفة مادة", "30021"));
*/            if (obj.CheckAction(JalMasterActivity.this, "30010", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(schedule, "جدول زيارات العميل", "30010"));
         /*   if (obj.CheckAction(JalMasterActivity.this, "30011", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(inventory, " جرد كميات العميل", "30011"));
          */        /*  if (obj.CheckAction(JalMasterActivity.this, "30020", SCR_ACTIONS.open.getValue()))
                        gridArray.add(new Items(survey_icon, "الاستبيان","30020"));*/
           /* if (obj.CheckAction(JalMasterActivity.this, "30013", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(stockadd, " ارجاع المواد", "30013"));
   */         if (ComInfo.ComNo == 4) {
                if (obj.CheckAction(JalMasterActivity.this, "30019", SCR_ACTIONS.open.getValue()))
                    gridArray.add(new Items(setting, "إعدادات عامة", "30019"));
            } else {
                gridArray.add(new Items(setting, "إعدادات عامة", "30019"));
            }
          /*  if (obj.CheckAction(JalMasterActivity.this, "30015", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(preapreqty, "طلب تجهيز كميات", "30015"));
*/          /*  if (obj.CheckAction(JalMasterActivity.this, "30018", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(updatetrans, "تعديل الحركات", "30018"));
 */          /* if (obj.CheckAction(JalMasterActivity.this, "30017", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(manbalance, "تسوية الجرد", "30017"));
*/          /*  if (obj.CheckAction(JalMasterActivity.this, "30016", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(dailysummery, " الملخص اليومي", "30016"));
*/            gridArray.add(new Items(logout, "خروج", "-1"));

        }else {
            if (obj.CheckAction(JalMasterActivity.this, "30002", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(gps, "خط السير", "30002"));
            if (obj.CheckAction(JalMasterActivity.this, "30003", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(invoice, "فاتورة ", "30003"));
            if (obj.CheckAction(JalMasterActivity.this, "30005", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(po, "طلب بيع", "30005"));
            if (obj.CheckAction(JalMasterActivity.this, "30004", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(rec_mony, "سند قبض", "30004"));
            if (obj.CheckAction(JalMasterActivity.this, "30001", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(acc, "كشف حساب", "30001"));

            if (obj.CheckAction(JalMasterActivity.this, "30020", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(img_visit, "صور الجولات", "30020"));

            if (obj.CheckAction(JalMasterActivity.this, "300201", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(recv_items, "سند تسليم بضاعة", "30021"));

            if (obj.CheckAction(JalMasterActivity.this, "30013", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(stockadd, " ارجاع المواد", "30013"));



            if (obj.CheckAction(JalMasterActivity.this, "30022", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(trans_from_store, "استعراض كميات المستودع", "30022"));
            if (1 == 1)
                gridArray.add(new Items(Transfer, "تحديث البيانات", "30008"));
          /*  if (obj.CheckAction(JalMasterActivity.this, "30021", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(ItemCostReport, "استعلام كلفة مادة", "30021"));*/
            if (obj.CheckAction(JalMasterActivity.this, "30010", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(schedule, "جدول زيارات العميل", "30010"));
             /*       if (obj.CheckAction(JalMasterActivity.this, "30011", SCR_ACTIONS.open.getValue()))
                 gridArray.add(new Items(inventory, " جرد كميات العميل", "30011"));
                  if (obj.CheckAction(JalMasterActivity.this, "30020", SCR_ACTIONS.open.getValue()))
                        gridArray.add(new Items(survey_icon, "الاستبيان","30020"));*/
            if (obj.CheckAction(JalMasterActivity.this, "30007", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(print, "استعراض المواد", "30007"));


            if (ComInfo.ComNo == 4) {
                if (obj.CheckAction(JalMasterActivity.this, "30019", SCR_ACTIONS.open.getValue()))
                    gridArray.add(new Items(setting, "إعدادات عامة", "30019"));
            } else {
                gridArray.add(new Items(setting, "إعدادات عامة", "30019"));
            }
            if (obj.CheckAction(JalMasterActivity.this, "30015", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(preapreqty, "طلب تجهيز كميات", "30015"));
           /* if (obj.CheckAction(JalMasterActivity.this, "30018", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(updatetrans, "تعديل الحركات", "30018"));*/
            if (obj.CheckAction(JalMasterActivity.this, "30017", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(manbalance, "تسوية الجرد", "30017"));
            if (obj.CheckAction(JalMasterActivity.this, "30016", SCR_ACTIONS.open.getValue()))
                gridArray.add(new Items(dailysummery, " الملخص اليومي", "30016"));
            gridArray.add(new Items(logout, "خروج", "-1"));


        }




        try {
            gridView = (GridView) findViewById(R.id.gridView1);
            customGridAdapter = new JalImageGridAdapter(this, R.layout.jel_row_grid, gridArray);
            //customGridAdapter = new CustomGridAdapter    (this, R.layout.gridimage, gridArray);

            //Cls_Main_Adapter apter = new Cls_Main_Adapter(this,gridArray);

            gridView.setAdapter(customGridAdapter);
        } catch (Exception ex) {

        }
        final String Permession = sharedPreferences.getString("Permession", "0");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {


          if (ComInfo.ComNo==Companies.Arabian.getValue() ||    !Permession.equalsIgnoreCase("0")){
               Intent k  =new Intent(v.getContext(),MainActivity.class);
               Items obj = new  Items( );
               obj = gridArray.get(position);


               switch (obj.getSCR_CODE()){
                   case "30002":
                       k = new Intent(v.getContext(),MainActivity.class);
                    break;


                   case "30003":
                       ComInfo.DocType = 1;
                       k = new Intent(v.getContext(), Sale_InvoiceActivity.class);
                       break;


                   case "30005":
                       k = new Intent(v.getContext(), OrdersItems.class);
                       break;


                   case "30004":
                       k = new Intent(v.getContext(),RecvVoucherActivity.class);
                       break;


                   case "30001":
                       k = new Intent(v.getContext(),Acc_ReportActivity.class);
                       break;



                   case "30007":
                       k = new Intent(v.getContext(),ItemGalleryActivity.class);
                       break;

                   case "30022":
                       k = new Intent(v.getContext(),TransQtyReportActivity.class);
                       break;

                   case "30008":
                       k = new Intent(v.getContext(),UpdateDataToMobileActivity.class);
                       break;

                   /*case "30021":
                       k = new Intent(v.getContext(),ItemCostActivity.class);
                       break;
*/
                   case "30010":
                       k = new Intent(v.getContext(),ScheduleManActivity.class);
                       break;

                   case "30011":
                       k = new Intent(v.getContext(),CustomerQty.class);
                       break;

                   case "30013":
                       ComInfo.DocType = 2;
                       k = new Intent(v.getContext(), Sale_ReturnActivity.class);
                       break;
                   case "30019":
                       k = new Intent(v.getContext(),SittingNew.class);
                       break;

                   case "30015":
                       k = new Intent(v.getContext(),PreapareManQty.class);
                       break;
                   case "30018":
                       k = new Intent(v.getContext(),EditeTransActivity.class);
                       break;

                   case "30017":
                       k = new Intent(v.getContext(),ManBalanceQtyActivity.class);
                       break;



                   case "30016":
                       k = new Intent(v.getContext(),ManSummeryNew.class);
                       break;



                   case "-1":
                       k = new Intent(v.getContext(),NewLoginActivity.class);
                       break;






               }



               startActivity(k);

           }else {


               //  Toast.makeText(getApplicationContext(), gridArray.get(position).getTitle(), Toast.LENGTH_SHORT).show();
               if (position == 0) {
                   if (!obj.CheckAction(v.getContext(), "30002", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "خط السير");

                   } else {

                       Intent k = new Intent(v.getContext(), MainActivity.class);
                       startActivity(k);
                   }
               } else if (position == 1) {
                   if (!obj.CheckAction(v.getContext(), "30003", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "فاتورة المبيعات ");
                   } else {
                       ComInfo.DocType = 1;
                         Intent k = new Intent(v.getContext(), Sale_InvoiceActivity.class);
                      // Intent k = new Intent(v.getContext(), Pos_Activity.class);
                       startActivity(k);
                   }
               } else if (position == 2) {

                   if (!obj.CheckAction(v.getContext(), "30005", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "طلب البيع");
                   } else {

                       Intent k = new Intent(v.getContext(), OrdersItems.class);
                       startActivity(k);
                   }

               } else if (position == 3) {

                   if (!obj.CheckAction(v.getContext(), "30004", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "سند القبض");
                   } else {

                       Intent k = new Intent(v.getContext(), RecvVoucherActivity.class);
                       startActivity(k);
                   }

               } else if (position == 4) {

                   if (!obj.CheckAction(v.getContext(), "30001", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "كشف حساب");
                   } else {

                       Intent k = new Intent(v.getContext(), Acc_ReportActivity.class);
                       startActivity(k);
                   }


               } else if (position == 5) {

                   if (!obj.CheckAction(v.getContext(), "30020", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "سند القبض");
                   } else {

                       Intent k = new Intent(v.getContext(), VisitImges.class);
                       startActivity(k);
                   }



               } else if (position == 6) {

                   if (!obj.CheckAction(v.getContext(), "30021", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "سند القبض");
                   } else {

                       ComInfo.DocType = 3;
                       Intent k = new Intent(v.getContext(), Delivery_VoucherAct.class);
                       startActivity(k);
                   }








               } else if (position == 11) {

                   if (!obj.CheckAction(v.getContext(), "30007", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "استعراض الصور");
                   } else {
                       Intent k = new Intent(v.getContext(), ItemGalleryActivity.class);
                       startActivity(k);
                   }


               } else if (position == 8) {
                 /*  if( !obj.CheckAction(v.getContext(),"30006",  SCR_ACTIONS.open.getValue())){
                       AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                       alertDialog.setTitle( "استعراض كميات المندوب"+"  "+ "30006");
                       alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                       alertDialog.setIcon(R.drawable.key);
                       alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                               return;
                           }
                       });
                       alertDialog.show();
                   }else {

                       Intent k = new Intent(v.getContext(),TransQtyReportActivity.class);
                       startActivity(k);
                   }*/

                   if (ComInfo.ComNo == 3) {
                       Intent k = new Intent(JalMasterActivity.this, TransQtyReportActivity.class);
                       startActivity(k);
                   } else {
                     /*  final String pass = DB.GetValue(JalMasterActivity.this, "Tab_Password", "Password", "PassNo = 4");
                       AlertDialog.Builder alertDialog = new AlertDialog.Builder(JalMasterActivity.this);
                       alertDialog.setTitle(DB.GetValue(JalMasterActivity.this, "Tab_Password", "PassDesc", "PassNo = 4"));

                       alertDialog.setMessage("الرجاء ادخال كلمة المرور الخاصة بفتح هذه الشاشة");

                       final EditText input = new EditText(JalMasterActivity.this);
                       input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                       input.setTransformationMethod(new PasswordTransformationMethod());
                       LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                               LinearLayout.LayoutParams.MATCH_PARENT,
                               LinearLayout.LayoutParams.MATCH_PARENT);
                       input.setLayoutParams(lp);
                       alertDialog.setView(input);
                       alertDialog.setIcon(R.drawable.key);

                       alertDialog.setPositiveButton("موافق",
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       String password = input.getText().toString();

                                       if (pass.equals(password)) {
                                           Intent k = new Intent(JalMasterActivity.this, TransQtyReportActivity.class);
                                           startActivity(k);
                                       } else {
                                           Toast.makeText(getApplicationContext(),
                                                   "كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });

                       alertDialog.setNegativeButton("لا",
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.cancel();
                                   }
                               });

                       alertDialog.show();*/
                       Intent k = new Intent(JalMasterActivity.this, TransQtyReportActivity.class);
                       startActivity(k);
                   }
               } else if (position == 9) {
                    /*if (!obj.CheckAction(v.getContext(), "30008", SCR_ACTIONS.open.getValue())) {
                        ViewDialog alert = new ViewDialog();
                        alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة" ,"30002");
                    } else {*/

                   Intent k = new Intent(v.getContext(), UpdateDataToMobileActivity.class);
                   startActivity(k);
                   // }


               } else if (position == 99) {
                   /*final String pass = DB.GetValue(JalMasterActivity.this, "Tab_Password", "Password", "PassNo = 1");
                   AlertDialog.Builder alertDialog = new AlertDialog.Builder(JalMasterActivity.this);
                   alertDialog.setTitle(DB.GetValue(JalMasterActivity.this, "Tab_Password", "PassDesc", "PassNo = 1"));

                   alertDialog.setMessage("الرجاء ادخال كلمة المرور الخاصة بفتح هذه الشاشة");

                   final EditText input = new EditText(JalMasterActivity.this);
                   input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                   input.setTransformationMethod(new PasswordTransformationMethod());
                   LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                           LinearLayout.LayoutParams.MATCH_PARENT,
                           LinearLayout.LayoutParams.MATCH_PARENT);
                   input.setLayoutParams(lp);
                   alertDialog.setView(input);
                   alertDialog.setIcon(R.drawable.key);

                   alertDialog.setPositiveButton("موافق",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   String password = input.getText().toString();

                                   if (pass.equals(password)) {
                                       Intent k = new Intent(JalMasterActivity.this, ItemCostActivity.class);
                                       startActivity(k);
                                   } else {
                                       Toast.makeText(getApplicationContext(),
                                               "كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });

                   alertDialog.setNegativeButton("لا",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.cancel();
                               }
                           });

                   alertDialog.show();
*/
                   Intent k = new Intent(JalMasterActivity.this, ItemCostActivity.class);
                   startActivity(k);
               } else if (position == 100) {
                   Intent k = new Intent(v.getContext(), NotificationActivity.class);
                   startActivity(k);

               } else if (position == 10) {
                   if (!obj.CheckAction(v.getContext(), "30010", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "جدول الزيارات");
                   } else {

                       Intent k = new Intent(v.getContext(), ScheduleManActivity.class);
                       startActivity(k);
                   }


              /*} else if (position == 10) {

                   if (!obj.CheckAction(v.getContext(), "30011", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "جرد كميات العميل");
                   } else {

                       Intent k = new Intent(v.getContext(), CustomerQty.class);
                       startActivity(k);
                   }



               } else if (position == 11) {
                    Intent k = new Intent(v.getContext(), QuestneerActivity.class);
                   //Intent k = new Intent(v.getContext(), MapsNewActivity.class);

                   startActivity(k);


               }
 */
               }  else if (position == 7) {
                   if (!obj.CheckAction(v.getContext(), "30013", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "ارجاع المواد");
                   } else {

                       // Intent k = new Intent(v.getContext(), CustomerReturnQtyActivity.class);
                       ComInfo.DocType = 2;
                       Intent k  = new Intent(v.getContext(), Sale_ReturnActivity.class);
                       startActivity(k);
                   }


               } else if (position == 12) {


                                     if (UserID.equalsIgnoreCase("admin")) {
                                         Intent k = new Intent(JalMasterActivity.this, SittingNew.class);
                                         startActivity(k);
                                     } else {
                                         Toast.makeText(getApplicationContext(),
                                                 "لا يمكن  فتح الشاشة", Toast.LENGTH_SHORT).show();
                                     }


               } else if (position == 13) {
                   if (!obj.CheckAction(v.getContext(), "30015", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "تجهيز الكميات");
                   } else {

                       Intent k = new Intent(v.getContext(), PreapareManQty.class);
                       startActivity(k);
                   }


             /*  } else if (position == 13) {


                   final String pass = DB.GetValue(JalMasterActivity.this, "Tab_Password", "Password", "PassNo = 2");
                   AlertDialog.Builder alertDialog = new AlertDialog.Builder(JalMasterActivity.this);
                   alertDialog.setTitle(DB.GetValue(JalMasterActivity.this, "Tab_Password", "PassDesc", "PassNo = 2"));
                   alertDialog.setMessage("الرجاء ادخال كلمة المرور الخاصة بفتح هذه الشاشة");

                   final EditText input = new EditText(JalMasterActivity.this);
                   input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                   input.setTransformationMethod(new PasswordTransformationMethod());
                   LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                           LinearLayout.LayoutParams.MATCH_PARENT,
                           LinearLayout.LayoutParams.MATCH_PARENT);
                   input.setLayoutParams(lp);
                   alertDialog.setView(input);
                   alertDialog.setIcon(R.drawable.key);

                   alertDialog.setPositiveButton("موافق",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   String password = input.getText().toString();

                                   if (pass.equals(password)) {
                                       Intent k = new Intent(JalMasterActivity.this, EditeTransActivity.class);
                                       startActivity(k);

                                   } else {
                                       Toast.makeText(getApplicationContext(),
                                               "كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();

                                   }

                               }
                           });

                   alertDialog.setNegativeButton("لا",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.cancel();
                               }
                           });

                   alertDialog.show();
*/

               } else if (position == 14) {
                 /*  if( !obj.CheckAction(v.getContext(),"30017",  SCR_ACTIONS.open.getValue())){
                       AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                       alertDialog.setTitle( "جرد كميات المندوب"+"  "+ "30017");
                       alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                       alertDialog.setIcon(R.drawable.key);
                       alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                               return;
                           }
                       });
                       alertDialog.show();
                   }else {

                       Intent k = new Intent(v.getContext(),ManBalanceQtyActivity.class);
                       startActivity(k);
                   }*/

                   final String pass = DB.GetValue(JalMasterActivity.this, "Tab_Password", "Password", "PassNo = 5");
                   AlertDialog.Builder alertDialog = new AlertDialog.Builder(JalMasterActivity.this);
                   alertDialog.setTitle(DB.GetValue(JalMasterActivity.this, "Tab_Password", "PassDesc", "PassNo = 5"));
                   alertDialog.setMessage("الرجاء ادخال كلمة المرور الخاصة بفتح هذه الشاشة");

                   final EditText input = new EditText(JalMasterActivity.this);
                   input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                   input.setTransformationMethod(new PasswordTransformationMethod());
                   LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                           LinearLayout.LayoutParams.MATCH_PARENT,
                           LinearLayout.LayoutParams.MATCH_PARENT);
                   input.setLayoutParams(lp);
                   alertDialog.setView(input);
                   alertDialog.setIcon(R.drawable.key);

                   alertDialog.setPositiveButton("موافق",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   String password = input.getText().toString();

                                   if (pass.equals(password)) {
                                       Intent k = new Intent(JalMasterActivity.this, ManBalanceQtyActivity.class);
                                       startActivity(k);

                                   } else {
                                       Toast.makeText(getApplicationContext(),
                                               "كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();

                                   }

                               }
                           });

                   alertDialog.setNegativeButton("لا",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.cancel();
                               }
                           });

                   alertDialog.show();


               } else if (position == 15) {

                   if (!obj.CheckAction(v.getContext(), "30016", SCR_ACTIONS.open.getValue())) {
                       ViewDialog alert = new ViewDialog();
                       alert.showDialog(JalMasterActivity.this, "نأسف أنت لا تملك صلاحية فتح هذه الشاشة", "الملخص اليومي");
                   } else {

                       Intent k = new Intent(v.getContext(), ManSummeryNew.class);
                       startActivity(k);
                   }


               } else if (position == 16) {
                   SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   editor.putString("Login", "No");
                   editor.commit();
                   Intent k = new Intent(v.getContext(), NewLoginActivity.class);
                   startActivity(k);


               } else if (position == 19) {

                   Intent k = new Intent(v.getContext(), DoctorReportActivity.class);
                   startActivity(k);


               }           /*    else if (position == 19) {
                   if( !obj.CheckAction(v.getContext(),"30017",SCR_ACTIONS.open.getValue())){
                       AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                       alertDialog.setTitle( "جرد كميات المندوب"+"  "+ "30017");
                       alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                       alertDialog.setIcon(R.drawable.key);
                       alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                               return;
                           }
                       });
                       alertDialog.show();
                   }else {

                       //Intent k = new Intent(v.getContext(),NewMapsActivity.class);
                       //startActivity(k);
                   }
               }*/

              /* else if (position == 19) {

               Intent k = new Intent(v.getContext(), ZepraActivity.class);
               startActivity(k);

           }*/

           }
            }
        });

         Fragment frag=new Header_Frag();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();
    }
    @Override
    public void onResume(){
        super.onResume();

        Call_marque();


    }

    private  void UpdateMsg() {
         // Toast.makeText(this,"Update" ,Toast.LENGTH_SHORT).show();
        final Handler _handler  = new Handler();

       final    SqlHandler sqlHandler;
          sqlHandler = new SqlHandler(this);

          SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String UserID = sharedPreferences.getString("UserID", "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(JalMasterActivity.this);

                ws.GetCustomersMsg(UserID);
                try {
                    Integer i;
                    String q;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Cusno = js.getJSONArray("Cusno");
                    JSONArray js_msg = js.getJSONArray("msg");
                    JSONArray js_Man_No = js.getJSONArray("Man_No");
                    JSONArray js_SaleManFlg = js.getJSONArray("SaleManFlg");



                    q = "Delete from CustomersMsg";
                    sqlHandler.executeQuery(q);
                    q = " delete from sqlite_sequence where name='CustomersMsg'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_Cusno.length(); i++) {
                        q = "INSERT INTO CustomersMsg(Cusno,msg,Man_No,SaleManFlg) values ('"
                                + js_Cusno.get(i).toString()
                                + "','" + js_msg.get(i).toString()
                                + "'," +js_Man_No.get(i).toString()+  ","+ js_SaleManFlg.get(i).toString() +")";
                        sqlHandler.executeQuery(q);

                    }
                    final int total = i;
                    _handler.post(new Runnable() {
                        public void run() {
                        //    Call_marque();
                        }
                    });
                } catch (final Exception e) {

                    _handler.post(new Runnable() {
                        public void run() {
                           // Call_marque();
                        }
                    });
                }
            }
        }).start();

    }
    private  void Call_marque(){


        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String u = sharedPreferences.getString("UserID", "");
            SqlHandler sqlHandler;
            sqlHandler = new SqlHandler(this);
            String msg = " ";


            String query = " select Distinct msg from CustomersMsg where  Man_No= " + u + " and  SaleManFlg =3 ";
            Cursor c1 = sqlHandler.selectQuery(query);

            if (c1 != null && c1.getCount() != 0) {
                msg = msg + "  الرجاء الانتباه  ";
                if (c1.moveToFirst()) {
                    do {
                        msg = msg + "  ***  ";
                        msg = msg + c1.getString(c1.getColumnIndex("msg")) + "   ****   ";

                    } while (c1.moveToNext());
                }
                c1.close();
                msg = msg + "  ";
                msg = msg + "     وشكرا    . ";
            } else {
                msg = "";
            }

            if (msg.length() > 2) {
                msg = msg.replace("GI", "");
                MyTextView tv_News = (MyTextView) findViewById(R.id.tv_News);
                Animation a = AnimationUtils.loadAnimation(this, R.anim.animationfile);
                a.reset();
                a.setRepeatMode(Animation.RESTART);
                a.setRepeatCount(Animation.INFINITE);
                tv_News.clearAnimation();
                tv_News.setText(msg   );
                tv_News.setSingleLine(true);
                tv_News.startAnimation(a);
            }
        }
        catch (Exception ex){}








    }
    protected void setSpeed1(TextView tv, float speed, boolean speedIsMultiplier) {

        try {
            Field f = tv.getClass().getDeclaredField("mMarquee");
            f.setAccessible(true);

            Object marquee = f.get(tv);
            if (marquee != null) {

                String scrollSpeedFieldName = "mScrollUnit";
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    scrollSpeedFieldName = "mPixelsPerSecond";

                Field mf = marquee.getClass().getDeclaredField(scrollSpeedFieldName);
                mf.setAccessible(true);

                float newSpeed = speed;
                if (speedIsMultiplier)
                    newSpeed = mf.getFloat(marquee) * speed;


                Toast.makeText(this,newSpeed+"",Toast.LENGTH_SHORT).show();
                newSpeed =2000;
                mf.setFloat(marquee, newSpeed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void setSpeed(TextView tv, float speed, boolean speedIsMultiplier) {

        try {
            Field f = tv.getClass().getDeclaredField("mMarquee");
            f.setAccessible(true);
            Object marquee = f.get(tv);
            if (marquee != null) {
                Field mf = marquee.getClass().getDeclaredField("mPixelsPerSecond");
                mf.setAccessible(true);
                float newSpeed = speed;
                if (speedIsMultiplier) {
                    newSpeed = mf.getFloat(marquee) * speed;
                }
                mf.setFloat(marquee, newSpeed);
                Log.i(this.getClass().getSimpleName(), String.format("%s marquee speed set to %f", tv, newSpeed));
            }
        } catch (Exception e) {
           Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public static void setMarqueeSpeed(TextView tv, float speed) {
        if (tv != null) {
            try {
                Field f = null;
                if (tv instanceof AppCompatTextView) {
                    f = tv.getClass().getSuperclass().getDeclaredField("mMarquee");
                } else {
                    f = tv.getClass().getDeclaredField("mMarquee");
                }
                if (f != null) {
                    f.setAccessible(true);
                    Object marquee = f.get(tv);
                    if (marquee != null) {
                        String scrollSpeedFieldName = "mScrollUnit";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            scrollSpeedFieldName = "mPixelsPerSecond";
                        }
                        Field mf = marquee.getClass().getDeclaredField(scrollSpeedFieldName);
                        mf.setAccessible(true);
                        mf.setFloat(marquee, speed);
                    }
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            String Login = sharedPreferences.getString("Login", "No");
            if (Login.toString().equals("No")) {
                Intent i = new Intent(this, NewLoginActivity.class);
                startActivity(i);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Login = sharedPreferences.getString("Login", "No");
        if (Login.toString().equals("No")) {
            Intent i = new Intent(this, NewLoginActivity.class);
            startActivity(i);
        }
        return;
    }
    public void ShwoMancard(View view) {
       /* DetailCardMan exampleDialog = new DetailCardMan();
        exampleDialog.show(getSupportFragmentManager(), "dialog");*/

        android.app.FragmentManager fragmentManager = getFragmentManager();
        DetailCardMan detailCardMan = new  DetailCardMan();
        detailCardMan.show( fragmentManager , null);
    }
}


