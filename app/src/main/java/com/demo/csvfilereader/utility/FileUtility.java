package com.demo.csvfilereader.utility;

import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;


public class FileUtility {
    protected static final String CACHE_FILES = "CACHE_FILES";
    protected static final String CACHE_NUMBER_OF_FILES = "CACHE_NUMBER_OF_FILES";
    public static final String STORAGE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "";


    public static String getFileSize(String size) {
        try {
            int size2 = Integer.parseInt(size);
            final String[] units = {"B", "KB", "MB", "GB", "TB"};
            int digitGroups = (int) (Math.log10(size2) / Math.log10(1024));
            return new DecimalFormat("#,##0.#").format(size2 / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        } catch (Exception e) {
            return "0 B";
        }
    }


    public static boolean deleteFile(String str) {
        try {
            if (new File(str).exists()) {
                return new File(str).delete();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFileName(String str, String str2) {
        int i = 1;
        String str3 = str;
        while (true) {
            if (new File(STORAGE + "/" + str3 + str2).exists()) {
                str3 = str + "(" + i + ")";
                i++;
            } else {
                return str3 + str2;
            }
        }
    }
}
