package com.nkdroid.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegistrationActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private EditText etUsername,etPassword;
    private Button btnRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initView();
    }



    private void initView() {
        etUsername= (EditText) findViewById(R.id.etUsername);
        etPassword= (EditText) findViewById(R.id.etPassword);
        btnRegistration= (Button) findViewById(R.id.btnRegister);


        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyField(etUsername)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Username", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(etPassword)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                } else {

                    //store in shared preference
                    PrefUtils.setLoggedIn(RegistrationActivity.this, true, etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
                    Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);

                    startActivity(intent);
                    finish();

                }

            }
        });

    }

    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("REGISTER");
            setSupportActionBar(toolbar);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);

        startActivity(intent);
        finish();
    }
}
