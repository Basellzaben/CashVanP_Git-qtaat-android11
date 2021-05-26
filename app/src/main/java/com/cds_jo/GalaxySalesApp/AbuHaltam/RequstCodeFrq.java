package com.cds_jo.GalaxySalesApp.AbuHaltam;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.tv.TvInputService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.GlobaleVar;
import com.cds_jo.GalaxySalesApp.Pos.Pos_Activity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.Convert_Sal_Invoice_To_Img_Ukrainian;
import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;


import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import okhttp3.internal.Internal;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequstCodeFrq  extends DialogFragment {
    View form ;
   EditText et_Discount,ed_bounse;
    RadioButton rdoPrecent, rdoAmt;
    Button btmRcode,btn_Cancel;
String msg;

    String email,pass;
    /*  @Override
      public void onStart()
      {
          super.onStart();

          // safety check
          if (getDialog() == null)
              return;
          int dialogWidth =  WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
          int dialogHeight =  WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here
          getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

      }*/
    @Override
    public void onStart()
    {
        super.onStart();
        if (getDialog() == null)
            return;

        int dialogWidth = WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);




    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form= inflater.inflate(R.layout.fragment_requst_code_frq, container, false);
        et_Discount =(EditText)form.findViewById(R.id.et_Discount);
        ed_bounse =(EditText)form.findViewById(R.id.ed_bounse);
        rdoPrecent =(RadioButton) form.findViewById(R.id.rdoPrecent);
        rdoAmt =(RadioButton)form.findViewById(R.id.rdoAmt);
        btmRcode =(Button) form.findViewById(R.id.btn_Save);
        rdoPrecent.setChecked(true);
        email ="mikhaldi1919@gmail.com";
        pass="Alkhaldi3754202";

     //   String u = sharedPreferences.getString("UserName", "");

        btmRcode.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                 String UserID= sharedPreferences.getString("UserName", "");

                 msg = UserID +"\n\n\n";
                 msg += "أسم العميل: "+ GlobaleVar.cusname +'\n';
                 msg +="نوع: "+"فاتورة بيع "+'\n';
                 msg +="قيمة الفاتورة: "+GlobaleVar.tvtotal+'\n';
                /* msg +="نوع الخصم :"+"50 بونص"+'\n';
                 msg +="https://www.youtube.com/";*/
                 if(et_Discount.getText().toString().equals("") && ed_bounse.getText().toString().equals(""))
                 {
                     Toast.makeText(getActivity(),"الرجاء أدخال الخصم",Toast.LENGTH_LONG).show();
                 }
                 else
                 {
                     if((!(et_Discount.getText().toString().equals("") ))&& ed_bounse.getText().toString().equals(""))
                     {
                         if(rdoPrecent.isChecked())
                         {
                             msg +="خصم: "+et_Discount.getText().toString()+"%"+'\n';
                         }
                         else
                         {
                             msg +="خصم: "+et_Discount.getText().toString()+"دينار"+'\n';
                         }

                 }else if((!(ed_bounse.getText().toString().equals("") )) && et_Discount.getText().toString().equals(""))
                     {
                         msg +="بونص: "+ed_bounse.getText().toString()+'\n';
                     }
                 }
                 msg +="http://10.0.1.104:9633/Home/";
                 if(!(et_Discount.getText().toString().equals("") && ed_bounse.getText().toString().equals("")))
                 {
                 Properties properties=new Properties();
                // properties.setProperty("mail.transport.protocol", "smtp");
                 properties.put("mail.smtp.host", "smtp.gmail.com");
                 properties.put("mail.smtp.socketFactory.port", "465");
                 properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                 properties.put("mail.smtp.auth", "true");
                 properties.put("mail.smtp.port", "465");

               //  properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
               //  properties.setProperty("mail.smtp.port", "465");

                 Session session=Session.getInstance(properties, new Authenticator() {
                     @Override
                     protected PasswordAuthentication getPasswordAuthentication() {
                         return new PasswordAuthentication(email,pass);
                     }
                 });

                 try {
                     Message message = new MimeMessage(session);
                     message.setFrom(new InternetAddress(email));
                     message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("msaadibsm@gmail.com"));
                     message.setSubject("طلب خصم");
                     message.setText(msg);
                     new SendMail().execute(message);
                 } catch (MessagingException e) {
                     e.printStackTrace();
                 }
             }}
         });
        btn_Cancel=(Button) form.findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return form;
    }

private class SendMail extends AsyncTask<Message,String,String> {

        private ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog= progressDialog.show(getActivity(),"الرجاءالانتظار ","جاري العمل على ارسال الطلب");
    }

    @Override
    protected String doInBackground(Message... messages) {
        try {
            Transport.send(messages[0]);
            return "s";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "f";
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        if(s.equals("s"))
        {
            AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);
            builder.setTitle(Html.fromHtml("<font color='#509324'>  نجح </font>"));
            builder.setMessage("تم ارسال الطلب");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                }
            });
            builder.show();
        }
        else {
            Toast.makeText(getActivity(),"00100",Toast.LENGTH_LONG).show();
        }
    }
}


}
