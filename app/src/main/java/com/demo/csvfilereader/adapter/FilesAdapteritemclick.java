package com.demo.csvfilereader.adapter;

import android.view.View;


public final class FilesAdapteritemclick implements View.OnClickListener {
    public final FilesAdapter filesAdapter;
    public final String string;

    public FilesAdapteritemclick(FilesAdapter filesAdapter, String str) {
        this.filesAdapter = filesAdapter;
        this.string = str;
    }

    @Override
    public void onClick(View view) {
        FilesAdapter.FilesAdapteritemclickpeeform(this.filesAdapter, this.string, view);
    }
}
