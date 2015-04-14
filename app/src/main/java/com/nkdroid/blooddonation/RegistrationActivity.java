package com.nkdroid.blooddonation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;



public class RegistrationActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<String> city=new ArrayList<>();
    ArrayList<String> area=new ArrayList<>();
    private Toolbar toolbar;
    private EditText name,bdate,address,weight,contact,email,password,cpass;
    private Spinner ecity,earea;
    private String sname,sbdate,sgender,saddress,sweight,scontact,semai,scity,sarea,spasswd,cspass,NAME;
    private TextView btnContinue;
    private  RadioGroup rg;
    private DatePickerDialog fromDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    public RegistrationActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setToolbar();

        city.add("Vadodara");

        area.add("Karelibaugh");
        area.add("Vaghodia");
        area.add("Subhanpura");
        area.add("Alkapuri");
        area.add("O.P Road");
        area.add("Nyaymandir");



        Spinner spcity = (Spinner) findViewById(R.id.spcity);
        spcity.setOnItemSelectedListener(this);
        TimeSpinnerAdapter tspcity=new TimeSpinnerAdapter(RegistrationActivity.this,city);
        spcity.setAdapter(tspcity);

        final Spinner sparea = (Spinner) findViewById(R.id.sparea);
        sparea.setOnItemSelectedListener(this);
        TimeSpinnerAdapter tsparea=new TimeSpinnerAdapter(RegistrationActivity.this,area);
        sparea.setAdapter(tsparea);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        rg = (RadioGroup) findViewById(R.id.gendergroup);
        name= (EditText) findViewById(R.id.fname);
        address=(EditText)findViewById(R.id.address);
        contact= (EditText) findViewById(R.id.contact);
        email= (EditText) findViewById(R.id.email);
        weight= (EditText) findViewById(R.id.weight);
        password= (EditText) findViewById(R.id.password);
        cpass= (EditText) findViewById(R.id.cpassword);

        earea= (Spinner) findViewById(R.id.sparea);
        ecity= (Spinner) findViewById(R.id.spcity);

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
                } else if (isEmptyField(bdate)) {
                    Toast.makeText(RegistrationActivity.this, "Please Select Birthdate", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(address)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Address", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(weight)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Weight", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(contact)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Contact", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(email)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter E-Mail", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(password)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(cpass)) {
                    Toast.makeText(RegistrationActivity.this, "Please Confirm Password", Toast.LENGTH_LONG).show();
                }
                else {

                    sname = name.getText().toString();
                    sbdate=bdate.getText().toString();
                    sgender = ((RadioButton)findViewById(rg.getCheckedRadioButtonId() )).getText().toString();
                    saddress=address.getText().toString();
                    sweight=weight.getText().toString();
                    scontact=contact.getText().toString();
                    semai=email.getText().toString();
                    scontact=contact.getText().toString();
                    sarea=earea.getSelectedItem().toString();
                    scity=ecity.getSelectedItem().toString();
                    spasswd=password.getText().toString();
                    cspass=cpass.getText().toString();
                    if (spasswd!=cspass) {
                        Toast.makeText(RegistrationActivity.this, "Password doesn't match ", Toast.LENGTH_LONG).show();
                    }
                    //store in shared preference
                    // PrefUtils.setLoggedIn(RegistrationActivity.this, true, etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
                    //Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                    Intent intent = new Intent(RegistrationActivity.this, RegistrationActivity2.class);

                    intent.putExtra("NAME",sname);
                    intent.putExtra("BDATE",sbdate);
                    intent.putExtra("GENDER",sgender);
                    intent.putExtra("ADDRESS",saddress);
                    intent.putExtra("CITY",scity);
                    intent.putExtra("AREA",sarea);
                    intent.putExtra("WEIGHT",sweight);
                    intent.putExtra("CONTACT",scontact);
                    intent.putExtra("EMAIL",semai);
                    intent.putExtra("PASSWD",spasswd);
                    // startActivity(intent);
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

    public class TimeSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public TimeSpinnerAdapter(Context context,ArrayList<String> asr) {
            this.asr=asr;
            activity = context;
        }

        public int getCount()
        {
            return asr.size();
        }

        public Object getItem(int i)
        {
            return asr.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(RegistrationActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#494949"));
            return  txt;

        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(RegistrationActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(16);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_spinner, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#ffffff"));
            return  txt;
        }
    }
}
