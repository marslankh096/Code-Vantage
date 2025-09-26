package com.demo.csvfilereader.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.evrencoskun.tableview.sort.SortState;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.activity.CsvFileViewerActivity;
import com.demo.csvfilereader.holder.ColumnHeaderViewHolder;
import com.demo.csvfilereader.popup.ColumnHeaderLongPressPopup;
import com.demo.csvfilereader.popup.RowHeaderLongPressPopup;


public class TableViewListener implements ITableViewListener {
    static CsvFileViewerActivity csvFileViewerActivity;
    private Context mContext;
    private TableView mTableView;
    private Toast mToast;
    int selected_column = -1;

    public TableViewListener(CsvFileViewerActivity csvFileViewerActivity2, TableView tableView) {
        this.mContext = tableView.getContext();
        csvFileViewerActivity = csvFileViewerActivity2;
        this.mTableView = tableView;
    }

    @Override
    public void onCellClicked(RecyclerView.ViewHolder cellView, int column, int row) {
        showToast("Cell " + column + " " + row + " has been clicked.");
    }

    private void showToast(String p_strMessage) {
        Toast.makeText(this.mContext, p_strMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCellLongPressed(RecyclerView.ViewHolder cellView, int column, int row) {
        showToast("Cell " + column + " " + row + " has been long pressed.");
    }

    @Override
    public void onColumnHeaderClicked(RecyclerView.ViewHolder columnHeaderView, int column) {
        if (this.selected_column != this.mTableView.getSelectedColumn()) {
            this.selected_column = column;
        } else {
            sortColumn(this.mContext, this.mTableView, column, null);
        }
        showToast("Column header  " + column + " has been clicked.");
    }

    @Override
    public void onColumnHeaderLongPressed(RecyclerView.ViewHolder columnHeaderView, int column) {
        if (columnHeaderView == null || !(columnHeaderView instanceof ColumnHeaderViewHolder)) {
            return;
        }
        ColumnHeaderLongPressPopup columnHeaderLongPressPopup = new ColumnHeaderLongPressPopup((ColumnHeaderViewHolder) columnHeaderView, this.mTableView);
        columnHeaderLongPressPopup.setActivity(csvFileViewerActivity);
        columnHeaderLongPressPopup.show();
    }

    @Override
    public void onRowHeaderClicked(RecyclerView.ViewHolder rowHeaderView, int row) {
        showToast("Row header " + row + " has been clicked.");
    }

    @Override
    public void onRowHeaderLongPressed(RecyclerView.ViewHolder rowHeaderView, int row) {
        new RowHeaderLongPressPopup(rowHeaderView, this.mTableView).show();
    }

    @Override
    public void onCellDoubleClicked(RecyclerView.ViewHolder cellView, int column, int row) {
        showToast("Cell " + column + " " + row + " has been clicked.");
    }

    @Override
    public void onColumnHeaderDoubleClicked(RecyclerView.ViewHolder columnHeaderView, int column) {
        showToast("Cell " + column + " " + column + " has been clicked.");
    }

    @Override
    public void onRowHeaderDoubleClicked(RecyclerView.ViewHolder rowHeaderView, int row) {
        showToast("Cell " + row + " " + row + " has been clicked.");
    }

    public static void sortColumn(Context context, TableView tableView, int column, SortState sortState) {
        AbstractTableAdapter adapter;
        CellRecyclerViewAdapter cellRecyclerViewAdapter;
        if (tableView == null || (adapter = tableView.getAdapter()) == null || (cellRecyclerViewAdapter = adapter.getCellRecyclerViewAdapter()) == null) {
            return;
        }
        if (cellRecyclerViewAdapter.getItemCount() > 2000) {
            new AlertDialog.Builder(context, R.style.dialog).setCancelable(false).setTitle(context.getString(R.string.warning)).setMessage(context.getString(R.string.prompt_sort_warning)).setPositiveButton(context.getString(R.string.okay), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i2) {
                    dialogInterface.dismiss();
                }
            }).create().show();
            return;
        }
        int itemCount = tableView.getColumnHeaderLayoutManager().getItemCount();
        Log.e("MYTAG", "ErrorNo: itemCount:" + itemCount);
        for (int temp = 0; temp < itemCount; temp++) {
            Log.e("MYTAG", "ErrorNo: sortColumn ==:" + temp);
            if (temp != column) {
                Log.e("MYTAG", "ErrorNo: sortColumn !=:" + temp);
                tableView.sortColumn(temp, SortState.UNSORTED);
            }
        }
        String str = null;
        if (sortState == null) {
            if (tableView.getSortingStatus(column) == SortState.UNSORTED) {
                tableView.sortColumn(column, SortState.ASCENDING);
                str = context.getString(R.string.ascend);
            } else if (tableView.getSortingStatus(column) == SortState.ASCENDING) {
                tableView.sortColumn(column, SortState.DESCENDING);
                str = context.getString(R.string.descend);
            } else if (tableView.getSortingStatus(column) == SortState.DESCENDING) {
                tableView.sortColumn(column, SortState.UNSORTED);
                csvFileViewerActivity.unSortTable();
                str = context.getString(R.string.no_sorting);
            }
            Toast.makeText(context, str, Toast.LENGTH_LONG).show();
            tableView.scrollToRowPosition(0);
        }
        if (sortState == SortState.UNSORTED) {
            tableView.sortColumn(column, SortState.UNSORTED);
            csvFileViewerActivity.unSortTable();
            str = context.getString(R.string.no_sorting);
        } else if (sortState == SortState.ASCENDING) {
            tableView.sortColumn(column, SortState.ASCENDING);
            str = context.getString(R.string.ascend);
        } else if (sortState == SortState.DESCENDING) {
            tableView.sortColumn(column, SortState.DESCENDING);
            str = context.getString(R.string.descend);
        }
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        tableView.scrollToRowPosition(0);
        Toast.makeText(context, "", Toast.LENGTH_LONG).show();
        tableView.scrollToRowPosition(0);
    }
}
