package edu.hitsz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.hitsz.data.Database;
import edu.hitsz.internet.Client;

public class LoadingActivity extends AppCompatActivity {
    public static int CONNECT_SUCCESS = 1;

    public static Handler handler;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        initVar();
    }

    private void initVar(){
        initHandler();
        Database.init();
        Client.getInstance().connect();
        LoadingDialog.getInstance(LoadingActivity.this).show();
    }

    @SuppressLint("HandlerLeak")
    private void initHandler(){
        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==CONNECT_SUCCESS){
                    intent=new Intent();
                    intent.setClass(LoadingActivity.this,LaunchActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }
}