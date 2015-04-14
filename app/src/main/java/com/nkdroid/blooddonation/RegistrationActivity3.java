package com.nkdroid.blooddonation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class RegistrationActivity3 extends ActionBarActivity {
    TextView Reg;
    String resp;
    private String name,dob,gender,weight,contact,email,address,city,area,passwd,bgrp;
    private Toolbar toolbar;
private ProgressDialog dialog;

    public static final String SOAP_ACTION = "http://tempuri.org/AddUser";
    public static  final String OPERATION_NAME = "AddUser";

    public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

    //public  final String SOAP_ADDRESS = "http://artist.somee.com/DPR.asmx";
    public static  final String SOAP_ADDRESS = " http://donateblood.somee.com/WebService.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activity3);
        setToolbar();
        Reg=(TextView)findViewById(R.id.btnRegister);

        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt = getIntent();
                name = intnt.getExtras().getString("NAME1");
                dob = intnt.getExtras().getString("BDATE1");
                gender = intnt.getExtras().getString("GENDER1");
                weight = intnt.getExtras().getString("WEIGHT1");
                contact = intnt.getExtras().getString("CONTACT1");
                email = intnt.getExtras().getString("EMAIL1");
                address = intnt.getExtras().getString("ADDRESS1");
                city = intnt.getExtras().getString("CITY1");
                area = intnt.getExtras().getString("AREA1");
                bgrp = intnt.getExtras().getString("BloodGrp");
                passwd = intnt.getExtras().getString("PASSWD1");
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        dialog = new ProgressDialog(RegistrationActivity3.this);
                        dialog.setMessage("Loading...");
                        dialog.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        resp = Call(name, dob, gender, weight, contact, email, address, city, area, bgrp, passwd);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }.execute();
                Intent intent = new Intent(RegistrationActivity3.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public String Call(String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8, String c9, String c10, String c11)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo p1=new PropertyInfo();
        p1.setName("name");
        p1.setValue(c1);
        p1.setType(String.class);
        request.addProperty(p1);

        PropertyInfo p2=new PropertyInfo();
        p2.setName("dob");
        p2.setValue(c2);
        p2.setType(String.class);
        request.addProperty(p2);

        PropertyInfo p3=new PropertyInfo();
        p3.setName("gender");
        p3.setValue(c3);
        p3.setType(String.class);
        request.addProperty(p3);


        PropertyInfo p4=new PropertyInfo();
        p4.setName("weight");
        p4.setValue(c4);
        p4.setType(String.class);
        request.addProperty(p4);

        PropertyInfo p5=new PropertyInfo();
        p5.setName("contact");
        p5.setValue(c5);
        p5.setType(String.class);
        request.addProperty(p5);

        PropertyInfo p6=new PropertyInfo();
        p6.setName("email");
        p6.setValue(c6);
        p6.setType(String.class);
        request.addProperty(p6);


        PropertyInfo p7=new PropertyInfo();
        p7.setName("address");
        p7.setValue(c7);
        p7.setType(String.class);
        request.addProperty(p7);

        PropertyInfo p8=new PropertyInfo();
        p8.setName("city");
        p8.setValue(c8);
        p8.setType(String.class);
        request.addProperty(p8);

        PropertyInfo p9=new PropertyInfo();
        p9.setName("area");
        p9.setValue(c9);
        p9.setType(String.class);
        request.addProperty(p9);

        PropertyInfo p10=new PropertyInfo();
        p10.setName("bgrp");
        p10.setValue(c10);
        p10.setType(String.class);
        request.addProperty(p10);

        PropertyInfo p11=new PropertyInfo();
        p11.setName("passwd");
        p11.setValue(c11);
        p11.setType(String.class);
        request.addProperty(p11);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;

        try
        {

            httpTransport.debug=true;
            httpTransport.call(SOAP_ACTION, envelope);

            response = envelope.getResponse();
        }
        catch (Exception ex)
        {
            // ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
            //displayExceptionMessage(ex.getMessage());
            //System.out.println(exception.getMessage());
        }
        return response.toString() ;
    }




    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            toolbar.setTitle("Register");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(RegistrationActivity3.this, RegistrationActivity2.class);

                    startActivity(intent);
//                    finish();
                }
            });
        }
    }
}
