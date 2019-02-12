package com.bel.antimatter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;
import app_utility.OfflineTransferService;
import app_utility.SharedPreferencesClass;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    FrameLayout flScanner;
    private CodeScanner mCodeScanner;
    LinearLayout llScan, llAmountParent;
    TextInputLayout etAmount;
    private Button btnScan, btnMyTransaction;
    CodeScannerView scannerView;
    TextView tvEmpID;
    int count = 0;
    DatabaseHandler db;
    SharedPreferencesClass sharedPreferencesClass;
    //int nAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initClasses();
        initViews();

        initClickEvents();
    }

    private void initClasses(){
        sharedPreferencesClass = new SharedPreferencesClass(MainActivity.this);
        db = new DatabaseHandler(MainActivity.this);
        if (!isMyServiceRunning(app_utility.OfflineTransferService.class)) {
            Intent in = new Intent(MainActivity.this, app_utility.OfflineTransferService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(in);
            } else {
                startService(in);
            }
        }

    }

    private void initViews(){
        flScanner = findViewById(R.id.fl_scanner);
        llScan = findViewById(R.id.ll_scan);
        llAmountParent = findViewById(R.id.ll_amount_parent);
        btnScan = findViewById(R.id.btn_scan);
        btnMyTransaction = findViewById(R.id.btn_my_transactions);
        scannerView = findViewById(R.id.scanner_view);
        etAmount = findViewById(R.id.et_amount);
        tvEmpID = findViewById(R.id.tv_emp_id);
        mCodeScanner = new CodeScanner(MainActivity.this, scannerView);
    }

    private void initClickEvents(){

       btnScan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mCodeScanner.startPreview();
               llScan.setVisibility(View.GONE);
               flScanner.setVisibility(View.VISIBLE);
               hideKeyboardFrom(etAmount);
           }
       });

        btnMyTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyTransactionFragment();
            }
        });

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //llParent.setVisibility(View.VISIBLE);
                        //flScannerMenu.setVisibility(View.VISIBLE);
                        //Toast.makeText(MainActivity.this, result.getText().trim(), Toast.LENGTH_SHORT).show();
                        tvEmpID.setText(result.getText().trim());
                        flScanner.setVisibility(View.GONE);
                        llScan.setVisibility(View.GONE);
                        llAmountParent.setVisibility(View.VISIBLE);
                        etAmount.getEditText().setText("");
                        //loadDoneFragment();
                        //MainActivity.onFragmentInteractionListener.onResultReceived("ENABLE_TOOLBAR", 1, "",null);
                    }
                });
            }
        });
    }

    private void openMyTransactionFragment(){
        Fragment newFragment;
        FragmentTransaction transaction;
        newFragment = MyTransactionFragment.newInstance("", "");
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll_scan, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void multiOnClick(View view){
        switch (view.getId()){
            case R.id.btn_done:
                validateNSave();
                hideKeyboardFrom(etAmount);
                break;
            case R.id.tv_10:
                etAmount.getEditText().setText("10");
                break;
            case R.id.tv_20:
                etAmount.getEditText().setText("20");
                break;
            case R.id.tv_30:
                etAmount.getEditText().setText("30");
                break;
            case R.id.tv_40:
                etAmount.getEditText().setText("40");
                break;
            case R.id.tv_50:
                etAmount.getEditText().setText("50");
                break;
            case R.id.tv_100:
                etAmount.getEditText().setText("100");
                break;
        }
    }

    private void validateNSave(){
        Date currentTime = Calendar.getInstance().getTime();
        String sAmount = etAmount.getEditText().getText().toString().trim();
        String sScannedID = tvEmpID.getText().toString().trim();
        if(sAmount.length()>=1) {
            //int nFinalAmount = Integer.valueOf(etAmount.getEditText().getText().toString().trim());
            String sCurrentTime = currentTime.toString().trim().substring(11, 16);
            String sEmpID = tvEmpID.getText().toString();
            String sStallName = sharedPreferencesClass.getUserName();

            //Toast.makeText(MainActivity.this, sCurrentTime + nFinalAmount, Toast.LENGTH_SHORT).show();

            db.addData(new DataBaseHelper(sEmpID, sScannedID, sStallName, sAmount, sCurrentTime));

            //flScanner.setVisibility(View.GONE);
            llScan.setVisibility(View.VISIBLE);
            llAmountParent.setVisibility(View.GONE);
            /*ArrayList<DataBaseHelper> dbData;
            dbData = new ArrayList<>(db.getAllPermanentData());

            ArrayList<DataBaseHelper> dbDataTmp;
            dbDataTmp = new ArrayList<>(db.getAllTemporaryData());*/
            //Toast.makeText(MainActivity.this, "" + dbData.size() + " " +dbDataTmp.size(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        //int count = getSupportFragmentManager().getBackStackEntryCount();
        if(count==0){
            llScan.setVisibility(View.VISIBLE);
            llAmountParent.setVisibility(View.GONE);
            count++;
        } else {
            super.onBackPressed();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
