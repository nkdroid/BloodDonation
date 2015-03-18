package com.nkdroid.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


public class HomeActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setToolbar();
        initView();
    }

    private void initView() {
        btnLogout= (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove sharedpreference
                PrefUtils.clearLogin(HomeActivity.this);
                Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("HOME");
            setSupportActionBar(toolbar);
        }
    }
}
