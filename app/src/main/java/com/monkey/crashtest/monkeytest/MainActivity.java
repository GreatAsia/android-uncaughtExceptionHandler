package com.monkey.crashtest.monkeytest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private  String s;
    private Button button;
    private static String TAG = "monkeycrashlog";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                onClickButton1(view);
                break;
            default:
                break;
        }
    }

    private void onClickButton1(View view) {

        button.setText("Waiting to Crash");


        //处理逻辑
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            e.getLocalizedMessage();
        }
        int x = 3/0;
        boolean result = s.equals("any string");
        System.out.println(result);


    }



}
