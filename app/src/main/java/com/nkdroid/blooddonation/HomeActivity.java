package com.nkdroid.blooddonation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nkdroid.blooddonation.model.Area;
import com.nkdroid.blooddonation.model.Notification;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;


public class HomeActivity extends ActionBarActivity implements BaseSliderView.OnSliderClickListener {
    private Toolbar toolbar;
    private ProgressDialog dialog;
    private SliderLayout mDemoSlider;
    private Button btnLogout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private GoogleCloudMessaging gcm;
    private String regid;
    Object response;
    private ListView postListView;
    private ArrayList<Notification> postArrayList;
    private PostAdapter postAdapter;
    public static final String SOAP_ACTION = "http://tempuri.org/getNotice";
    public static  final String OPERATION_NAME = "getNotice";
    public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public static  final String SOAP_ADDRESS = "http://donateblood.somee.com/WebService.asmx";


    private String PROJECT_NUMBER = "92884720384";

    //    private ArrayAdapter<String> navigationDrawerAdapter;
    private String[] leftSliderData = {"Search",
            "Profile", "Inbox", "Request", "About Us", "Contact Us", "Settings", "Logout"};
    private int[] imagelist = {R.drawable.ic_action_search,
            R.drawable.ic_social_person,
            R.drawable.ic_social_plus_one,
            R.drawable.ic_action_invert_colors,
            R.drawable.ic_social_people_outline,
            R.drawable.ic_communication_phone,
            R.drawable.ic_action_settings_applications,
            R.drawable.ic_action_launch};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setToolbar();
        initView();
        initDrawer();
        getRegId();
        setImageSlider();
//        notify_list();
        getNotifications();
    }

    public void notify_list()
    {

    }


    public class PostAdapter extends BaseAdapter {

        Context context;
        ArrayList<Notification> postArrayList;



        public PostAdapter(Context context, ArrayList<Notification> postArrayList) {

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
            TextView txtPostTitle,txtPostDate,txtPostDescription,city;

        }

        public View getView(final int position, View convertView,ViewGroup parent) {

            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.activity_notification, parent, false);
                holder = new ViewHolder();
                holder.txtPostTitle = (TextView) convertView.findViewById(R.id.title);
                holder.txtPostDate = (TextView) convertView.findViewById(R.id.date);
                holder.txtPostDescription = (TextView) convertView.findViewById(R.id.description);
                holder.city = (TextView) convertView.findViewById(R.id.city);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtPostTitle.setText(postArrayList.get(position).Title);
            holder.txtPostDate.setText(postArrayList.get(position).End_Date);
            holder.txtPostDescription.setText(postArrayList.get(position).Description);
            holder.city.setText(postArrayList.get(position).City);

            return convertView;
        }

    }



    private void setImageSlider() {
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);


     //   HashMap<String, String> url_maps = new HashMap<String, String>();
      //  url_maps.put("Image-1", "http://www.google.co.in/imgres?imgurl=http://previews.123rf.com/images/gigello/gigello1306/gigello130600041/20667270-Blood-donation-Medical-background-Stock-Vector.jpg&imgrefurl=http://www.123rf.com/photo_20667270_blood-donation-medical-background.html&h=1300&w=1300&tbnid=iIQ0C_qyCBxPIM:&zoom=1&docid=mpvmP3uuTlJIWM&ei=kTcRVcPnFZHk8AX6-ILYCQ&tbm=isch&ved=0CGkQMyhDMEM");
      //  url_maps.put("Image-2", "http://www.google.co.in/imgres?imgurl=http://www.blufftonicon.com/sites/default/files/images/articles/2013/7385-next-red-cross-blood-drive-feb.7-first-mennonite.jpeg&imgrefurl=http://www.blufftonicon.com/news/2013/01/18/next-red-cross-blood-drive-feb-7-first-mennonite&h=151&w=335&tbnid=Iojc6hiEZpiBfM:&zoom=1&docid=idgkUKE_uAm1vM&ei=kTcRVcPnFZHk8AX6-ILYCQ&tbm=isch&ved=0CEwQMygmMCY");
      //  url_maps.put("Image-3", "http://www.google.co.in/imgres?imgurl=http://www.karaikudiblooddonors.com/images/donor-icon.gif&imgrefurl=http://www.karaikudiblooddonors.com/viewrequester.php?id%3D18&h=60&w=197&tbnid=bwOFb9E-TC12fM:&zoom=1&docid=hBq_aFYVQusujM&ei=kTcRVcPnFZHk8AX6-ILYCQ&tbm=isch&ved=0CGIQMyg8MDw");
      //  url_maps.put("Image-4", "http://www.google.co.in/imgres?imgurl=http://files.smashingmagazine.com/wallpapers/june-13/someone-needing-blood-somewhere/jun-13-donate_blood-preview.jpg&imgrefurl=http://www.nebraskadigital.com/category/smashing/page/34/&h=281&w=500&tbnid=oTMrEPfnHNhgHM:&zoom=1&docid=XEQo-4i7jlJzVM&ei=kTcRVcPnFZHk8AX6-ILYCQ&tbm=isch&ved=0CHgQMyhSMFI");


        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("image-1", R.drawable.image1);
        file_maps.put("image-2", R.drawable.image2);
        file_maps.put("image-3", R.drawable.image3);
        file_maps.put("image-4", R.drawable.image4);


        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);


            //add your extra information
            textSliderView.getBundle()
                    .putString("extra", name);


            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
    }

    private void initView() {

    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("HOME");
            setSupportActionBar(toolbar);
        }
    }

    private void initDrawer() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        leftDrawerList.setAdapter(new DrawerAdapter(HomeActivity.this, leftSliderData));
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public class DrawerAdapter extends BaseAdapter {

        Context context;
        String[] arraylist;

        public DrawerAdapter(Context context, String[] drawerTitleListValue) {
            this.context = context;
            arraylist = drawerTitleListValue;
        }

        public int getCount() {
            return arraylist.length;
        }

        public Object getItem(int position) {
            return arraylist[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            TextView title;
            ImageView imgIcon;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_drawer, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.txtTitle);
                holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(arraylist[position]);
            holder.imgIcon.setImageResource(imagelist[position]);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(intent);
                    }else if (position == 1) {
                        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }else if (position == 2) {
                        Intent intent = new Intent(HomeActivity.this, InboxActivity.class);
                        startActivity(intent);
                    }else if (position == 3) {
                        Intent intent = new Intent(HomeActivity.this, RequestActivity.class);
                        startActivity(intent);
                    }else if (position == 4) {
                        Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                    }else if (position == 5) {
                        Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
                        startActivity(intent);
                    }else if (position == 6) {
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                    }else if (position == 7) {
                        PrefUtils.clearLogin(HomeActivity.this);
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
            return convertView;
        }
    }


    public void getRegId(){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(HomeActivity.this);
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    Log.e("GCM ID :", regid);

                    if(regid==null || regid==""){
                        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                        alert.setTitle("Error");
                        alert.setMessage("Internal Server Error");
                        alert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getRegId();
                                dialog.dismiss();
                            }
                        });
                        alert.setNegativeButton("exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        alert.show();
                    } else {
                        // Store GCM ID in sharedpreference
                        SharedPreferences sharedPreferences=getSharedPreferences("GCM",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("GCM_ID",regid);
                        editor.commit();

                        // TODO further logic

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }.execute();

    } // end of getRegId

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        Toast.makeText(this, baseSliderView.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }


    private void getNotifications() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(HomeActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
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
                    Toast.makeText(HomeActivity.this,response.toString()+"",Toast.LENGTH_LONG).show();
                }
                StringTokenizer tokens = new StringTokenizer(response.toString(), "=");
                List<String> mylist=new ArrayList<String>();
                for(int i=0;tokens.hasMoreTokens();i++){
                    StringTokenizer tokens1 = new StringTokenizer(tokens.nextToken(), ";");

                    mylist.add(tokens1.nextToken());
                }
                mylist.remove(0);

                final int partitionSize =4;
                List<List<String>> partitions = new LinkedList<List<String>>();
                for (int i = 0; i < mylist.size(); i += partitionSize) {
                    partitions.add(mylist.subList(i,
                            i + Math.min(partitionSize, mylist.size() - i)));
                }

                postArrayList=new ArrayList<Notification>();
                for(int k=0;k<partitions.size();k++){
                    postArrayList.add(new Notification(partitions.get(k).get(0),partitions.get(k).get(1),partitions.get(k).get(2),partitions.get(k).get(3)));
                }


                postListView=(ListView)findViewById(R.id.listView);
                postAdapter=new PostAdapter(HomeActivity.this,postArrayList);
                postListView.setAdapter(postAdapter);

            }

        }.execute();

    }

}
