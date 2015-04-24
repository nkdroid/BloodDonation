package com.nkdroid.blooddonation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
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


public class SearchActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ListView postListView;
    private ArrayList<String> postArrayList;
    private PostAdapter postAdapter;
    private ProgressDialog dialog;
    Object response;
    private ArrayList<BloodBank> bankList;
    public static final String SOAP_ACTION1 = "http://tempuri.org/SearchBloodBank";
    public static  final String OPERATION_NAME1 = "SearchBloodBank";
    public static  final String WSDL_TARGET_NAMESPACE1 = "http://tempuri.org/";
    public static  final String SOAP_ADDRESS1 = "http://donateblood.somee.com/WebService.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setToolbar();
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(SearchActivity.this);
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
                    Toast.makeText(SearchActivity.this, response.toString() + "", Toast.LENGTH_LONG).show();
                }
                StringTokenizer tokens = new StringTokenizer(response.toString(), "=");
                List<String> mylist=new ArrayList<String>();
                for(int i=0;tokens.hasMoreTokens();i++){
                    StringTokenizer tokens1 = new StringTokenizer(tokens.nextToken(), ";");

                    mylist.add(tokens1.nextToken());
                }
                mylist.remove(0);

                int partitionSize =7;
                List<List<String>> partitions = new LinkedList<List<String>>();
                for (int i = 0; i < mylist.size(); i += partitionSize) {
                    partitions.add(mylist.subList(i,
                            i + Math.min(partitionSize, mylist.size() - i)));
                }

                bankList=new ArrayList<BloodBank>();
                for(int k=0;k<partitions.size();k++){
                    bankList.add(new BloodBank(partitions.get(k).get(0),partitions.get(k).get(1),partitions.get(k).get(2),partitions.get(k).get(3),partitions.get(k).get(4),partitions.get(k).get(5),partitions.get(k).get(6)));
                }

                postListView=(ListView)findViewById(R.id.listView);
                postAdapter=new PostAdapter(SearchActivity.this,bankList);
                postListView.setAdapter(postAdapter);


            }

        }.execute();

    }

    public class PostAdapter extends BaseAdapter {

        Context context;
        ArrayList<BloodBank> postArrayList;
        ArrayList<BloodBank> arraylist;


        public PostAdapter(Context context, ArrayList<BloodBank> postArrayList) {

            this.context = context;
            this.postArrayList = postArrayList;
            arraylist = new ArrayList<BloodBank>();
            arraylist.addAll(postArrayList);
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
            TextView txtPostTitle,txtPostDate,txtPostDescription;

        }

        public View getView(final int position, View convertView,ViewGroup parent) {

            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_notificationview, parent, false);
                holder = new ViewHolder();
                holder.txtPostTitle = (TextView) convertView.findViewById(R.id.textView);
                holder.txtPostDate = (TextView) convertView.findViewById(R.id.textView2);
                holder.txtPostDescription = (TextView) convertView.findViewById(R.id.textView3);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtPostTitle.setText(postArrayList.get(position).Name);
//            holder.txtPostDate.setText(postArrayList.get(position).getPost_date());
//            holder.txtPostDescription.setText(postArrayList.get(position).getDescription());


            return convertView;
        }
        // Filter Class
        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());

            postArrayList.clear();
            if (charText.length() == 0) {
                postArrayList.addAll(arraylist);

            } else {
                for (BloodBank movieDetails : arraylist) {
                    if (charText.length() != 0 && movieDetails.Name.toLowerCase(Locale.getDefault()).contains(charText)) {
                        postArrayList.add(movieDetails);
                    }

                }
            }
            notifyDataSetChanged();
        }
    }


    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            toolbar.setTitle("Search");
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(MainActivity.this, "called", Toast.LENGTH_SHORT).show();



                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                postAdapter.filter(searchQuery.toString().trim());
                postListView.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_bar) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
