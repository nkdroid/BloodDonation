package com.nkdroid.blooddonation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
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

import com.nkdroid.blooddonation.model.Inbox;
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


public class FragmentOne extends Fragment {
    private  ProgressDialog dialog;
    Object response;

    UserClass userClass;
    private ListView searchRequest;
    private ArrayList<Inbox> inboxRecivedList;
    public static final String SOAP_ACTION1 = "http://tempuri.org/InboxSent";
    public static  final String OPERATION_NAME1 = "InboxSent";

    public static  final String WSDL_TARGET_NAMESPACE1 = "http://tempuri.org/";

    //public  final String SOAP_ADDRESS = "http://artist.somee.com/DPR.asmx";
    public static  final String SOAP_ADDRESS1 = "http://donateblood.somee.com/WebService.asmx";
    public static FragmentOne newInstance(String param1, String param2) {
        FragmentOne fragment = new FragmentOne();

        return fragment;
    }

    public FragmentOne() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userClass=PrefUtils.getCurrentUser(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView= inflater.inflate(R.layout.fragment_fragment_one, container, false);
        searchRequest= (ListView) convertView.findViewById(R.id.searchRequest);
        getInboxReceived();
        return convertView;
    }

    private void getInboxReceived() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(getActivity());
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE1,OPERATION_NAME1);
                PropertyInfo p1=new PropertyInfo();
                p1.setName("email");
                p1.setValue(userClass.email);
                p1.setType(String.class);
                request.addProperty(p1);
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
                    Log.e("response sent: ", response.toString() + "");
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
                try {

                    Log.e("response: ", response.toString() + "");
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), response.toString() + "", Toast.LENGTH_LONG).show();
                }
                StringTokenizer tokens = new StringTokenizer(response.toString(), "=");
                List<String> mylist=new ArrayList<String>();
                for(int i=0;tokens.hasMoreTokens();i++){
                    StringTokenizer tokens1 = new StringTokenizer(tokens.nextToken(), ";");

                    mylist.add(tokens1.nextToken());
                }
                mylist.remove(0);

                int partitionSize =5;
                List<List<String>> partitions = new LinkedList<List<String>>();
                for (int i = 0; i < mylist.size(); i += partitionSize) {
                    partitions.add(mylist.subList(i,
                            i + Math.min(partitionSize, mylist.size() - i)));
                }

                inboxRecivedList=new ArrayList<Inbox>();
                for(int k=0;k<partitions.size();k++){
                    inboxRecivedList.add(new Inbox(partitions.get(k).get(0),partitions.get(k).get(1),partitions.get(k).get(2),partitions.get(k).get(3),partitions.get(k).get(4)));
                }


                PostAdapter postAdapter=new PostAdapter(getActivity(),inboxRecivedList);
                searchRequest.setAdapter(postAdapter);
                Log.e("inboxSent size: ", inboxRecivedList.size() + "");
            }catch (Exception e){
                e.printStackTrace();
            }

            }

        }.execute();
    }

    public class PostAdapter extends BaseAdapter {

        Context context;
        ArrayList<Inbox> postArrayList;



        public PostAdapter(Context context, ArrayList<Inbox> postArrayList) {

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
            holder.txtPostTitle.setText(postArrayList.get(position).Sender_UserId);
            holder.mobile.setText(postArrayList.get(position).Date_of_Request);
            holder.address.setText(postArrayList.get(position).Required_BloodGroup);
            holder.email.setText(postArrayList.get(position).Area+" "+postArrayList.get(position).City);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return convertView;
        }

    }

}
