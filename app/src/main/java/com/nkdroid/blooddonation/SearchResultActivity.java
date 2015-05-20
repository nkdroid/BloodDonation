package com.nkdroid.blooddonation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nkdroid.blooddonation.model.BloodBank;
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
import java.util.Locale;
import java.util.StringTokenizer;


public class SearchResultActivity extends ActionBarActivity {

    Object response;
    private ListView searchRequest;
    private ArrayList<UserClass> searchUsers;
    private PostAdapter postAdapter;
    public static final String SOAP_ACTION = "http://tempuri.org/AddRequest";
    public static  final String OPERATION_NAME = "AddRequest";
    public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public static  final String SOAP_ADDRESS = "http://donateblood.somee.com/WebService.asmx";
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchRequest= (ListView) findViewById(R.id.searchRequest);
        searchUsers=PrefUtils.getSearchUsers(SearchResultActivity.this).searchUserList;
        postAdapter=new PostAdapter(SearchResultActivity.this,searchUsers);
        searchRequest.setAdapter(postAdapter);

    }

    public class PostAdapter extends BaseAdapter {

        Context context;
        ArrayList<UserClass> postArrayList;



        public PostAdapter(Context context, ArrayList<UserClass> postArrayList) {

            this.context = context;
            this.postArrayList = postArrayList;

        }

        public int getCount() {
            return postArrayList.size();
        }

        public Object getItem(int position) {
            return postArrayList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }



        class ViewHolder {
            TextView txtPostTitle,mobile,address,email;

        }

        public View getView(final int position, View convertView,ViewGroup parent) {

            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_notificationview, parent, false);
                holder = new ViewHolder();
                holder.txtPostTitle = (TextView) convertView.findViewById(R.id.textView);
                holder.mobile = (TextView) convertView.findViewById(R.id.mobile);
                holder.address = (TextView) convertView.findViewById(R.id.address);
                holder.email = (TextView) convertView.findViewById(R.id.email);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtPostTitle.setText(postArrayList.get(position).name);
            holder.mobile.setText(postArrayList.get(position).contact);
            holder.address.setText(postArrayList.get(position).address);
            holder.email.setText(postArrayList.get(position).email);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequest(position);
                }
            });
            return convertView;
        }

    }

    private void sendRequest(final int position) {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(SearchResultActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
               
                PropertyInfo p1=new PropertyInfo();
                p1.setName("email");
                p1.setValue(PrefUtils.getCurrentUser(SearchResultActivity.this).email);
                p1.setType(String.class);
                request.addProperty(p1);

                PropertyInfo p2=new PropertyInfo();
                p2.setName("selectedUser_email");
                p2.setValue(searchUsers.get(position).email);
                p2.setType(String.class);
                request.addProperty(p2);

                PropertyInfo p3=new PropertyInfo();
                p3.setName("bgroup");
                p3.setValue(searchUsers.get(position).bgrp);
                p3.setType(String.class);
                request.addProperty(p3);

                PropertyInfo p4=new PropertyInfo();
                p4.setName("city");
                p4.setValue(searchUsers.get(position).city);
                p4.setType(String.class);
                request.addProperty(p4);

                PropertyInfo p5=new PropertyInfo();
                p5.setName("area");
                p5.setValue(searchUsers.get(position).area);
                p5.setType(String.class);
                request.addProperty(p5);

                PropertyInfo p6=new PropertyInfo();
                p6.setName("date");
                p6.setValue("11-10-2015");
                p6.setType(String.class);
                request.addProperty(p6);
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
                    Toast.makeText(SearchResultActivity.this, response.toString() + "", Toast.LENGTH_LONG).show();
                }
//                StringTokenizer tokens = new StringTokenizer(response.toString(), "=");
//                List<String> mylist=new ArrayList<String>();
//                for(int i=0;tokens.hasMoreTokens();i++){
//                    StringTokenizer tokens1 = new StringTokenizer(tokens.nextToken(), ";");
//
//                    mylist.add(tokens1.nextToken());
//                }
//                mylist.remove(0);
//
//                int partitionSize =20;
//                List<List<String>> partitions = new LinkedList<List<String>>();
//                for (int i = 0; i < mylist.size(); i += partitionSize) {
//                    partitions.add(mylist.subList(i,
//                            i + Math.min(partitionSize, mylist.size() - i)));
//                }
//
//                searchUsersList=new ArrayList<UserClass>();
//                for(int k=0;k<partitions.size();k++){
//                    searchUsersList.add(new UserClass(partitions.get(k).get(0),partitions.get(k).get(1),partitions.get(k).get(2),partitions.get(k).get(3),partitions.get(k).get(4),partitions.get(k).get(5),partitions.get(k).get(6),partitions.get(k).get(7),partitions.get(k).get(8),partitions.get(k).get(9),partitions.get(k).get(10),partitions.get(k).get(11),partitions.get(k).get(12),partitions.get(k).get(13),partitions.get(k).get(14),partitions.get(k).get(15),partitions.get(k).get(16),partitions.get(k).get(17),partitions.get(k).get(18),partitions.get(k).get(19)));
//                }
//                SearchUser searchUser=new SearchUser();
//                searchUser.searchUserList=searchUsersList;
//                PrefUtils.setSearchUsers(searchUser,SearchResultActivity.this);
//
//                Log.e("search result",searchUsersList.size()+"");
//
//                Intent i =new Intent(SearchResultActivity.this,SearchResultActivity.class);
//                startActivity(i);
            }

        }.execute();

    }

}
