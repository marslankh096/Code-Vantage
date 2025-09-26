package com.demo.csvfilereader.popup;

import android.app.Activity;
import android.content.Context;

import androidx.core.app.ActivityCompat;

import com.demo.csvfilereader.activity.HomeActivity;

import java.util.Objects;


public final class PermissonDialogListener implements PermissionDialogClickListener {
    public final HomeActivity delMainActivity;
    public final String[] strings;

    public PermissonDialogListener(HomeActivity delMainActivity, String[] strArr) {
        this.delMainActivity = delMainActivity;
        this.strings = strArr;
    }

    @Override
    public void onPermissionClick(boolean z) {
        HomeActivity homeActivity = this.delMainActivity;
        String[] strArr = this.strings;
        Objects.requireNonNull(homeActivity);
        if (z) {
            requestPermission(homeActivity.context, strArr, 100);
        }
    }

    public static void requestPermission(Context context, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
    }
}
