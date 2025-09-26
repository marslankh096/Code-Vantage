package com.demo.csvfilereader.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.model.Cell;


public class CellViewHolder extends AbstractViewHolder {
    private Cell cell;
    public final LinearLayout cell_container;
    public final TextView cell_textview;

    public CellViewHolder(View view) {
        super(view);
        this.cell_textview = (TextView) view.findViewById(R.id.cell_data);
        this.cell_container = (LinearLayout) view.findViewById(R.id.cell_container);
    }

    public void setCell(Cell cell2) {
        this.cell = cell2;
        this.cell_textview.setText(String.valueOf(cell2.getData()));
        this.cell_container.getLayoutParams().width = -2;
        this.cell_textview.requestLayout();
    }
}
