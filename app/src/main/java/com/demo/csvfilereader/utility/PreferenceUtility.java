package com.demo.csvfilereader.utility;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceUtility {
    public static void saveIntegerPrefrence(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public static int getIntegerPrefrence(Context context, String str, int i) {
        return context.getSharedPreferences(context.getPackageName(), 0).getInt(str, i);
    }

    public static void saveLongPrefrence(Context context, String str, long j) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        edit.putLong(str, j);
        edit.apply();
    }

    public static long getLongPrefrence(Context context, String str, long j) {
        return context.getSharedPreferences(context.getPackageName(), 0).getLong(str, j);
    }

    public static void saveStringPrefrence(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static String getStringPrefrence(Context context, String str, String str2) {
        return context.getSharedPreferences(context.getPackageName(), 0).getString(str, str2);
    }

    public static void saveBooleanPrefrence(Context context, String str, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    public static boolean getBooleanPrefrence(Context context, String str, boolean z) {
        return context.getSharedPreferences(context.getPackageName(), 0).getBoolean(str, z);
    }
}
