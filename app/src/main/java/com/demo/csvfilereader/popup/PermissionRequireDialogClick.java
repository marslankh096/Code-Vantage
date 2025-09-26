package com.demo.csvfilereader.popup;

import android.view.View;


public final class PermissionRequireDialogClick implements View.OnClickListener {
    public final int Id;
    public final PermissionRequireDialog permissionRequireDialog;

    public PermissionRequireDialogClick(PermissionRequireDialog permissionRequireDialog, int i) {
        this.Id = i;
        this.permissionRequireDialog = permissionRequireDialog;
    }

    @Override
    public void onClick(View view) {
        if (this.Id == 0) {
            PermissionRequireDialog permissionRequireDialog = this.permissionRequireDialog;
            permissionRequireDialog.permissionDialogClickListener.onPermissionClick(true);
            permissionRequireDialog.dialog.dismiss();
            return;
        }
        PermissionRequireDialog permissionRequireDialog2 = this.permissionRequireDialog;
        permissionRequireDialog2.permissionDialogClickListener.onPermissionClick(false);
        permissionRequireDialog2.dialog.dismiss();
    }
}
