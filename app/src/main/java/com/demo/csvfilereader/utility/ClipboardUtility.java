package com.demo.csvfilereader.utility;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.demo.csvfilereader.R;


public class ClipboardUtility {
    public static void copyToClipboard(Context context, String str, String str2) {
        ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(context.getString(R.string.app_name), str));
        if (str2 == null) {
            Toast.makeText(context, context.getString(R.string.prompt_copied_to_clipboard), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, str2, Toast.LENGTH_LONG).show();
        }
    }
}
