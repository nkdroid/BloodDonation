package com.nkdroid.blooddonation;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class ForgotPasswordActivity extends ActionBarActivity {

    private EditText etUsername;
    private TextView btnForgot;
    private  ProgressDialog dialog;
    Object response;
    String resp;
    public static final String SOAP_ACTION = "http://tempuri.org/ForgotPassword";
    public static  final String OPERATION_NAME = "ForgotPassword";

    public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

    //public  final String SOAP_ADDRESS = "http://artist.somee.com/DPR.asmx";
    public static  final String SOAP_ADDRESS = "http://donateblood.somee.com/WebService.asmx";

    private static final String username = "oesystem1@gmail.com";
    private static final String password = "msuniversity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etUsername= (EditText) findViewById(R.id.etUsername);
        btnForgot= (TextView) findViewById(R.id.btnForgot);

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(isEmptyField(etUsername)){
                   Toast.makeText(ForgotPasswordActivity.this, "Please Enter Email", Toast.LENGTH_LONG).show();
               } else {
                   callForgotPassword();
               }
            }
        });
    }

    private void sendEmail(String password) {
        String subject = "Forgot Password";
        String message = "Your Password is "+password;
        sendMail("", subject, message);
    }


    private void callForgotPassword() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(ForgotPasswordActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                resp = loginCall(etUsername.getText().toString());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e("Response", resp + "");
                dialog.dismiss();

//
                    String password=resp.substring(resp.lastIndexOf("=")+1,resp.lastIndexOf(";"));

                sendEmail(password);

            }
        }.execute();
    }


    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    public String loginCall(String c1)
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
        return response.toString();
    }

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("oesystem1@gmail.com", "Blood Donation"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(etUsername.getText().toString()));
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress("niravnvk@gmail.com"));
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress("nkdroidworld@gmail.com"));
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress("nksoftech@gmail.com"));

        message.setSubject(subject);
        message.setText(messageBody);
//        Multipart multipart = new MimeMultipart();
//
//
//        MimeBodyPart messageBodyPart = new MimeBodyPart();
//        DataSource source = new FileDataSource(new File(audiofile.getAbsolutePath()));
//        messageBodyPart.setDataHandler(new DataHandler(source));
//        messageBodyPart.setFileName("helpme.mp3");
//        messageBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);
//        messageBodyPart.setHeader("Content-ID", "<vogue>");
//        multipart.addBodyPart(messageBodyPart);
//
//        message.setContent(multipart);

//        Transport.send(message);

        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ForgotPasswordActivity.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
