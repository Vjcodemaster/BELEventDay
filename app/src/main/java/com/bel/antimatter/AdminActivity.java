package com.bel.antimatter;

import androidx.appcompat.app.AppCompatActivity;
import app_utility.SharedPreferencesClass;
import app_utility.StaticReferenceClass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class AdminActivity extends AppCompatActivity {
    TextInputLayout etAdminID, etAdminPassword;
    Button btnLogin;
    SharedPreferencesClass sharedPreferencesClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initViews();
        sharedPreferencesClass = new SharedPreferencesClass(AdminActivity.this);
        initClickEvents();
    }

    private void initViews(){
        etAdminID = findViewById(R.id.et_admin_id);
        etAdminPassword = findViewById(R.id.et_admin_password);
        btnLogin = findViewById(R.id.btn_admin_login);
    }

    private void initClickEvents(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin();
            }
        });
    }

    private void validateLogin(){
        String ID = etAdminID.getEditText().getText().toString().trim();
        String Password = etAdminPassword.getEditText().getText().toString().trim();
        if(ID.equals(StaticReferenceClass.ADMIN_ID)){
            if(Password.equals(StaticReferenceClass.ADMIN_PASSWORD)){
                Intent registerActivityIn = new Intent(AdminActivity.this, RegisterEmployeeActivity.class);
                startActivity(registerActivityIn);
                sharedPreferencesClass.setUserType(0, true); //admin user
                finish();
            } else {
                Toast.makeText(AdminActivity.this, "Invalid Password", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(AdminActivity.this, "Invalid ID", Toast.LENGTH_LONG).show();
        }
    }

    /*public void onClick(View view) {

    }*/
}
