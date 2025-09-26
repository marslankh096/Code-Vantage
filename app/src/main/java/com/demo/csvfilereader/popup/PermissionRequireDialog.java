package com.demo.csvfilereader.popup;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.demo.csvfilereader.R;


public class PermissionRequireDialog extends DialogFragment {
    public Dialog dialog = null;
    public PermissionDialogClickListener permissionDialogClickListener;
    public TextView txtPermissionAllow;
    public TextView txtPermissionDeny;
    public TextView txtPermissionDesc;

    public PermissionRequireDialog(PermissionDialogClickListener permissionDialogClickListener2) {
        this.permissionDialogClickListener = permissionDialogClickListener2;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog dialog = new Dialog(getActivity());
        this.dialog = dialog;
        dialog.setContentView(R.layout.layout_permission_require);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.dialog.getWindow().setLayout((int) (displayMetrics.widthPixels * 0.9d), -2);
        this.txtPermissionAllow = (TextView) this.dialog.findViewById(R.id.tv_allow);
        this.txtPermissionDeny = (TextView) this.dialog.findViewById(R.id.tv_deny);
        this.txtPermissionAllow.setOnClickListener(new PermissionRequireDialogClick(this, 0));
        this.txtPermissionDeny.setOnClickListener(new PermissionRequireDialogClick(this, 1));
        return this.dialog;
    }
}
