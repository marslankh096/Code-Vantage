package com.demo.csvfilereader.holder;

import android.view.View;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.demo.csvfilereader.R;


public class RowHeaderViewHolder extends AbstractViewHolder {
    public final TextView row_header_textview;

    public RowHeaderViewHolder(View view) {
        super(view);
        this.row_header_textview = (TextView) view.findViewById(R.id.row_header_textview);
    }
}
