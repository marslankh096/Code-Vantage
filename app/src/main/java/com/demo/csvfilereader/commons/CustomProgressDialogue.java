package com.demo.csvfilereader.commons;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.demo.csvfilereader.R;


public class CustomProgressDialogue extends Dialog {
    public CustomProgressDialogue(Context context) {
        super(context);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(attributes);
        setTitle((CharSequence) null);
        setCancelable(false);
        setOnCancelListener(null);
        setContentView(LayoutInflater.from(context).inflate(R.layout.custom_progress, (ViewGroup) null));
    }
}
