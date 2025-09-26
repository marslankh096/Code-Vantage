package com.demo.csvfilereader.file;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

import com.demo.csvfilereader.R;


public class StorageAccess {


    public interface OnStorageAccessListener {
        void onStorageAccessResult(boolean z);
    }

    public static void requestStorageAccess(Activity activity, OnStorageAccessListener onStorageAccessListener) {
        if (Build.VERSION.SDK_INT < 23) {
            onStorageAccessListener.onStorageAccessResult(true);
        } else {
            if (hasLegacyStorageAccess(activity)) {
                onStorageAccessListener.onStorageAccessResult(true);
            } else {
                requestStorageAccessDialog(activity, onStorageAccessListener);
            }
        }
    }

    public static void requestStorageAccessDialog(final Activity activity, final OnStorageAccessListener onStorageAccessListener) {
        new AlertDialog.Builder(activity).setCancelable(false).setTitle(R.string.prompt_storage_access_title).setMessage(R.string.prompt_storage_access).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onStorageAccessListener.onStorageAccessResult(false);
                if (Build.VERSION.SDK_INT >= 17) {
                    if (activity.isDestroyed()) {
                        return;
                    }
                } else if (activity.isFinishing()) {
                    return;
                }
                dialogInterface.dismiss();
            }
        }).setPositiveButton(R.string.grant, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Build.VERSION.SDK_INT >= 23) {
                    StorageAccess.requestLegacyStorageAccess(activity, onStorageAccessListener);
                }
                if (Build.VERSION.SDK_INT >= 17) {
                    if (activity.isDestroyed()) {
                        return;
                    }
                } else if (activity.isFinishing()) {
                    return;
                }
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public static boolean hasLegacyStorageAccess(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public static void requestLegacyStorageAccess(Activity activity, final OnStorageAccessListener onStorageAccessListener) {


    }
}
