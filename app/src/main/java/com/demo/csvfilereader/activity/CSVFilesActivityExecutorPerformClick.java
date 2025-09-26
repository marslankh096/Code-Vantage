package com.demo.csvfilereader.activity;


public final class CSVFilesActivityExecutorPerformClick implements Runnable {
    public final CSVFilesActivity csvFilesActivity;

    public CSVFilesActivityExecutorPerformClick(CSVFilesActivity cSVFilesActivity) {
        this.csvFilesActivity = cSVFilesActivity;
    }

    @Override
    public void run() {
        CSVFilesActivity.CSVFilesActivityExecutorPerformOperations(this.csvFilesActivity);
    }
}
