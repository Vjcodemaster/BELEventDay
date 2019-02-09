package com.bel.antimatter;

import androidx.appcompat.app.AppCompatActivity;
import app_utility.BELAsyncTask;
import app_utility.OnAsyncInterfaceListener;
import app_utility.SharedPreferencesClass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements OnAsyncInterfaceListener {

    Button btnLogin;
    TextInputLayout etID;
    SharedPreferencesClass sharedPreferencesClass;
    public static OnAsyncInterfaceListener onAsyncInterfaceListener;
    private LottieAnimationView lottieAnimationView;

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
        onAsyncInterfaceListener = this;
    }

    private void initView() {
        btnLogin = findViewById(R.id.btn_stall_login);
        etID = findViewById(R.id.et_stall_id);
        lottieAnimationView = findViewById(R.id.lottie_view);

    }

    private void initClickEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sID = etID.getEditText().getText().toString().toLowerCase().trim();

                if (sID.length() <= 2) {
                    Toast.makeText(LoginActivity.this, "Invalid ID", Toast.LENGTH_LONG).show();
                } else {
                    BELAsyncTask belAsyncTask = new BELAsyncTask(LoginActivity.this);
                    belAsyncTask.execute(String.valueOf(5), sID);
                }
            }
        });
    }

    @Override
    public void onAsyncComplete(String sMSG, int type, String sResult) {
        switch (sMSG){
            case "LOGIN_SUCCESS":
                Intent inMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(inMainActivity);
            break;
        }
    }
}
