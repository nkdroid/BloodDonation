package com.nkdroid.blooddonation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nkdroid.blooddonation.model.UserClass;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class RegistrationActivity3 extends ActionBarActivity {
    TextView Reg;

    private Toolbar toolbar;
    private ProgressDialog dialog;
    String resp;
    private CheckBox heartd,tb,highbp,anemia,hiv;

    private UserClass userClass;
    public static final String SOAP_ACTION = "http://tempuri.org/AddUser";
    public static  final String OPERATION_NAME = "AddUser";

    public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

    //public  final String SOAP_ADDRESS = "http://artist.somee.com/DPR.asmx";
    public static  final String SOAP_ADDRESS = "http://donateblood.somee.com/WebService.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activity3);
        heartd= (CheckBox) findViewById(R.id.heartd);
        tb= (CheckBox) findViewById(R.id.tb);
        highbp= (CheckBox) findViewById(R.id.highbp);
        anemia= (CheckBox) findViewById(R.id.anemia);
        hiv= (CheckBox) findViewById(R.id.hiv);

        setToolbar();
        Reg=(TextView)findViewById(R.id.btnRegister);
        userClass=new UserClass();
        userClass=PrefUtils.getCurrentUser(RegistrationActivity3.this);
        userClass.privacy="1";
        userClass.donation_status="0";
        userClass.req_status="0";
        userClass.heartd="0";
        userClass.tb="0";
        userClass.highbp="0";
        userClass.amenia="0";
        userClass.hiv="0";
        heartd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userClass.heartd="1";
                } else {
                    userClass.heartd="0";
                }
            }
        });

        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userClass.tb="1";
                } else {
                    userClass.tb="0";
                }
            }
        });

        highbp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userClass.highbp="1";
                } else {
                    userClass.highbp="0";
                }
            }
        });

        anemia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userClass.amenia="1";
                } else {
                    userClass.amenia="0";
                }
            }
        });

        hiv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userClass.hiv="1";
                } else {
                    userClass.hiv="0";
                }
            }
        });


        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("",userClass.name+"");
                Log.e("",userClass.dob+"");
                Log.e("",userClass.gender+"");
                Log.e("",userClass.weight+"");
                Log.e("",userClass.contact+"");
                Log.e("",userClass.privacy+"");
                Log.e("",userClass.email+"");
                Log.e("",userClass.address+"");
                Log.e("",userClass.city+"");
                Log.e("",userClass.area+"");
                Log.e("",userClass.bgrp+"");
                Log.e("",userClass.password+"");
                Log.e("",userClass.donation_status+"");
                Log.e("",userClass.hiv+"");
                Log.e("",userClass.highbp+"");
                Log.e("",userClass.tb+"");
                Log.e("",userClass.heartd+"");
                Log.e("",userClass.amenia+"");
                Log.e("",userClass.req_status+"");
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
                        resp = Call(userClass.name,userClass.dob,userClass.gender,userClass.weight,userClass.contact,userClass.privacy,userClass.email,userClass.address,userClass.city,userClass.area,userClass.bgrp,userClass.password,userClass.donation_status,userClass.hiv,userClass.highbp,userClass.tb,userClass.heartd,userClass.amenia,userClass.req_status);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        try {
                            Log.e("response",resp+"");
//                            Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        if(resp.equalsIgnoreCase("1")){
                            PrefUtils.setCurrentUser(userClass,RegistrationActivity3.this);
                            Intent intent = new Intent(RegistrationActivity3.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), "Error while inserting...try again", Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();

            }
        });
    }

    public String Call(String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8, String c9, String c10, String c11, String c12, String c13, String c14, String c15, String c16, String c17, String c18, String c19)
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
        p6.setName("privacy");
        p6.setValue(c6);
        p6.setType(String.class);
        request.addProperty(p6);


        PropertyInfo p7=new PropertyInfo();
        p7.setName("email");
        p7.setValue(c7);
        p7.setType(String.class);
        request.addProperty(p7);

        PropertyInfo p8=new PropertyInfo();
        p8.setName("address");
        p8.setValue(c8);
        p8.setType(String.class);
        request.addProperty(p8);

        PropertyInfo p9=new PropertyInfo();
        p9.setName("city");
        p9.setValue(c9);
        p9.setType(String.class);
        request.addProperty(p9);

        PropertyInfo p10=new PropertyInfo();
        p10.setName("area");
        p10.setValue(c10);
        p10.setType(String.class);
        request.addProperty(p10);

        PropertyInfo p11=new PropertyInfo();
        p11.setName("bgrp");
        p11.setValue(c11);
        p11.setType(String.class);
        request.addProperty(p11);

        PropertyInfo p12=new PropertyInfo();
        p12.setName("password");
        p12.setValue(c12);
        p12.setType(String.class);
        request.addProperty(p12);

        PropertyInfo p13=new PropertyInfo();
        p13.setName("donation_status");
        p13.setValue(c13);
        p13.setType(String.class);
        request.addProperty(p13);

        PropertyInfo p14=new PropertyInfo();
        p14.setName("hiv");
        p14.setValue(c14);
        p14.setType(String.class);
        request.addProperty(p14);

        PropertyInfo p15=new PropertyInfo();
        p15.setName("highbp");
        p15.setValue(c15);
        p15.setType(String.class);
        request.addProperty(p15);

        PropertyInfo p16=new PropertyInfo();
        p16.setName("tb");
        p16.setValue(c16);
        p16.setType(String.class);
        request.addProperty(p16);

        PropertyInfo p17=new PropertyInfo();
        p17.setName("heartd");
        p17.setValue(c17);
        p17.setType(String.class);
        request.addProperty(p17);

        PropertyInfo p18=new PropertyInfo();
        p18.setName("anemia");
        p18.setValue(c18);
        p18.setType(String.class);
        request.addProperty(p18);

        PropertyInfo p19=new PropertyInfo();
        p19.setName("req_status");
        p19.setValue(c19);
        p19.setType(String.class);
        request.addProperty(p19);

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
        return response.toString();
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
