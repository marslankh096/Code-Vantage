package com.demo.csvfilereader.adapter;

import android.view.View;

import kotlin.jvm.internal.Ref;


public final class FilesAdapterfavclick implements View.OnClickListener {
    public final int anInt;
    public final Ref.BooleanRef booleanRef;
    public final FilesAdapter filesAdapter;
    public final String string;
    public final FilesAdapter.ViewHolder viewHolder;

    public FilesAdapterfavclick(Ref.BooleanRef booleanRef, FilesAdapter.ViewHolder viewHolder, FilesAdapter filesAdapter, int i, String str) {
        this.booleanRef = booleanRef;
        this.viewHolder = viewHolder;
        this.filesAdapter = filesAdapter;
        this.anInt = i;
        this.string = str;
    }

    @Override
    public final void onClick(View view) {
        FilesAdapter.FilesAdapterfavclickperform(this.booleanRef, this.viewHolder, this.filesAdapter, this.anInt, this.string, view);
    }
}
