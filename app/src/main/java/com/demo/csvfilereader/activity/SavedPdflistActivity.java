package com.demo.csvfilereader.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.csvfilereader.AdsGoogle;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.adapter.ConversionsAdapter;
import com.demo.csvfilereader.model.Conversion;
import com.demo.csvfilereader.utility.PreferenceUtility;
import com.demo.csvfilereader.utility.SortUtility;

import eu.amirs.JSON;

import java.io.File;
import java.util.ArrayList;


public class SavedPdflistActivity extends AppCompatActivity {
    ImageView imageViewBack;
    RecyclerView recyclerView = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_pdflist);

        AdsGoogle adsGoogle = new AdsGoogle(this);
        adsGoogle.Banner_Show((RelativeLayout) findViewById(R.id.banner), this);
        adsGoogle.Interstitial_Show_Counter(this);



        initialize();
    }

    public void deleteSavedVideo(String str) {
        ArrayList<Conversion> convertedFiles = getConvertedFiles(this);
        for (int i = 0; i < convertedFiles.size(); i++) {
            if (convertedFiles.get(i).CONVERTED_FILE_PATH.equals(str)) {
                convertedFiles.remove(i);
            }
        }
        saveConvertedFiles(this, convertedFiles);
    }

    public static void saveConvertedFiles(Context context, ArrayList<Conversion> arrayList) {
        PreferenceUtility.saveIntegerPrefrence(context, Conversion.TAG_CONVERSIONS_SIZE, arrayList.size());
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(i + "_" + Conversion.TAG_CONVERTED_FILE_PATH);
            arrayList2.add(arrayList.get(i).CONVERTED_FILE_PATH);
            arrayList2.add(i + "_" + Conversion.TAG_CONVERTED_FILE_NAME);
            arrayList2.add(arrayList.get(i).CONVERTED_FILE_NAME);
            arrayList2.add(i + "_" + Conversion.TAG_CONVERTED_FILE_DURATION);
            arrayList2.add(arrayList.get(i).CONVERTED_FILE_DURATION);
            arrayList2.add(i + "_" + Conversion.TAG_CONVERTED_FILE_SIZE);
            arrayList2.add(arrayList.get(i).CONVERTED_FILE_SIZE);
        }
        PreferenceUtility.saveStringPrefrence(context, Conversion.TAG_CONVERSIONS, JSON.create(JSON.dic((String[]) arrayList2.toArray(new String[arrayList2.size()]))).toString());
    }

    private void initialize() {
        this.imageViewBack = (ImageView) findViewById(R.id.backBtn_pdf);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setHasFixedSize(true);
        refreshConversionList();
        Window window = getWindow();
        window.clearFlags(1 << 26);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        this.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedPdflistActivity.this.onBackPressed();
            }
        });
    }

    public static ArrayList<Conversion> getConvertedFiles(Context context) {
        ArrayList<Conversion> arrayList = new ArrayList<>();
        String stringPrefrence = PreferenceUtility.getStringPrefrence(context, Conversion.TAG_CONVERSIONS, null);
        int integerPrefrence = PreferenceUtility.getIntegerPrefrence(context, Conversion.TAG_CONVERSIONS_SIZE, 0);
        try {
            if (stringPrefrence != null) {
                JSON json = new JSON(stringPrefrence);
                for (int i = 0; i < integerPrefrence; i++) {
                    Conversion conversion = new Conversion();
                    conversion.CONVERTED_FILE_PATH = json.key(i + "_" + Conversion.TAG_CONVERTED_FILE_PATH).stringValue();
                    conversion.CONVERTED_FILE_NAME = json.key(i + "_" + Conversion.TAG_CONVERTED_FILE_NAME).stringValue();
                    conversion.CONVERTED_FILE_DURATION = json.key(i + "_" + Conversion.TAG_CONVERTED_FILE_DURATION).stringValue();
                    conversion.CONVERTED_FILE_SIZE = json.key(i + "_" + Conversion.TAG_CONVERTED_FILE_SIZE).stringValue();
                    if (new File(conversion.CONVERTED_FILE_PATH).exists()) {
                        arrayList.add(conversion);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYTAG", "ErrorNo: getConvertedFiles:" + e);
        }
        return arrayList;
    }

    public void refreshConversionList() {
        ArrayList<Conversion> reverseConversions = SortUtility.reverseConversions(getConvertedFiles(this));
        if (reverseConversions.size() > 0) {
            ((TextView) findViewById(R.id.no_media)).setVisibility(View.GONE);
        } else {
            ((TextView) findViewById(R.id.no_media)).setVisibility(View.VISIBLE);
        }
        this.recyclerView.setAdapter(new ConversionsAdapter(getApplicationContext(), this, reverseConversions));
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
