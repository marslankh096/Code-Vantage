package com.demo.csvfilereader.utility;

import com.demo.csvfilereader.model.Cell;
import com.demo.csvfilereader.model.ColumnHeader;
import com.demo.csvfilereader.model.RowHeader;

import java.util.ArrayList;
import java.util.List;


public class TableUtility {
    public static int CELL_SIZE;
    private static int COLUMN_SIZE;
    private static int ROW_SIZE;
    ArrayList<List<String>> csv_data;

    public TableUtility(ArrayList<List<String>> arrayList) {
        this.csv_data = arrayList;
        if (arrayList.size() > 0) {
            COLUMN_SIZE = arrayList.get(0).size();
            ROW_SIZE = arrayList.size() - 1;
        }
        CELL_SIZE = COLUMN_SIZE * ROW_SIZE;
    }

    public int getColumnSize() {
        return COLUMN_SIZE;
    }

    public int getRowSize() {
        return ROW_SIZE;
    }

    public int getCellSize() {
        return CELL_SIZE;
    }

    public List<ColumnHeader> getColumnHeaderList() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < COLUMN_SIZE; i++) {
            if (this.csv_data.size() > 0 && i < this.csv_data.get(0).size()) {
                arrayList.add(new ColumnHeader(String.valueOf(i), this.csv_data.get(0).get(i)));
            }
        }
        return arrayList;
    }

    public List<RowHeader> getRowHeaderList() {
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i <= ROW_SIZE; i++) {
            String valueOf = String.valueOf(i);
            arrayList.add(new RowHeader(valueOf, "" + i));
        }
        return arrayList;
    }

    public List<RowHeader> getDummyRowHeaderList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new RowHeader(String.valueOf(1), ""));
        return arrayList;
    }

    public List<List<Cell>> getCellList() {
        String str;
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i <= ROW_SIZE; i++) {
            ArrayList arrayList2 = new ArrayList();
            List arrayList3 = new ArrayList();
            if (i <= this.csv_data.size() - 1) {
                arrayList3 = this.csv_data.get(i);
            }
            for (int i2 = 0; i2 < COLUMN_SIZE; i2++) {
                if (i2 > arrayList3.size() - 1 || (str = (String) arrayList3.get(i2)) == null) {
                    str = "      ";
                }
                arrayList2.add(new Cell(i + "-" + i2, str));
            }
            arrayList.add(arrayList2);
        }
        return arrayList;
    }

    public List<List<Cell>> getDummyCellList(int i) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < i; i2++) {
            arrayList2.add(new Cell("0-" + i2, ""));
        }
        arrayList.add(arrayList2);
        return arrayList;
    }


}
