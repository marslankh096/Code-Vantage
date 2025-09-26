package com.demo.csvfilereader;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class PrivacyPolicyActivity extends AppCompatActivity {
    private static Dialog loading_dialog;
    private static TextView loading_dialog_message;
    String dialog_message = "Fetching Privacy & Policy";
    protected WebView privacy_web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        SetView();
    }

    private void SetView() {
        try {
            setContentView(R.layout.activity_privacy_policy);

            AdsGoogle adsGoogle = new AdsGoogle(this);
            adsGoogle.Banner_Show((RelativeLayout) findViewById(R.id.banner), this);
            adsGoogle.Interstitial_Show_Counter(this);

            ShowLoadingDialog(this.dialog_message);
            WebView webView = (WebView) findViewById(R.id.privacy_web_view);
            this.privacy_web_view = webView;
            webView.setWebViewClient(new MyWebViewClient());
            this.privacy_web_view.getSettings().setUseWideViewPort(true);
            this.privacy_web_view.getSettings().setLoadWithOverviewMode(true);
            this.privacy_web_view.getSettings().setSupportZoom(true);
            this.privacy_web_view.getSettings().setBuiltInZoomControls(true);
            this.privacy_web_view.loadUrl("http://www.google.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyWebViewClient extends WebViewClient {
        private MyWebViewClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (PrivacyPolicyActivity.this.privacy_web_view.getProgress() == 100) {
                PrivacyPolicyActivity.DismissLoadingDialog();
            }
        }
    }

    public void LoadingDialog(final String message) {
        Dialog dialog = new Dialog(this, R.style.TransparentBackground);
        loading_dialog = dialog;
        dialog.requestWindowFeature(1);
        loading_dialog.setContentView(R.layout.dialog_loading);
        TextView textView = (TextView) loading_dialog.findViewById(R.id.dialog_loading_txt_message);
        loading_dialog_message = textView;
        textView.setText(message);
        loading_dialog.show();
    }

    private void ShowLoadingDialog(String message) {
        LoadingDialog(message);
    }


    public static void DismissLoadingDialog() {
        Dialog dialog = loading_dialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        BackScreen();
    }

    private void BackScreen() {
        finish();
    }
}
