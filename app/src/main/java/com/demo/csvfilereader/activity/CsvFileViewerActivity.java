package com.demo.csvfilereader.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.documentfile.provider.DocumentFile;

import com.demo.csvfilereader.AdsGoogle;
import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.filter.Filter;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.google.android.material.textfield.TextInputLayout;

import com.itextpdf.text.PageSize;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.adapter.TableViewAdapter;
import com.demo.csvfilereader.file.FileFolderPicker;
import com.demo.csvfilereader.file.FileHelper;
import com.demo.csvfilereader.file.StorageAccess;
import com.demo.csvfilereader.listener.TableViewListener;
import com.demo.csvfilereader.model.Cell;
import com.demo.csvfilereader.model.ColumnHeader;
import com.demo.csvfilereader.model.Conversion;
import com.demo.csvfilereader.model.RowHeader;
import com.demo.csvfilereader.model.SearchResult;
import com.demo.csvfilereader.utility.ClipboardUtility;
import com.demo.csvfilereader.utility.CsvUtility;
import com.demo.csvfilereader.utility.DeviceInfo;
import com.demo.csvfilereader.utility.NumberFormatUtility;
import com.demo.csvfilereader.utility.PDFUtility;
import com.demo.csvfilereader.utility.PathsUtility;
import com.demo.csvfilereader.utility.PreferenceUtility;
import com.demo.csvfilereader.utility.TableUtility;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import eu.amirs.JSON;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import kotlin.jvm.internal.Intrinsics;

import org.apache.commons.io.FilenameUtils;

public class CsvFileViewerActivity extends AppCompatActivity {
    public static final String CSV_FILE = "CSV_FILE";
    public static String CSV_FILE_PATH = "CSV_FILE_PATH";
    TableViewAdapter adapter;
    List<List<Cell>> cell_list;
    String[] column_filter_values;
    List<ColumnHeader> column_header_list;
    String[] column_titles;
    ArrayList<List<String>> csv_data;
    ImageView imageViewBack;
    ImageView imageViewLinenumber;
    ImageView imageViewNewFile;
    ImageView imageViewRefresh;

    ImageView imageViewSave;
    ImageView imageViewShare;


    ImageView imageViewscrollbottom;
    ImageView imageViewscrollto;
    ImageView imageViewscrolltop;
    ImageView imageViewtextAlignment;

    ImageView imageViewtextbackground;
    ImageView imageViewtextcolor;
    ImageView imageViewtextfont;
    ImageView imageViewtextsize;
    ArrayList<List<String>> master_csv_data;
    public FileFolderPicker picker;
    Animation push_animation;


    RelativeLayout relativeLayoutLinenumber;
    RelativeLayout relativeLayoutNewFile;

    RelativeLayout relativeLayoutSave;
    RelativeLayout relativeLayoutScrollbottom;
    RelativeLayout relativeLayoutScrolltop;
    RelativeLayout relativeLayoutShare;

    RelativeLayout relativeLayoutTextalignment;
    RelativeLayout relativeLayoutTextbackground;
    RelativeLayout relativeLayoutTextcolor;
    RelativeLayout relativeLayoutTextfont;
    RelativeLayout relativeLayoutTextsize;
    RelativeLayout relativeLayoutfirst;
    RelativeLayout relativeLayoutscrollto;
    List<RowHeader> row_header_list;
    TableView table;
    Filter tableFilter;
    TableUtility table_utility;
    TextView textViewTitle;
    public int COLUMN_SIZE = 0;
    public int ROW_SIZE = 0;
    int column_to_scroll_to = -1;
    int filter_column_search_index = 0;
    String query = "";
    int row_column_width = 0;
    ArrayList<SearchResult> search_data = new ArrayList<>();
    int search_display_index = 0;
    boolean search_mode = false;
    boolean show_cell_highlight = true;
    boolean show_line_number = true;
    int table_background_color = -1;
    String table_filter_value = "";
    int table_highlight_color = Color.parseColor("#F7EFDA");
    int text_alignment = 17;
    int text_color = ViewCompat.MEASURED_STATE_MASK;
    int text_font = 0;
    int text_size = 14;
    String title = "";
    int pos = 0;


    public enum ProgressType {
        SPINNER,
        BAR
    }


    public enum TextStyle {
        NORMAL,
        BOLD,
        ITALIC,
        BOLD_ITALIC
    }

    public void updateTableCount(int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_csv_file_viewer);

        AdsGoogle adsGoogle = new AdsGoogle(this);
        adsGoogle.Interstitial_Show_Counter(this);


        this.picker = new FileFolderPicker(this);
        this.push_animation = AnimationUtils.loadAnimation(this, R.anim.button_push);
        initialize();
        clicklisteners();
        Window window = getWindow();
        window.clearFlags(1 << 26);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        if (getIntent().hasExtra(CSV_FILE)) {
            try {
                openFile();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            openContent();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
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

    public static ArrayList<Conversion> getConvertedFiles(Context context) {
        ArrayList<Conversion> arrayList = new ArrayList<>();
        String stringPrefrence = PreferenceUtility.getStringPrefrence(context, Conversion.TAG_CONVERSIONS, null);
        int integerPrefrence = PreferenceUtility.getIntegerPrefrence(context, Conversion.TAG_CONVERSIONS_SIZE, 0);
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
        return arrayList;
    }


    public class GetCsvData extends AsyncTask<String, Integer, String> {
        ArrayList<List<String>> csv_data;
        InputStream csv_file_inputStream;
        File csv_file_path;

        @Override
        public void onPreExecute() {
        }

        public GetCsvData(CsvFileViewerActivity csvFileViewerActivity, File file) {
            this.csv_data = new ArrayList<>();
            this.csv_file_inputStream = null;
            this.csv_file_path = file;
        }

        public GetCsvData(CsvFileViewerActivity csvFileViewerActivity, InputStream inputStream) {
            this.csv_data = new ArrayList<>();
            this.csv_file_inputStream = null;
            this.csv_file_path = null;
            this.csv_file_inputStream = inputStream;
        }

        @Override
        public String doInBackground(String... strArr) {
            int i = 0;
            if (this.csv_file_inputStream == null) {
                CsvParserSettings csvParserSettings = new CsvParserSettings();
                csvParserSettings.detectFormatAutomatically();
                List<String[]> parseAll = new CsvParser(csvParserSettings).parseAll(this.csv_file_path);

                parseAll.size();

                while (i < parseAll.size()) {
                    this.csv_data.add(new ArrayList(Arrays.asList(parseAll.get(i))));
                    i++;
                }
                return null;
            }
            CsvParserSettings csvParserSettings2 = new CsvParserSettings();
            csvParserSettings2.detectFormatAutomatically();
            List<String[]> parseAll2 = new CsvParser(csvParserSettings2).parseAll(this.csv_file_inputStream);
            parseAll2.size();
            while (i < parseAll2.size()) {
                this.csv_data.add(new ArrayList(Arrays.asList(parseAll2.get(i))));
                i++;
            }


            return null;
        }

        @Override
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            CsvFileViewerActivity.this.showAlertDialog(this.csv_data);
            CsvFileViewerActivity.this.initializeConverter(this.csv_data);
        }
    }

    public void initializeConverter(final ArrayList<List<String>> arrayList) {
        this.csv_data = arrayList;
    }

    public void showAlertDialog(ArrayList<List<String>> csv_data) {
        if (new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + File.separator + getString(R.string.app_name) + File.separator + this.title + ".pdf"))).exists()) {
            Toast.makeText(this, "Already Saved as PDF in the" + Environment.DIRECTORY_DOCUMENTS + File.separator + getString(R.string.app_name) + File.separator + "...", Toast.LENGTH_SHORT).show();
            return;
        }
        PDFUtility.createPDF(this, this.title, csv_data, PageSize.A4);
    }

    private void initialize() {
        this.textViewTitle = (TextView) findViewById(R.id.txttitle);
        this.relativeLayoutfirst = (RelativeLayout) findViewById(R.id.relativelayoutfirst);
        this.relativeLayoutShare = (RelativeLayout) findViewById(R.id.relativeLayoutShare);
        this.relativeLayoutNewFile = (RelativeLayout) findViewById(R.id.btn_Newfile);
        this.relativeLayoutSave = (RelativeLayout) findViewById(R.id.relativeLayouttext_Save);


        this.relativeLayoutLinenumber = (RelativeLayout) findViewById(R.id.relativeLayoutlinenumber);
        this.relativeLayoutScrolltop = (RelativeLayout) findViewById(R.id.relativeLayoutscrolltop);
        this.relativeLayoutScrollbottom = (RelativeLayout) findViewById(R.id.relativeLayoutscrollbottom);
        this.relativeLayoutscrollto = (RelativeLayout) findViewById(R.id.relativeLayoutscrollto);
        this.relativeLayoutTextsize = (RelativeLayout) findViewById(R.id.relativeLayouttext_size);
        this.relativeLayoutTextalignment = (RelativeLayout) findViewById(R.id.relativeLayouttext_alignment);
        this.relativeLayoutTextcolor = (RelativeLayout) findViewById(R.id.relativeLayouttext_color);
        this.relativeLayoutTextfont = (RelativeLayout) findViewById(R.id.relativeLayouttext_font);
        this.relativeLayoutTextbackground = (RelativeLayout) findViewById(R.id.relativeLayouttext_background);


        this.imageViewBack = (ImageView) findViewById(R.id.backBtn);
        this.imageViewRefresh = (ImageView) findViewById(R.id.btn_Refresh);
        this.imageViewShare = (ImageView) findViewById(R.id.btnshare);
        this.imageViewNewFile = (ImageView) findViewById(R.id.btnNewfile);
        this.imageViewSave = (ImageView) findViewById(R.id.btn_Save);


        this.imageViewLinenumber = (ImageView) findViewById(R.id.btn_linenumber);
        this.imageViewscrolltop = (ImageView) findViewById(R.id.btn_scrolltop);
        this.imageViewscrollbottom = (ImageView) findViewById(R.id.btn_scrollbottom);
        this.imageViewscrollto = (ImageView) findViewById(R.id.btn_scrollto);
        this.imageViewtextsize = (ImageView) findViewById(R.id.btn_text_size);
        this.imageViewtextAlignment = (ImageView) findViewById(R.id.btn_text_alignment);
        this.imageViewtextcolor = (ImageView) findViewById(R.id.btn_text_color);
        this.imageViewtextfont = (ImageView) findViewById(R.id.btn_text_font);
        this.imageViewtextbackground = (ImageView) findViewById(R.id.btn_text_background);


    }

    private void clicklisteners() {
        this.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.onBackPressed();
            }
        });
        this.relativeLayoutSave.setOnClickListener(new AnonymousClass2());
        this.imageViewRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.reloadTable();
                ((RelativeLayout) CsvFileViewerActivity.this.findViewById(R.id.table_holder)).setBackgroundColor(-1);
                CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_h_icn);
                CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
                CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_icn);
                CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_icn);


                CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);
                CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
                CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
                CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
                CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
                CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
                CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
                CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
                CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


            }
        });
        this.relativeLayoutNewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                final Dialog dialog = new Dialog(CsvFileViewerActivity.this, R.style.TransparentBackground);
                dialog.setContentView(R.layout.deletedialog);
                TextView textView = (TextView) dialog.findViewById(R.id.dialogtitletxt);
                textView.setText(R.string.newfileopen);
                textView.setGravity(17);
                ((LinearLayout) dialog.findViewById(R.id.Yes_del)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        CsvFileViewerActivity.this.startActivity(new Intent(CsvFileViewerActivity.this.getApplicationContext(), CSVFilesActivity.class));
                        CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
                        CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.new_file_h_icn);
                        CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_icn);
                        CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_icn);


                        CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);
                        CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
                        CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
                        CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
                        CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
                        CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
                        CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
                        CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
                        CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


                    }
                });
                ((LinearLayout) dialog.findViewById(R.id.No_del)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        this.relativeLayoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
                CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
                CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_h_icn);
                CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_icn);


                CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);
                CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
                CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
                CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
                CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
                CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
                CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
                CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
                CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


                CsvFileViewerActivity.this.shareFile(CsvFileViewerActivity.CSV_FILE_PATH, CsvFileViewerActivity.this);
            }
        });


        this.relativeLayoutLinenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_h_icn);


                CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
                CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
                CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
                CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
                CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
                CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
                CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
                CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


                CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
                CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
                CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_icn);
                CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_icn);
                CsvFileViewerActivity.this.setLineNumberDialog();
            }
        });
        this.relativeLayoutScrolltop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_h_icn);
                CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);


                CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
                CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
                CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
                CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
                CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
                CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
                CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


                CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
                CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
                CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_icn);
                CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_icn);
                CsvFileViewerActivity.this.scrollTo(0);
            }
        });
        this.relativeLayoutScrollbottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_h_icn);
                CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
                CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);


                CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
                CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
                CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
                CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
                CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
                CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


                CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
                CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
                CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_icn);
                CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_icn);
                if (CsvFileViewerActivity.this.adapter != null) {
                    CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                    csvFileViewerActivity.scrollTo(csvFileViewerActivity.adapter.getCellRecyclerViewAdapter().getItemCount() - 1);
                }
            }
        });
        this.relativeLayoutscrollto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_h_icn);
                CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
                CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
                CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);


                CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
                CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
                CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
                CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
                CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


                CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
                CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
                CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_icn);
                CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_icn);
                CsvFileViewerActivity.this.scrollToPosition();
            }
        });
        this.relativeLayoutTextsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_h_icn);
                CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
                CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
                CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
                CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);


                CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
                CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
                CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
                CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


                CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
                CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
                CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_icn);
                CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_icn);
                CsvFileViewerActivity.this.setTextSizeDialog();
            }
        });
        this.relativeLayoutTextalignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_h_icn);
                CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
                CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
                CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
                CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
                CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);


                CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
                CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
                CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


                CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
                CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
                CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_icn);
                CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_icn);
                CsvFileViewerActivity.this.setTextAlignment();
            }
        });
        this.relativeLayoutTextcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_h_icn);
                CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
                CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
                CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
                CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
                CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
                CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);


                CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
                CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


                CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
                CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
                CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_icn);
                CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_icn);
                CsvFileViewerActivity.this.setTextColor();
            }
        });
        this.relativeLayoutTextfont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_h_icn);
                CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
                CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
                CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
                CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
                CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
                CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
                CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);


                CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


                CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
                CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
                CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_icn);
                CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_icn);
                CsvFileViewerActivity.this.setTextFontDialog();
            }
        });
        this.relativeLayoutTextbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_h_icn);
                CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
                CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
                CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
                CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
                CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
                CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
                CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
                CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);


                CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
                CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
                CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_icn);
                CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_icn);
                CsvFileViewerActivity.this.setTableBackgroundColor();
            }
        });


    }


    public class AnonymousClass2 implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(CsvFileViewerActivity.this, R.style.TransparentBackground);
            dialog.setContentView(R.layout.deletedialog);
            TextView textView = (TextView) dialog.findViewById(R.id.dialogtitletxt);
            textView.setText(R.string.pdfcreate);
            textView.setGravity(17);
            ((LinearLayout) dialog.findViewById(R.id.Yes_del)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    CsvFileViewerActivity.this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
                    CsvFileViewerActivity.this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
                    CsvFileViewerActivity.this.imageViewShare.setImageResource(R.drawable.share_icn);
                    CsvFileViewerActivity.this.imageViewSave.setImageResource(R.drawable.save_h_icn);


                    CsvFileViewerActivity.this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);
                    CsvFileViewerActivity.this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
                    CsvFileViewerActivity.this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
                    CsvFileViewerActivity.this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
                    CsvFileViewerActivity.this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
                    CsvFileViewerActivity.this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
                    CsvFileViewerActivity.this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
                    CsvFileViewerActivity.this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
                    CsvFileViewerActivity.this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                CsvFileViewerActivity.CSV_FILE_PATH = CsvFileViewerActivity.this.getIntent().getExtras().getCharSequence(CsvFileViewerActivity.CSV_FILE).toString();
                                new GetCsvData(CsvFileViewerActivity.this, new File(CsvFileViewerActivity.CSV_FILE_PATH)).execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1000L);
                }
            });
            ((LinearLayout) dialog.findViewById(R.id.No_del)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public final void shareFile(String file, Context context) {
        Intrinsics.checkNotNullParameter(file, "file");
        Intrinsics.checkNotNullParameter(context, "context");
        Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(file));
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("*/*");
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.putExtra("android.intent.extra.SUBJECT", Intrinsics.stringPlus("Sharing File from", context.getString(R.string.app_name)));
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.app_name)));
    }

    private void openFile() {
        CSV_FILE_PATH = getIntent().getExtras().getCharSequence(CSV_FILE).toString();


        try {
            Uri uriForFile = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(CSV_FILE_PATH));
            this.title = FilenameUtils.getBaseName(DocumentFile.fromSingleUri(this, uriForFile).getName());
            CsvUtility.getCsvData(this, getContentResolver().openInputStream(uriForFile));
        } catch (Exception e2) {
            try {
                e2.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }

        this.textViewTitle.setText(this.title);
        if (getSupportActionBar() != null) {
            this.textViewTitle.setText(this.title);
        }
    }

    public Uri getUri() {
        Intent intent = getIntent();
        Uri uri = null;
        if (intent == null) {
            return null;
        }
        if (intent.getClipData() == null) {
            return intent.getData();
        }
        ClipData clipData = intent.getClipData();
        if (clipData == null) {
            return null;
        }
        for (int i = 0; i < clipData.getItemCount(); i++) {
            ClipData.Item itemAt = clipData.getItemAt(i);
            if (itemAt != null) {
                uri = itemAt.getUri();
            }
        }
        return uri;
    }

    private void openContent() {
        StorageAccess.requestStorageAccess(this, new StorageAccess.OnStorageAccessListener() {
            @Override
            public void onStorageAccessResult(boolean z) {
                if (z) {
                    CsvFileViewerActivity.this.openContentWeHaveStorageAccess();
                }
            }
        });
    }

    public void openContentWeHaveStorageAccess() {
        Uri uri = getUri();
        if (uri != null) {
            if (DeviceInfo.isLegacyDevice()) {
                try {
                    String name = FilenameUtils.getName(PathsUtility.getPath(this, uri));
                    if (!name.toLowerCase(Locale.getDefault()).endsWith(".csv")) {
                        AlertDialog.Builder cancelable = new AlertDialog.Builder(this).setCancelable(false);
                        cancelable.setMessage(((Object) getString(R.string.prompt_cannot_open_file)) + "\n\n" + name).setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                CsvFileViewerActivity.this.finish();
                            }
                        }).create().show();
                        return;
                    }
                    this.textViewTitle.setText(name);
                    CsvUtility.getCsvData(this, getContentResolver().openInputStream(uri));
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            this.textViewTitle.setText(DocumentFile.fromSingleUri(this, uri).getName());
            try {
                CsvUtility.getCsvData(this, getContentResolver().openInputStream(uri));
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
        cannotOpenFile();
    }

    private void cannotOpenFile() {
        new AlertDialog.Builder(this).setCancelable(false).setMessage(getString(R.string.prompt_cannot_open_file)).setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                CsvFileViewerActivity.this.finish();
            }
        }).create().show();
    }

    public void highlightResult(final SearchResult searchResult) {
        TableView tableView = this.table;
        if (tableView == null || tableView.getAdapter() == null) {
            return;
        }
        this.table.scrollToColumnPosition(searchResult.COLUMN);
        this.table.scrollToRowPosition(searchResult.ROW - 1);
        showProgressBar("", ProgressType.SPINNER);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (CsvFileViewerActivity.this.table != null) {
                    if (CsvFileViewerActivity.this.table.isRowVisible(searchResult.ROW) && CsvFileViewerActivity.this.table.isColumnVisible(searchResult.COLUMN)) {
                        CsvFileViewerActivity.this.table.setSelectedCell(searchResult.COLUMN, searchResult.ROW - 1);
                    }
                    CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                    Toast makeText = Toast.makeText(csvFileViewerActivity, CsvFileViewerActivity.this.column_titles[searchResult.COLUMN] + "\n\nRow " + searchResult.ROW, Toast.LENGTH_LONG);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                }
                CsvFileViewerActivity.this.hideProgress();
            }
        }, 1200L);
    }

    public void navigateSearchResult(int i) {
        if (i >= this.search_data.size()) {
            this.search_display_index = 0;
            i = 0;
        }
        if (i < 0) {
            i = this.search_data.size() - 1;
            this.search_display_index = i;
        }
        highlightResult(this.search_data.get(i));
    }

    public void makeTable(ArrayList<List<String>> arrayList, boolean z) {
        if (z) {
            this.master_csv_data = arrayList;
        }
        this.csv_data = arrayList;
        this.table_utility = new TableUtility(arrayList);
        this.COLUMN_SIZE = this.table_utility.getColumnSize();
        this.ROW_SIZE = this.table_utility.getRowSize();
        if (z) {
            new CreateTable(this, (RelativeLayout) findViewById(R.id.table_holder), this.table_utility).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new ReCreateTable(this, (RelativeLayout) findViewById(R.id.table_holder), this.table_utility).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void unSortTable() {
        if (this.table == null || this.adapter == null) {
            return;
        }
        this.table.getAdapter().setAllItems(this.column_header_list, this.row_header_list, this.cell_list);
        this.table.getAdapter().notifyDataSetChanged();
    }


    public class CreateTable extends AsyncTask<String, Integer, String> {
        Context context;
        RelativeLayout table_holder;
        TableUtility table_utility;

        public CreateTable(Context context2, RelativeLayout relativeLayout, TableUtility tableUtility) {
            this.context = null;
            this.table_holder = null;
            this.table_utility = null;
            this.context = context2;
            this.table_utility = tableUtility;
            this.table_holder = relativeLayout;
            CsvFileViewerActivity.this.table = new TableView(context2);
        }

        @Override
        public void onPreExecute() {
            CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
            csvFileViewerActivity.showProgressBar(csvFileViewerActivity.getString(R.string.prompt_creating_table), ProgressType.SPINNER);
        }

        @Override
        public String doInBackground(String... strArr) {
            CsvFileViewerActivity.this.adapter = new TableViewAdapter(this.context);
            CsvFileViewerActivity.this.table.setAdapter(CsvFileViewerActivity.this.adapter);
            CsvFileViewerActivity.this.column_header_list = this.table_utility.getColumnHeaderList();
            CsvFileViewerActivity.this.row_header_list = this.table_utility.getRowHeaderList();
            CsvFileViewerActivity.this.cell_list = this.table_utility.getCellList();
            if (CsvFileViewerActivity.this.row_header_list.size() == 0) {
                CsvFileViewerActivity.this.row_header_list = this.table_utility.getDummyRowHeaderList();
            }
            if (CsvFileViewerActivity.this.cell_list.size() == 0) {
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                csvFileViewerActivity.cell_list = this.table_utility.getDummyCellList(csvFileViewerActivity.column_header_list.size());
            }
            CsvFileViewerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (CsvFileViewerActivity.this.adapter != null) {
                        CsvFileViewerActivity.this.adapter.setAllItems(CsvFileViewerActivity.this.column_header_list, CsvFileViewerActivity.this.row_header_list, CsvFileViewerActivity.this.cell_list);
                    }
                }
            });
            return null;
        }

        @Override
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            CsvFileViewerActivity.this.hideProgress();
            this.table_holder.removeAllViews();
            this.table_holder.addView(CsvFileViewerActivity.this.table);
            TableView tableView = CsvFileViewerActivity.this.table;
            CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;


            tableView.setTableViewListener(new TableViewListener(csvFileViewerActivity, csvFileViewerActivity.table));
            CsvFileViewerActivity csvFileViewerActivity2 = CsvFileViewerActivity.this;
            csvFileViewerActivity2.tableFilter = new Filter(csvFileViewerActivity2.table);
            CsvFileViewerActivity csvFileViewerActivity3 = CsvFileViewerActivity.this;
            csvFileViewerActivity3.row_column_width = csvFileViewerActivity3.table.getRowHeaderWidth();
            CsvFileViewerActivity csvFileViewerActivity4 = CsvFileViewerActivity.this;
            csvFileViewerActivity4.updateTableCount(csvFileViewerActivity4.ROW_SIZE);
            if (CsvFileViewerActivity.this.csv_data.size() >= 1) {
                CsvFileViewerActivity csvFileViewerActivity5 = CsvFileViewerActivity.this;
                csvFileViewerActivity5.column_filter_values = new String[csvFileViewerActivity5.csv_data.get(0).size()];
                CsvFileViewerActivity csvFileViewerActivity6 = CsvFileViewerActivity.this;
                csvFileViewerActivity6.column_titles = new String[csvFileViewerActivity6.csv_data.get(0).size()];
            } else {
                CsvFileViewerActivity.this.column_filter_values = new String[0];
                CsvFileViewerActivity.this.column_titles = new String[0];
            }
            for (int i = 0; i < CsvFileViewerActivity.this.column_filter_values.length; i++) {
                CsvFileViewerActivity.this.column_filter_values[i] = "";
                if (CsvFileViewerActivity.this.csv_data.get(0).get(i) != null) {
                    CsvFileViewerActivity.this.column_titles[i] = CsvFileViewerActivity.this.csv_data.get(0).get(i);
                } else {
                    CsvFileViewerActivity.this.column_titles[i] = "";
                }
            }
        }
    }


    public class ReCreateTable extends AsyncTask<String, Integer, String> {
        Context context;
        RelativeLayout table_holder;
        TableUtility table_utility;

        public ReCreateTable(Context context2, RelativeLayout relativeLayout, TableUtility tableUtility) {
            this.context = null;
            this.table_holder = null;
            this.table_utility = null;
            this.context = context2;
            this.table_utility = tableUtility;
            this.table_holder = relativeLayout;
            CsvFileViewerActivity.this.table = new TableView(context2);
        }

        @Override
        public void onPreExecute() {
            CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
            csvFileViewerActivity.showProgressBar(csvFileViewerActivity.getString(R.string.prompt_recreating_table), ProgressType.SPINNER);
        }

        @Override
        public String doInBackground(String... strArr) {
            CsvFileViewerActivity.this.adapter = new TableViewAdapter(this.context);
            CsvFileViewerActivity.this.table.setAdapter(CsvFileViewerActivity.this.adapter);
            CsvFileViewerActivity.this.column_header_list = this.table_utility.getColumnHeaderList();
            CsvFileViewerActivity.this.row_header_list = this.table_utility.getRowHeaderList();
            CsvFileViewerActivity.this.cell_list = this.table_utility.getCellList();
            if (CsvFileViewerActivity.this.row_header_list.size() == 0) {
                CsvFileViewerActivity.this.row_header_list = this.table_utility.getDummyRowHeaderList();
            }
            if (CsvFileViewerActivity.this.cell_list.size() == 0) {
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                csvFileViewerActivity.cell_list = this.table_utility.getDummyCellList(csvFileViewerActivity.column_header_list.size());
            }
            CsvFileViewerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (CsvFileViewerActivity.this.adapter != null) {
                        CsvFileViewerActivity.this.adapter.setAllItems(CsvFileViewerActivity.this.column_header_list, CsvFileViewerActivity.this.row_header_list, CsvFileViewerActivity.this.cell_list);
                    }
                }
            });
            return null;
        }

        @Override
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            CsvFileViewerActivity.this.hideProgress();
            this.table_holder.removeAllViews();
            this.table_holder.addView(CsvFileViewerActivity.this.table);
            TableView tableView = CsvFileViewerActivity.this.table;
            CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;


            tableView.setTableViewListener(new TableViewListener(csvFileViewerActivity, csvFileViewerActivity.table));
            CsvFileViewerActivity csvFileViewerActivity2 = CsvFileViewerActivity.this;
            csvFileViewerActivity2.tableFilter = new Filter(csvFileViewerActivity2.table);
            CsvFileViewerActivity csvFileViewerActivity3 = CsvFileViewerActivity.this;
            csvFileViewerActivity3.row_column_width = csvFileViewerActivity3.table.getRowHeaderWidth();
            CsvFileViewerActivity csvFileViewerActivity4 = CsvFileViewerActivity.this;
            csvFileViewerActivity4.updateTableCount(csvFileViewerActivity4.ROW_SIZE);
            if (CsvFileViewerActivity.this.search_mode && CsvFileViewerActivity.this.search_data.size() > 0) {
                CsvFileViewerActivity.this.search_display_index = 0;
                CsvFileViewerActivity csvFileViewerActivity5 = CsvFileViewerActivity.this;
                csvFileViewerActivity5.highlightResult(csvFileViewerActivity5.search_data.get(0));
            }
            if (CsvFileViewerActivity.this.column_to_scroll_to != -1) {
                if (CsvFileViewerActivity.this.table != null) {
                    CsvFileViewerActivity.this.table.scrollToColumnPosition(CsvFileViewerActivity.this.column_to_scroll_to);
                }
                CsvFileViewerActivity.this.column_to_scroll_to = -1;
            }
        }
    }

    public void copySelectedCell() {
        if (this.table == null || this.adapter == null) {
            return;
        }
        int selectedColumn = this.table.getSelectedColumn();
        int selectedRow = this.table.getSelectedRow();
        if (selectedColumn == -1 || selectedRow == -1) {
            Toast.makeText(this, getString(R.string.prompt_select_cell), Toast.LENGTH_LONG).show();
        } else {
            ClipboardUtility.copyToClipboard(this, this.adapter.getCellItem(selectedColumn, selectedRow).getContent().toString(), null);
        }
    }

    public void copySelectedRow() {
        String[] strArr;
        int selectedRow = 0;
        TableView tableView = this.table;
        if (tableView == null || this.adapter == null || (strArr = this.column_titles) == null) {
            return;
        }
        int length = strArr.length;
        if (tableView.getSelectedRow() == -1) {
            Toast.makeText(this, getString(R.string.prompt_select_row), Toast.LENGTH_LONG).show();
            return;
        }
        String str = "";
        for (int i = 0; i < length; i++) {
            str = str + this.adapter.getCellItem(i, selectedRow).getContent().toString() + ",\n";
        }
        ClipboardUtility.copyToClipboard(this, str, "Row '" + (selectedRow + 1) + "' value(s) " + getString(R.string.prompt_row_column_copy));
    }

    @SuppressLint("ResourceType")
    public void copyColumn(int i) {
        TableView tableView = this.table;
        if (tableView == null || this.adapter == null) {
            return;
        }
        if (i < 0) {
            i = tableView.getSelectedColumn();
        }
        if (i < 0) {
            Toast.makeText(this, getString(R.string.prompt_select_column), Toast.LENGTH_LONG).show();
            return;
        }
        final List<Cell> cellColumnItems = this.adapter.getCellColumnItems(i);
        final int size = cellColumnItems.size();
        if (size <= 500) {
            copySelectedColumn(i);
            return;
        }
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(70, 30, 40, 10);
        TextInputLayout textInputLayout = new TextInputLayout(this);
        final EditText editText = new EditText(this);
        editText.setText(String.valueOf(0));
        editText.setLayoutParams(layoutParams);
        editText.setInputType(3);
        textInputLayout.setLayoutParams(layoutParams);
        textInputLayout.addView(editText);
        textInputLayout.setHint(getString(R.string.start_row));
        textInputLayout.setDefaultHintTextColor(ColorStateList.valueOf(ViewCompat.MEASURED_STATE_MASK));
        textInputLayout.setBoxBackgroundColor(ContextCompat.getColor(this, 17170443));
        textInputLayout.setBoxBackgroundMode(2);
        TextInputLayout textInputLayout2 = new TextInputLayout(this);
        final EditText editText2 = new EditText(this);
        editText2.setText(String.valueOf((int) 500));
        editText2.setLayoutParams(layoutParams);
        editText2.setInputType(3);
        textInputLayout2.setLayoutParams(layoutParams);
        textInputLayout2.addView(editText2);
        textInputLayout2.setHint(getString(R.string.end_row));
        textInputLayout2.setEnabled(false);
        textInputLayout2.setDefaultHintTextColor(ColorStateList.valueOf(ViewCompat.MEASURED_STATE_MASK));
        textInputLayout2.setBoxBackgroundColor(ContextCompat.getColor(this, 17170443));
        textInputLayout2.setBoxBackgroundMode(2);
        linearLayout.addView(textInputLayout);
        linearLayout.addView(textInputLayout2);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(((Object) "Column ") + getString(R.string.prompt_max_copy)).setCancelable(false).setView(linearLayout).setPositiveButton(R.string.copy, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i2) {
                String obj = editText.getText().toString();
                if (obj.trim().isEmpty()) {
                    return;
                }
                if (TextUtils.isDigitsOnly(obj)) {
                    int parseInt = Integer.parseInt(obj);
                    int i3 = 0;
                    if (parseInt < 0) {
                        parseInt = 0;
                    }
                    int i4 = parseInt - 1;
                    if (i4 < 0) {
                        i4 = 0;
                    }
                    String str = "";
                    while (i4 < i4) {
                        str = str + ((Cell) cellColumnItems.get(i4)).getData().toString() + ",\n";
                        i3++;
                        if (i3 >= 500) {
                            break;
                        }
                        i4++;
                    }
                    ClipboardUtility.copyToClipboard(CsvFileViewerActivity.this, str, "Column '" + CsvFileViewerActivity.this.column_titles[i3] + "' values(s) " + CsvFileViewerActivity.this.getString(R.string.prompt_row_column_copy));
                    return;
                }
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                Toast.makeText(csvFileViewerActivity, csvFileViewerActivity.getString(R.string.prompt_valid_number), Toast.LENGTH_LONG).show();
            }
        }).setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i2) {
                dialogInterface.dismiss();
            }
        });
        if (isDestroyed()) {
            return;
        }
        builder.create().show();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i2, int i22, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i2, int i22, int i3) {
                if (charSequence.toString().isEmpty() || !charSequence.toString().matches("\\d+(?:\\.\\d+)?")) {
                    return;
                }
                int parseInt = Integer.parseInt(charSequence.toString());
                if (parseInt < 0) {
                    editText.setText(String.valueOf(0));
                    parseInt = 0;
                }
                int i4 = parseInt + 500;
                int i5 = size;
                if (i4 > i5) {
                    i4 = i5;
                }
                editText2.setText(String.valueOf(i4));
            }
        });
    }

    private void copySelectedColumn(int i) {
        TableView tableView = this.table;
        if (tableView == null || this.adapter == null) {
            return;
        }
        if (i < 0) {
            i = tableView.getSelectedColumn();
        }
        if (i < 0) {
            Toast.makeText(this, getString(R.string.prompt_select_column), Toast.LENGTH_LONG).show();
            return;
        }
        List<Cell> cellColumnItems = this.adapter.getCellColumnItems(i);
        String str = "";
        int i2 = 0;
        for (int i3 = 0; i3 < cellColumnItems.size(); i3++) {
            str = str + cellColumnItems.get(i3).getData().toString() + ",\n";
            i2++;
            if (i2 >= 1000) {
                break;
            }
        }
        ClipboardUtility.copyToClipboard(this, str, "Column '" + this.column_titles[i] + "' values(s) " + getString(R.string.prompt_row_column_copy));
    }

    @SuppressLint("ResourceType")
    public void filterColumn(final int i) {
        if (this.column_filter_values != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.setMargins(150, 30, 20, 10);
            TextInputLayout textInputLayout = new TextInputLayout(this);
            final EditText editText = new EditText(this);
            editText.setInputType(1);
            editText.setText(this.column_filter_values[i]);
            textInputLayout.setLayoutParams(layoutParams);
            textInputLayout.addView(editText);
            textInputLayout.setHint(getString(R.string.prompt_enter_filter_text));
            textInputLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CDCDCD")));
            textInputLayout.setBoxBackgroundColor(ContextCompat.getColor(this, 17170443));
            textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.addView(textInputLayout, layoutParams);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(((Object) getString(R.string.filter_column)) + " (" + this.column_titles[i] + ")").setView(linearLayout).setNegativeButton(R.string.clear, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i2) {
                    editText.setText("");
                    CsvFileViewerActivity.this.column_filter_values[i2] = "";
                    dialogInterface.dismiss();
                    CsvFileViewerActivity.this.filterColumn(i2);
                }
            }).setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i2) {
                    dialogInterface.dismiss();
                }
            }).setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i2) {
                    CsvFileViewerActivity.this.column_filter_values[i2] = editText.getText().toString();
                    dialogInterface.dismiss();
                    ((RelativeLayout) CsvFileViewerActivity.this.findViewById(R.id.table_holder)).removeAllViews();
                    CsvFileViewerActivity.this.adapter = null;
                    if (CsvFileViewerActivity.this.getIntent().hasExtra(CsvFileViewerActivity.CSV_FILE)) {
                        CsvFileViewerActivity.this.column_to_scroll_to = i2;
                        CsvUtility.filterCsvData(CsvFileViewerActivity.this, CsvFileViewerActivity.CSV_FILE_PATH, CsvFileViewerActivity.this.query, CsvFileViewerActivity.this.table_filter_value, CsvFileViewerActivity.this.column_filter_values);
                        return;
                    }
                    try {
                        InputStream openInputStream = CsvFileViewerActivity.this.getContentResolver().openInputStream(CsvFileViewerActivity.this.getUri());
                        CsvFileViewerActivity.this.column_to_scroll_to = i2;
                        CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                        CsvUtility.filterCsvData(csvFileViewerActivity, openInputStream, csvFileViewerActivity.query, CsvFileViewerActivity.this.table_filter_value, CsvFileViewerActivity.this.column_filter_values);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            if (Build.VERSION.SDK_INT >= 17) {
                if (isDestroyed()) {
                    return;
                }
            } else if (isFinishing()) {
                return;
            }
            builder.create().show();
        }
    }

    @SuppressLint("ResourceType")
    public void scrollToPosition() {
        TableViewAdapter tableViewAdapter = this.adapter;
        if (tableViewAdapter == null || tableViewAdapter.getCellRecyclerViewAdapter() == null || this.adapter.getCellRecyclerViewAdapter().getItemCount() == 0) {
            return;
        }
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_scrolltoposition, (ViewGroup) null);
        final AlertDialog show = new AlertDialog.Builder(this, R.style.AlertDialogview).setCancelable(false).setView(inflate).show();
        Window window = show.getWindow();
        window.setLayout(-1, -2);
        window.setGravity(17);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = 0.7f;
        attributes.flags = 2;
        show.getWindow().setBackgroundDrawableResource(17170445);
        final EditText editText = (EditText) inflate.findViewById(R.id.edittext);
        ((TextView) inflate.findViewById(R.id.txtlinenumber)).setText(((Object) getString(R.string.menu_scroll_to)) + "  (1 - " + this.adapter.getCellRecyclerViewAdapter().getItemCount() + ")");
        ((ImageView) inflate.findViewById(R.id.btn_Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
            }
        });
        ((ImageView) inflate.findViewById(R.id.btn_Done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                String trim = editText.getText().toString().trim();
                if (trim.contains(" ")) {
                    trim = trim.replace(" ", "");
                }
                if (trim.isEmpty() || !TextUtils.isDigitsOnly(trim) || trim.contains(".") || trim.contains("#") || trim.contains("*") || trim.contains(",") || trim.contains("(") || trim.contains(")") || trim.contains("/") || trim.contains("-") || trim.contains("_") || trim.contains("+") || !NumberFormatUtility.isValidNumber(trim) || !trim.matches("\\d+(?:\\.\\d+)?")) {
                    return;
                }
                try {
                    CsvFileViewerActivity.this.scrollTo(Integer.valueOf(trim).intValue() - 1);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        show.show();
    }

    public void scrollTo(int i) {
        TableView tableView = this.table;
        if (tableView != null) {
            tableView.scrollToRowPosition(i);
        }
    }

    @SuppressLint("ResourceType")
    public void setLineNumberDialog() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_setnumber, (ViewGroup) null);
        final AlertDialog show = new AlertDialog.Builder(this, R.style.AlertDialogview).setCancelable(false).setView(inflate).show();
        Window window = show.getWindow();
        window.setLayout(-1, -2);
        window.setGravity(17);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = 0.7f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        show.getWindow().setBackgroundDrawableResource(17170445);
        final TextView textView = (TextView) inflate.findViewById(R.id.txtEnable);
        final TextView textView2 = (TextView) inflate.findViewById(R.id.txtDisable);
        final ImageView imageView = (ImageView) inflate.findViewById(R.id.btnchecked);
        final ImageView imageView2 = (ImageView) inflate.findViewById(R.id.btnunchecked);
        ImageView imageView3 = (ImageView) inflate.findViewById(R.id.btn_Done);
        if (this.show_line_number) {
            imageView.setImageResource(R.drawable.checkbox_h_icn);
            imageView2.setImageResource(R.drawable.checkbox_icn);
            textView.setTextColor(Color.parseColor("#000000"));
            textView2.setTextColor(Color.parseColor("#6F7D87"));
        } else {
            imageView.setImageResource(R.drawable.checkbox_icn);
            imageView2.setImageResource(R.drawable.checkbox_h_icn);
            textView.setTextColor(Color.parseColor("#6F7D87"));
            textView2.setTextColor(Color.parseColor("#000000"));
        }
        ((RelativeLayout) inflate.findViewById(R.id.relativeLayoutEnable)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.checkbox_h_icn);
                imageView2.setImageResource(R.drawable.checkbox_icn);
                CsvFileViewerActivity.this.show_line_number = true;
                textView.setTextColor(Color.parseColor("#000000"));
                textView2.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        ((RelativeLayout) inflate.findViewById(R.id.relativeLayoutDisable)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.checkbox_icn);
                imageView2.setImageResource(R.drawable.checkbox_h_icn);
                CsvFileViewerActivity.this.show_line_number = false;
                textView.setTextColor(Color.parseColor("#6F7D87"));
                textView2.setTextColor(Color.parseColor("#000000"));
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                show.dismiss();
                if (CsvFileViewerActivity.this.show_line_number) {
                    if (CsvFileViewerActivity.this.table != null) {
                        CsvFileViewerActivity.this.table.setRowHeaderWidth(CsvFileViewerActivity.this.row_column_width);
                    }
                } else if (CsvFileViewerActivity.this.table != null) {
                    CsvFileViewerActivity.this.table.setRowHeaderWidth(0);
                }
            }
        });
        show.show();
    }

    @SuppressLint("ResourceType")
    public void setTextSizeDialog() {
        final AlertDialog show = new AlertDialog.Builder(this, R.style.AlertDialogview).setCancelable(false).setView(LayoutInflater.from(this).inflate(R.layout.dialog_settextsize, (ViewGroup) null)).show();
        Window window = show.getWindow();
        window.setLayout(-1, -2);
        window.setGravity(17);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = 0.7f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        show.getWindow().setBackgroundDrawableResource(17170445);
        final SeekBar seekBar = (SeekBar) show.findViewById(R.id.seekBar);
        seekBar.setProgress(this.text_size - 12);
        seekBar.setMax(20);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar2) {
                CsvFileViewerActivity.this.text_size = seekBar2.getProgress() + 12;
            }
        });
        ((ImageView) show.findViewById(R.id.btn_Done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    v.startAnimation(push_animation);
                    show.dismiss();
                    text_size = seekBar.getProgress() + 12;
                    setTextSize(text_size);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MYTAG", "ErrorNo: onClick:" + e);
                }

            }
        });
        ((ImageView) show.findViewById(R.id.btn_Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                CsvFileViewerActivity.this.text_size = 12;
                show.dismiss();
            }
        });
        show.show();
    }

    public void setTextSize(int size) {
        if (this.table == null || this.table.getAdapter() == null) {
            return;
        }
        this.table.getAdapter().addRow(0, new RowHeader("12345", "No."), this.table_utility.getColumnHeaderList());
        TableViewAdapter tableViewAdapter = this.adapter;
        if (tableViewAdapter != null) {
            tableViewAdapter.setTextSize(size);
            this.adapter.notifyDataSetChanged();
        }
        for (int i2 = 0; i2 < this.COLUMN_SIZE; i2++) {
            this.table.remeasureColumnWidth(i2);
        }
        this.table.getAdapter().removeRow(0);
        TableViewAdapter tableViewAdapter2 = this.adapter;
        if (tableViewAdapter2 != null) {
            tableViewAdapter2.notifyDataSetChanged();
        }
    }

    @SuppressLint("ResourceType")
    public void setTextFontDialog() {
        TextView textView;
        TextView textView2;
        ImageView imageView;
        TextView textView3;
        ImageView imageView2;
        TextView textView4;
        ImageView imageView3;
        TextView textView5;
        TextView textView6 = null;
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_setpickfontstyle, (ViewGroup) null);
        final AlertDialog show = new AlertDialog.Builder(this, R.style.AlertDialogview).setCancelable(false).setView(inflate).show();
        Window window = show.getWindow();
        window.setLayout(-1, -2);
        window.setGravity(17);
        show.getWindow().setBackgroundDrawableResource(17170445);
        final TextView textView7 = (TextView) inflate.findViewById(R.id.txtArial);
        final TextView textView8 = (TextView) inflate.findViewById(R.id.txtBaskerville);
        final TextView textView9 = (TextView) inflate.findViewById(R.id.txtBookmanOldStyle);
        final TextView textView10 = (TextView) inflate.findViewById(R.id.txtCalibri);
        TextView textView11 = (TextView) inflate.findViewById(R.id.txtGaramond);
        TextView textView12 = (TextView) inflate.findViewById(R.id.txtGeorgia);
        TextView textView13 = (TextView) inflate.findViewById(R.id.txtHelvetica);
        TextView textView14 = (TextView) inflate.findViewById(R.id.txtTimesnewroman);
        TextView textView15 = (TextView) inflate.findViewById(R.id.txtverdana);
        final ImageView imageView4 = (ImageView) inflate.findViewById(R.id.btncheckedArial);
        final ImageView imageView5 = (ImageView) inflate.findViewById(R.id.btnuncheckedBaskerville);
        final ImageView imageView6 = (ImageView) inflate.findViewById(R.id.btnuncheckedBookmanOldStyle);
        final ImageView imageView7 = (ImageView) inflate.findViewById(R.id.btnuncheckedCalibri);
        final ImageView imageView8 = (ImageView) inflate.findViewById(R.id.btnuncheckedGaramond);
        ImageView imageView9 = (ImageView) inflate.findViewById(R.id.btnuncheckedGeorgia);
        ImageView imageView10 = (ImageView) inflate.findViewById(R.id.btnuncheckedHelvetica);
        ImageView imageView11 = (ImageView) inflate.findViewById(R.id.btnuncheckedTimesnewroman);
        final ImageView imageView12 = (ImageView) inflate.findViewById(R.id.btnuncheckedverdana);
        int i = this.pos;
        if (i == 0) {
            imageView4.setImageResource(R.drawable.checkbox_h_icn);
            imageView5.setImageResource(R.drawable.checkbox_icn);
            imageView6.setImageResource(R.drawable.checkbox_icn);
            imageView7.setImageResource(R.drawable.checkbox_icn);
            imageView8.setImageResource(R.drawable.checkbox_icn);
            imageView9.setImageResource(R.drawable.checkbox_icn);
            imageView10.setImageResource(R.drawable.checkbox_icn);
            imageView11.setImageResource(R.drawable.checkbox_icn);
            imageView12.setImageResource(R.drawable.checkbox_icn);
            textView7.setTextColor(Color.parseColor("#000000"));
            textView8.setTextColor(Color.parseColor("#6F7D87"));
            textView9.setTextColor(Color.parseColor("#6F7D87"));
            textView10.setTextColor(Color.parseColor("#6F7D87"));
            textView11.setTextColor(Color.parseColor("#6F7D87"));
            textView12.setTextColor(Color.parseColor("#6F7D87"));
            textView13.setTextColor(Color.parseColor("#6F7D87"));
            textView14.setTextColor(Color.parseColor("#6F7D87"));
            textView15.setTextColor(Color.parseColor("#6F7D87"));
            imageView2 = imageView10;
            textView4 = textView14;
            imageView3 = imageView9;
            textView5 = textView15;
            textView2 = textView11;
            imageView = imageView11;
            textView3 = textView13;
            textView = textView12;
        } else {
            if (i == 1) {
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_h_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView9.setImageResource(R.drawable.checkbox_icn);
                imageView10.setImageResource(R.drawable.checkbox_icn);
                imageView11.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#000000"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView2 = textView11;
                textView2.setTextColor(Color.parseColor("#6F7D87"));
                textView12.setTextColor(Color.parseColor("#6F7D87"));
                textView13.setTextColor(Color.parseColor("#6F7D87"));
                textView14.setTextColor(Color.parseColor("#6F7D87"));
                textView6 = textView15;
                textView6.setTextColor(Color.parseColor("#6F7D87"));
            } else if (i == 2) {
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_h_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView9.setImageResource(R.drawable.checkbox_icn);
                imageView10.setImageResource(R.drawable.checkbox_icn);
                imageView11.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#000000"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView2 = textView11;
                textView2.setTextColor(Color.parseColor("#6F7D87"));
                textView12.setTextColor(Color.parseColor("#6F7D87"));
                textView13.setTextColor(Color.parseColor("#6F7D87"));
                textView14.setTextColor(Color.parseColor("#6F7D87"));
                textView6 = textView15;
                textView6.setTextColor(Color.parseColor("#6F7D87"));
            } else if (i == 3) {
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_h_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView9.setImageResource(R.drawable.checkbox_icn);
                imageView10.setImageResource(R.drawable.checkbox_icn);
                imageView11.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#000000"));
                textView2 = textView11;
                textView2.setTextColor(Color.parseColor("#6F7D87"));
                textView12.setTextColor(Color.parseColor("#6F7D87"));
                textView13.setTextColor(Color.parseColor("#6F7D87"));
                textView14.setTextColor(Color.parseColor("#6F7D87"));
                textView6 = textView15;
                textView6.setTextColor(Color.parseColor("#6F7D87"));
            } else if (i == 4) {
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_h_icn);
                imageView9.setImageResource(R.drawable.checkbox_icn);
                imageView10.setImageResource(R.drawable.checkbox_icn);
                imageView11.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView2 = textView11;
                textView2.setTextColor(Color.parseColor("#000000"));
                textView12.setTextColor(Color.parseColor("#6F7D87"));
                textView13.setTextColor(Color.parseColor("#6F7D87"));
                textView14.setTextColor(Color.parseColor("#6F7D87"));
                textView6 = textView15;
                textView6.setTextColor(Color.parseColor("#6F7D87"));
            } else if (i == 5) {
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView9.setImageResource(R.drawable.checkbox_h_icn);
                imageView10.setImageResource(R.drawable.checkbox_icn);
                imageView11.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView2 = textView11;
                textView2.setTextColor(Color.parseColor("#6F7D87"));
                textView12.setTextColor(Color.parseColor("#000000"));
                textView13.setTextColor(Color.parseColor("#6F7D87"));
                textView14.setTextColor(Color.parseColor("#6F7D87"));
                textView6 = textView15;
                textView6.setTextColor(Color.parseColor("#6F7D87"));
            } else if (i == 6) {
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView9.setImageResource(R.drawable.checkbox_icn);
                imageView10.setImageResource(R.drawable.checkbox_h_icn);
                imageView11.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView2 = textView11;
                textView2.setTextColor(Color.parseColor("#6F7D87"));
                textView12.setTextColor(Color.parseColor("#6F7D87"));
                textView13.setTextColor(Color.parseColor("#000000"));
                textView14.setTextColor(Color.parseColor("#6F7D87"));
                textView6 = textView15;
                textView6.setTextColor(Color.parseColor("#6F7D87"));
            } else if (i == 7) {
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView9.setImageResource(R.drawable.checkbox_icn);
                imageView10.setImageResource(R.drawable.checkbox_icn);
                imageView11.setImageResource(R.drawable.checkbox_h_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView2 = textView11;
                textView2.setTextColor(Color.parseColor("#6F7D87"));
                textView12.setTextColor(Color.parseColor("#6F7D87"));
                textView13.setTextColor(Color.parseColor("#6F7D87"));
                textView14.setTextColor(Color.parseColor("#000000"));
                textView6 = textView15;
                textView6.setTextColor(Color.parseColor("#6F7D87"));
            } else if (i == 8) {
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView9.setImageResource(R.drawable.checkbox_icn);
                imageView10.setImageResource(R.drawable.checkbox_icn);
                imageView11.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_h_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView2 = textView11;
                textView2.setTextColor(Color.parseColor("#6F7D87"));
                textView = textView12;
                textView.setTextColor(Color.parseColor("#6F7D87"));
                imageView = imageView11;
                textView3 = textView13;
                textView3.setTextColor(Color.parseColor("#6F7D87"));
                imageView2 = imageView10;
                textView4 = textView14;
                textView4.setTextColor(Color.parseColor("#6F7D87"));
                imageView3 = imageView9;
                textView5 = textView15;
                textView5.setTextColor(Color.parseColor("#000000"));
            } else {
                textView = textView12;
                textView2 = textView11;
                imageView = imageView11;
                textView3 = textView13;
                imageView2 = imageView10;
                textView4 = textView14;
                imageView3 = imageView9;
                textView5 = textView15;
            }
            imageView3 = imageView9;
            textView5 = textView6;
            textView = textView12;
            imageView = imageView11;
            textView3 = textView13;
            imageView2 = imageView10;
            textView4 = textView14;
        }
        ImageView imageView13 = (ImageView) inflate.findViewById(R.id.btn_Done);
        ImageView imageView14 = (ImageView) inflate.findViewById(R.id.btn_Cancel);
        RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutArial);
        RelativeLayout relativeLayout2 = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutBaskerville);
        RelativeLayout relativeLayout3 = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutBookmanOldStyle);
        RelativeLayout relativeLayout4 = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutCalibri);
        RelativeLayout relativeLayout5 = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutGaramond);
        RelativeLayout relativeLayout6 = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutGeorgia);
        RelativeLayout relativeLayout7 = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutHelvetica);
        RelativeLayout relativeLayout8 = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutTimesnewroman);
        RelativeLayout relativeLayout9 = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutverdana);
        final TextView textView16 = textView5;
        final ImageView imageView15 = imageView3;
        final TextView textView17 = textView4;
        final ImageView imageView16 = imageView2;
        final ImageView imageView17 = imageView;
        final TextView textView18 = textView3;
        final TextView textView19 = textView;
        final TextView textView20 = textView2;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CsvFileViewerActivity.this.pos = 0;
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                csvFileViewerActivity.text_font = csvFileViewerActivity.pos;
                imageView4.setImageResource(R.drawable.checkbox_h_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView15.setImageResource(R.drawable.checkbox_icn);
                imageView16.setImageResource(R.drawable.checkbox_icn);
                imageView17.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#000000"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView20.setTextColor(Color.parseColor("#6F7D87"));
                textView19.setTextColor(Color.parseColor("#6F7D87"));
                textView18.setTextColor(Color.parseColor("#6F7D87"));
                textView17.setTextColor(Color.parseColor("#6F7D87"));
                textView16.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CsvFileViewerActivity.this.pos = 1;
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                csvFileViewerActivity.text_font = csvFileViewerActivity.pos;
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_h_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView15.setImageResource(R.drawable.checkbox_icn);
                imageView16.setImageResource(R.drawable.checkbox_icn);
                imageView17.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#000000"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView20.setTextColor(Color.parseColor("#6F7D87"));
                textView19.setTextColor(Color.parseColor("#6F7D87"));
                textView18.setTextColor(Color.parseColor("#6F7D87"));
                textView17.setTextColor(Color.parseColor("#6F7D87"));
                textView16.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CsvFileViewerActivity.this.pos = 2;
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                csvFileViewerActivity.text_font = csvFileViewerActivity.pos;
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_h_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView15.setImageResource(R.drawable.checkbox_icn);
                imageView16.setImageResource(R.drawable.checkbox_icn);
                imageView17.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#000000"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView20.setTextColor(Color.parseColor("#6F7D87"));
                textView19.setTextColor(Color.parseColor("#6F7D87"));
                textView18.setTextColor(Color.parseColor("#6F7D87"));
                textView17.setTextColor(Color.parseColor("#6F7D87"));
                textView16.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        relativeLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CsvFileViewerActivity.this.pos = 3;
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                csvFileViewerActivity.text_font = csvFileViewerActivity.pos;
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_h_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView15.setImageResource(R.drawable.checkbox_icn);
                imageView16.setImageResource(R.drawable.checkbox_icn);
                imageView17.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#000000"));
                textView20.setTextColor(Color.parseColor("#6F7D87"));
                textView19.setTextColor(Color.parseColor("#6F7D87"));
                textView18.setTextColor(Color.parseColor("#6F7D87"));
                textView17.setTextColor(Color.parseColor("#6F7D87"));
                textView16.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CsvFileViewerActivity.this.pos = 4;
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                csvFileViewerActivity.text_font = csvFileViewerActivity.pos;
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_h_icn);
                imageView15.setImageResource(R.drawable.checkbox_icn);
                imageView16.setImageResource(R.drawable.checkbox_icn);
                imageView17.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView20.setTextColor(Color.parseColor("#000000"));
                textView19.setTextColor(Color.parseColor("#6F7D87"));
                textView18.setTextColor(Color.parseColor("#6F7D87"));
                textView17.setTextColor(Color.parseColor("#6F7D87"));
                textView16.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        relativeLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CsvFileViewerActivity.this.pos = 5;
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                csvFileViewerActivity.text_font = csvFileViewerActivity.pos;
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView15.setImageResource(R.drawable.checkbox_h_icn);
                imageView16.setImageResource(R.drawable.checkbox_icn);
                imageView17.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView20.setTextColor(Color.parseColor("#6F7D87"));
                textView19.setTextColor(Color.parseColor("#000000"));
                textView18.setTextColor(Color.parseColor("#6F7D87"));
                textView17.setTextColor(Color.parseColor("#6F7D87"));
                textView16.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        relativeLayout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CsvFileViewerActivity.this.pos = 6;
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                csvFileViewerActivity.text_font = csvFileViewerActivity.pos;
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView15.setImageResource(R.drawable.checkbox_icn);
                imageView16.setImageResource(R.drawable.checkbox_h_icn);
                imageView17.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView20.setTextColor(Color.parseColor("#6F7D87"));
                textView19.setTextColor(Color.parseColor("#6F7D87"));
                textView18.setTextColor(Color.parseColor("#000000"));
                textView17.setTextColor(Color.parseColor("#6F7D87"));
                textView16.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        relativeLayout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CsvFileViewerActivity.this.pos = 7;
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                csvFileViewerActivity.text_font = csvFileViewerActivity.pos;
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView15.setImageResource(R.drawable.checkbox_icn);
                imageView16.setImageResource(R.drawable.checkbox_icn);
                imageView17.setImageResource(R.drawable.checkbox_h_icn);
                imageView12.setImageResource(R.drawable.checkbox_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView20.setTextColor(Color.parseColor("#6F7D87"));
                textView19.setTextColor(Color.parseColor("#6F7D87"));
                textView18.setTextColor(Color.parseColor("#6F7D87"));
                textView17.setTextColor(Color.parseColor("#000000"));
                textView16.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        relativeLayout9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CsvFileViewerActivity.this.pos = 8;
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                csvFileViewerActivity.text_font = csvFileViewerActivity.pos;
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                imageView6.setImageResource(R.drawable.checkbox_icn);
                imageView7.setImageResource(R.drawable.checkbox_icn);
                imageView8.setImageResource(R.drawable.checkbox_icn);
                imageView15.setImageResource(R.drawable.checkbox_icn);
                imageView16.setImageResource(R.drawable.checkbox_icn);
                imageView17.setImageResource(R.drawable.checkbox_icn);
                imageView12.setImageResource(R.drawable.checkbox_h_icn);
                textView7.setTextColor(Color.parseColor("#6F7D87"));
                textView8.setTextColor(Color.parseColor("#6F7D87"));
                textView9.setTextColor(Color.parseColor("#6F7D87"));
                textView10.setTextColor(Color.parseColor("#6F7D87"));
                textView20.setTextColor(Color.parseColor("#6F7D87"));
                textView19.setTextColor(Color.parseColor("#6F7D87"));
                textView18.setTextColor(Color.parseColor("#6F7D87"));
                textView17.setTextColor(Color.parseColor("#6F7D87"));
                textView16.setTextColor(Color.parseColor("#000000"));
            }
        });
        imageView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    v.startAnimation(push_animation);
                    setTextFont(pos);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MYTAG", "ErrorNo: onClick:" + e);
                }

                show.dismiss();
            }
        });
        imageView14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                show.dismiss();
            }
        });
        show.show();
    }

    public void setTextFont(int size) {
        try {
            TableView tableView = this.table;
            if (tableView != null && tableView.getAdapter() != null) {
                this.table.getAdapter().addRow(0, new RowHeader("12345", "No."), this.table_utility.getColumnHeaderList());
            }
            TableViewAdapter tableViewAdapter = this.adapter;
            if (tableViewAdapter != null) {
                tableViewAdapter.setTextFont(size);
                this.adapter.notifyDataSetChanged();
            }
            for (int i2 = 0; i2 < this.COLUMN_SIZE; i2++) {
                this.table.remeasureColumnWidth(i2);
            }
            TableView tableView2 = this.table;
            if (tableView2 != null && tableView2.getAdapter() != null) {
                this.table.getAdapter().removeRow(0);
            }
            this.adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYTAG", "ErrorNo: setTextFont:" + e);
        }


    }

    public void setTextAlignment(int i) {
        this.adapter.setTextAlignment(i);
        this.adapter.notifyDataSetChanged();
    }

    public void setTextColor(int i) {
        TableViewAdapter tableViewAdapter = this.adapter;
        if (tableViewAdapter != null) {
            tableViewAdapter.setTextColor(i);
            this.adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("ResourceType")
    public void setTextAlignment() {
        int i = this.text_alignment;
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_setalignment, (ViewGroup) null);
        final AlertDialog show = new AlertDialog.Builder(this, R.style.AlertDialogview).setCancelable(false).setView(inflate).show();
        Window window = show.getWindow();
        window.setLayout(-1, -2);
        window.setGravity(17);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = 0.7f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        show.getWindow().setBackgroundDrawableResource(17170445);
        final TextView textView = (TextView) inflate.findViewById(R.id.txtCenter);
        final TextView textView2 = (TextView) inflate.findViewById(R.id.txtTop);
        final TextView textView3 = (TextView) inflate.findViewById(R.id.txtBottom);
        final TextView textView4 = (TextView) inflate.findViewById(R.id.txtLeft);
        final TextView textView5 = (TextView) inflate.findViewById(R.id.txtRight);
        final ImageView imageView = (ImageView) inflate.findViewById(R.id.btncheckedCenter);
        final ImageView imageView2 = (ImageView) inflate.findViewById(R.id.btnuncheckedTop);
        final ImageView imageView3 = (ImageView) inflate.findViewById(R.id.btnuncheckedBottom);
        final ImageView imageView4 = (ImageView) inflate.findViewById(R.id.btnuncheckedLeft);
        final ImageView imageView5 = (ImageView) inflate.findViewById(R.id.btnuncheckedRight);
        ImageView imageView6 = (ImageView) inflate.findViewById(R.id.btn_Done);
        ImageView imageView7 = (ImageView) inflate.findViewById(R.id.btn_Cancel);
        RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutCenter);
        RelativeLayout relativeLayout2 = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutTop);
        RelativeLayout relativeLayout3 = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutBottom);
        RelativeLayout relativeLayout4 = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutLeft);
        RelativeLayout relativeLayout5 = (RelativeLayout) inflate.findViewById(R.id.relativeLayoutRight);
        int i2 = this.text_alignment;
        if (i2 == 17) {
            imageView.setImageResource(R.drawable.checkbox_h_icn);
            imageView2.setImageResource(R.drawable.checkbox_icn);
            imageView3.setImageResource(R.drawable.checkbox_icn);
            imageView4.setImageResource(R.drawable.checkbox_icn);
            imageView5.setImageResource(R.drawable.checkbox_icn);
            textView.setTextColor(Color.parseColor("#000000"));
            textView2.setTextColor(Color.parseColor("#6F7D87"));
            textView3.setTextColor(Color.parseColor("#6F7D87"));
            textView4.setTextColor(Color.parseColor("#6F7D87"));
            textView5.setTextColor(Color.parseColor("#6F7D87"));
        } else if (i2 == 48) {
            imageView.setImageResource(R.drawable.checkbox_icn);
            imageView2.setImageResource(R.drawable.checkbox_h_icn);
            imageView3.setImageResource(R.drawable.checkbox_icn);
            imageView4.setImageResource(R.drawable.checkbox_icn);
            imageView5.setImageResource(R.drawable.checkbox_icn);
            textView.setTextColor(Color.parseColor("#6F7D87"));
            textView2.setTextColor(Color.parseColor("#000000"));
            textView3.setTextColor(Color.parseColor("#6F7D87"));
            textView4.setTextColor(Color.parseColor("#6F7D87"));
            textView5.setTextColor(Color.parseColor("#6F7D87"));
        } else if (i2 == 80) {
            imageView.setImageResource(R.drawable.checkbox_icn);
            imageView2.setImageResource(R.drawable.checkbox_icn);
            imageView3.setImageResource(R.drawable.checkbox_h_icn);
            imageView4.setImageResource(R.drawable.checkbox_icn);
            imageView5.setImageResource(R.drawable.checkbox_icn);
            textView.setTextColor(Color.parseColor("#6F7D87"));
            textView2.setTextColor(Color.parseColor("#6F7D87"));
            textView3.setTextColor(Color.parseColor("#000000"));
            textView4.setTextColor(Color.parseColor("#6F7D87"));
            textView5.setTextColor(Color.parseColor("#6F7D87"));
        } else if (i2 == 3) {
            imageView.setImageResource(R.drawable.checkbox_icn);
            imageView2.setImageResource(R.drawable.checkbox_icn);
            imageView3.setImageResource(R.drawable.checkbox_icn);
            imageView4.setImageResource(R.drawable.checkbox_h_icn);
            imageView5.setImageResource(R.drawable.checkbox_icn);
            textView.setTextColor(Color.parseColor("#6F7D87"));
            textView2.setTextColor(Color.parseColor("#6F7D87"));
            textView3.setTextColor(Color.parseColor("#6F7D87"));
            textView4.setTextColor(Color.parseColor("#000000"));
            textView5.setTextColor(Color.parseColor("#6F7D87"));
        } else if (i2 == 5) {
            imageView.setImageResource(R.drawable.checkbox_icn);
            imageView2.setImageResource(R.drawable.checkbox_icn);
            imageView3.setImageResource(R.drawable.checkbox_icn);
            imageView4.setImageResource(R.drawable.checkbox_icn);
            imageView5.setImageResource(R.drawable.checkbox_h_icn);
            textView.setTextColor(Color.parseColor("#6F7D87"));
            textView2.setTextColor(Color.parseColor("#6F7D87"));
            textView3.setTextColor(Color.parseColor("#6F7D87"));
            textView4.setTextColor(Color.parseColor("#6F7D87"));
            textView5.setTextColor(Color.parseColor("#000000"));
        }
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.checkbox_h_icn);
                imageView2.setImageResource(R.drawable.checkbox_icn);
                imageView3.setImageResource(R.drawable.checkbox_icn);
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                CsvFileViewerActivity.this.text_alignment = 17;
                textView.setTextColor(Color.parseColor("#000000"));
                textView2.setTextColor(Color.parseColor("#6F7D87"));
                textView3.setTextColor(Color.parseColor("#6F7D87"));
                textView4.setTextColor(Color.parseColor("#6F7D87"));
                textView5.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.checkbox_icn);
                imageView2.setImageResource(R.drawable.checkbox_h_icn);
                imageView3.setImageResource(R.drawable.checkbox_icn);
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                CsvFileViewerActivity.this.text_alignment = 48;
                textView.setTextColor(Color.parseColor("#6F7D87"));
                textView2.setTextColor(Color.parseColor("#000000"));
                textView3.setTextColor(Color.parseColor("#6F7D87"));
                textView4.setTextColor(Color.parseColor("#6F7D87"));
                textView5.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.checkbox_icn);
                imageView2.setImageResource(R.drawable.checkbox_icn);
                imageView3.setImageResource(R.drawable.checkbox_h_icn);
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                CsvFileViewerActivity.this.text_alignment = 80;
                textView.setTextColor(Color.parseColor("#6F7D87"));
                textView2.setTextColor(Color.parseColor("#6F7D87"));
                textView3.setTextColor(Color.parseColor("#000000"));
                textView4.setTextColor(Color.parseColor("#6F7D87"));
                textView5.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        relativeLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.checkbox_icn);
                imageView2.setImageResource(R.drawable.checkbox_icn);
                imageView3.setImageResource(R.drawable.checkbox_icn);
                imageView4.setImageResource(R.drawable.checkbox_h_icn);
                imageView5.setImageResource(R.drawable.checkbox_icn);
                CsvFileViewerActivity.this.text_alignment = 3;
                textView.setTextColor(Color.parseColor("#6F7D87"));
                textView2.setTextColor(Color.parseColor("#6F7D87"));
                textView3.setTextColor(Color.parseColor("#6F7D87"));
                textView4.setTextColor(Color.parseColor("#000000"));
                textView5.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.checkbox_icn);
                imageView2.setImageResource(R.drawable.checkbox_icn);
                imageView3.setImageResource(R.drawable.checkbox_icn);
                imageView4.setImageResource(R.drawable.checkbox_icn);
                imageView5.setImageResource(R.drawable.checkbox_h_icn);
                CsvFileViewerActivity.this.text_alignment = 5;
                textView.setTextColor(Color.parseColor("#6F7D87"));
                textView2.setTextColor(Color.parseColor("#6F7D87"));
                textView3.setTextColor(Color.parseColor("#6F7D87"));
                textView4.setTextColor(Color.parseColor("#6F7D87"));
                textView5.setTextColor(Color.parseColor("#000000"));
            }
        });
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                if (CsvFileViewerActivity.this.text_alignment == 17) {
                    CsvFileViewerActivity.this.setTextAlignment(17);
                } else if (CsvFileViewerActivity.this.text_alignment == 48) {
                    CsvFileViewerActivity.this.setTextAlignment(49);
                } else if (CsvFileViewerActivity.this.text_alignment == 80) {
                    CsvFileViewerActivity.this.setTextAlignment(81);
                } else if (CsvFileViewerActivity.this.text_alignment == 3) {
                    CsvFileViewerActivity.this.setTextAlignment(19);
                } else if (CsvFileViewerActivity.this.text_alignment == 5) {
                    CsvFileViewerActivity.this.setTextAlignment(21);
                }
                show.dismiss();
            }
        });
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                show.dismiss();
            }
        });
        show.show();
    }

    @SuppressLint("ResourceType")
    public void setTextColor() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_selectedcellcolor, (ViewGroup) null);
        final AlertDialog show = new AlertDialog.Builder(this, R.style.AlertDialogview).setCancelable(false).setView(inflate).show();
        Window window = show.getWindow();
        window.setLayout(-1, -2);
        window.setGravity(17);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = 0.7f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        show.getWindow().setBackgroundDrawableResource(17170445);
        ((TextView) inflate.findViewById(R.id.txtlinenumber)).setText(R.string.prompt_text_color);
        ((ColorPickerView) inflate.findViewById(R.id.color_picker_view)).addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                CsvFileViewerActivity.this.text_color = selectedColor;
            }
        });
        ((ImageView) inflate.findViewById(R.id.btn_Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                show.dismiss();
            }
        });
        ((ImageView) inflate.findViewById(R.id.btn_Done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                show.dismiss();
                CsvFileViewerActivity csvFileViewerActivity = CsvFileViewerActivity.this;
                csvFileViewerActivity.setTextColor(csvFileViewerActivity.text_color);
            }
        });
        show.show();
    }

    @SuppressLint("ResourceType")
    public void setTableBackgroundColor() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_selectedcellcolor, (ViewGroup) null);
        final AlertDialog show = new AlertDialog.Builder(this, R.style.AlertDialogview).setCancelable(false).setView(inflate).show();
        Window window = show.getWindow();
        window.setLayout(-1, -2);
        window.setGravity(17);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = 0.7f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        show.getWindow().setBackgroundDrawableResource(17170445);
        ((TextView) inflate.findViewById(R.id.txtlinenumber)).setText(R.string.prompt_bg_color);
        ((ColorPickerView) inflate.findViewById(R.id.color_picker_view)).addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                if (CsvFileViewerActivity.this.table != null && CsvFileViewerActivity.this.adapter != null) {
                    CsvFileViewerActivity.this.table_highlight_color = selectedColor;
                }
                CsvFileViewerActivity.this.table_background_color = selectedColor;
            }
        });
        ((ImageView) inflate.findViewById(R.id.btn_Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                show.dismiss();
            }
        });
        ((ImageView) inflate.findViewById(R.id.btn_Done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    v.startAnimation(push_animation);
                    show.dismiss();
                    if (table == null || table.getAdapter() == null || adapter == null) {
                        return;
                    }
                    table.getAdapter().addRow(0, new RowHeader("12345", "No."), table_utility.getColumnHeaderList());
                    table.setUnSelectedColor(table_background_color);
                    ((RelativeLayout) findViewById(R.id.table_holder)).setBackgroundColor(table_background_color);
                    adapter.setTableBackgroundColor(table_background_color);

                    Log.e("MYTAG", "ErrorNo: onClick:" + COLUMN_SIZE);


                    table.getAdapter().removeRow(0);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MYTAG", "ErrorNo: onClick BackgroundColor:" + e);

                }

            }
        });
        show.show();
    }

    @SuppressLint("ResourceType")
    public void removeCellHighlightColor() {
        if (this.table == null || this.adapter == null) {
            return;
        }
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_removehighlitecolor, (ViewGroup) null);
        final AlertDialog show = new AlertDialog.Builder(this, R.style.AlertDialogview).setCancelable(false).setView(inflate).show();
        Window window = show.getWindow();
        window.setLayout(-1, -2);
        window.setGravity(17);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = 0.7f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        show.getWindow().setBackgroundDrawableResource(17170445);
        final TextView textView = (TextView) inflate.findViewById(R.id.txtEnable);
        final TextView textView2 = (TextView) inflate.findViewById(R.id.txtDisable);
        final ImageView imageView = (ImageView) inflate.findViewById(R.id.btnchecked);
        final ImageView imageView2 = (ImageView) inflate.findViewById(R.id.btnunchecked);
        ImageView imageView3 = (ImageView) inflate.findViewById(R.id.btn_Done);
        ((TextView) inflate.findViewById(R.id.txtlinenumber)).setText(R.string.action_highlight);
        if (this.show_cell_highlight) {
            imageView.setImageResource(R.drawable.checkbox_h_icn);
            imageView2.setImageResource(R.drawable.checkbox_icn);
            textView.setTextColor(Color.parseColor("#000000"));
            textView2.setTextColor(Color.parseColor("#6F7D87"));
        } else {
            imageView.setImageResource(R.drawable.checkbox_icn);
            imageView2.setImageResource(R.drawable.checkbox_h_icn);
            textView2.setTextColor(Color.parseColor("#000000"));
            textView.setTextColor(Color.parseColor("#6F7D87"));
        }
        ((RelativeLayout) inflate.findViewById(R.id.relativeLayoutEnable)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.checkbox_h_icn);
                imageView2.setImageResource(R.drawable.checkbox_icn);
                CsvFileViewerActivity.this.show_cell_highlight = true;
                textView.setTextColor(Color.parseColor("#000000"));
                textView2.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        ((RelativeLayout) inflate.findViewById(R.id.relativeLayoutDisable)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.checkbox_icn);
                imageView2.setImageResource(R.drawable.checkbox_h_icn);
                CsvFileViewerActivity.this.show_cell_highlight = false;
                textView2.setTextColor(Color.parseColor("#000000"));
                textView.setTextColor(Color.parseColor("#6F7D87"));
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                show.dismiss();
                if (CsvFileViewerActivity.this.show_cell_highlight) {
                    CsvFileViewerActivity.this.table.setSelectedColor(CsvFileViewerActivity.this.table_highlight_color);
                    CsvFileViewerActivity.this.adapter.notifyDataSetChanged();
                    return;
                }
                CsvFileViewerActivity.this.table.setSelectedColor(Color.parseColor("#90FFFFFF"));
                CsvFileViewerActivity.this.adapter.notifyDataSetChanged();
            }
        });
        show.show();
    }

    @SuppressLint("ResourceType")
    public void setCellHighlightColor() {
        if (isDestroyed()) {
            return;
        }
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_selectedcellcolor, (ViewGroup) null);
        final AlertDialog show = new AlertDialog.Builder(this, R.style.AlertDialogview).setCancelable(false).setView(inflate).show();
        Window window = show.getWindow();
        window.setLayout(-1, -2);
        window.setGravity(17);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = 0.7f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        show.getWindow().setBackgroundDrawableResource(17170445);
        ((ColorPickerView) inflate.findViewById(R.id.color_picker_view)).addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                if (CsvFileViewerActivity.this.table == null || CsvFileViewerActivity.this.adapter == null) {
                    return;
                }
                CsvFileViewerActivity.this.table_highlight_color = selectedColor;
            }
        });
        ((ImageView) inflate.findViewById(R.id.btn_Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                show.dismiss();
            }
        });
        ((ImageView) inflate.findViewById(R.id.btn_Done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(CsvFileViewerActivity.this.push_animation);
                show.dismiss();
                CsvFileViewerActivity.this.table.setSelectedColor(CsvFileViewerActivity.this.table_highlight_color);
                CsvFileViewerActivity.this.adapter.notifyDataSetChanged();
            }
        });
        show.show();
    }


    public void reloadTable() {
        this.show_line_number = true;
        this.text_size = 14;
        this.filter_column_search_index = 0;
        this.table_filter_value = "";
        if (this.column_filter_values != null) {
            int i = 0;
            while (true) {
                String[] strArr = this.column_filter_values;
                if (i >= strArr.length) {
                    break;
                }
                strArr[i] = "";
                i++;
            }
        }
        if (this.csv_data != null) {
            ((RelativeLayout) findViewById(R.id.table_holder)).removeAllViews();
            this.adapter = null;
            makeTable(this.master_csv_data, false);
        } else if (getIntent().hasExtra(CSV_FILE)) {
            try {
                openFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                openContent();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void showProgressBar(final String str, final ProgressType progressType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout) CsvFileViewerActivity.this.findViewById(R.id.progress_indicator)).setVisibility(View.GONE);
                ((TextView) CsvFileViewerActivity.this.findViewById(R.id.progress_text)).setText(str);
                if (progressType == ProgressType.SPINNER) {
                    ((ProgressBar) CsvFileViewerActivity.this.findViewById(R.id.progress_spinner)).setVisibility(View.GONE);
                }
                if (progressType == ProgressType.BAR) {
                    ((ProgressBar) CsvFileViewerActivity.this.findViewById(R.id.progress_bar)).setVisibility(View.GONE);
                    ((TextView) CsvFileViewerActivity.this.findViewById(R.id.progress_percentage)).setVisibility(View.GONE);
                    ((TextView) CsvFileViewerActivity.this.findViewById(R.id.progress_percentage)).setText("0 %");
                    ((ProgressBar) CsvFileViewerActivity.this.findViewById(R.id.progress_bar)).setProgress(0);
                    ((ProgressBar) CsvFileViewerActivity.this.findViewById(R.id.progress_bar)).setMax(100);
                }
            }
        });
    }

    public void showProgress(final int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) CsvFileViewerActivity.this.findViewById(R.id.progress_percentage)).setText(i + " %");
                ((ProgressBar) CsvFileViewerActivity.this.findViewById(R.id.progress_bar)).setProgress(i);
            }
        });
    }

    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout) CsvFileViewerActivity.this.findViewById(R.id.progress_indicator)).setVisibility(View.GONE);
                ((TextView) CsvFileViewerActivity.this.findViewById(R.id.progress_percentage)).setVisibility(View.GONE);
                ((ProgressBar) CsvFileViewerActivity.this.findViewById(R.id.progress_bar)).setVisibility(View.GONE);
                ((ProgressBar) CsvFileViewerActivity.this.findViewById(R.id.progress_spinner)).setVisibility(View.GONE);
                ((TextView) CsvFileViewerActivity.this.findViewById(R.id.progress_percentage)).setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onDestroy() {
        if (!getIntent().hasExtra(CSV_FILE)) {
            String str = CSV_FILE_PATH;
            FileHelper.deleteCacheFile(new File(getCacheDir(), str.substring(str.lastIndexOf("/") + 1)).getAbsolutePath());
        }
        super.onDestroy();
    }


    @Override
    public void onStart() {
        super.onStart();
        this.imageViewtextcolor.setImageResource(R.drawable.text_color_icn);
        this.imageViewtextAlignment.setImageResource(R.drawable.text_alignment_icn);
        this.imageViewtextsize.setImageResource(R.drawable.text_size_icn);
        this.imageViewscrollto.setImageResource(R.drawable.scroll_position_icn);
        this.imageViewscrollbottom.setImageResource(R.drawable.scroll_bottom_icn);
        this.imageViewscrolltop.setImageResource(R.drawable.scroll_top_icn);
        this.imageViewLinenumber.setImageResource(R.drawable.line_number_icn);


        this.imageViewtextfont.setImageResource(R.drawable.text_fontstyles_icn);
        this.imageViewtextbackground.setImageResource(R.drawable.bg_color_icn);


        this.imageViewRefresh.setImageResource(R.drawable.refresh_icn);
        this.imageViewNewFile.setImageResource(R.drawable.newfileicn);
        this.imageViewShare.setImageResource(R.drawable.share_icn);
    }
}
