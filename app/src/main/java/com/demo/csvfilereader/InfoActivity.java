package com.demo.csvfilereader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.tv.AdRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class InfoActivity extends AppCompatActivity {
    public static Activity info_activity;


    RelativeLayout rel_privacy;
    RelativeLayout rel_rate_us;
    RelativeLayout rel_share_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetView();
    }

    private void SetView() {
        try {
            setContentView(R.layout.activity_info);

            AdsGoogle adsGoogle = new AdsGoogle(this);
            adsGoogle.Banner_Show((RelativeLayout) findViewById(R.id.banner), this);
            adsGoogle.Interstitial_Show_Counter(this);


            info_activity = this;
            this.rel_share_app = (RelativeLayout) findViewById(R.id.setting_rel_share_app);
            this.rel_rate_us = (RelativeLayout) findViewById(R.id.setting_rel_rate_us);
            this.rel_privacy = (RelativeLayout) findViewById(R.id.setting_rel_privacy);


            this.rel_share_app.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EUGeneralClass.ShareApp(InfoActivity.this);
                }
            });
            this.rel_rate_us.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EUGeneralClass.RateApp(InfoActivity.this);
                }
            });
            this.rel_privacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InfoActivity.this.PrivacyPolicyScreen();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void PrivacyPolicyScreen() {
        startActivity(new Intent(this, PrivacyPolicyActivity.class));
    }


}
