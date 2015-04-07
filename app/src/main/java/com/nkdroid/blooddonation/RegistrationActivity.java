package com.nkdroid.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class RegistrationActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    String[] bgrp= {"A+","A-","B+","B-","AB+","AB-","O+","O-"};
    private Toolbar toolbar;
    private EditText name,cntct,email,cityname,areacode;
    private TextView btnContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setToolbar();

        Spinner spin = (Spinner) findViewById(R.id.spbg);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,bgrp);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        initView();
    }



    private void initView() {
        name= (EditText) findViewById(R.id.fname);
       cntct= (EditText) findViewById(R.id.contact);
        email= (EditText) findViewById(R.id.email);
        cityname= (EditText) findViewById(R.id.area);
        areacode= (EditText) findViewById(R.id.areacode);
        btnContinue= (TextView)findViewById(R.id.btncont);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyField(name)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Name", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(cntct)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Contact", Toast.LENGTH_LONG).show();
                }
                else if (isEmptyField(email)) {
                        Toast.makeText(RegistrationActivity.this, "Please Enter EMail", Toast.LENGTH_LONG).show();
                }
                else if (isEmptyField(cityname)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter City Name", Toast.LENGTH_LONG).show();
                }
                else if (isEmptyField(areacode)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Area Code", Toast.LENGTH_LONG).show();
                }
                    else {

                    //store in shared preference
                   // PrefUtils.setLoggedIn(RegistrationActivity.this, true, etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
                    //Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);

                   // startActivity(intent);
                    finish();

                }

            }
        });

    }

    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("REGISTER");
            setSupportActionBar(toolbar);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);

        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
