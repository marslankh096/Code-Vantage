package com.demo.csvfilereader;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.valdesekamdem.library.mdtoast.MDToast;


public class EUGeneralClass {
    private static String TAG = "EUGeneralClass: ";
    public static boolean is_online;
    private static Context mContext;

    public EUGeneralClass(Context ctx) {
        mContext = ctx;
    }

    public static void RateApp(Context ctx) {
        try {
            mContext = ctx;
            ConformRateDialog(mContext, "https://play.google.com/store/apps/details?id=" + mContext.getPackageName(), "Rate App", "If you enjoy this app, would you mind taking a moment to rate it?\nThanks for your support!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ShareApp(Context ctx) {
        try {
            mContext = ctx;
            String str = mContext.getResources().getString(R.string.app_name) + " :";
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", str);
            intent.putExtra("android.intent.extra.TEXT", str + "\n" + ("https://play.google.com/store/apps/details?id=" + mContext.getPackageName()));
            mContext.startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean isOnline(Context mContext2) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext2.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean z = true;
        if (Build.VERSION.SDK_INT >= 23) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) {
                return false;
            }
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities == null || (!networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) && !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) && !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH))) {
                z = false;
            }
            return Boolean.valueOf(z);
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return Boolean.valueOf((activeNetworkInfo == null || !activeNetworkInfo.isConnected()) ? false : false);
    }

    public static void ShowSuccessToast(Context mContext2, String toast_message) {
        MDToast.makeText(mContext2, toast_message, MDToast.LENGTH_SHORT, Toast.LENGTH_LONG).show();
    }

    public static void ShowInfoToast(Context mContext2, String toast_message) {
        MDToast.makeText(mContext2, toast_message, MDToast.LENGTH_SHORT, Toast.LENGTH_SHORT).show();
    }

    public static void ShowWarningToast(Context mContext2, String toast_message) {
        MDToast.makeText(mContext2, toast_message, MDToast.LENGTH_SHORT, 2).show();
    }

    public static void ShowErrorToast(Context mContext2, String toast_message) {
        MDToast.makeText(mContext2, toast_message, MDToast.LENGTH_SHORT, 3).show();
    }

    public static void ConformRateDialog(final Context mContext2, final String appUrl, final String header, final String message) {
        final Dialog dialog = new Dialog(mContext2, R.style.TransparentBackground);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_rate);
        Button button = (Button) dialog.findViewById(R.id.dialog_conform_btn_yes);
        Button button2 = (Button) dialog.findViewById(R.id.dialog_conform_btn_no);
        ((TextView) dialog.findViewById(R.id.dialog_conform_txt_header)).setText(header);
        ((TextView) dialog.findViewById(R.id.dialog_conform_txt_message)).setText(message);
        button.setText("Rate now");
        button2.setText("Cancel");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mContext2.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(appUrl)));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void ExitDialog(final Context mContext2, final Activity mActivity) {
        final Dialog dialog = new Dialog(mContext2, R.style.TransparentBackground);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_exit);
        ((TextView) dialog.findViewById(R.id.dialog_exit_txt_header)).setText("Confirm Exit");
        ((TextView) dialog.findViewById(R.id.dialog_exit_txt_message)).setText("Are you sure you want to exit from application?\nThank You for Using Our Application!");
        ((TextView) dialog.findViewById(R.id.dialog_exit_txt_continue)).setText("Quit");
        ((TextView) dialog.findViewById(R.id.dialog_exit_txt_cancel)).setText("Cancel");
        ((RelativeLayout) dialog.findViewById(R.id.dialog_exit_rel_continue)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EUGeneralClass.FinishApp(mActivity);
            }
        });
        ((RelativeLayout) dialog.findViewById(R.id.dialog_exit_rel_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void ExitApp(Activity mActivity) {
        mActivity.finishAndRemoveTask();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    public static void FinishApp(Activity mActivity) {
        mActivity.finish();
    }
}
