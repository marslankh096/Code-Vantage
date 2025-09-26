package com.demo.csvfilereader.utility;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.itextpdf.text.Annotation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;


public class PathsUtility {
    public static String getPath(Context context, Uri uri) {
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                return getUriRealPathAboveKitkat(context, uri);
            }
            return getRealPath(context.getContentResolver(), uri, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DREG", "FilePath Catch: " + e);
            return getFilePathFromURI(context, uri);
        }
    }

    private static String getFilePathFromURI(Context context, Uri uri) {
        String fileName = getFileName(uri);
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        Environment.getExternalStorageDirectory().getPath();
        File file = new File(context.getCacheDir() + File.separator + fileName);
        StringBuilder sb = new StringBuilder();
        sb.append("FilePath copyFile: ");
        sb.append(file);
        Log.d("DREG", sb.toString());
        copy(context, uri, file);
        return file.getAbsolutePath();
    }

    public static String getFileName(Uri uri) {
        String path;
        int lastIndexOf;
        if (uri == null || (lastIndexOf = (path = uri.getPath()).lastIndexOf(47)) == -1) {
            return null;
        }
        return path.substring(lastIndexOf + 1);
    }

    public static void copy(Context context, Uri uri, File file) {
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            if (openInputStream != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                IOUtils.copy(openInputStream, fileOutputStream);
                openInputStream.close();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getUriRealPathAboveKitkat(Context context, Uri uri) {
        if (context == null || uri == null) {
            return "";
        }
        if (isContentUri(uri)) {
            if (isGooglePhotoDoc(uri.getAuthority())) {
                return uri.getLastPathSegment();
            }
            return getRealPath(context.getContentResolver(), uri, null);
        } else if (isFileUri(uri)) {
            return uri.getPath();
        } else {
            if (isDocumentUri(context, uri)) {
                String documentId = DocumentsContract.getDocumentId(uri);
                String authority = uri.getAuthority();
                if (isMediaDoc(authority)) {
                    String[] split = documentId.split(":");
                    if (split.length != 2) {
                        return "";
                    }
                    String str = split[0];
                    String str2 = split[1];
                    Uri uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    if ("image".equals(str)) {
                        uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(str)) {
                        uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(str)) {
                        uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    ContentResolver contentResolver = context.getContentResolver();
                    return getRealPath(contentResolver, uri2, "_id = " + str2);
                } else if (isDownloadDoc(authority)) {
                    return getRealPath(context.getContentResolver(), ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId).longValue()), null);
                } else {
                    if (isExternalStoreDoc(authority)) {
                        String[] split2 = documentId.split(":");
                        if (split2.length != 2) {
                            return "";
                        }
                        String str3 = split2[0];
                        String str4 = split2[1];
                        if ("primary".equalsIgnoreCase(str3)) {
                            return Environment.getExternalStorageDirectory() + "/" + str4;
                        }
                        return "";
                    }
                    return "";
                }
            }
            return "";
        }
    }

    private static boolean isDocumentUri(Context context, Uri uri) {
        if (context == null || uri == null) {
            return false;
        }
        return DocumentsContract.isDocumentUri(context, uri);
    }

    private static boolean isContentUri(Uri uri) {
        return uri != null && "content".equalsIgnoreCase(uri.getScheme());
    }

    private static boolean isFileUri(Uri uri) {
        return uri != null && Annotation.FILE.equalsIgnoreCase(uri.getScheme());
    }

    private static boolean isExternalStoreDoc(String str) {
        return "com.android.externalstorage.documents".equals(str);
    }

    private static boolean isDownloadDoc(String str) {
        return "com.android.providers.downloads.documents".equals(str);
    }

    private static boolean isMediaDoc(String str) {
        return "com.android.providers.media.documents".equals(str);
    }

    private static boolean isGooglePhotoDoc(String str) {
        return "com.google.android.apps.photos.content".equals(str);
    }

    private static String getRealPath(ContentResolver contentResolver, Uri uri, String str) {
        String[] strArr = null;
        Cursor query = contentResolver.query(uri, strArr, str, strArr, null);
        if (query == null || !query.moveToFirst()) {
            return "";
        }
        if (uri != MediaStore.Images.Media.EXTERNAL_CONTENT_URI && uri != MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
            Uri uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        return query.getString(query.getColumnIndex("_data"));
    }
}
