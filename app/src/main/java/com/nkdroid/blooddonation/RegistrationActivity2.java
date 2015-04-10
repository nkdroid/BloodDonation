package com.nkdroid.blooddonation;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class RegistrationActivity2 extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    String[] bgrp= {"A+","A-","B+","B-","AB+","AB-","O+","O-"};

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activity2);

        setToolbar();

        Spinner spbloodgroup = (Spinner) findViewById(R.id.spbg);
        spbloodgroup.setOnItemSelectedListener(this);
        ArrayAdapter abloodgroup = new ArrayAdapter(this, android.R.layout.simple_spinner_item,bgrp);
        abloodgroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spbloodgroup.setAdapter(abloodgroup);

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
                    Intent intent = new Intent(RegistrationActivity2.this, RegistrationActivity.class);

                    startActivity(intent);
//                    finish();
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegistrationActivity2.this, RegistrationActivity.class);

        startActivity(intent);
//        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
