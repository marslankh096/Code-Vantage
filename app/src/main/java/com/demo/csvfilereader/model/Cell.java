package com.demo.csvfilereader.model;

import com.evrencoskun.tableview.filter.IFilterableModel;
import com.evrencoskun.tableview.sort.ISortableModel;


public class Cell implements ISortableModel, IFilterableModel {
    public Object mData;
    private String mFilterKeyword;
    private String mId;

    public Cell(String str) {
        this.mId = str;
    }

    public Cell(String str, Object obj) {
        this.mId = str;
        this.mData = obj;
        this.mFilterKeyword = String.valueOf(obj);
    }

    @Override
    public String getId() {
        return this.mId;
    }

    @Override
    public Object getContent() {
        return this.mData;
    }

    public Object getData() {
        return this.mData;
    }

    public void setData(String str) {
        this.mData = str;
    }

    public String getFilterKeyword() {
        return this.mFilterKeyword;
    }

    public void setFilterKeyword(String str) {
        this.mFilterKeyword = str;
    }

    @Override
    public String getFilterableKeyword() {
        return this.mFilterKeyword;
    }
}
