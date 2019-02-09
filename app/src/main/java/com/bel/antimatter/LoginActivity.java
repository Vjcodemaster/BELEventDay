package com.bel.antimatter;

import androidx.appcompat.app.AppCompatActivity;
import app_utility.BELAsyncTask;
import app_utility.OnAsyncInterfaceListener;
import app_utility.SharedPreferencesClass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements OnAsyncInterfaceListener {

    Button btnLogin;
    TextInputLayout etID;
    SharedPreferencesClass sharedPreferencesClass;
    public static OnAsyncInterfaceListener onAsyncInterfaceListener;
    private LottieAnimationView lottieAnimationView;
    ArrayList<String> alStallNames;
    ImageButton ibAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initClasses();

        initView();
        initClickEvents();
    }

    private void initClasses() {
        sharedPreferencesClass = new SharedPreferencesClass(LoginActivity.this);
        if (sharedPreferencesClass.getUserLogStatus()) {
            if (sharedPreferencesClass.getUserType() == 0) {
                Intent inRegisterActivity = new Intent(LoginActivity.this, RegisterEmployeeActivity.class);
                startActivity(inRegisterActivity);
            } else {
                startMainActivity();
            }
            finish();
        }
        onAsyncInterfaceListener = this;
        alStallNames = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.stall_names)));
    }

    private void initView() {
        btnLogin = findViewById(R.id.btn_stall_login);
        etID = findViewById(R.id.et_stall_id);
        lottieAnimationView = findViewById(R.id.lottie_view);
        ibAdmin = findViewById(R.id.ib_admin);

    }

    private void initClickEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sID = etID.getEditText().getText().toString().toLowerCase().trim();

                if (sID.length() <= 4) {
                    Toast.makeText(LoginActivity.this, "Invalid ID", Toast.LENGTH_LONG).show();
                } else {
                    if (alStallNames.contains(sID)) {
                        startMainActivity();
                        sharedPreferencesClass.setUserLogStatus(true, sID);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid ID, Contact Admin", Toast.LENGTH_LONG).show();
                    }
                    /*BELAsyncTask belAsyncTask = new BELAsyncTask(LoginActivity.this);
                    belAsyncTask.execute(String.valueOf(5), sID);*/
                }
            }
        });
        ibAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminActivity = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(adminActivity);
            }
        });
    }

    @Override
    public void onAsyncComplete(String sMSG, int type, String sResult) {
        switch (sMSG) {
            case "LOGIN_SUCCESS":
                startMainActivity();
                break;
        }
    }

    private void startMainActivity() {
        Intent inMainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(inMainActivity);
    }
}
