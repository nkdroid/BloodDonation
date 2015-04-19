package com.nkdroid.blooddonation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

    public class LoginActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private EditText etUsername,etPassword;
    private TextView btnNewRegistration;
    private TextView btnLogin;
        private  ProgressDialog dialog;

        String uname,passwd;
        int resp;
        public static final String SOAP_ACTION = "http://tempuri.org/UserLogin";
        public static  final String OPERATION_NAME = "UserLogin";

        public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

        //public  final String SOAP_ADDRESS = "http://artist.somee.com/DPR.asmx";
        public static  final String SOAP_ADDRESS = "http://donateblood.somee.com/WebService.asmx";

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //check isLogin or not
        if(PrefUtils.isLoggedIn(LoginActivity.this)){
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

        setToolbar();
        initView();
    }

    private void initView() {
        etUsername= (EditText) findViewById(R.id.etUsername);
        etPassword= (EditText) findViewById(R.id.etPassword);
        btnLogin= (TextView) findViewById(R.id.btnLogin);
        btnNewRegistration=(TextView)findViewById(R.id.btnNewRegistration);
        btnNewRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyField(etUsername)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Username", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(etPassword)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                } else {
                    uname = etUsername.getText().toString();
                    passwd = etPassword.getText().toString();
                    // if( user.getUsername().equals(etUsername.getText().toString().trim()) && user.getPassword().equals(etPassword.getText().toString().trim())){

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            dialog = new ProgressDialog(LoginActivity.this);
                            dialog.setMessage("Loading...");
                            dialog.show();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            resp = loginCall(uname,passwd);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Log.e("Response",resp+"");
                            dialog.dismiss();
                            if(resp==1)
                            {
                                User user = PrefUtils.getLoggedIn(LoginActivity.this);
                                PrefUtils.setLoggedIn(LoginActivity.this, true, user.getUsername(), user.getPassword());
                                Log.e("username", user.getUsername() + "");

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Authentication Error"+resp, Toast.LENGTH_LONG).show();
                            }
                        }
                    }.execute();


                }
            }
        });
 }

        public int loginCall(String c1, String c2)
        {
            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
            PropertyInfo p1=new PropertyInfo();
            p1.setName("email");
            p1.setValue(c1);
            p1.setType(String.class);
            request.addProperty(p1);

            PropertyInfo p2=new PropertyInfo();
            p2.setName("passwd");
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
            return Integer.parseInt(response.toString());
        }



        private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Login");
            setSupportActionBar(toolbar);
        }
    }

    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

}
