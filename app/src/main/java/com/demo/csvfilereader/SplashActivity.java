package com.demo.csvfilereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.csvfilereader.activity.HomeActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            requestWindowFeature(1);
            getWindow().setFlags(1024, 1024);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);



            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    HomeScreen();
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void HomeScreen() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ExitApp();
    }

    public void ExitApp() {
        moveTaskToBack(true);
        finish();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}
