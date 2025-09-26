package com.demo.csvfilereader.file;

import android.content.Context;
import android.text.format.DateFormat;

import androidx.documentfile.provider.DocumentFile;

import com.demo.csvfilereader.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;


public class FileHelper {
    public static String getFileSize(String str) {
        if (new File(str).exists()) {
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(2);
            double length = new File(str).length() / FileUtils.ONE_KB;
            if (length >= 1024.0d) {
                StringBuilder sb = new StringBuilder();
                Double.isNaN(length);
                sb.append(decimalFormat.format(length / 1024.0d));
                sb.append("MB");
                return sb.toString();
            }
            return decimalFormat.format(length) + "KB";
        }
        return "";
    }

    public static String getFileDate(Context context, String str) {
        File file = new File(str);
        return file.exists() ? getFormattedDate(context, file.lastModified()) : "";
    }

    public static String getFormattedDate(Context context, long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        Calendar calendar2 = Calendar.getInstance();
        if (calendar2.get(5) == calendar.get(5) && calendar2.get(2) == calendar.get(2) && calendar2.get(1) == calendar.get(1)) {
            return context.getString(R.string.today);
        }
        if (calendar2.get(5) - calendar.get(5) == 1 && calendar2.get(2) == calendar.get(2) && calendar2.get(1) == calendar.get(1)) {
            return context.getString(R.string.yesterday);
        }
        if (calendar2.get(1) == calendar.get(1)) {
            return DateFormat.format("EEE, MMM d", calendar).toString();
        }
        return DateFormat.format("MMM dd yyyy", calendar).toString();
    }

    public static String getFileDate(Context context, DocumentFile documentFile) {
        return documentFile.exists() ? getFormattedDate(context, documentFile.lastModified()) : "";
    }

    public static String getFileSize(DocumentFile documentFile) {
        if (documentFile.exists()) {
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(2);
            double length = documentFile.length() / FileUtils.ONE_KB;
            if (length >= 1024.0d) {
                StringBuilder sb = new StringBuilder();
                Double.isNaN(length);
                sb.append(decimalFormat.format(length / 1024.0d));
                sb.append("MB");
                return sb.toString();
            }
            return decimalFormat.format(length) + "KB";
        }
        return "";
    }

    public static void deleteCacheFile(String str) {
        try {
            if (new File(str).exists()) {
                new File(str).delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
