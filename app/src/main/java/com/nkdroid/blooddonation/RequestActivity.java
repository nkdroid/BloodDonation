package com.nkdroid.blooddonation;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nkdroid.blooddonation.model.UserClass;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;


public class RequestActivity extends ActionBarActivity {
    Object response;
    Spinner spbloodgroup,spcity;
    ArrayList<String> city=new ArrayList<>();
    private Toolbar toolbar;
    ArrayList<String> bgrp=new ArrayList<>();
    private ProgressDialog dialog;
    public static final String SOAP_ACTION1 = "http://tempuri.org/getcity";
    public static  final String OPERATION_NAME1 = "getcity";
    public static  final String WSDL_TARGET_NAMESPACE1 = "http://tempuri.org/";
    public static  final String SOAP_ADDRESS1 = "http://donateblood.somee.com/WebService.asmx";

    public static final String SOAP_ACTION = "http://tempuri.org/SearchUsers";
    public static  final String OPERATION_NAME = "SearchUsers";
    public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public static  final String SOAP_ADDRESS = "http://donateblood.somee.com/WebService.asmx";
    private TextView btnreq;
    private ArrayList<UserClass> searchUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        setToolbar();
        btnreq= (TextView) findViewById(R.id.btnreq);
        btnreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUsers();
            }
        });
        bgrp.add("A+");
        bgrp.add("A-");
        bgrp.add("B+");
        bgrp.add("B-");
        bgrp.add("AB+");
        bgrp.add("AB-");
        bgrp.add("O+");
        bgrp.add("O-");

        spcity = (Spinner) findViewById(R.id.spcity);
         spbloodgroup = (Spinner) findViewById(R.id.spbg);

        TimeSpinnerAdapter tsp=new TimeSpinnerAdapter(RequestActivity.this,bgrp);
        spbloodgroup.setAdapter(tsp);

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(RequestActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE1,OPERATION_NAME1);

                PropertyInfo p1=new PropertyInfo();
                p1.setName("bgroup");
                p1.setValue("A+");
                p1.setType(String.class);
                request.addProperty(p1);

                PropertyInfo p2=new PropertyInfo();
                p2.setName("city");
                p2.setValue("Baroda");
                p2.setType(String.class);
                request.addProperty(p2);
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
                    Toast.makeText(RequestActivity.this, response.toString() + "", Toast.LENGTH_LONG).show();
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
                TimeSpinnerAdapter1 tspcity=new TimeSpinnerAdapter1(RequestActivity.this,city);
                spcity.setAdapter(tspcity);

            }

        }.execute();
        

        }

    private void searchUsers() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(RequestActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

                PropertyInfo p1=new PropertyInfo();
                p1.setName("bgroup");
                p1.setValue("A+");
                p1.setType(String.class);
                request.addProperty(p1);

                PropertyInfo p2=new PropertyInfo();
                p2.setName("city");
                p2.setValue("Baroda");
                p2.setType(String.class);
                request.addProperty(p2);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
                httpTransport.debug=true;
                try {
                    httpTransport.call(SOAP_ACTION, envelope);
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
                    Toast.makeText(RequestActivity.this, response.toString() + "", Toast.LENGTH_LONG).show();
                }
                StringTokenizer tokens = new StringTokenizer(response.toString(), "=");
                List<String> mylist=new ArrayList<String>();
                for(int i=0;tokens.hasMoreTokens();i++){
                    StringTokenizer tokens1 = new StringTokenizer(tokens.nextToken(), ";");

                    mylist.add(tokens1.nextToken());
                }
                mylist.remove(0);

                int partitionSize =20;
                List<List<String>> partitions = new LinkedList<List<String>>();
                for (int i = 0; i < mylist.size(); i += partitionSize) {
                    partitions.add(mylist.subList(i,
                            i + Math.min(partitionSize, mylist.size() - i)));
                }

                searchUsersList=new ArrayList<UserClass>();
                for(int k=0;k<partitions.size();k++){
                    searchUsersList.add(new UserClass(partitions.get(k).get(0),partitions.get(k).get(1),partitions.get(k).get(2),partitions.get(k).get(3),partitions.get(k).get(4),partitions.get(k).get(5),partitions.get(k).get(6),partitions.get(k).get(7),partitions.get(k).get(8),partitions.get(k).get(9),partitions.get(k).get(10),partitions.get(k).get(11),partitions.get(k).get(12),partitions.get(k).get(13),partitions.get(k).get(14),partitions.get(k).get(15),partitions.get(k).get(16),partitions.get(k).get(17),partitions.get(k).get(18),partitions.get(k).get(19)));
                }

                Log.e("search result",searchUsersList.size()+"");

            }

        }.execute();

    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            toolbar.setTitle("Request Blood");
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
            TextView txt = new TextView(RequestActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#494949"));
            return  txt;



        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(RequestActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(16);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_spinner, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#ffffff"));
            return  txt;
        }
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
            TextView txt = new TextView(RequestActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#494949"));
            return  txt;

        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(RequestActivity.this);
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
