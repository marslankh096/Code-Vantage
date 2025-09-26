package com.demo.csvfilereader.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.csvfilereader.AdsGoogle;
import com.demo.csvfilereader.databinding.ActivityFavouriteFilesBinding;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.adapter.FavFilesAdapter;
import com.demo.csvfilereader.model.FilesModelClass;

import io.paperdb.Paper;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;


public final class FavouriteFilesActivity extends AppCompatActivity {

    public FavFilesAdapter adapter;
    private ArrayList<FilesModelClass> arrayList;
    private final Executor executor;
    private final Handler handler = new Handler(Looper.getMainLooper());
    Animation push_animation;
    public ActivityFavouriteFilesBinding view;

    public FavouriteFilesActivity() {
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        Intrinsics.checkNotNullExpressionValue(newSingleThreadExecutor, "newSingleThreadExecutor()");
        this.executor = newSingleThreadExecutor;
    }

    public ActivityFavouriteFilesBinding getView() {
        ActivityFavouriteFilesBinding activityFavouriteFilesBinding = this.view;
        if (activityFavouriteFilesBinding != null) {
            return activityFavouriteFilesBinding;
        }
        Intrinsics.throwUninitializedPropertyAccessException("view");
        return null;
    }

    public void setView(ActivityFavouriteFilesBinding activityFavouriteFilesBinding) {
        Intrinsics.checkNotNullParameter(activityFavouriteFilesBinding, "<set-?>");
        this.view = activityFavouriteFilesBinding;
    }

    public FavFilesAdapter getAdapter() {
        FavFilesAdapter favFilesAdapter = this.adapter;
        if (favFilesAdapter != null) {
            return favFilesAdapter;
        }
        Intrinsics.throwUninitializedPropertyAccessException("adapter");
        return null;
    }

    public void setAdapter(FavFilesAdapter favFilesAdapter) {
        Intrinsics.checkNotNullParameter(favFilesAdapter, "<set-?>");
        this.adapter = favFilesAdapter;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityFavouriteFilesBinding inflate = ActivityFavouriteFilesBinding.inflate(getLayoutInflater());
        Intrinsics.checkNotNullExpressionValue(inflate, "inflate(layoutInflater)");
        setView(inflate);
        setContentView(getView().getRoot());

        AdsGoogle adsGoogle = new AdsGoogle(this);
        adsGoogle.Banner_Show((RelativeLayout) findViewById(R.id.banner), this);
        adsGoogle.Interstitial_Show_Counter(this);


        this.arrayList = new ArrayList<>();
        Paper.init(this);
        this.executor.execute(new FavouriteFilesActivityExcutor(this));
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.button_push);
        getView().backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(FavouriteFilesActivity.this.push_animation);
                Intrinsics.checkNotNullParameter(this, "this");
                FavouriteFilesActivity.this.onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(1 << 26);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    public static void FavouriteFilesActivityExecutorperform(FavouriteFilesActivity favouriteFilesActivity) {
        Intrinsics.checkNotNullParameter(favouriteFilesActivity, "this");
        favouriteFilesActivity.getData();
        favouriteFilesActivity.handler.post(new FavouriteFilesActivityGetdata(favouriteFilesActivity));
    }

    public static void FavouriteFilesActivityGetdata(FavouriteFilesActivity favouriteFilesActivity) {
        try {
            Intrinsics.checkNotNullParameter(favouriteFilesActivity, "this");
            ArrayList<FilesModelClass> arrayList = favouriteFilesActivity.arrayList;
            ArrayList<FilesModelClass> arrayList2 = null;
            if (arrayList == null) {
                Intrinsics.throwUninitializedPropertyAccessException("arrayList");
                arrayList = null;
            }
            if (arrayList.size() < 1) {
                favouriteFilesActivity.getView().nofileTV.setVisibility(View.VISIBLE);
                return;
            }
            favouriteFilesActivity.getView().nofileTV.setVisibility(View.GONE);
            favouriteFilesActivity.getView().favRV.setLayoutManager(new LinearLayoutManager(favouriteFilesActivity));
            favouriteFilesActivity.getView().favRV.setHasFixedSize(true);
            ArrayList<FilesModelClass> arrayList3 = favouriteFilesActivity.arrayList;
            if (arrayList3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("arrayList");
                arrayList3 = null;
            }
            CollectionsKt.reverse(arrayList3);
            ArrayList<FilesModelClass> arrayList4 = favouriteFilesActivity.arrayList;
            if (arrayList4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("arrayList");
            } else {
                arrayList2 = arrayList4;
            }
            favouriteFilesActivity.setAdapter(new FavFilesAdapter(favouriteFilesActivity, arrayList2, "fav"));
            favouriteFilesActivity.getView().favRV.setAdapter(favouriteFilesActivity.getAdapter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData() {
        try {
            ArrayList arrayList = (ArrayList) Paper.book().read("fav", new ArrayList());
            Intrinsics.checkNotNull(arrayList);
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                int i2 = i + 1;
                File file = new File((String) arrayList.get(i));
                String file_Name = file.getName();
                String stringSizeLengthFile = getStringSizeLengthFile(file.length());
                String absolutePath = file.getAbsolutePath();
                String date = getDate(file.lastModified());
                if (new File(absolutePath).exists()) {
                    ArrayList<FilesModelClass> arrayList2 = this.arrayList;
                    if (arrayList2 == null) {
                        arrayList2 = null;
                    }
                    arrayList2.add(new FilesModelClass(file_Name, stringSizeLengthFile, absolutePath.toString(), date));
                }
                i = i2;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
