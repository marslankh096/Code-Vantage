package com.demo.csvfilereader.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.tv.AdRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.demo.csvfilereader.AdsGoogle;
import com.demo.csvfilereader.InfoActivity;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.file.FileFolderPicker;
import com.demo.csvfilereader.popup.PermissionRequireDialog;
import com.demo.csvfilereader.popup.PermissonDialogListener;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    public ImageView buttonInfo;
    public Context context;
    public FileFolderPicker picker;
    Animation push_animation;

    @Override
    public void onCreate(Bundle bundle) {
        try {
            super.onCreate(bundle);
            setContentView(R.layout.activity_home);

            AdsGoogle adsGoogle = new AdsGoogle(this);
            adsGoogle.Banner_Show((RelativeLayout) findViewById(R.id.banner), this);
            adsGoogle.Interstitial_Show_Counter(this);


            this.push_animation = AnimationUtils.loadAnimation(this, R.anim.button_push);
            this.buttonInfo = (ImageView) findViewById(R.id.btn_Info);
            this.buttonInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(HomeActivity.this.push_animation);
                    HomeActivity.this.startActivity(new Intent(HomeActivity.this.getApplicationContext(), InfoActivity.class));
                }
            });
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.clearFlags(1 << 26);
                window.addFlags(Integer.MIN_VALUE);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            }
            this.context = this;
            this.picker = new FileFolderPicker(this);
            ((LinearLayout) findViewById(R.id.prompt_click_to_pick_a_file_holder)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(HomeActivity.this.push_animation);
                    HomeActivity.this.goForScanningFile();
                    new File(HomeActivity.this.context.getFilesDir(), "YOUR_DIR").mkdirs();
                }
            });
            ((LinearLayout) findViewById(R.id.favfiles)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(HomeActivity.this.push_animation);
                    HomeActivity.this.startActivity(new Intent(HomeActivity.this.getApplicationContext(), FavouriteFilesActivity.class));
                }
            });
            ((LinearLayout) findViewById(R.id.pdfconvfiles)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(HomeActivity.this.push_animation);
                    HomeActivity.this.startActivity(new Intent(HomeActivity.this.getApplicationContext(), SavedPdflistActivity.class));
                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            return !Environment.isExternalStorageManager();
        }
        return (ContextCompat.checkSelfPermission(this, "READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "WRITE_EXTERNAL_STORAGE") == 0) ? false : true;
    }

    @SuppressLint("ResourceType")
    public void ConformDeleteDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.setContentView(R.layout.layout_permission_require);
        dialog.findViewById(R.id.tv_allow).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                try {
                    Intent intent = new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                    startActivityForResult(intent, 2296);
                } catch (Exception unused) {
                    Intent intent2 = new Intent();
                    intent2.setAction("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION");
                    startActivityForResult(intent2, 2296);
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_deny).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public final void goForScanningFile() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!checkPermission()) {
                startActivity(new Intent(getApplicationContext(), CSVFilesActivity.class));
                return;
            } else {
                ConformDeleteDialog();
                return;
            }
        }
        String[] strArr = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (!isPermissionsGranted(this, strArr)) {
            new PermissionRequireDialog(new PermissonDialogListener(this, strArr)).show(getSupportFragmentManager(), "SHOW");
        } else {
            startActivity(new Intent(getApplicationContext(), CSVFilesActivity.class));
        }
    }

    public static boolean isPermissionsGranted(Context context, String[] permissions) {
        boolean z = false;
        for (String str : permissions) {
            z = ContextCompat.checkSelfPermission(context, str) == 0;
            if (!z) {
                break;
            }
        }
        return z;
    }
}
