package com.bel.antimatter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class MainActivity extends AppCompatActivity {

    FrameLayout flScanner;
    private CodeScanner mCodeScanner;
    LinearLayout llScan, llAmountParent;
    private Button btnScan;
    CodeScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        initClickEvents();
    }

    private void initViews(){
        flScanner = findViewById(R.id.fl_scanner);
        llScan = findViewById(R.id.ll_scan);
        llAmountParent = findViewById(R.id.ll_amount_parent);
        btnScan = findViewById(R.id.btn_scan);
        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(MainActivity.this, scannerView);
    }

    private void initClickEvents(){

       btnScan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               llScan.setVisibility(View.GONE);
               flScanner.setVisibility(View.VISIBLE);
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
                        Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        flScanner.setVisibility(View.GONE);
                        llScan.setVisibility(View.GONE);
                        llAmountParent.setVisibility(View.VISIBLE);
                        //loadDoneFragment();
                        //MainActivity.onFragmentInteractionListener.onResultReceived("ENABLE_TOOLBAR", 1, "",null);
                    }
                });
            }
        });
    }
}
