package com.demo.csvfilereader.popup;

import android.content.Context;
import android.view.MenuItem;
import android.widget.PopupMenu;

import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.ITableView;


public class RowHeaderLongPressPopup extends PopupMenu implements PopupMenu.OnMenuItemClickListener {
    private Context mContext;
    private int mRowPosition;
    private ITableView mTableView;

    private void createMenuItem() {
    }

    public RowHeaderLongPressPopup(RecyclerView.ViewHolder viewHolder, ITableView iTableView) {
        super(viewHolder.itemView.getContext(), viewHolder.itemView);
        this.mTableView = iTableView;
        this.mContext = viewHolder.itemView.getContext();
        this.mRowPosition = viewHolder.getAdapterPosition();
        initialize();
    }

    private void initialize() {
        createMenuItem();
        setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 1) {
            this.mTableView.scrollToColumnPosition(15);
        } else if (itemId != 2) {
            if (itemId == 3) {
                this.mTableView.getAdapter().removeRow(this.mRowPosition);
            }
        } else if (this.mTableView.isColumnVisible(1)) {
            this.mTableView.hideColumn(1);
        } else {
            this.mTableView.showColumn(1);
        }
        return true;
    }
}
