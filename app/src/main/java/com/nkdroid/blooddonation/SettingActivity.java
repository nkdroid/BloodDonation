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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.nkdroid.blooddonation.model.UserClass;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class SettingActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private Switch tbtndonation;
    private UserClass user;
    String resp;
    private  ProgressDialog dialog;

    public static final String SOAP_ACTION = "http://tempuri.org/UpdateMyDStatus";
    public static  final String OPERATION_NAME = "UpdateMyDStatus";

    public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

    //public  final String SOAP_ADDRESS = "http://artist.somee.com/DPR.asmx";
    public static  final String SOAP_ADDRESS = "http://donateblood.somee.com/WebService.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tbtndonation= (Switch) findViewById(R.id.tbtndonation);
//        user=PrefUtils.getCurrentUser(SettingActivity.this);
//
//        if(user.donation_status.equalsIgnoreCase("1")){
//            tbtndonation.setChecked(true);
//        } else {
//            tbtndonation.setChecked(false);
//        }
        tbtndonation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                updateDonateStatus(1);
                }else {
                    updateDonateStatus(0);
                }
            }
        });

        setToolbar();

    }

    private void updateDonateStatus(final int i) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(SettingActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                resp = loginCall(user.email,i+"");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e("Response", resp + "");
                dialog.dismiss();
                if(resp.equalsIgnoreCase("1"))
                {
                    user.donation_status=i+"";
                    PrefUtils.setCurrentUser(user,SettingActivity.this);

                    Toast.makeText(getApplicationContext(), "Status Updated Successfully" + resp, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Authentication Error" + resp, Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            toolbar.setTitle("Setting");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public String loginCall(String c1, String c2)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo p1=new PropertyInfo();
        p1.setName("email");
        p1.setValue(c1);
        p1.setType(String.class);
        request.addProperty(p1);

        PropertyInfo p2=new PropertyInfo();
        p2.setName("donation_status");
        p2.setValue(c2);
        p2.setType(String.class);
        request.addProperty(p2);
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
}
