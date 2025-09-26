package com.demo.csvfilereader.file;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class FileFolderPicker {
    AppCompatActivity activity;
    OnFileSelectedListener fileSelectedListener;
    ActivityResultLauncher<Intent> folderResult;
    OnFolderSelectedListener folderSelectedListener;
    FilePickerMode mode = FilePickerMode.MULTIPLE;
    ActivityResultLauncher<Intent> multipleFileResult;
    ActivityResultLauncher<Intent> singleFileResult;


    public enum FilePickerMode {
        SINGLE,
        MULTIPLE
    }


    public interface OnFileSelectedListener {
        void onFileSelected(ArrayList<String> arrayList);
    }


    public interface OnFolderSelectedListener {
        void onFolderSelected(Uri uri);
    }

    public FileFolderPicker(AppCompatActivity appCompatActivity) {
        this.activity = appCompatActivity;
        setupSingleFilePickerIntent();
        setupMultipleFilePickerIntent();
        setupFolderPickerIntent();
    }

    private void setupSingleFilePickerIntent() {
        this.singleFileResult = this.activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() {
            @Override
            public final void onActivityResult(Object obj) {
                FileFolderPicker.this.setupSingleFilePickerIntentFileFolderPicker((ActivityResult) obj);
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void setupSingleFilePickerIntentFileFolderPicker(ActivityResult activityResult) {
        Intent data;
        Uri data2;
        if (activityResult.getResultCode() != -1 || activityResult == null || (data = activityResult.getData()) == null || (data2 = data.getData()) == null) {
            return;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(data2.toString());
        int flags = data.getFlags() & 3;
        AppCompatActivity appCompatActivity = this.activity;
        if (appCompatActivity != null) {
            try {
                appCompatActivity.getContentResolver().takePersistableUriPermission(data2, flags);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OnFileSelectedListener onFileSelectedListener = this.fileSelectedListener;
        if (onFileSelectedListener != null) {
            onFileSelectedListener.onFileSelected(arrayList);
        }
    }

    private void setupMultipleFilePickerIntent() {
        this.multipleFileResult = this.activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() {
            @Override
            public final void onActivityResult(Object obj) {
                FileFolderPicker.this.setupMultipleFilePickerIntentFileFolderPicker((ActivityResult) obj);
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void setupMultipleFilePickerIntentFileFolderPicker(ActivityResult activityResult) {
        Intent data;
        Uri uri;
        if (activityResult.getResultCode() != -1 || activityResult == null || (data = activityResult.getData()) == null) {
            return;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item itemAt = clipData.getItemAt(i);
                if (itemAt != null && (uri = itemAt.getUri()) != null) {
                    arrayList.add(uri.toString());
                    int flags = data.getFlags() & 3;
                    AppCompatActivity appCompatActivity = this.activity;
                    if (appCompatActivity != null) {
                        try {
                            appCompatActivity.getContentResolver().takePersistableUriPermission(uri, flags);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            Uri data2 = data.getData();
            if (data2 != null) {
                arrayList.add(data2.toString());
                int flags2 = data.getFlags() & 3;
                AppCompatActivity appCompatActivity2 = this.activity;
                if (appCompatActivity2 != null) {
                    try {
                        appCompatActivity2.getContentResolver().takePersistableUriPermission(data2, flags2);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        OnFileSelectedListener onFileSelectedListener = this.fileSelectedListener;
        if (onFileSelectedListener != null) {
            onFileSelectedListener.onFileSelected(arrayList);
        }
    }

    private void setupFolderPickerIntent() {
        this.folderResult = this.activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() {
            @Override
            public final void onActivityResult(Object obj) {
                FileFolderPicker.this.setupFolderPickerIntentFileFolderPicker((ActivityResult) obj);
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void setupFolderPickerIntentFileFolderPicker(ActivityResult activityResult) {
        Intent data;
        Uri data2;
        if (activityResult.getResultCode() != -1 || activityResult == null || (data = activityResult.getData()) == null || (data2 = data.getData()) == null) {
            return;
        }
        int flags = data.getFlags() & 3;
        AppCompatActivity appCompatActivity = this.activity;
        if (appCompatActivity != null) {
            try {
                appCompatActivity.getContentResolver().takePersistableUriPermission(data2, flags);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OnFolderSelectedListener onFolderSelectedListener = this.folderSelectedListener;
        if (onFolderSelectedListener != null) {
            onFolderSelectedListener.onFolderSelected(data2);
        }
    }
}
