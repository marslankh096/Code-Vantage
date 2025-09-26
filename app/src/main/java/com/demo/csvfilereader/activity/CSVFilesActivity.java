package com.demo.csvfilereader.activity;

import android.database.Cursor;
import android.media.tv.AdRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.csvfilereader.AdsGoogle;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.adapter.FilesAdapter;
import com.demo.csvfilereader.databinding.ActivityCsvfilesBinding;
import com.demo.csvfilereader.model.FilesModelClass;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;


public final class CSVFilesActivity extends AppCompatActivity {

    public FilesAdapter adapter;
    AdRequest banner_adRequest;
    public ActivityCsvfilesBinding binding;
    private final Executor executor;
    private ArrayList<FilesModelClass> filesList = new ArrayList<>();
    private final Handler handler;

    Animation push_animation;
    RelativeLayout rel_ad_layout;

    public CSVFilesActivity() {
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        Intrinsics.checkNotNullExpressionValue(newSingleThreadExecutor, "newSingleThreadExecutor()");
        this.executor = newSingleThreadExecutor;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public ActivityCsvfilesBinding getBinding() {
        ActivityCsvfilesBinding activityCsvfilesBinding = this.binding;
        if (activityCsvfilesBinding != null) {
            return activityCsvfilesBinding;
        }
        Intrinsics.throwUninitializedPropertyAccessException("binding");
        return null;
    }

    public void setBinding(ActivityCsvfilesBinding activityCsvfilesBinding) {
        Intrinsics.checkNotNullParameter(activityCsvfilesBinding, "<set-?>");
        this.binding = activityCsvfilesBinding;
    }

    public FilesAdapter getAdapter() {
        FilesAdapter filesAdapter = this.adapter;
        if (filesAdapter != null) {
            return filesAdapter;
        }
        Intrinsics.throwUninitializedPropertyAccessException("adapter");
        return null;
    }

    public void setAdapter(FilesAdapter filesAdapter) {
        Intrinsics.checkNotNullParameter(filesAdapter, "<set-?>");
        this.adapter = filesAdapter;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityCsvfilesBinding inflate = ActivityCsvfilesBinding.inflate(getLayoutInflater());
        Intrinsics.checkNotNullExpressionValue(inflate, "inflate(layoutInflater)");
        setBinding(inflate);
        setContentView(getBinding().getRoot());

        AdsGoogle adsGoogle = new AdsGoogle(this);
        adsGoogle.Banner_Show((RelativeLayout) findViewById(R.id.banner), this);
        adsGoogle.Interstitial_Show_Counter(this);


        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.button_push);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(1 << 26);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        this.executor.execute(new CSVFilesActivityExecutor(this));
        getBinding().backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(CSVFilesActivity.this.push_animation);
                Intrinsics.checkNotNullParameter(this, "this");
                CSVFilesActivity.this.onBackPressed();
            }
        });
    }

    public static void CSVFilesActivityExecutorPerform(CSVFilesActivity cSVFilesActivity) {
        Intrinsics.checkNotNullParameter(cSVFilesActivity, "this");
        cSVFilesActivity.getCSVFiles();
        cSVFilesActivity.handler.post(new CSVFilesActivityExecutorPerformClick(cSVFilesActivity));
    }

    public static void CSVFilesActivityExecutorPerformOperations(CSVFilesActivity cSVFilesActivity) {
        Intrinsics.checkNotNullParameter(cSVFilesActivity, "this");
        if (cSVFilesActivity.filesList.size() < 1) {
            cSVFilesActivity.getBinding().nofileTV.setVisibility(View.VISIBLE);
        }
        cSVFilesActivity.getBinding().progressbarrr.setVisibility(View.GONE);
        cSVFilesActivity.getBinding().csvFileRV.setLayoutManager(new LinearLayoutManager(cSVFilesActivity));
        cSVFilesActivity.getBinding().csvFileRV.setHasFixedSize(true);
        cSVFilesActivity.setAdapter(new FilesAdapter(cSVFilesActivity, cSVFilesActivity.filesList, "csv"));
        cSVFilesActivity.getBinding().csvFileRV.setAdapter(cSVFilesActivity.getAdapter());
    }

    private void getCSVFiles() {
        String str = null;
        Cursor query = getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_data", "title", "_size", "date_modified"}, str, null, str);
        if (query != null) {
            while (query.moveToNext()) {
                String uri = query.getString(0);
                String title = query.getString(1);
                String size = query.getString(2);
                String date = query.getString(3);
                Intrinsics.checkNotNullExpressionValue(uri, "uri");
                if (StringsKt.endsWith(uri, ".csv", false)) {
                    Intrinsics.checkNotNullExpressionValue(date, "date");
                    String date2 = getDate(Long.parseLong(date));
                    Intrinsics.checkNotNullExpressionValue(size, "size");
                    String stringSizeLengthFile = getStringSizeLengthFile(Long.parseLong(size));
                    if (new File(uri).exists()) {
                        ArrayList<FilesModelClass> arrayList = this.filesList;
                        Intrinsics.checkNotNullExpressionValue(title, "title");
                        arrayList.add(new FilesModelClass(title, stringSizeLengthFile, uri, date2));
                    }
                }
            }
            query.close();
        }
    }

    public String getStringSizeLengthFile(long j) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        float f = (float) j;
        if (f < 1048576.0f) {
            return Intrinsics.stringPlus(decimalFormat.format(f / 1024.0f), " Kb");
        }
        if (f < 1.0737418E9f) {
            return Intrinsics.stringPlus(decimalFormat.format(f / 1048576.0f), " Mb");
        }
        return f < 1.0995116E12f ? Intrinsics.stringPlus(decimalFormat.format(f / 1.0737418E9f), " Gb") : "";
    }

    public String getDate(long j) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Intrinsics.checkNotNullExpressionValue(calendar, "getInstance()");
        calendar.setTimeInMillis(j);
        String format = simpleDateFormat.format(calendar.getTime());
        Intrinsics.checkNotNullExpressionValue(format, "formatter.format(calendar.time)");
        return format;
    }
}
