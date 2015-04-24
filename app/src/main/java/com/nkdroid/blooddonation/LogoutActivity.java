package com.nkdroid.blooddonation;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class LogoutActivity extends ActionBarActivity {

    private EditText etUsername;
    private TextView btnSubmit;
    public static final String SOAP_ACTION = "http://tempuri.org/ForgotPassword";
    public static  final String OPERATION_NAME = "ForgotPassword";

    public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

    //public  final String SOAP_ADDRESS = "http://artist.somee.com/DPR.asmx";
    public static  final String SOAP_ADDRESS = "http://donateblood.somee.com/WebService.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        etUsername= (EditText) findViewById(R.id.etUsername);
        btnSubmit= (TextView) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
