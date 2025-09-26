package com.demo.csvfilereader.adapter;

import android.view.View;

import java.io.File;


public final class FilesAdapterItemclicks implements View.OnClickListener {
    public final File file;
    public final FilesAdapter filesAdapter;
    public final String string;

    public FilesAdapterItemclicks(FilesAdapter filesAdapter, String str, File file) {
        this.filesAdapter = filesAdapter;
        this.string = str;
        this.file = file;
    }

    @Override
    public final void onClick(View view) {
        FilesAdapter.FilesAdapterItemclicksperform(this.filesAdapter, this.string, this.file, view);
    }
}
