package com.demo.csvfilereader.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.demo.csvfilereader.R;
import com.demo.csvfilereader.activity.CsvFileViewerActivity;
import com.demo.csvfilereader.holder.CellViewHolder;
import com.demo.csvfilereader.holder.ColumnHeaderViewHolder;
import com.demo.csvfilereader.holder.RowHeaderViewHolder;
import com.demo.csvfilereader.model.Cell;
import com.demo.csvfilereader.model.ColumnHeader;
import com.demo.csvfilereader.model.RowHeader;
import com.demo.csvfilereader.utility.FontsUtility;


public class TableViewAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {
    private static final String LOG_TAG = "TableViewAdapter";
    View corner;
    Typeface f3629tf;
    Context mContext;
    private final LayoutInflater mInflater;
    int table_background_color;
    int text_alignment;
    int text_color;
    int text_font;
    int text_size;
    CsvFileViewerActivity.TextStyle text_style;
    TextView title;

    @Override
    public int getCellItemViewType(int i) {
        return 0;
    }

    @Override
    public int getColumnHeaderItemViewType(int i) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int i) {
        return 0;
    }


    @Override
    public void onBindCellViewHolder(@NonNull AbstractViewHolder abstractViewHolder, @Nullable Cell obj, int i, int i2) {
        CellViewHolder cellViewHolder = (CellViewHolder) abstractViewHolder;
        cellViewHolder.setCell((Cell) obj);
        cellViewHolder.cell_textview.setTextSize(this.text_size);
        cellViewHolder.cell_textview.setGravity(this.text_alignment);
        cellViewHolder.cell_textview.setTextColor(this.text_color);
        cellViewHolder.cell_textview.setTypeface(this.f3629tf);
        if (this.text_style == CsvFileViewerActivity.TextStyle.NORMAL) {
            cellViewHolder.cell_textview.setTypeface(cellViewHolder.cell_textview.getTypeface(), Typeface.NORMAL);
        }
        if (this.text_style == CsvFileViewerActivity.TextStyle.BOLD) {
            cellViewHolder.cell_textview.setTypeface(cellViewHolder.cell_textview.getTypeface(), Typeface.BOLD);
        }
        if (this.text_style == CsvFileViewerActivity.TextStyle.ITALIC) {
            cellViewHolder.cell_textview.setTypeface(cellViewHolder.cell_textview.getTypeface(), Typeface.ITALIC);
        }
        if (this.text_style == CsvFileViewerActivity.TextStyle.BOLD_ITALIC) {
            cellViewHolder.cell_textview.setTypeface(cellViewHolder.cell_textview.getTypeface(), Typeface.BOLD_ITALIC);
        }
    }

    @Override
    public void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder abstractViewHolder, ColumnHeader obj, int i) {
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) abstractViewHolder;
        columnHeaderViewHolder.setColumnHeader((ColumnHeader) obj);
        columnHeaderViewHolder.column_header_textview.setTextSize(this.text_size);
        columnHeaderViewHolder.column_header_textview.setTextColor(this.text_color);
        columnHeaderViewHolder.column_header_textview.setTypeface(this.f3629tf);
        columnHeaderViewHolder.column_header_textview.setTypeface(columnHeaderViewHolder.column_header_textview.getTypeface(), Typeface.BOLD);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder abstractViewHolder, RowHeader obj, int i) {
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) abstractViewHolder;
        rowHeaderViewHolder.row_header_textview.setText(String.valueOf(((RowHeader) obj).getData()));
        rowHeaderViewHolder.row_header_textview.setTextSize(this.text_size);
        rowHeaderViewHolder.row_header_textview.setTextColor(this.text_color);
        rowHeaderViewHolder.row_header_textview.setTypeface(this.f3629tf);
        rowHeaderViewHolder.row_header_textview.setTypeface(rowHeaderViewHolder.row_header_textview.getTypeface(), Typeface.BOLD);
    }


    public TableViewAdapter(Context context) {
        this.corner = null;
        this.mContext = context;
        this.title = null;
        this.text_size = 14;
        this.text_alignment = 17;
        this.text_color = ViewCompat.MEASURED_STATE_MASK;
        this.text_font = 0;
        this.f3629tf = null;
        this.text_style = CsvFileViewerActivity.TextStyle.NORMAL;
        this.table_background_color = -1;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.f3629tf = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/Arial.ttf");
    }

    public void setTextSize(int i) {
        this.text_size = i;
        TextView textView = this.title;
        if (textView != null) {
            textView.setTextSize(i);
        }
    }

    public void setTextAlignment(int i) {
        this.text_alignment = i;
    }

    public void setTextColor(int i) {
        this.text_color = i;
        TextView textView = this.title;
        if (textView != null) {
            textView.setTextColor(i);
        }
    }

    public void setTextStyle(CsvFileViewerActivity.TextStyle textStyle) {
        this.text_style = textStyle;
    }

    public void setTextFont(int i) {
        this.text_font = i;
        String[] listFonts = FontsUtility.listFonts(this.mContext, "fonts");
        AssetManager assets = this.mContext.getAssets();
        Typeface createFromAsset = Typeface.createFromAsset(assets, "fonts/" + listFonts[i] + ".ttf");
        this.f3629tf = createFromAsset;
        TextView textView = this.title;
        if (textView != null) {
            textView.setTypeface(createFromAsset);
            TextView textView2 = this.title;
            textView2.setTypeface(textView2.getTypeface(), Typeface.BOLD);
        }
    }

    public void setTableBackgroundColor(int i) {
        this.table_background_color = i;
        if (this.corner != null) {
            this.corner.setBackgroundColor(i);
        }
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup viewGroup, int i) {
        return new CellViewHolder(this.mInflater.inflate(R.layout.table_view_cell_layout, viewGroup, false));
    }


    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup viewGroup, int i) {
        return new ColumnHeaderViewHolder(this.mInflater.inflate(R.layout.table_view_column_header_layout, viewGroup, false), getTableView());
    }

    @NonNull
    @Override
    public View onCreateCornerView(@NonNull ViewGroup parent) {
        View inflate = this.mInflater.inflate(R.layout.table_view_corner_layout, (ViewGroup) null);
        this.corner = inflate;
        TextView textView = (TextView) inflate.findViewById(R.id.title);
        this.title = textView;
        textView.setTextColor(this.text_color);
        this.title.setTypeface(this.f3629tf);
        TextView textView2 = this.title;
        textView2.setTypeface(textView2.getTypeface(), Typeface.BOLD);
        this.corner.setBackgroundColor(this.table_background_color);
        return this.corner;
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup viewGroup, int i) {
        return new RowHeaderViewHolder(this.mInflater.inflate(R.layout.table_view_row_header_layout, viewGroup, false));
    }


}
