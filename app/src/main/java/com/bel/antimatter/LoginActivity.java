package com.bel.antimatter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import app_utility.BELAsyncTask;
import app_utility.OnAsyncInterfaceListener;
import app_utility.PermissionHandler;
import app_utility.SharedPreferencesClass;
import app_utility.StaticReferenceClass;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

import static app_utility.PermissionHandler.CAMERA_PERMISSION;
import static app_utility.PermissionHandler.hasPermissions;
import static app_utility.StaticReferenceClass.CAMERA_CODE;

public class LoginActivity extends AppCompatActivity implements OnAsyncInterfaceListener {

    Button btnLogin;
    TextInputLayout etID, etIP;
    SharedPreferencesClass sharedPreferencesClass;
    public static OnAsyncInterfaceListener onAsyncInterfaceListener;
    private LottieAnimationView lottieAnimationView;
    ArrayList<String> alStallNames;
    ImageButton ibAdmin;
    private int nPermissionFlag = 0;
    String sIP;

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
etIP = findViewById(R.id.et_ip);
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
                        sIP = etIP.getEditText().getText().toString();
                        StaticReferenceClass.SERVER_URL = sIP;
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

    @Override
    public void onStart() {
        super.onStart();
        if (!hasPermissions(LoginActivity.this, CAMERA_PERMISSION)) {
            ActivityCompat.requestPermissions(LoginActivity.this, CAMERA_PERMISSION, CAMERA_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int PERMISSION_ALL, String permissions[], int[] grantResults) {
        StringBuilder sMSG = new StringBuilder();
        if (PERMISSION_ALL == CAMERA_CODE) {
            for (String sPermission : permissions) {
                switch (sPermission) {
                    case Manifest.permission.CAMERA:
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.CAMERA)) {
                                //Show permission explanation dialog...
                                //showPermissionExplanation(SignInActivity.this.getResources().getString(R.string.phone_explanation));
                                //Toast.makeText(SignInActivity.this, "not given", Toast.LENGTH_SHORT).show();
                                sMSG.append("CAMERA, ");
                                nPermissionFlag = 0;
                            } else {
                                //Never ask again selected, or device policy prohibits the app from having that permission.
                                //So, disable that feature, or fall back to another situation...
                                //@SuppressWarnings("unused") AlertDialogs alertDialogs = new AlertDialogs(HomeScreen.this, 1, mListener);
                                //Toast.makeText(SignInActivity.this, "permission never ask", Toast.LENGTH_SHORT).show();
                                //showPermissionExplanation(HomeScreenActivity.this.getResources().getString(R.string.phone_explanation));
                                sMSG.append("CAMERA, ");
                                nPermissionFlag = 0;
                            }
                        }
                        break;
                    /*case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                //Show permission explanation dialog...
                                //showPermissionExplanation(SignInActivity.this.getResources().getString(R.string.phone_explanation));
                                //Toast.makeText(SignInActivity.this, "not given", Toast.LENGTH_SHORT).show();
                                sMSG.append("STORAGE, ");
                                nPermissionFlag = 0;
                            } else {
                                //Never ask again selected, or device policy prohibits the app from having that permission.
                                //So, disable that feature, or fall back to another situation...
                                //@SuppressWarnings("unused") AlertDialogs alertDialogs = new AlertDialogs(HomeScreen.this, 1, mListener);
                                //Toast.makeText(SignInActivity.this, "permission never ask", Toast.LENGTH_SHORT).show();
                                //showPermissionExplanation(HomeScreenActivity.this.getResources().getString(R.string.phone_explanation));
                                sMSG.append("STORAGE, ");
                                nPermissionFlag = 0;
                            }
                        }
                        break;*/
                }
            }
            if (!sMSG.toString().equals("") && !sMSG.toString().equals(" ")) {
                PermissionHandler permissionHandler = new PermissionHandler(LoginActivity.this, 0, sMSG.toString(), nPermissionFlag);
            }
        }
    }
}
