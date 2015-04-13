package com.nkdroid.blooddonation;

import android.content.Intent;
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
   private String name,dob,gender,weight,contact,email,address,city,area,passwd,bgrp;
    private Toolbar toolbar;
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
                name = intnt.getStringExtra("NAME");
                dob = intnt.getStringExtra("BDATE");
                gender = intnt.getExtras().getString("GENDER");
                weight = intnt.getExtras().getString("WEIGHT");
                contact = intnt.getExtras().getString("CONTACT");
                email = intnt.getExtras().getString("EMAIL");
                address = intnt.getExtras().getString("ADDRESS");
                city = intnt.getExtras().getString("CITY");
                area = intnt.getExtras().getString("AREA");
                passwd = intnt.getExtras().getString("PASSWD");
                bgrp = intnt.getExtras().getString("BloodGrp");

                Intent intent = new Intent(RegistrationActivity3.this,LoginActivity.class);
                    startActivity(intent);

                }


        });
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
