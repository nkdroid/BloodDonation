package com.nkdroid.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private EditText etUsername,etPassword;
    private TextView btnNewRegistration;
    private TextView btnLogin;
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
        btnNewRegistration= (TextView) findViewById(R.id.btnNewRegistration);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmptyField(etUsername)){
                    Toast.makeText(LoginActivity.this,"Please Enter Username",Toast.LENGTH_LONG).show();
                } else if(isEmptyField(etPassword)){
                    Toast.makeText(LoginActivity.this,"Please Enter Password",Toast.LENGTH_LONG).show();
                } else {
                    // check from shared preference
                    User user=PrefUtils.getLoggedIn(LoginActivity.this);
                    PrefUtils.setLoggedIn(LoginActivity.this,true,user.getUsername(),user.getPassword());
                    Log.e("username", user.getUsername()+"");
                   // if( user.getUsername().equals(etUsername.getText().toString().trim()) && user.getPassword().equals(etPassword.getText().toString().trim())){
                        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    //} else {
                      //  Toast.makeText(LoginActivity.this,"Please Enter Valid Username or Password",Toast.LENGTH_LONG).show();
                    //}

                }
            }
        });

        btnNewRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);

                startActivity(intent);
                finish();
            }
        });


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
