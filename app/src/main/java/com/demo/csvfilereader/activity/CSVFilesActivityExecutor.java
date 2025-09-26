package com.demo.csvfilereader.activity;


public final class CSVFilesActivityExecutor implements Runnable {
    public final CSVFilesActivity csvFilesActivity;

    public CSVFilesActivityExecutor(CSVFilesActivity cSVFilesActivity) {
        this.csvFilesActivity = cSVFilesActivity;
    }

    @Override
    public void run() {
        CSVFilesActivity.CSVFilesActivityExecutorPerform(this.csvFilesActivity);
    }
}
