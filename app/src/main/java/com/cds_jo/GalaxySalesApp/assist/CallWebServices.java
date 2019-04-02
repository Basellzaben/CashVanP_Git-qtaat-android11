package com.cds_jo.GalaxySalesApp.assist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cds_jo.GalaxySalesApp.Companies;
import com.cds_jo.GalaxySalesApp.We_Result;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.io.EOFException;

public class CallWebServices  {
    Context context;
    Activity Activity;
    String IPAddress = "";
    //Namespace of the Webservice - can be found in WSDL
     private static String NAMESPACE = "http://tempuri.org/";
     //Webservice URL - WSDL File location
     //private  String URL = "http://192.168.1.60/Webservices/WebService1.asmx";
      private  String URL = "";
      int TIME_OUT_CONN = 50000;
      public  CallWebServices(Context _context ){
        context = _context;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        IPAddress =sharedPreferences.getString("ServerIP", "");
       //1 bustanji 2 bristage 3-salsel 4-goodsystem 5-tariget system 6-
          //URL = "http://"+IPAddress+"/GIWS/CV.asmx";//bustanji or prastaige
        //  URL = "http://"+IPAddress+"/CV.asmx";//  salasel or goodsystem or good targit Or arabia
      //URL = "http://46.32.100.34:9090/GIWS/CV.asmx";
       // URL = "http://192.168.8.101/GIWS/CV.asmx";
       // URL = "http://92.253.93.52:3755/CV.asmx";
        //URL  = "http://192.168.1.118/GIWS/CV.asmx";
         URL = "http://10.0.1.166/GIWS/CV.asmx";
          //URL = "http://192.168.1.118/GIWS/CV.asmx";
           URL = "http://92.253.126.39:3750/CV.asmx";//شركة خط التجميل
       //   URL = "http://192.168.1.100:3750/CV.asmx";
        //   URL = "http://92.253.22.118:3733/CV.asmx";// Targit
       //   URL = "http://194.165.133.147:85/CV.asmx";// Okrania
    }


    public void CallReport(String AccNo, String FromDate,String ToDate,String UserNo) {

        We_Result.Msg="";
        We_Result.ID =-1;

        SoapObject request = new SoapObject(NAMESPACE, "GetAccReport");

        PropertyInfo parm_AccNo = new PropertyInfo();

        parm_AccNo.setName("AccNo");

        parm_AccNo.setValue(AccNo);
        parm_AccNo.setType(String.class);

        PropertyInfo parm_FDate = new PropertyInfo();
        parm_FDate.setName("FDate");
        parm_FDate.setValue(FromDate);
        parm_FDate.setType(String.class);


        PropertyInfo parm_TDate = new PropertyInfo();
        parm_TDate.setName("TDate");
        parm_TDate.setValue(ToDate);
        parm_TDate.setType(String.class);



        PropertyInfo parm_UserNo = new PropertyInfo();
        parm_UserNo.setName("UserNo");
        parm_UserNo.setValue(UserNo);
        parm_UserNo.setType(String.class);

        // Add the property to request object
        request.addProperty(parm_AccNo);
        request.addProperty(parm_FDate);
        request.addProperty(parm_TDate);
        request.addProperty(parm_UserNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/GetAccReport", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");

        }
        //Return resTxt to calling object

    }
    public void SaveTrandferStoreQty(String Json) {
        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "SaveStoreQty");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");

        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,40000);
        Object  response =null;
        try {
            androidHttpTransport.call( "http://tempuri.org/SaveStoreQty", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.Msg =  result.getProperty("Msg").toString().replace("#M"," الكمية غير متاحة من المادة").replace("#QTY","كمية المستودع ");

            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void GetAccNo(String AccNo, String webMethName) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetAccNo");

        PropertyInfo parm_AccNo = new PropertyInfo();

        parm_AccNo.setName("AccNo");
        parm_AccNo.setValue(AccNo);
        parm_AccNo.setType(String.class);



        request.addProperty(parm_AccNo);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetAccNo", envelope);
           SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
        //Return resTxt to calling object

    }
    public void GetRndNum() {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "Get_RndNum");




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/Get_RndNum", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void GetAccNoBanks() {

        We_Result.Msg="";
        We_Result.ID =-1;

            SoapObject request = new SoapObject(NAMESPACE, "GetBanks");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
            SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/GetBanks", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void Get_Stores() {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetStores");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call("http://tempuri.org/GetStores", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void GetCustomers(String ManNo) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetCustomers");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/GetCustomers", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void GetCustomersMsg(String ManNo) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetCustomersMsg");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/GetCustomersMsg", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void Get_CustLastPrice(String ManNo) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "LastCustPrice");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,TIME_OUT_CONN);

        try {

            androidHttpTransport.call("http://tempuri.org/LastCustPrice", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void GetTab_Password() {
        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "Get_Tab_Password");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/Get_Tab_Password", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//;+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void GetGetManPermession(String ManNo) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetManPermession");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/GetManPermession", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void Get_ManExceptions(String ManNo) {

        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetManExceptions");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/GetManExceptions", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void GetOrdersSerials(String ManNo) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetMaxOrders");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/GetMaxOrders", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void GetItems( String ManNo ) {


        SoapObject request = new SoapObject(NAMESPACE, "GetItems");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/GetItems", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            We_Result.Msg="";
            We_Result.ID =-1;

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            We_Result.Msg="";
            We_Result.ID =-1;
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            We_Result.Msg="";
            We_Result.ID =-1;
        }

    }
    public void GetUnites() {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetUnites");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetUnites", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void GetUnitItems() {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetUnitItems");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/GetUnitItems", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }

    }
    public void Getcurf() {
        We_Result.Msg="";
        We_Result.ID =-1;

        SoapObject request = new SoapObject(NAMESPACE, "Getcurf");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/Getcurf", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
    }
    public void deptf( String ManNo) {

        We_Result.Msg="";
        We_Result.ID =-1;

        SoapObject request = new SoapObject(NAMESPACE, "deptf");
        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/deptf", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }

    }
    public long  Save_po(String Json, String webMethName) {
        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo StrJson = new PropertyInfo();
        StrJson.setName("JsonStr");
        StrJson.setValue(Json);
        StrJson.setType(String.class);
        request.addProperty(StrJson);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call( "http://tempuri.org/Insert_PurshOrder", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // We_Result.Msg =  result.getProperty("Msg").toString().replace("#B","الباتش").replace("#I"," المادة").replace("#M","الكمية غير متاحة من المادة ");
            We_Result.Msg =  result.getProperty("Msg").toString().replace("#I"," المادة").replace("#M","الكمية غير متاحة من المادة ");
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;// + String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
         return  We_Result.ID;

    }
    public void Save_VisitImages(String CustomerNo,String OrderDate,String UserNo,String Visit_OrderNo,String Order_No
              , String ImageTime ,String ImageDesc, String ImgBase64 ) {


        String resTxt = null;

        SoapObject request = new SoapObject(NAMESPACE, "Save_Visit_Images");

        PropertyInfo sayCustomerNo = new PropertyInfo();
        sayCustomerNo.setName("CustomerNo");
        sayCustomerNo.setValue(CustomerNo);
        sayCustomerNo.setType(String.class);
        request.addProperty(sayCustomerNo);


        PropertyInfo saOrderDate = new PropertyInfo();
        saOrderDate.setName("OrderDate");
        saOrderDate.setValue(OrderDate);
        sayCustomerNo.setType(String.class);
        request.addProperty(saOrderDate);

        PropertyInfo sayUserNo = new PropertyInfo();
        sayUserNo.setName("UserNo");
        sayUserNo.setValue(UserNo);
        sayUserNo.setType(String.class);
        request.addProperty(sayUserNo);


        PropertyInfo sayVisit_OrderNo = new PropertyInfo();
        sayCustomerNo.setName("CustomerNo");
        sayCustomerNo.setValue(CustomerNo);
        sayCustomerNo.setType(String.class);
        request.addProperty(sayCustomerNo);

        PropertyInfo sayOrder_No = new PropertyInfo();
        sayOrder_No.setName("Order_No");
        sayOrder_No.setValue(Order_No);
        sayOrder_No.setType(String.class);
        request.addProperty(sayOrder_No);



        PropertyInfo sayImageTime = new PropertyInfo();
        sayImageTime.setName("ImageTime");
        sayImageTime.setValue(ImageTime);
        sayImageTime.setType(String.class);
        request.addProperty(sayImageTime);


        PropertyInfo sayImageDesc = new PropertyInfo();
        sayImageDesc.setName("ImageDesc");
        sayImageDesc.setValue(ImageDesc);
        sayImageDesc.setType(String.class);
        request.addProperty(sayImageDesc);



        PropertyInfo parm_ImgBase64 = new PropertyInfo();
        parm_ImgBase64.setName("ImgBase64");
        parm_ImgBase64.setValue(ImgBase64);
        parm_ImgBase64.setType(String.class);
        request.addProperty(parm_ImgBase64);




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call( "http://tempuri.org/Save_Visit_Images", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;// + String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void SavePrepareQty(String Json ) {
        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "SavePrepareQty");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call( "http://tempuri.org/SavePrepareQty", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();



            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg = "عملية الاتصال بالسيرفر لم تتم بنجاح";// + String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
        }

    }
    public void ItemCostReport(String ItemNo, String webMethName) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GatMobileItemCost");

        PropertyInfo parm_AccNo = new PropertyInfo();

        parm_AccNo.setName("ItemNo");
        parm_AccNo.setValue(ItemNo);
        parm_AccNo.setType(String.class);

        PropertyInfo parm_FDate = new PropertyInfo();
        parm_FDate.setName("FDate");
        parm_FDate.setValue("FDate");
        parm_FDate.setType(String.class);


        PropertyInfo parm_TDate = new PropertyInfo();
        parm_TDate.setName("TDate");
        parm_TDate.setValue("TDate");
        parm_TDate.setType(String.class);


        request.addProperty(parm_AccNo);
        request.addProperty(parm_FDate);
        request.addProperty(parm_TDate);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GatMobileItemCost", envelope);

            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg = "عملية الاتصال بالسيرفر لم تتم بنجاح";// + String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }

    }
    public void GetManf() {

        We_Result.Msg="";
        We_Result.ID =-1;
        try {
        SoapObject request = new SoapObject(NAMESPACE, "GetManf");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;


            androidHttpTransport.call("http://tempuri.org/GetManf", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf( e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void GetDrivers() {

        We_Result.Msg="";
        We_Result.ID =-1;
        try {
            SoapObject request = new SoapObject(NAMESPACE, "GetDrivers");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Object  response =null;


            androidHttpTransport.call("http://tempuri.org/GetDrivers", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf( e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void GetCusLastTrans(String AccNo, String Man) {

        We_Result.Msg="";
        We_Result.ID =-1;
        try {


            SoapObject request = new SoapObject(NAMESPACE, "GetCusLastTrans");

            PropertyInfo parm_AccNo = new PropertyInfo();
            parm_AccNo.setName("AccNo");
            parm_AccNo.setValue(AccNo);
            parm_AccNo.setType(String.class);
            request.addProperty(parm_AccNo);



            PropertyInfo parm_Man = new PropertyInfo();
            parm_Man.setName("Man");
            parm_Man.setValue(Man);
            parm_Man.setType(String.class);
            request.addProperty(parm_Man);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Object  response =null;


            androidHttpTransport.call("http://tempuri.org/GetCusLastTrans", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf( e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }

    public void GetSaleManPath(String No,String DayNum ,String Tr_Date) {

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetSaleManPath");

        PropertyInfo parm_No = new PropertyInfo();

        parm_No.setName("No");
        parm_No.setValue(No);
        parm_No.setType(String.class);
        request.addProperty(parm_No);

        PropertyInfo parm_DayNum = new PropertyInfo();
        parm_DayNum.setName("DayNum");
        parm_DayNum.setValue(DayNum);
        parm_DayNum.setType(String.class);
        request.addProperty(parm_DayNum);

        PropertyInfo parm_Tr_Date = new PropertyInfo();
        parm_Tr_Date.setName("Tr_Date");
        parm_Tr_Date.setValue(Tr_Date);
        parm_Tr_Date.setType(String.class);
        request.addProperty(parm_Tr_Date);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetSaleManPath", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public long SavePayment(String Json) {
        We_Result.Msg =  "";
        We_Result.ID = -1;
        SoapObject request = new SoapObject(NAMESPACE, "SavePayment");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");

        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,40000);
        Object  response =null;
        try {
            We_Result.ID = -1;
            androidHttpTransport.call( "http://tempuri.org/SavePayment", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }

return    We_Result.ID;
    }
    public void Get_Offers_Groups(String UserNo) {

        We_Result.Msg="";
        We_Result.ID =-1;


        SoapObject request = new SoapObject(NAMESPACE, "Get_Offers_Groups");

        PropertyInfo parm_UserNo = new PropertyInfo();
        parm_UserNo.setName("UserNo");
        parm_UserNo.setValue(UserNo);
        parm_UserNo.setType(String.class);
        request.addProperty(parm_UserNo);




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/Get_Offers_Groups", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void Get_Offers_Dtl_Cond() {

        We_Result.Msg="";
        We_Result.ID =-1;

        try {
        SoapObject request = new SoapObject(NAMESPACE, "Get_Offers_Dtl_Cond");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;

            androidHttpTransport.call("http://tempuri.org/Get_Offers_Dtl_Cond", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        }

    catch (Exception e) {
        We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"        ;
        We_Result.ID = Long.parseLong("-404");
        e.printStackTrace();

    }


    }
    public void GetOffers_Hdr(String UserNo) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "Offers_Hdr");
        PropertyInfo parm_UserNo = new PropertyInfo();
        parm_UserNo.setName("UserNo");
        parm_UserNo.setValue(UserNo);
        parm_UserNo.setType(String.class);
        request.addProperty(parm_UserNo);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/Offers_Hdr", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void Get_Offers_Dtl_Gifts() {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "Get_Offers_Dtl_Gifts");
        // Property which holds input parameters

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/Get_Offers_Dtl_Gifts", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
        //Return resTxt to calling object

    }
    public void TrnsferQtyFromMobile(String ManNo, String Max_Order,String TDate) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "TrnsferQty");
        // Property which holds input parameters

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);



        PropertyInfo parm_Max_Order = new PropertyInfo();
        parm_Max_Order.setName("Max_Order");
        parm_Max_Order.setValue(Max_Order);
        parm_Max_Order.setType(String.class);
        request.addProperty(parm_Max_Order);

        PropertyInfo parm_TDate = new PropertyInfo();
        parm_TDate.setName("TDate");
        parm_TDate.setValue(TDate);
        parm_TDate.setType(String.class);
        request.addProperty(parm_TDate);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call("http://tempuri.org/TrnsferQty", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void TrnsferQtyFromMobileBatch(String ItemNo, String StoreNo ) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "TrnsferQtyBatch");
        // Property which holds input parameters

        PropertyInfo parm_ItemNo = new PropertyInfo();
        parm_ItemNo.setName("ItemNo");
        parm_ItemNo.setValue(ItemNo);
        parm_ItemNo.setType(String.class);
        request.addProperty(parm_ItemNo);



        PropertyInfo parm_StoreNo = new PropertyInfo();
        parm_StoreNo.setName("StoreNo");
        parm_StoreNo.setValue(StoreNo);
        parm_StoreNo.setType(String.class);
        request.addProperty(parm_StoreNo);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,1000);

        try {
            androidHttpTransport.call("http://tempuri.org/TrnsferQtyBatch", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
        //Return resTxt to calling object

    }
    public long Save_Sal_Invoice(String Json) {
        We_Result.Msg =  "";
        We_Result.ID = -1;

       SoapObject request = new SoapObject(NAMESPACE, "Insert_Sale_Invoice");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,40000);
        Object  response =null;
        try {
            androidHttpTransport.call( "http://tempuri.org/Insert_Sale_Invoice", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {

            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
        }
        return   We_Result.ID;

    }
    public void Save_Ret_Sal_Invoice(String Json) {
        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "Insert_Ret_Sale_Invoice");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call( "http://tempuri.org/Insert_Ret_Sale_Invoice", envelope);

            SoapObject result  = (SoapObject) envelope.getResponse();



            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();



        }


    }
    public void SaveCustLocation(String Json) {
        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "SaveCustLocation");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call( "http://tempuri.org/SaveCustLocation", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");

            e.printStackTrace();


        }
        //Return resTxt to calling object

    }

   public long  SaveManVisits(String Json ) {
          try {
       We_Result.Msg="";
       We_Result.ID =-1;
       SoapObject request = new SoapObject(NAMESPACE, "SaveManVisits");
       PropertyInfo StrJson = new PropertyInfo();
       StrJson.setName("JsonStr");
       StrJson.setValue(Json);
       StrJson.setType(String.class);
       request.addProperty(StrJson);

       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
               SoapEnvelope.VER11);
       envelope.dotNet=true;

       envelope.setOutputSoapObject(request);

       HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
       Object  response =null;


           androidHttpTransport.call( "http://tempuri.org/SaveManVisits", envelope);
           SoapObject result  = (SoapObject) envelope.getResponse();
           We_Result.Msg =  result.getProperty("Msg").toString();
           We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



       } catch (NullPointerException   en){
           We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح";
           We_Result.ID = Long.parseLong("-404");
           en.printStackTrace();


       } catch (EOFException eof ){
           We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح";
           We_Result.ID = Long.parseLong("-404");
           eof.printStackTrace();

       }
       catch (Exception e) {
           We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;// + String.valueOf(e.getMessage().toString());
           We_Result.ID = Long.parseLong("-404");
           e.printStackTrace();

       }
       return  We_Result.ID;

   }
    public long SaveManLoactios(String Json) {
        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "SaveManLoactions");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call( "http://tempuri.org/SaveManLoactions", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt

        }
         return   We_Result.ID;

    }
    public void GetcompanyInfo() {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetCopmanyInfo");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetCopmanyInfo", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }

    }
    public void GetCASHCUST(String ManNo) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetCASHCUST");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/GetCASHCUST", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void Get_Items_Categs(String ManNo) {

        We_Result.Msg="";
        We_Result.ID =-1;


       SoapObject request = new SoapObject(NAMESPACE, "GetItems_Categ");
        PropertyInfo ParmUsere = new PropertyInfo();
        ParmUsere.setName("ManNo");
        ParmUsere.setValue(ManNo);
        ParmUsere.setType(String.class);
        request.addProperty(ParmUsere);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
               envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/GetItems_Categ", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");


        }


    }
    public void Get_Cust_Categs(String ManNo) {

        We_Result.Msg="";
        We_Result.ID =-1;


        SoapObject request = new SoapObject(NAMESPACE, "Get_Cust_Categ");
        PropertyInfo ParmUsere = new PropertyInfo();
        ParmUsere.setName("ManNo");
        ParmUsere.setValue(ManNo);
        ParmUsere.setType(String.class);
        request.addProperty(ParmUsere);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/Get_Cust_Categ", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");


        }


    }
    public void SaveDoctorReport(String Json) {
        We_Result.Msg="";
        We_Result.ID =-1;

        SoapObject request = new SoapObject(NAMESPACE, "SaveDoctorReport");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");

        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,40000);

        try {
            androidHttpTransport.call( "http://tempuri.org/SaveDoctorReport", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();

            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");

        }


    }
    public void GetAreas() {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetAreas");




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetAreas", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");

        }


    }

    public void ShareUsedCode(String Json) {
        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "SaveUsedCodes");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call( "http://tempuri.org/SaveUsedCodes", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();

            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");

        }


    }
    public void SaveCashCust(String Json) {
        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "SaveCashCust");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call( "http://tempuri.org/SaveCashCust", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();

            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;
            We_Result.ID = Long.parseLong("-404");

        }

    }
    public void CallManLocationReport(String Man_No, String SuperVisor,String  FDate,String TDate,String Flag ) {

        We_Result.Msg="";
        We_Result.ID =-1;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GetManLocations");
        // Property which holds input parameters
        PropertyInfo parm_Man_No = new PropertyInfo();
        parm_Man_No.setName("Man_No");
        parm_Man_No.setValue(Man_No);
        parm_Man_No.setType(String.class);

        PropertyInfo parm_SuperVisor = new PropertyInfo();
        parm_SuperVisor.setName("SuperVisor");
        parm_SuperVisor.setValue(SuperVisor);
        parm_SuperVisor.setType(String.class);


        PropertyInfo parm_FDate = new PropertyInfo();
        parm_FDate.setName("FDate");
        parm_FDate.setValue(FDate);
        parm_FDate.setType(String.class);




        PropertyInfo parm_ToDate = new PropertyInfo();
        parm_ToDate.setName("TDate");
        parm_ToDate.setValue(TDate);
        parm_ToDate.setType(String.class);


        PropertyInfo parm_Flag = new PropertyInfo();
        parm_Flag.setName("Flag");
        parm_Flag.setValue(Flag);
        parm_Flag.setType(String.class);


        // Add the property to request object
        request.addProperty(parm_Man_No);
        request.addProperty(parm_SuperVisor);
        request.addProperty(parm_FDate);
        request.addProperty(parm_ToDate);
        request.addProperty(parm_Flag);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call("http://tempuri.org/GetManLocations", envelope);

            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");

        }

    }
    public void CallAllManLocation (String Man_No, String SuperVisor,String  FDate,String TDate,String Flag ) {

        We_Result.Msg="";
        We_Result.ID =-1;

        SoapObject request = new SoapObject(NAMESPACE, "GetAllManLocations");

        PropertyInfo parm_Man_No = new PropertyInfo();
        parm_Man_No.setName("Man_No");
        parm_Man_No.setValue(Man_No);
        parm_Man_No.setType(String.class);

        PropertyInfo parm_SuperVisor = new PropertyInfo();
        parm_SuperVisor.setName("SuperVisor");
        parm_SuperVisor.setValue(SuperVisor);
        parm_SuperVisor.setType(String.class);


        PropertyInfo parm_FDate = new PropertyInfo();
        parm_FDate.setName("FDate");
        parm_FDate.setValue(FDate);
        parm_FDate.setType(String.class);




        PropertyInfo parm_ToDate = new PropertyInfo();
        parm_ToDate.setName("TDate");
        parm_ToDate.setValue(TDate);
        parm_ToDate.setType(String.class);


        PropertyInfo parm_Flag = new PropertyInfo();
        parm_Flag.setName("Flag");
        parm_Flag.setValue(Flag);
        parm_Flag.setType(String.class);


        // Add the property to request object
        request.addProperty(parm_Man_No);
        request.addProperty(parm_SuperVisor);
        request.addProperty(parm_FDate);
        request.addProperty(parm_ToDate);
        request.addProperty(parm_Flag);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call("http://tempuri.org/GetAllManLocations", envelope);

            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");

        }
        //Return resTxt to calling object

    }
    public void SendRequestPermission(String ManNo,String CustNo, String OrderNo, String Desc  ,String Type, String Amt) {

        We_Result.Msg="";
        We_Result.ID =-1;
    SoapObject request = new SoapObject(NAMESPACE, "SendRequestPermission");


    PropertyInfo Info_ManNo = new PropertyInfo();
    Info_ManNo.setName("ManNo");
    Info_ManNo.setValue(ManNo);
    Info_ManNo.setType(String.class);
    request.addProperty(Info_ManNo);


    PropertyInfo Info_CustNo = new PropertyInfo();
    Info_CustNo.setName("CustNo");
    Info_CustNo.setValue(CustNo);
    Info_CustNo.setType(String.class);
    request.addProperty(Info_CustNo);



    PropertyInfo Info_OrderNo = new PropertyInfo();
    Info_OrderNo.setName("OrderNo");
    Info_OrderNo.setValue(OrderNo);
    Info_OrderNo.setType(String.class);
    request.addProperty(Info_OrderNo);


    PropertyInfo Info_Desc = new PropertyInfo();
    Info_Desc.setName("Desc");
    Info_Desc.setValue(Desc);
    Info_Desc.setType(String.class);
    request.addProperty(Info_Desc);


    PropertyInfo Info_Type = new PropertyInfo();
    Info_Type.setName("Type");
    Info_Type.setValue(Type);
    Info_Type.setType(String.class);
    request.addProperty(Info_Type);


    PropertyInfo Info_Amt = new PropertyInfo();
    Info_Amt.setName("Amt");
    Info_Amt.setValue(Amt);
    Info_Amt.setType(String.class);
    request.addProperty(Info_Amt);



    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
            SoapEnvelope.VER11);
    envelope.dotNet=true;

    envelope.setOutputSoapObject(request);

    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,40000);

    try {

        androidHttpTransport.call( "http://tempuri.org/SendRequestPermission", envelope);
        SoapObject result  = (SoapObject) envelope.getResponse();



        We_Result.Msg =  result.getProperty("Msg").toString();
        We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



    } catch (Exception e) {

        We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
        We_Result.ID = Long.parseLong("-404");


    }


}
    public void Get_Request_Permission (String ManNo,String CustNo, String OrderNo, String Type ) {

        We_Result.Msg="";
        We_Result.ID =-1;

        SoapObject request = new SoapObject(NAMESPACE, "GetRequestPermission");


        PropertyInfo Info_ManNo = new PropertyInfo();
        Info_ManNo.setName("ManNo");
        Info_ManNo.setValue(ManNo);
        Info_ManNo.setType(String.class);
        request.addProperty(Info_ManNo);


        PropertyInfo Info_CustNo = new PropertyInfo();
        Info_CustNo.setName("CustNo");
        Info_CustNo.setValue(CustNo);
        Info_CustNo.setType(String.class);
        request.addProperty(Info_CustNo);



        PropertyInfo Info_OrderNo = new PropertyInfo();
        Info_OrderNo.setName("OrderNo");
        Info_OrderNo.setValue(OrderNo);
        Info_OrderNo.setType(String.class);
        request.addProperty(Info_OrderNo);




        PropertyInfo Info_Type = new PropertyInfo();
        Info_Type.setName("Type");
        Info_Type.setValue(Type);
        Info_Type.setType(String.class);
        request.addProperty(Info_Type);




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call("http://tempuri.org/GetRequestPermission", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
        }


    }
    public void Get_ServerDateTime() {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetServerDateTime");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/GetServerDateTime", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public String Tafkeet(String Amt) {

        We_Result.Msg = "";
        We_Result.ID = -1;
        SoapObject request = new SoapObject(NAMESPACE, "Get_Tafkeet");

        PropertyInfo parm_Amt = new PropertyInfo();
        parm_Amt.setName("Amt");
        parm_Amt.setValue(Amt);
        parm_Amt.setType(String.class);
        request.addProperty(parm_Amt);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {

            androidHttpTransport.call("http://tempuri.org/Get_Tafkeet", envelope);
            SoapObject result = (SoapObject) envelope.getResponse();
            We_Result.Msg = result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg = "عملية الاتصال بالسيرفر لم تتم بنجاح";//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
        return We_Result.Msg;
    }
    public void Get_Price_List(String ManNo) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetPriceList");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/GetPriceList", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void SaveCusImnages( String Acc,String ImageDesc ,String ImageBase64 ) {

        We_Result.Msg="";
        We_Result.ID =-1;
        String resTxt = null;

        SoapObject request = new SoapObject(NAMESPACE, "Save_Cusf_Attachament");
        PropertyInfo Parm_Acc = new PropertyInfo();
        Parm_Acc.setName("Acc");
        Parm_Acc.setValue(Acc);
        Parm_Acc.setType(String.class);
        request.addProperty(Parm_Acc);


        PropertyInfo sayImageDesc = new PropertyInfo();
        sayImageDesc.setName("ImageDesc");
        sayImageDesc.setValue(ImageDesc);
        sayImageDesc.setType(String.class);
        request.addProperty(sayImageDesc);


        PropertyInfo parm_ImageBase64= new PropertyInfo();
        parm_ImageBase64.setName("ImageBase64");
        parm_ImageBase64.setValue(ImageBase64);
        parm_ImageBase64.setType(String.class);
        request.addProperty(parm_ImageBase64);




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call( "http://tempuri.org/Save_Cusf_Attachament", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID =Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;// + String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public String PostCustCardAndGetAcc(String CusName,String OrderNo, String Area , String CustType, String Mobile , String Acc
                        , String Lat,String Lng,String GpsLocation  ,String UserID ,String COMPUTERNAME) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "SaveCustCardAndGetAcc");

        PropertyInfo Para_CusName = new PropertyInfo();
        Para_CusName.setName("CusName");
        Para_CusName.setValue(CusName);
        Para_CusName.setType(String.class);
        request.addProperty(Para_CusName);


        PropertyInfo Param_OrderNo = new PropertyInfo();
        Param_OrderNo.setName("OrderNo");
        Param_OrderNo.setValue(OrderNo);
        Param_OrderNo.setType(String.class);
        request.addProperty(Param_OrderNo);

        PropertyInfo Param_Area = new PropertyInfo();
        Param_Area.setName("Area");
        Param_Area.setValue(Area);
        Param_Area.setType(String.class);
        request.addProperty(Param_Area);


        PropertyInfo Parm_CustType = new PropertyInfo();
        Parm_CustType.setName("CustType");
        Parm_CustType.setValue(CustType);
        Parm_CustType.setType(String.class);
        request.addProperty(Parm_CustType);

        PropertyInfo Parm_Mobile = new PropertyInfo();
        Parm_Mobile.setName("Mobile");
        Parm_Mobile.setValue(Mobile);
        Parm_Mobile.setType(String.class);
        request.addProperty(Parm_Mobile);



        PropertyInfo Parm_Acc = new PropertyInfo();
        Parm_Acc.setName("Acc");
        Parm_Acc.setValue(Acc);
        Parm_Acc.setType(String.class);
        request.addProperty(Parm_Acc);

        PropertyInfo Parm_Lat = new PropertyInfo();
        Parm_Lat.setName("Lat");
        Parm_Lat.setValue(Lat);
        Parm_Lat.setType(String.class);
        request.addProperty(Parm_Lat);


        PropertyInfo Parm_Long = new PropertyInfo();
        Parm_Long.setName("Lng");
        Parm_Long.setValue(Lng);
        Parm_Long.setType(String.class);
        request.addProperty(Parm_Long);


        PropertyInfo Parm_GpsLocation = new PropertyInfo();
        Parm_GpsLocation.setName("GpsLocation");
        Parm_GpsLocation.setValue(GpsLocation);
        Parm_GpsLocation.setType(String.class);
        request.addProperty(Parm_GpsLocation);





        PropertyInfo parm_UserID= new PropertyInfo();
        parm_UserID.setName("UserID");
        parm_UserID.setValue(UserID);
        parm_UserID.setType(String.class);
        request.addProperty(parm_UserID);


        PropertyInfo parm_COMPUTERNAME= new PropertyInfo();
        parm_COMPUTERNAME.setName("COMPUTERNAME");
        parm_COMPUTERNAME.setValue(COMPUTERNAME);
        parm_COMPUTERNAME.setType(String.class);
        request.addProperty(parm_COMPUTERNAME);



        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/SaveCustCardAndGetAcc", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


        return We_Result.ID+"";  }

}
