package com.nkdroid.blooddonation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;


public class RegistrationActivity2 extends ActionBarActivity implements ImageChooserListener,AdapterView.OnItemSelectedListener {

    ArrayList<String> bgrp=new ArrayList<>();
    private TextView txtButton;
    private ImageView imageView;
    private int chooserType;
    private String filePath;
    private String imageFilePath;
    private boolean isProfilePicAdded = false;
    private ImageChooserManager imageChooserManager;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private TextView btncontinue;
    private int f=0,t=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activity2);

        setToolbar();

        bgrp.add("A+");
        bgrp.add("A-");
        bgrp.add("B+");
        bgrp.add("B-");
        bgrp.add("AB+");
        bgrp.add("AB-");
        bgrp.add("O+");
        bgrp.add("O-");


        Spinner spbloodgroup = (Spinner) findViewById(R.id.spbg);
        spbloodgroup.setOnItemSelectedListener(this);
        TimeSpinnerAdapter tsp=new TimeSpinnerAdapter(RegistrationActivity2.this,bgrp);
        spbloodgroup.setAdapter(tsp);

        txtButton= (TextView) findViewById(R.id.txtButton);
        imageView= (ImageView) findViewById(R.id.imageView);
        registerForContextMenu(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContextMenu(imageView);
            }
        });
        txtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    progressDialog = new ProgressDialog(RegistrationActivity2.this);
                    progressDialog.setMessage("Uploading...");
                    progressDialog.show();
                    t = postImage();
            }

        });


        btncontinue=(TextView)findViewById(R.id.btncontinue1);

        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t==0) {
                    Toast.makeText(RegistrationActivity2.this, "Please Upload Proof", Toast.LENGTH_LONG).show();
                }
                else {

                    //store in shared preference
                    // PrefUtils.setLoggedIn(RegistrationActivity.this, true, etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
                    //Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);

                    // startActivity(intent);
                    Intent intent = new Intent(RegistrationActivity2.this,RegistrationActivity3.class);

                    startActivity(intent);

                }

            }
        });

    }




    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            toolbar.setTitle("Register");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegistrationActivity2.this, RegistrationActivity.class);

                    startActivity(intent);
//                    finish();
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegistrationActivity2.this, RegistrationActivity.class);

        startActivity(intent);
//        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, 1, Menu.NONE, "Camera");
        menu.add(0, 2, Menu.NONE, "Choose from gallery");
        if(isProfilePicAdded == false){
        }else {
            menu.add(0, 3, Menu.NONE, "Remove Picture");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 1:
                takePicture();
                break;
            case 2:
                chooseImage();
                break;
            case 3:
                displayNoneImage();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void displayNoneImage() {

        isProfilePicAdded = false;
        imageView.setImageResource(R.drawable.ic_none_profile);
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,ChooserType.REQUEST_PICK_PICTURE, "cabkab", true);
        imageChooserManager.setImageChooserListener(this);
        try {
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,ChooserType.REQUEST_CAPTURE_PICTURE, "cabkab", true);
        imageChooserManager.setImageChooserListener(this);
        try {
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (image != null) {
                    imageView.setImageURI(Uri.parse(new File(image.getFileThumbnail()).toString()));
                    imageFilePath = image.getFilePathOriginal().toString();
                }
            }
        });
    }

    @Override
    public void onError(String s) {

        displayNoneImage();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }

            isProfilePicAdded = true;
            imageChooserManager.submit(requestCode, data);
        } else if(resultCode == RESULT_CANCELED){

        }else{
            displayNoneImage();
        }
    }

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType,"cabkab", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }


    public int postImage() {


        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                Log.e("file path: ", imageFilePath + "");
                File imageFile = new File(imageFilePath);
                uploadFile(imageFile);

                return null;
            }
        } .execute();
        t=1;

        return t;
    }

    public void uploadFile(File fileName){

        Log.e("filename",fileName+"");
        FTPClient client = new FTPClient();

        try {
            client.connect("198.37.116.29",21);
            client.login("donateblood", "Donateblood1");
            client.setType(FTPClient.TYPE_AUTO);
            client.changeDirectory("/www.DEMOweb5.somee.com/images/");

            client.upload(fileName, new MyTransferListener());
            Log.e("filename",fileName+"");
        } catch (Exception e) {
            e.printStackTrace();

            try {
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    /*******  Used to file upload and show progress  **********/

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {

            Log.e("filename","Upload Started ");
            // Transfer started
//                Toast.makeText(getActivity(), " Upload Started ...", Toast.LENGTH_SHORT).show();
            System.out.println(" Upload Started ...");
        }

        public void transferred(int length) {
            System.out.println(" transferred ..." );
//                Toast.makeText(getActivity(), " transferred ..." + length, Toast.LENGTH_SHORT).show();
        }

        public void completed() {
            // Transfer completed
            System.out.println(" completed ...");
            progressDialog.dismiss();
            Toast.makeText(getApplication(), " completed ...", Toast.LENGTH_LONG).show();



        }

        public void aborted() {
            // Transfer aborted
            System.out.println(" transfer aborted ,please try again..." );
//                Toast.makeText(getActivity()," transfer aborted ,please try again...", Toast.LENGTH_SHORT).show();
        }

        public void failed() {
            // Transfer failed
            System.out.println(" failed ..." );
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
            TextView txt = new TextView(RegistrationActivity2.this);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#494949"));
            return  txt;



        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(RegistrationActivity2.this);
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
