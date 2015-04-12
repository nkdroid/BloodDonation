package com.nkdroid.blooddonation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class RegistrationActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    String[] city= {"Vadodara"};
    String[] area= {"Karelibaugh","Vaghodia","Subhanpura","O.P Road","Nyaymandir","Alkapuri"};

    private Toolbar toolbar;
    private EditText name,cntct,email,cityname,areacode,bdate;
    private TextView btnContinue;

    private DatePickerDialog fromDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setToolbar();



        Spinner spcity = (Spinner) findViewById(R.id.spcity);
        spcity.setOnItemSelectedListener(this);
        ArrayAdapter acity = new ArrayAdapter(this, android.R.layout.simple_spinner_item,city);
        acity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcity.setAdapter(acity);

        Spinner sparea = (Spinner) findViewById(R.id.sparea);
        sparea.setOnItemSelectedListener(this);
        ArrayAdapter aarea = new ArrayAdapter(this, android.R.layout.simple_spinner_item,area);
        aarea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sparea.setAdapter(aarea);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        initView();

    }



    private void initView() {
        name= (EditText) findViewById(R.id.fname);
        cntct= (EditText) findViewById(R.id.contact);
        email= (EditText) findViewById(R.id.email);
        cityname= (EditText) findViewById(R.id.area);
        areacode= (EditText) findViewById(R.id.areacode);
        btnContinue= (TextView)findViewById(R.id.btncont);
        bdate = (EditText) findViewById(R.id.bdate);
        bdate.setInputType(InputType.TYPE_NULL);
        bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
                fromDatePickerDialog.show();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyField(name)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Name", Toast.LENGTH_LONG).show();
                }/* else if (isEmptyField(cntct)) {
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
                }*/ else {

                    //store in shared preference
                    // PrefUtils.setLoggedIn(RegistrationActivity.this, true, etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
                    //Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);

                    // startActivity(intent);
                    Intent intent = new Intent(RegistrationActivity.this, RegistrationActivity2.class);

                    startActivity(intent);
                    finish();

                }

            }
        });

    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                bdate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


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
            toolbar.setTitle("Register");
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
