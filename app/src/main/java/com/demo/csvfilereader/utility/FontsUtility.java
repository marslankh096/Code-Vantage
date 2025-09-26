package com.demo.csvfilereader.utility;

import android.content.Context;


public class FontsUtility {
    public static String[] listFonts(Context context, String str) {
        String[] strArr;
        try {
            strArr = context.getResources().getAssets().list(str);
        } catch (Exception e) {
            e.printStackTrace();
            strArr = null;
        }
        if (strArr == null) {
            return new String[0];
        }
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = strArr[i].replace(".ttf", "");
        }
        return strArr;
    }
}
