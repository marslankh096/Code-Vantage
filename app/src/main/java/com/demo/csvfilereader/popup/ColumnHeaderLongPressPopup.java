package com.demo.csvfilereader.popup;

import android.content.Context;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.sort.SortState;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.activity.CsvFileViewerActivity;
import com.demo.csvfilereader.holder.ColumnHeaderViewHolder;


public class ColumnHeaderLongPressPopup extends PopupMenu implements PopupMenu.OnMenuItemClickListener {
    private static final int ASCENDING = 0;
    private static final int COPY_COLUMN = 5;
    private static final int DESCENDING = 1;
    private static final int FILTER = 3;
    private static final int SCROLL_TO = 4;
    CsvFileViewerActivity csvFileViewerActivity;
    private Context mContext;
    private ITableView mTableView;
    private ColumnHeaderViewHolder mViewHolder;
    private int mXPosition;

    private void changeMenuItemVisibility() {
    }

    public ColumnHeaderLongPressPopup(ColumnHeaderViewHolder columnHeaderViewHolder, ITableView iTableView) {
        super(columnHeaderViewHolder.itemView.getContext(), columnHeaderViewHolder.itemView);
        ColumnHeaderViewHolder columnHeaderViewHolder2 = (ColumnHeaderViewHolder) this.mTableView.getColumnHeaderRecyclerView().findViewHolderForAdapterPosition(this.mXPosition);
        this.mViewHolder = columnHeaderViewHolder2;
        this.mXPosition = columnHeaderViewHolder2.getAdapterPosition();
        this.mViewHolder = columnHeaderViewHolder;
        this.mTableView = iTableView;
        this.mContext = columnHeaderViewHolder.itemView.getContext();
        initialize();
    }

    private void initialize() {
        createMenuItem();
        changeMenuItemVisibility();
        setOnMenuItemClickListener(this);
    }

    public void setActivity(CsvFileViewerActivity csvFileViewerActivity2) {
        this.csvFileViewerActivity = csvFileViewerActivity2;
    }

    private void createMenuItem() {
        getMenu().add(0, 3, 0, this.mContext.getString(R.string.filter_column));
        getMenu().add(0, 4, 1, this.mContext.getString(R.string.action_scroll_to));
        getMenu().add(0, 5, 2, this.mContext.getString(R.string.menu_copy_column));
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 0) {
            this.mTableView.sortColumn(this.mXPosition, SortState.ASCENDING);
        } else if (itemId == 1) {
            this.mTableView.sortColumn(this.mXPosition, SortState.DESCENDING);
        } else if (itemId == 3) {
            this.csvFileViewerActivity.filterColumn(this.mXPosition);
        } else if (itemId == 4) {
            this.csvFileViewerActivity.scrollToPosition();
        } else if (itemId == 5) {
            this.csvFileViewerActivity.copyColumn(this.mXPosition);
        }
        return true;
    }
}
