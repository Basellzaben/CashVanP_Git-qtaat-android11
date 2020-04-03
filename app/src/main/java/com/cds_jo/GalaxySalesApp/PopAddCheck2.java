package com.cds_jo.GalaxySalesApp;

import android.app.DatePickerDialog;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.CheckAdapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Bank_search_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Check;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Methdes.MethodToUse;
import Methdes.MyTextView;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class PopAddCheck2 extends DialogFragment implements View.OnClickListener {
    View form;
    RelativeLayout add, cancel;
    ListView items_Lsit;
    TextView itemnm;
    public String ItemNo = "";
    SqlHandler sqlHandler;
    float min = 0;
    EditText filter;
    EditText edCheckData;
    ImageButton btn_filter_search;
    String UnitNo = "";
    String UnitName = "";
    String str = "";
    RadioButton rad_Per;
    RadioButton rad_Amt;
    Spinner sp_banks;
    ArrayList<Cls_Bank_Search> cls_bank_searches;
    SqlHandler sql_Handler;
    private Calendar calendar;
    private int year, month, day;
    //    ListView lstView ;
    ArrayList<Cls_Check> ChecklList;
    Button minday, minmonth, minyear, plusyear, plusmonth, plusday;
    EditText year2, month2, day2;
    int CurrentYear;

    //    @Override
//    public void onStart()
//    {
//        super.onStart();
//
//        // safety check
//        if (getDialog() == null)
//            return;
//
//        int dialogWidth = 1000;//340; // specify a value here
//        int dialogHeight = 700;//400; // specify a value here
//
//        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
//
//
//    }
    @Override
    public void onStart() {
        super.onStart();


        if (getDialog() == null)
            return;


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getDialog().getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(lp);
        //getDialog().getWindow().setBacsp_bankskgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setWindowAnimations(0);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog2);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        form = inflater.inflate(R.layout.fragment_pop_add_check2, container, false);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP|Gravity.RIGHT);
        getDialog().setTitle("معلومات الشيكات");
        add = (RelativeLayout) form.findViewById(R.id.btn_save_Check);
        add.setOnClickListener(this);
        cancel = (RelativeLayout) form.findViewById(R.id.btn_Back);



        TextView backto = (TextView) form.findViewById(R.id.textView266);
        backto.setTypeface(MethodToUse.SetTFace(getActivity()));
        TextView addd = (TextView) form.findViewById(R.id.textView267);
        addd.setTypeface(MethodToUse.SetTFace(getActivity()));

        plusyear = (Button) form.findViewById(R.id.plusyear);
        plusmonth = (Button) form.findViewById(R.id.plusmonth);
        plusday = (Button) form.findViewById(R.id.plusday);
        minyear = (Button) form.findViewById(R.id.minyear);
        minmonth = (Button) form.findViewById(R.id.minmonth);
        minday = (Button) form.findViewById(R.id.minday);

        year2 = (EditText) form.findViewById(R.id.year2);
        month2 = (EditText) form.findViewById(R.id.month2);
        day2 = (EditText) form.findViewById(R.id.day2);


        cancel.setOnClickListener(this);
//        lstView = (ListView) form.findViewById(R.id.lstCheck);
        ChecklList = new ArrayList<Cls_Check>();
        ChecklList.clear();
        TextView textView = (TextView) form.findViewById(R.id.textView246);
        MyTextView ss = (MyTextView) form.findViewById(R.id.textView257);

        textView.setTypeface(MethodToUse.SetTFace(getActivity()));
        ss.setTypeface(MethodToUse.SetTFace(getActivity()));



//      lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//          @Override
//           public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//
//               final  Integer pos = position;
//               AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//             alertDialog.setTitle("حذف الشيكات");
//             alertDialog.setMessage("هل انت متاكد من عملية الحذف ..؟");
//
//            alertDialog.setIcon(R.drawable.delete);
//
//
//              alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
//                   public void onClick(DialogInterface dialog, int which) {
//                       DeleteCheck(pos);
//                  }
//               });
//
//
//              alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
//                   public void onClick(DialogInterface dialog, int which) {
//
//                       dialog.cancel();
//                  }
//               });
//
//
//              alertDialog.show();
//
//      }
//        });
        sp_banks = (Spinner) form.findViewById(R.id.sp_banks);

        cls_bank_searches = new ArrayList<Cls_Bank_Search>();
        cls_bank_searches.clear();

        sql_Handler = new SqlHandler(getActivity());
        FillBanks();

        final Calendar c = Calendar.getInstance();
        c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);


        calendar = Calendar.getInstance();
        year = c.get(Calendar.YEAR);

        month = c.get(Calendar.MONTH);

        day = c.get(Calendar.DAY_OF_MONTH);
        showDate(year, month, day);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


        SimpleDateFormat Myear = new SimpleDateFormat("yyyy", Locale.ENGLISH);

         CurrentYear = Integer.parseInt( Myear.format(new Date()));


//        showList();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat mdformatyear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String stryear = mdformatyear.format(calendar.getTime());
        displayyear(stryear);

        SimpleDateFormat mdformatmonth = new SimpleDateFormat("MM", Locale.ENGLISH);
        String strmonth = mdformatmonth.format(calendar.getTime());
        displaymonth(strmonth);

        SimpleDateFormat mdformatday = new SimpleDateFormat("dd", Locale.ENGLISH);
        String strday = mdformatday.format(calendar.getTime());
        displayday(strday);

        plusyear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int t = Integer.parseInt(year2.getText().toString());
                year2.setText(String.valueOf(t + 1));
            }

        });

        plusmonth.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int t = Integer.parseInt(month2.getText().toString());
                month2.setText(String.valueOf(t + 1));
                if (t >= 12) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("تجاوزت حد الاشهر")
                            .setConfirmText("رجــــوع")
                            .show();
                            month2.setText("12");
                            month2.setError("required!");
                            month2.requestFocus();
                            return;
                }
            }
        });
        plusday.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int t = Integer.parseInt(day2.getText().toString());
                day2.setText(String.valueOf(t + 1));
                if (t >= 31) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("تجاوزت حد الايام")
                            .setConfirmText("رجــــوع")
                            .show();
                            day2.setText("31");
                            day2.setError("required!");
                            day2.requestFocus();
                            return;
                }
            }
        });
        minyear.setOnClickListener(new View.OnClickListener() {

            private int t;

            public void onClick(View v) {
                int t = Integer.parseInt(year2.getText().toString());
                year2.setText(String.valueOf(t - 1));
                if (t <=0) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("تجاوزت حد السنة")
                            .setConfirmText("رجــــوع")
                            .show();
                            year2.setText("2019");
                            year2.setError("required!");
                            year2.requestFocus();
                            return;
                }
            }
        });
        minmonth.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int t = Integer.parseInt(month2.getText().toString());
                month2.setText(String.valueOf(t - 1));
                if (t <= 1) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("تجاوزت حد الشهر")
                            .setConfirmText("رجــــوع")
                            .show();
                            month2.setText("1");
                            month2.setError("required!");
                            month2.requestFocus();
                            return;
                }
            }
        });
        minday.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int t = Integer.parseInt(day2.getText().toString());
                day2.setText(String.valueOf(t - 1));
                if (t <= 1 ) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                           // .setCustomImage(R.mipmap.icon_delete)
                            .setContentText("تجاوزت حد الايام")
                            .setConfirmText("رجــــوع")
                            .show();
                    day2.setText("1");
                    day2.setError("required!");
                    day2.requestFocus();
                    return;

                }
            }
        });
        return form;
    }

    private void displayyear(String num) {
        EditText year = (EditText) form.findViewById(R.id.year2);
        year2.setText(num);
    }

    private void displaymonth(String num) {
        EditText month = (EditText) form.findViewById(R.id.month2);
        month2.setText(num);
    }

    private void displayday(String num) {
        EditText day = (EditText) form.findViewById(R.id.day2);
        day2.setText(num);
    }

    public void setDate(View view) {
        //showDialog(999);
       /* Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();*/
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day

            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {


    }

    /*   CheckData.setText(new StringBuilder().append(intToString(Integer.valueOf(day), 2)).append("/")
                    .append(intToString(Integer.valueOf(month), 2)).append("/").append(year));
*/
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }

    private void DeleteCheck(int pos) {
        ChecklList.remove(pos);
        Integer pos1 = ChecklList.size();
        Cls_Check obj = new Cls_Check();
        for (int x = 0; x < pos1; x++) {
            obj = new Cls_Check();
            obj = ChecklList.get(x);
            ChecklList.remove(x);
            obj.setSer(x + 1);
            ChecklList.add(x, obj);
        }
        CheckAdapter checkAdapter = new CheckAdapter(
                getActivity(), ChecklList);
//    lstView.setAdapter(checkAdapter);
        UpdateTable();
        total();

    }

    private void UpdateTable() {
        String q = "delete from t_RecCheck ";
        sql_Handler = new SqlHandler(this.getActivity());
        sql_Handler.executeQuery(q);
        Cls_Check obj = new Cls_Check();
        for (int i = 0; i < ChecklList.size(); i++) {
            obj = new Cls_Check();
            obj = ChecklList.get(i);
            q = "insert into t_RecCheck (CheckNo,CheckDate,BankNo, Amnt ) values ( '" +
                    obj.getCheckNo()
                    + "','" + obj.getCheckDate()
                    + "','" + obj.getBankNo()
                    + "','" + obj.getAmnt() + "')";

            sql_Handler.executeQuery(q);

        }


    }

    private void showList() {

        ChecklList.clear();
//        lstView.setAdapter(null);

        sql_Handler = new SqlHandler(this.getActivity());

        String query = "Select distinct rc.CheckNo,rc.CheckDate,rc.BankNo, rc.Amnt  , b.Bank from  t_RecCheck rc  left join banks b on b.bank_num = rc.BankNo ";
        Integer i = 1;
        Cursor c1 = sql_Handler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Check cls_check_obj = new Cls_Check();
                    cls_check_obj.setSer(i);
                    cls_check_obj.setCheckNo(c1.getString(c1
                            .getColumnIndex("CheckNo")));
                    cls_check_obj.setCheckDate(c1.getString(c1
                            .getColumnIndex("CheckDate")));
                    cls_check_obj.setBankName(c1.getString(c1
                            .getColumnIndex("Bank")));
                    cls_check_obj.setBankNo(c1.getString(c1
                            .getColumnIndex("BankNo")));
                    cls_check_obj.setAmnt(c1.getString(c1
                            .getColumnIndex("Amnt")));

                    ChecklList.add(cls_check_obj);
                    i = i + 1;
                } while (c1.moveToNext());


            }
            c1.close();
        }


        CheckAdapter checkAdapter = new CheckAdapter(
                this.getActivity(), ChecklList);
//        lstView.setAdapter(checkAdapter);
        total();

        //  json = new Gson().toJson(contactList);
    }


    private void FillBanks() {
        String query = "SELECT distinct * FROM banks ";

        Cursor c1 = sql_Handler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Bank_Search cls_bank_search = new Cls_Bank_Search();

                    cls_bank_search.setNo(c1.getString(c1
                            .getColumnIndex("bank_num")));
                    cls_bank_search.setName(c1.getString(c1
                            .getColumnIndex("Bank")));

                    cls_bank_searches.add(cls_bank_search);


                } while (c1.moveToNext());

            }
            c1.close();
        }
        Cls_Bank_search_Adapter cls_bank_search_adapter = new Cls_Bank_search_Adapter(
                getActivity(), cls_bank_searches);
        sp_banks.setAdapter(cls_bank_search_adapter);

    }

    private Double SToD(String str) {
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat) nf;
        str = str.replace(",", "");
        Double d = 0.0;
        if (str.length() == 0) {
            str = "0";
        }
        if (str.length() > 0)
            try {
                d = Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            } catch (Exception ex) {
                str = "0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }

    private boolean isValidDate(String dateOfBirth) {
        boolean valid = true;

        if (dateOfBirth.trim().length() < 10) {
            return false;
        }
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(edCheckData.getText().toString());

        } catch (Exception e) {
            valid = false;
            return valid;
        }


        String[] parts = dateOfBirth.split("/");
        String part1 = parts[2];
        String part2 = parts[1];
        String part3 = parts[0];
        if (SToD(part1) < 0 || SToD(part1) > 31) {
            valid = false;
            return valid;
        }
        if (SToD(part2) < 0 || SToD(part2) > 12) {
            valid = false;
            return valid;
        }

        if (SToD(part3) < 1990 || SToD(part3) > 2050) {
            valid = false;
            return valid;
        }

        return valid;
    }

    public void onClick(View v) {

        Cls_Bank_Search bank = (Cls_Bank_Search) ((Spinner) form.findViewById(R.id.sp_banks)).getSelectedItem();

        if (v == cancel) {
            // ((RecvVoucherActivity) getActivity()).UpdateCheck();

            this.dismiss();
        }

        if (v == add) {
            EditText CheckNo = (EditText) form.findViewById(R.id.CheckNo);
            TextView CheckData = (TextView) form.findViewById(R.id.CheckData);
            EditText ChekAmt = (EditText) form.findViewById(R.id.ChekAmt);
            MyTextView Chekday = (MyTextView) form.findViewById(R.id.Chekday);
            year2 = (EditText) form.findViewById(R.id.year2);
            month2 = (EditText) form.findViewById(R.id.month2);
            day2 = (EditText) form.findViewById(R.id.day2);


            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("تاريخ الشيك");
            alertDialog.setMessage("هناك خطأ في طريقة ادخال  تاريخ الشيك ، الرجاء ادخال التاريخ كالتالي 2017/01/01");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton("نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //   edCheckData.setText("");
                }
            });


            if (CheckNo.getText().toString().length() == 0) {
                CheckNo.setError("required!");
                CheckNo.requestFocus();
                return;
            }

            if ((year2.getText().toString().length() == 0)) {
                year2.setError("required!");
                year2.requestFocus();
                return;
            }

            if (month2.getText().toString().length() == 0) {
                month2.setError("required!");
                month2.requestFocus();
                return;
            }

            if (day2.getText().toString().length() == 0) {
                day2.setError("required!");
                day2.requestFocus();
                return;
            }
            if (ChekAmt.getText().toString().length() == 0) {
                ChekAmt.setError("required!");
                ChekAmt.requestFocus();
                return;
            }

            int t1 = Integer.parseInt(day2.getText().toString());
            if (t1 > 31 || t1<=0) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        //.setCustomImage(R.mipmap.icon_delete)
                        .setContentText("تجاوزت حد الايام")
                        .setConfirmText("رجــــوع")
                        .show();
                        day2.setText("31");
                        return;


            }

            int t = Integer.parseInt(month2.getText().toString());
            if (t > 12 || t<=0) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        //.setCustomImage(R.mipmap.icon_delete)
                        .setContentText("تجاوزت حد الشهر")
                        .setConfirmText("رجــــوع")
                        .show();
                month2.setText("12");
                return;
            }
            int t2 = Integer.parseInt(year2.getText().toString());
            if (t2 < CurrentYear) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        //.setCustomImage(R.mipmap.icon_delete)
                        .setContentText("يجب ان تكون السنة الحالية او اكبر ")
                        .setConfirmText("رجــــوع")
                        .show();
                        year2.setText("2019");
                        return;


            }

            int day = Integer.parseInt(day2.getText().toString());
            int month = Integer.parseInt(month2.getText().toString());

            String Day_Str,Month_Str;
            //String date =String.format("%02d", day, Locale.ENGLISH) +"/" +  String.format("%02d",  month,Locale.ENGLISH)+"/" +  year2.getText().toString();
           if ((day+"").length()==1){
               Day_Str = "0"+ day+"";
            }else{
               Day_Str =   day+"";
           }

            if ((month+"").length()==1){
                Month_Str = "0"+ month+"";
            }else{
                Month_Str =   month+"";
            }

            String date =  Day_Str  +"/" +     Month_Str  +"/" +  year2.getText().toString();

            SimpleDateFormat mdformatyear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            String stryear = mdformatyear.format(calendar.getTime());
            displayyear(stryear);

            SimpleDateFormat mdformatmonth = new SimpleDateFormat("MM", Locale.ENGLISH);
            String strmonth = mdformatmonth.format(calendar.getTime());
            displaymonth(strmonth);

            SimpleDateFormat mdformatday = new SimpleDateFormat("dd", Locale.ENGLISH);
            String strday = mdformatday.format(calendar.getTime());
            displayday(strday);

            ((RecvVoucherActivity) getActivity()).save_Check(CheckNo.getText().toString(), ChekAmt.getText().toString(), bank, date.toString());
            save_Check(CheckNo.getText().toString(), ChekAmt.getText().toString(), bank, date.toString(),Chekday.getText().toString());

            ChekAmt.setText("");
            year2.setText(stryear);
            month2.setText(strmonth);
            day2.setText(strday);
            CheckNo.setText("");
          //  Chekday.setText("");
            CheckNo.requestFocus();
            // ((RecvVoucherActivity) getActivity()).UpdateCheck();
        }
    }

    public void save_Check(String CheckNo, String ChekAmt, Cls_Bank_Search bank, String Check_Data,String Chekday) {


     /* TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
        if (DocNo.getText().toString().length() == 0) {
            DocNo.setError("required!");
            DocNo.requestFocus();
            return;
        }*/


        Cls_Check cls_check_obj = new Cls_Check();
//        cls_check_obj.setSer(Integer.valueOf(lstView.getCount() + 1));
        cls_check_obj.setCheckNo(CheckNo);
        cls_check_obj.setCheckDate(Check_Data);
        cls_check_obj.setBankNo(bank.getNo().toString());
        cls_check_obj.setBankName(bank.getName().toString());
        cls_check_obj.setAmnt(ChekAmt);
        cls_check_obj.setallowday(Chekday);
        ChecklList.add(cls_check_obj);

        CheckAdapter checkAdapter = new CheckAdapter(
                this.getActivity(), ChecklList);
//        lstView.setAdapter(checkAdapter);

        sql_Handler = new SqlHandler(this.getActivity());
        String q = "insert into t_RecCheck (CheckNo,CheckDate,BankNo, Amnt ) values ( '" +
                CheckNo
                + "','" + Check_Data
                + "','" + bank.getNo().toString()
                + "','" + ChekAmt
                + "')";

        sql_Handler.executeQuery(q);

        total();
        /*CalcTotal();*/
    }

    private void total() {
        int x1 = 0;
        Cls_Check cls_check_obj = new Cls_Check();
        Double sum = 0.0;
        for (int x = 0; x < ChecklList.size(); x++) {
            cls_check_obj = new Cls_Check();
            cls_check_obj = ChecklList.get(x);
            sum = sum + Double.parseDouble(cls_check_obj.getAmnt());
            x1 = x;
        }
        TextView tv_CheckAmt = (TextView) form.findViewById(R.id.tv_CheckAmt);
        tv_CheckAmt.setTypeface(MethodToUse.SetTFace(getActivity()));
        tv_CheckAmt.setText(sum + "");
        TextView tv_Check_count = (TextView) form.findViewById(R.id.tv_Check_count);
        tv_Check_count.setTypeface(MethodToUse.SetTFace(getActivity()));
        tv_Check_count.setText((x1 + 1) + "");

        if (ChecklList.size() == 0) {
            tv_Check_count.setText("لا يوجد شيكات");
        }
    }


}

