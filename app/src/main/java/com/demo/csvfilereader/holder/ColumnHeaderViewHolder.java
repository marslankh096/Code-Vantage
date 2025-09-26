package com.demo.csvfilereader.holder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.evrencoskun.tableview.sort.SortState;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.model.ColumnHeader;


public class ColumnHeaderViewHolder extends AbstractSorterViewHolder {
    private static final String LOG_TAG = "ColumnHeaderViewHolder";
    public final Drawable arrow_down;
    public final Drawable arrow_no_sort;
    public final Drawable arrow_up;
    public final LinearLayout column_header_container;
    public final ImageButton column_header_sortButton;
    public final TextView column_header_textview;
    public final LinearLayout column_header_view;
    private View.OnClickListener mSortButtonClickListener;
    public final ITableView tableView;

    public ColumnHeaderViewHolder(View view, ITableView iTableView) {
        super(view);
        this.mSortButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                if (ColumnHeaderViewHolder.this.getSortState() == SortState.ASCENDING) {
                    ColumnHeaderViewHolder.this.tableView.sortColumn(ColumnHeaderViewHolder.this.getAdapterPosition(), SortState.DESCENDING);
                } else if (ColumnHeaderViewHolder.this.getSortState() == SortState.DESCENDING) {
                    ColumnHeaderViewHolder.this.tableView.sortColumn(ColumnHeaderViewHolder.this.getAdapterPosition(), SortState.ASCENDING);
                } else {
                    ColumnHeaderViewHolder.this.tableView.sortColumn(ColumnHeaderViewHolder.this.getAdapterPosition(), SortState.DESCENDING);
                }
            }
        };
        this.tableView = iTableView;
        this.column_header_textview = (TextView) view.findViewById(R.id.column_header_textView);
        this.column_header_container = (LinearLayout) view.findViewById(R.id.column_header_container);
        this.column_header_view = (LinearLayout) view.findViewById(R.id.column_header_view);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.column_header_sortButton);
        this.column_header_sortButton = imageButton;
        this.arrow_up = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_arrow_up);
        this.arrow_down = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_arrow_down);
        this.arrow_no_sort = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_no_sort);
        imageButton.setOnClickListener(this.mSortButtonClickListener);
    }

    public void setColumnHeader(ColumnHeader columnHeader) {
        this.column_header_textview.setText(String.valueOf(columnHeader.getData()));
        this.column_header_container.getLayoutParams().width = -2;
        this.column_header_textview.requestLayout();
    }

    @Override
    public void onSortingStatusChanged(SortState sortState) {
        super.onSortingStatusChanged(sortState);
        this.column_header_container.getLayoutParams().width = -2;
        controlSortState(sortState);
        this.column_header_textview.requestLayout();
        this.column_header_sortButton.requestLayout();
        this.column_header_container.requestLayout();
        this.itemView.requestLayout();
    }

    private void controlSortState(SortState sortState) {
        if (sortState == SortState.UNSORTED) {
            this.column_header_sortButton.setImageDrawable(this.arrow_no_sort);
        } else if (sortState == SortState.ASCENDING) {
            this.column_header_sortButton.setImageDrawable(this.arrow_up);
        } else if (sortState == SortState.DESCENDING) {
            this.column_header_sortButton.setImageDrawable(this.arrow_down);
        }
    }
}
