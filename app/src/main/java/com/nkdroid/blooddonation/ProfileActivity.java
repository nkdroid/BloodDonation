package com.nkdroid.blooddonation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nkdroid.blooddonation.model.Area;
import com.nkdroid.blooddonation.model.UserClass;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;


public class ProfileActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener
{

    private UserClass userClass;
    private EditText bGroup;
    private CheckBox heartd,tb,highbp,anemia,hiv;

    int temp=0;
    private ProgressDialog dialog;
    int resp;
    String responseValue;
    String c1;
    public static final String SOAP_ACTION = "http://tempuri.org/UpdateMyProfile";
    public static  final String OPERATION_NAME = "UpdateMyProfile";

    public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

    //public  final String SOAP_ADDRESS = "http://artist.somee.com/DPR.asmx";
    public static  final String SOAP_ADDRESS = "http://donateblood.somee.com/WebService.asmx";

    
    public static final String SOAP_ACTION1 = "http://tempuri.org/getcity";
    public static  final String OPERATION_NAME1 = "getcity";
    public static  final String WSDL_TARGET_NAMESPACE1 = "http://tempuri.org/";
    public static  final String SOAP_ADDRESS1 = "http://donateblood.somee.com/WebService.asmx";

    public static final String SOAP_ACTION2 = "http://tempuri.org/getarea";
    public static  final String OPERATION_NAME2 = "getarea";
    public static  final String WSDL_TARGET_NAMESPACE2 = "http://tempuri.org/";
    public static  final String SOAP_ADDRESS2 = "http://donateblood.somee.com/WebService.asmx";

    ArrayList<String> city=new ArrayList<>();
    ArrayList<Area> area=new ArrayList<Area>();
    private Toolbar toolbar;
    private EditText name,bdate,address,weight,contact,email,password,cpass;
//    private Spinner ecity,earea;
    private String sname,sbdate,sgender,saddress,sweight,scontact,semai,scity,sarea,spasswd,cspass,NAME;
    private TextView btnContinue;
    private RadioGroup rg;
    private DatePickerDialog fromDatePickerDialog;
    Object response;
    private SimpleDateFormat dateFormatter;
    Spinner spcity;
    Spinner sparea;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        userClass=PrefUtils.getCurrentUser(ProfileActivity.this);
        setToolbar();

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(ProfileActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE1,OPERATION_NAME1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS1);
                httpTransport.debug=true;
                try {
                    httpTransport.call(SOAP_ACTION1, envelope);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                try {
                    response=  envelope.getResponse();
                    Log.e("response: ", response.toString() + "");
                } catch (SoapFault soapFault) {
                    soapFault.printStackTrace();
                    Log.e("response: ", response.toString() + "");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dialog.dismiss();

                try {

                    Log.e("response: ", response.toString() + "");
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, response.toString() + "", Toast.LENGTH_LONG).show();
                }
                StringTokenizer tokens = new StringTokenizer(response.toString(), "=");
                List<String> mylist=new ArrayList<String>();
                for(int i=0;tokens.hasMoreTokens();i++){
                    StringTokenizer tokens1 = new StringTokenizer(tokens.nextToken(), ";");

                    mylist.add(tokens1.nextToken());
                }
                mylist.remove(0);

                int partitionSize =1;
                List<List<String>> partitions = new LinkedList<List<String>>();
                for (int i = 0; i < mylist.size(); i += partitionSize) {
                    partitions.add(mylist.subList(i,
                            i + Math.min(partitionSize, mylist.size() - i)));
                }

                city=new ArrayList<String>();
                for(int k=0;k<partitions.size();k++){
                    city.add(partitions.get(k).get(0));
                }

                callGetArea();

            }

        }.execute();

        spcity= (Spinner) findViewById(R.id.spcity);
        spcity.setOnItemSelectedListener(this);

        sparea = (Spinner) findViewById(R.id.sparea);
        sparea.setOnItemSelectedListener(this);

        heartd= (CheckBox) findViewById(R.id.heartd);
        tb= (CheckBox) findViewById(R.id.tb);
        highbp= (CheckBox) findViewById(R.id.highbp);
        anemia= (CheckBox) findViewById(R.id.anemia);
        hiv= (CheckBox) findViewById(R.id.hiv);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        bGroup= (EditText) findViewById(R.id.bgroup);

        rg = (RadioGroup) findViewById(R.id.gendergroup);
        name= (EditText) findViewById(R.id.fname);
        address=(EditText)findViewById(R.id.address);
        contact= (EditText) findViewById(R.id.contact);
        email= (EditText) findViewById(R.id.email);
        weight= (EditText) findViewById(R.id.weight);
        password= (EditText) findViewById(R.id.password);
        cpass= (EditText) findViewById(R.id.cpassword);

//        earea= (Spinner) findViewById(R.id.sparea);
//        ecity= (Spinner) findViewById(R.id.spcity);

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

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyField(name)) {
                    Toast.makeText(ProfileActivity.this, "Please Enter Name", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(bdate)) {
                    Toast.makeText(ProfileActivity.this, "Please Select Birthdate", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(address)) {
                    Toast.makeText(ProfileActivity.this, "Please Enter Address", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(weight)) {
                    Toast.makeText(ProfileActivity.this, "Please Enter Weight", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(contact)) {
                    Toast.makeText(ProfileActivity.this, "Please Enter Contact", Toast.LENGTH_LONG).show();
                }
                else {
                    userClass.name=name.getText().toString();
                    userClass.dob=bdate.getText().toString();
                    int selectedId = rg.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    userClass.gender=radioButton.getText().toString();
                    userClass.address=address.getText().toString();
                    userClass.city=city.get(spcity.getSelectedItemPosition());
                    userClass.area=area.get(sparea.getSelectedItemPosition()).city;
                    userClass.weight=weight.getText().toString();
                    userClass.contact=contact.getText().toString();
                    userClass.email=email.getText().toString();
                    userClass.password=password.getText().toString();




                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            dialog = new ProgressDialog(ProfileActivity.this);
                            dialog.setMessage("Loading...");
                            dialog.show();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            responseValue = Call(userClass.name,userClass.dob,userClass.gender,userClass.weight,userClass.contact,userClass.privacy,userClass.email,userClass.address,userClass.city,userClass.area,userClass.bgrp,userClass.password,userClass.donation_status,userClass.hiv,userClass.highbp,userClass.tb,userClass.heartd,userClass.amenia,userClass.req_status);
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
                            if(responseValue.equalsIgnoreCase("1")){
                                PrefUtils.setCurrentUser(userClass,ProfileActivity.this);
                                Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Error while inserting...try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    }.execute();


                }
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


    private void callGetArea() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(ProfileActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE2,OPERATION_NAME2);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS2);
                httpTransport.debug=true;
                try {
                    httpTransport.call(SOAP_ACTION2, envelope);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                try {
                    response=  envelope.getResponse();
                    Log.e("response: ", response.toString() + "");
                } catch (SoapFault soapFault) {
                    soapFault.printStackTrace();
                    Log.e("response: ", response.toString() + "");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dialog.dismiss();

                try {

                    Log.e("response: ", response.toString() + "");
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this,response.toString()+"",Toast.LENGTH_LONG).show();
                }
                StringTokenizer tokens = new StringTokenizer(response.toString(), "=");
                List<String> mylist=new ArrayList<String>();
                for(int i=0;tokens.hasMoreTokens();i++){
                    StringTokenizer tokens1 = new StringTokenizer(tokens.nextToken(), ";");

                    mylist.add(tokens1.nextToken());
                }
                mylist.remove(0);

                final int partitionSize =2;
                List<List<String>> partitions = new LinkedList<List<String>>();
                for (int i = 0; i < mylist.size(); i += partitionSize) {
                    partitions.add(mylist.subList(i,
                            i + Math.min(partitionSize, mylist.size() - i)));
                }

                area=new ArrayList<Area>();
                for(int k=0;k<partitions.size();k++){
                    area.add(new Area(partitions.get(k).get(0),partitions.get(k).get(1)));
                }


                TimeSpinnerAdapter1 tspcity=new TimeSpinnerAdapter1(ProfileActivity.this,city);
                spcity.setAdapter(tspcity);
                spcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<Area> tempList=new ArrayList<Area>();
                        for(int i=0;i<area.size();i++){
                            Log.e("city............",area.get(i).area+"");
                            if(area.get(i).area.equalsIgnoreCase(city.get(position))){
                                tempList.add(area.get(i));
                            }
                        }
                        TimeSpinnerAdapter tsparea = new TimeSpinnerAdapter(ProfileActivity.this, tempList);
                        sparea.setAdapter(tsparea);

                        if(temp==0) {
                            for (int i = 0; i < city.size(); i++) {
                                if (city.get(i).equals(userClass.city)) {
                                    spcity.setSelection(i);
                                }
                            }
                            for (int i = 0; i < area.size(); i++) {
                                if (area.get(i).city.equals(userClass.area)) {
                                    sparea.setSelection(i);
                                }
                            }
                            temp++;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //set All avalues

                name.setText(userClass.name+"");
                bdate.setText(userClass.dob+"");
                if(userClass.gender.equalsIgnoreCase("Male")){
                    rg.check(R.id.radioMale);
                } else {
                    rg.check(R.id.radioFemale);
                }

                address.setText(userClass.address+"");
                weight.setText(userClass.weight+"");
                contact.setText(userClass.contact+"");
                email.setText(userClass.email);
                if(userClass.heartd.equalsIgnoreCase("1")){
                    heartd.setChecked(true);
                } else {
                    heartd.setChecked(false);
                }

                if(userClass.tb.equalsIgnoreCase("1")){
                    tb.setChecked(true);
                } else {
                    tb.setChecked(false);
                }

                if(userClass.highbp.equalsIgnoreCase("1")){
                    highbp.setChecked(true);
                } else {
                    highbp.setChecked(false);
                }

                if(userClass.amenia.equalsIgnoreCase("1")){
                    anemia.setChecked(true);
                } else {
                    anemia.setChecked(false);
                }

                if(userClass.hiv.equalsIgnoreCase("1")){
                    hiv.setChecked(true);
                } else {
                    hiv.setChecked(false);
                }

                bGroup.setText(userClass.bgrp+"");




            }

        }.execute();

    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar userAge = new GregorianCalendar(year,monthOfYear,dayOfMonth);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                if (minAdultAge.before(userAge))
                {
                    Toast.makeText(ProfileActivity.this,"Please Enter Valid Date",Toast.LENGTH_LONG).show();
                } else {
                    bdate.setText(dateFormatter.format(userAge.getTime()));
                }

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
            toolbar.setTitle("Profile");
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        private ArrayList<Area> asr;

        public TimeSpinnerAdapter(Context context,ArrayList<Area> asr) {
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
            TextView txt = new TextView(ProfileActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position).city);
            txt.setTextColor(Color.parseColor("#494949"));
            return  txt;

        }

        public View getView(int position
                , View view, ViewGroup viewgroup) {
            TextView txt = new TextView(ProfileActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(16);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_spinner, 0);
            txt.setText(asr.get(position).city);
            txt.setTextColor(Color.parseColor("#ffffff"));
            return  txt;
        }
    }

    public static boolean isEmailPattern(EditText param1) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
    }

    public static boolean isPasswordMatch(EditText param1, EditText param2) {
        boolean isMatch = false;
        if (param1.getText().toString().equals(param2.getText().toString())) {
            isMatch = true;
        }
        return isMatch;
    }

    public class TimeSpinnerAdapter1 extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public TimeSpinnerAdapter1(Context context,ArrayList<String> asr) {
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
            TextView txt = new TextView(ProfileActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#494949"));
            return  txt;

        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(ProfileActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(16);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_spinner, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#ffffff"));
            return  txt;
        }
    }


    public int loginCall(String c1)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo p1=new PropertyInfo();
        p1.setName("email");
        p1.setValue(c1);
        p1.setType(String.class);
        request.addProperty(p1);



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
        return Integer.parseInt(response.toString());
    }


}
