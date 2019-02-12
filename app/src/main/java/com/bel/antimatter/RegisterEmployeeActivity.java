package com.bel.antimatter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import app_utility.AdminRegisterService;
import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.Result;

public class RegisterEmployeeActivity extends AppCompatActivity {

    FrameLayout flScanner;
    private CodeScanner mCodeScanner;
    LinearLayout llScan, llRegister;
    TextInputLayout etEmpID;
    private Button btnRegister, btnScan;
    CodeScannerView scannerView;
    TextView tvScanID;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_employee);
        initViews();

        initClickEvents();
        Intent adminServiceIn = new Intent(RegisterEmployeeActivity.this, AdminRegisterService.class);
        startService(adminServiceIn);
    }

    private void initClickEvents(){

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeScanner.startPreview();
                llScan.setVisibility(View.GONE);
                flScanner.setVisibility(View.VISIBLE);
                hideKeyboardFrom(etEmpID);

            }
        });

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                RegisterEmployeeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //llParent.setVisibility(View.VISIBLE);
                        //flScannerMenu.setVisibility(View.VISIBLE);
                        //Toast.makeText(RegisterEmployeeActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        tvScanID.setText(result.getText().trim());
                        flScanner.setVisibility(View.GONE);
                        llScan.setVisibility(View.GONE);
                        llRegister.setVisibility(View.VISIBLE);
                        etEmpID.getEditText().setText("");
                        //loadDoneFragment();
                        //MainActivity.onFragmentInteractionListener.onResultReceived("ENABLE_TOOLBAR", 1, "",null);
                    }
                });
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateNSave();
                hideKeyboardFrom(etEmpID);
            }
        });
    }

    public void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initViews(){
        flScanner = findViewById(R.id.fl_scanner);
        llScan = findViewById(R.id.ll_scan);
        llRegister = findViewById(R.id.ll_register);
        scannerView = findViewById(R.id.scanner_view);
        etEmpID = findViewById(R.id.et_emp_ID);
        btnScan = findViewById(R.id.btn_scan);
        btnRegister = findViewById(R.id.btn_register);
        tvScanID = findViewById(R.id.tv_scan_id);
        mCodeScanner = new CodeScanner(RegisterEmployeeActivity.this, scannerView);
        db = new DatabaseHandler(RegisterEmployeeActivity.this);
    }

    private void validateNSave(){
        String sEmpID = etEmpID.getEditText().getText().toString().trim();
        String sScanID = tvScanID.getText().toString().trim();

        if(sEmpID.length()>=4) {
            db.addDataToEmployeeTable(new DataBaseHelper(sEmpID, sScanID));
            llScan.setVisibility(View.VISIBLE);
            llRegister.setVisibility(View.GONE);
        } else {
            Toast.makeText(RegisterEmployeeActivity.this, "Please enter a valid Employee ID", Toast.LENGTH_LONG).show();
        }
    }
}
