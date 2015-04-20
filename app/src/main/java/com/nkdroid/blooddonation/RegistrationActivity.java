package com.nkdroid.blooddonation;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;


public class RegistrationActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private ProgressDialog dialog;
    int resp;
    String c1;
    public static final String SOAP_ACTION = "http://tempuri.org/CheckUniqueEmailId";
    public static  final String OPERATION_NAME = "CheckUniqueEmailId";
    public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
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
    private Spinner ecity,earea;
    private String sname,sbdate,sgender,saddress,sweight,scontact,semai,scity,sarea,spasswd,cspass,NAME;
    private TextView btnContinue;
    private  RadioGroup rg;
    private DatePickerDialog fromDatePickerDialog;
    Object response;
    private SimpleDateFormat dateFormatter;
    Spinner spcity;
    Spinner sparea;
    public RegistrationActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setToolbar();


//        city.add("Vadodara");
//
//        area.add("Karelibaugh");
//        area.add("Vaghodia");
//        area.add("Subhanpura");
//        area.add("Alkapuri");
//        area.add("O.P Road");
//        area.add("Nyaymandir");

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(RegistrationActivity.this);
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
                    Toast.makeText(RegistrationActivity.this,response.toString()+"",Toast.LENGTH_LONG).show();
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
                }else if (!isEmailPattern(email)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valid E-Mail", Toast.LENGTH_LONG).show();
                }else if (isEmptyField(password)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(cpass)) {
                    Toast.makeText(RegistrationActivity.this, "Please Confirm Password", Toast.LENGTH_LONG).show();
                } else if (!isPasswordMatch(cpass,password)) {
                    Toast.makeText(RegistrationActivity.this, "Password doesn't match", Toast.LENGTH_LONG).show();
                }
                else {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            dialog = new ProgressDialog(RegistrationActivity.this);
                            dialog.setMessage("Loading...");
                            dialog.show();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            resp = loginCall(email.getText().toString().trim());
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Log.e("Response",resp+"");
                            dialog.dismiss();
                            if(resp==1)
                            {
                                UserClass userClass=new UserClass();
                                userClass.name=name.getText().toString();
                                userClass.dob=bdate.getText().toString();
                                int selectedId = rg.getCheckedRadioButtonId();
                                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                                userClass.gender=radioButton.getText().toString();
                                userClass.address=address.getText().toString();
                                userClass.city=city.get(ecity.getSelectedItemPosition());
                                userClass.area=area.get(earea.getSelectedItemPosition()).area;
                                userClass.weight=weight.getText().toString();
                                userClass.contact=contact.getText().toString();
                                userClass.email=email.getText().toString();
                                userClass.password=password.getText().toString();

                                Log.e("name",userClass.name+"");
                                Log.e("dob",userClass.dob+"");
                                Log.e("gender",userClass.gender+"");
                                Log.e("address",userClass.address+"");
                                Log.e("city",userClass.city+"");
                                Log.e("area",userClass.area+"");
                                Log.e("weight",userClass.weight+"");
                                Log.e("contact",userClass.contact+"");
                                Log.e("email",userClass.email+"");
                                Log.e("password",userClass.password+"");


                                PrefUtils.setCurrentUser(userClass,RegistrationActivity.this);

                                Intent intent = new Intent(RegistrationActivity.this, RegistrationActivity2.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Email Already Exist", Toast.LENGTH_LONG).show();
                            }
                        }
                    }.execute();

                }
            }
        });

    }

    private void callGetArea() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(RegistrationActivity.this);
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
                    Toast.makeText(RegistrationActivity.this,response.toString()+"",Toast.LENGTH_LONG).show();
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


                TimeSpinnerAdapter1 tspcity=new TimeSpinnerAdapter1(RegistrationActivity.this,city);
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
                        TimeSpinnerAdapter tsparea = new TimeSpinnerAdapter(RegistrationActivity.this, tempList);
                        sparea.setAdapter(tsparea);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

        }.execute();

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
            TextView txt = new TextView(RegistrationActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position).city);
            txt.setTextColor(Color.parseColor("#494949"));
            return  txt;

        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(RegistrationActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(16);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_spinner, 0);
            txt.setText(asr.get(i).city);
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
