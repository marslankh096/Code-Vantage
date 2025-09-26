package com.demo.csvfilereader.utility;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.demo.csvfilereader.R;

import java.io.File;
import java.util.Locale;


public class MenuOptionsUtility {
    public static void shareFile(Context context, String str) {
        Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(str));
        if (uriForFile != null) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uriForFile, context.getContentResolver().getType(uriForFile));
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            context.startActivity(intent);
        }
    }

    @SuppressLint("WrongConstant")
    public static void viewFile(final Context context, String str) {
        final String upperCase = str.substring(str.lastIndexOf(".") + 1, str.length()).toUpperCase(Locale.getDefault());
        Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(str));
        if (uriForFile != null) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uriForFile, context.getContentResolver().getType(uriForFile));
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION << 26);
            if (context.getPackageManager().queryIntentActivities(intent, 65536).size() > 0) {
                context.startActivity(intent);
                return;
            }
            AlertDialog.Builder cancelable = new AlertDialog.Builder(context).setCancelable(false);
            cancelable.setMessage(context.getString(R.string.prompt_install_viewer) + " " + upperCase + " File.").setNegativeButton(context.getString(R.string.go_to_store), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent2 = new Intent("android.intent.action.VIEW");
                    intent2.setData(Uri.parse("http://play.google.com/store/search?q=" + upperCase + "+file+viewer&c=apps"));
                    context.startActivity(intent2);
                    dialogInterface.dismiss();
                }
            }).setPositiveButton(context.getString(R.string.okay), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        }
    }
}
