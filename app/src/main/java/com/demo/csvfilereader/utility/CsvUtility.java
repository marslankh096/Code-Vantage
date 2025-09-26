package com.demo.csvfilereader.utility;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.demo.csvfilereader.R;
import com.demo.csvfilereader.activity.CsvFileViewerActivity;
import com.demo.csvfilereader.model.SearchResult;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ObjectRowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CsvUtility {
    public static void getCsvData(CsvFileViewerActivity csvFileViewerActivity, File file) {
        new GetCsvData(csvFileViewerActivity, file).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void getCsvData(CsvFileViewerActivity csvFileViewerActivity, InputStream inputStream) {
        new GetCsvData(csvFileViewerActivity, inputStream).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void filterCsvData(CsvFileViewerActivity csvFileViewerActivity, String str, String str2, String str3, String[] strArr) {
        if (DeviceInfo.isLegacyDevice()) {
            new FilterCsvData(csvFileViewerActivity, new File(str), str2, str3, strArr).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return;
        }
        try {
            new FilterCsvData(csvFileViewerActivity, csvFileViewerActivity.getContentResolver().openInputStream(Uri.parse(str)), str2, str3, strArr).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void filterCsvData(CsvFileViewerActivity csvFileViewerActivity, InputStream inputStream, String str, String str2, String[] strArr) {
        new FilterCsvData(csvFileViewerActivity, inputStream, str, str2, strArr).execute();
    }

    public static class GetCsvData extends AsyncTask<String, Integer, String> {
        CsvFileViewerActivity context = null;
        ArrayList<List<String>> csv_data = new ArrayList<>();
        InputStream csv_file_inputStream = null;
        File csv_file_path = null;
        int current_progress = 0;

        public GetCsvData(CsvFileViewerActivity csvFileViewerActivity, File file) {
            this.context = csvFileViewerActivity;
            this.csv_file_path = file;
        }

        public GetCsvData(CsvFileViewerActivity csvFileViewerActivity, InputStream inputStream) {
            this.context = csvFileViewerActivity;
            this.csv_file_inputStream = inputStream;
        }

        public void onPreExecute() {
            CsvFileViewerActivity csvFileViewerActivity = this.context;
            csvFileViewerActivity.showProgressBar(csvFileViewerActivity.getString(R.string.prompt_extracting_csv), CsvFileViewerActivity.ProgressType.BAR);
        }

        public String doInBackground(String... strArr) {
            int i = 0;


            if (this.csv_file_inputStream == null) {
                Log.e("MYTAG", "ErrorNo: doInBackground 1:" + csv_data.size());
                CsvParserSettings csvParserSettings = new CsvParserSettings();
                csvParserSettings.detectFormatAutomatically();
                csvParserSettings.setMaxCharsPerColumn(40960);
                csvParserSettings.setMaxColumns(5120);
                List<String[]> parseAll = new CsvParser(csvParserSettings).parseAll(this.csv_file_path);
                int size = parseAll.size();
                while (i < parseAll.size()) {
                    this.csv_data.add(new ArrayList(Arrays.asList((Object[]) parseAll.get(i))));
                    double d = (double) (i * 100);
                    double d2 = (double) size;
                    Double.isNaN(d);
                    Double.isNaN(d2);
                    int i2 = (int) (d / d2);
                    if (i2 - this.current_progress > 5) {
                        this.context.showProgress(i2);
                        this.current_progress = i2;
                    }
                    i++;
                }
                return null;
            }
            Log.e("MYTAG", "ErrorNo: doInBackground 2:" + csv_data.size());
            CsvParserSettings csvParserSettings2 = new CsvParserSettings();
            csvParserSettings2.detectFormatAutomatically();
            csvParserSettings2.setMaxCharsPerColumn(40960);
            csvParserSettings2.setMaxColumns(5120);
            List<String[]> parseAll2 = new CsvParser(csvParserSettings2).parseAll(this.csv_file_inputStream);
            int size2 = parseAll2.size();
            String[] strings = parseAll2.get(0);
            for (String s : strings) {
                Log.e("MYTAG", "ErrorNo: ?List int :" + s);
            }
            while (i < parseAll2.size()) {
                this.csv_data.add(new ArrayList(Arrays.asList((Object[]) parseAll2.get(i))));
                double d3 = (double) (i * 100);
                double d4 = (double) size2;
                Double.isNaN(d3);
                Double.isNaN(d4);
                int i3 = (int) (d3 / d4);
                if (i3 - this.current_progress > 5) {
                    this.context.showProgress(i3);
                    this.current_progress = i3;
                }
                i++;
            }
            Log.e("MYTAG", "ErrorNo: doInBackground 3:" + csv_data.size());
            return null;
        }

        public void onPostExecute(String str) {
            super.onPostExecute(str);
            this.context.hideProgress();
            this.context.makeTable(this.csv_data, true);
        }
    }

    public static class FilterCsvData extends AsyncTask<String, Integer, String> {
        String[] column_filter_values = null;
        CsvFileViewerActivity context = null;
        ArrayList<List<String>> csv_data = new ArrayList<>();
        InputStream csv_file_inputStream = null;
        File csv_file_path = null;
        int current_progress = 0;
        String filter = null;
        String query = null;
        int row_count = 0;
        ArrayList<SearchResult> search_data = new ArrayList<>();

        public FilterCsvData(CsvFileViewerActivity csvFileViewerActivity, File file, String str, String str2, String[] strArr) {
            this.context = csvFileViewerActivity;
            this.csv_file_path = file;
            this.filter = str2;
            this.column_filter_values = strArr;
            if (str != null && !str.trim().isEmpty()) {
                this.query = str;
            }
        }

        public FilterCsvData(CsvFileViewerActivity csvFileViewerActivity, InputStream inputStream, String str, String str2, String[] strArr) {
            this.context = csvFileViewerActivity;
            this.csv_file_inputStream = inputStream;
            this.filter = str2;
            this.column_filter_values = strArr;
            if (str != null && !str.trim().isEmpty()) {
                this.query = str;
            }
        }

        public void onPreExecute() {
            CsvFileViewerActivity csvFileViewerActivity = this.context;
            csvFileViewerActivity.showProgressBar(csvFileViewerActivity.getString(R.string.prompt_filtering_csv), CsvFileViewerActivity.ProgressType.BAR);
        }

        public String doInBackground(String... strArr) {
            int i = 0;
            if (this.csv_file_inputStream == null) {
                final ArrayList arrayList = new ArrayList();
                ObjectRowProcessor r5 = new ObjectRowProcessor() {
                    public void rowProcessed(Object[] objArr, ParsingContext parsingContext) {
                        if (FilterCsvData.this.row_count == 0) {
                            arrayList.add(FilterCsvData.this.getRowTitle(objArr));
                            FilterCsvData.this.row_count++;
                        } else if (Arrays.toString(objArr).toLowerCase(Locale.getDefault()).contains(FilterCsvData.this.filter.toLowerCase(Locale.getDefault()))) {
                            String[] row = FilterCsvData.this.getRow(objArr, FilterCsvData.this.column_filter_values);
                            if (row != null) {
                                arrayList.add(row);
                                FilterCsvData.this.row_count++;
                            }
                        }
                    }
                };
                CsvParserSettings csvParserSettings = new CsvParserSettings();
                csvParserSettings.detectFormatAutomatically();
                csvParserSettings.setMaxCharsPerColumn(40960);
                csvParserSettings.setMaxColumns(5120);
                csvParserSettings.setProcessor(r5);
                new CsvParser(csvParserSettings).parse(this.csv_file_path);
                int size = arrayList.size();

                Log.e("MYTAG", "ErrorNo: doInBackground 1:" + size);
                while (i < arrayList.size()) {
                    this.csv_data.add(new ArrayList(Arrays.asList((Object[]) arrayList.get(i))));
                    double d = (double) (i * 100);
                    double d2 = (double) size;
                    Double.isNaN(d);
                    Double.isNaN(d2);
                    int i2 = (int) (d / d2);
                    if (i2 - this.current_progress > 5) {
                        this.context.showProgress(i2);
                        this.current_progress = i2;
                    }
                    i++;
                }
                return null;
            }
            final ArrayList arrayList2 = new ArrayList();
            ObjectRowProcessor r52 = new ObjectRowProcessor() {
                public void rowProcessed(Object[] objArr, ParsingContext parsingContext) {
                    if (FilterCsvData.this.row_count == 0) {
                        FilterCsvData.this.row_count++;
                        arrayList2.add(FilterCsvData.this.getRowTitle(objArr));
                    } else if (Arrays.toString(objArr).toLowerCase(Locale.getDefault()).contains(FilterCsvData.this.filter.toLowerCase(Locale.getDefault()))) {
                        FilterCsvData filterCsvData = FilterCsvData.this;
                        String[] row = filterCsvData.getRow(objArr, filterCsvData.column_filter_values);
                        if (row != null) {
                            arrayList2.add(row);
                            FilterCsvData.this.row_count++;
                        }
                    }
                }
            };

            CsvParserSettings csvParserSettings2 = new CsvParserSettings();
            csvParserSettings2.detectFormatAutomatically();
            csvParserSettings2.setMaxCharsPerColumn(40960);
            csvParserSettings2.setMaxColumns(5120);
            csvParserSettings2.setProcessor(r52);
            new CsvParser(csvParserSettings2).parse(this.csv_file_inputStream);
            int size2 = arrayList2.size();
            Log.e("MYTAG", "ErrorNo: doInBackground 2:" + size2);

            while (i < arrayList2.size()) {
                this.csv_data.add(new ArrayList(Arrays.asList((Object[]) arrayList2.get(i))));
                double d3 = (double) (i * 100);
                double d4 = (double) size2;
                Double.isNaN(d3);
                Double.isNaN(d4);
                int i3 = (int) (d3 / d4);
                if (i3 - this.current_progress > 5) {
                    this.context.showProgress(i3);
                    this.current_progress = i3;
                }
                i++;
            }
            return null;
        }

        public void onPostExecute(String str) {
            super.onPostExecute(str);
            this.context.hideProgress();
            this.context.makeTable(this.csv_data, false);
        }

        public String[] getRowTitle(Object[] objArr) {
            String[] strArr = new String[objArr.length];
            for (int i = 0; i < objArr.length; i++) {
                strArr[i] = String.valueOf(objArr[i]);
            }
            return strArr;
        }

        public String[] getRow(Object[] objArr, String[] strArr) {
            int length = objArr.length;
            String[] strArr2 = new String[length];
            for (int i = 0; i < objArr.length; i++) {
                if (objArr[i] == null) {
                    strArr2[i] = "     ";
                } else {
                    strArr2[i] = String.valueOf(objArr[i]);
                }
            }
            if (strArr == null || length != strArr.length) {
                return null;
            }
            SearchResult searchResult = null;
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (!strArr2[i2].trim().isEmpty()) {
                    if (this.query != null && strArr2[i2].toLowerCase(Locale.getDefault()).contains(this.query)) {
                        searchResult = new SearchResult();
                        searchResult.COLUMN = i2;
                        searchResult.ROW = this.row_count;
                    }
                    if (!strArr2[i2].toLowerCase(Locale.getDefault()).contains(strArr[i2].toLowerCase(Locale.getDefault()))) {
                        return null;
                    }
                }
            }
            if (!(this.query == null || searchResult == null)) {
                this.search_data.add(searchResult);
            }
            return strArr2;
        }
    }
}




































































































































































































































































































































