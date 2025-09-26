package com.demo.csvfilereader.model;

import kotlin.jvm.internal.Intrinsics;


public final class FilesModelClass {
    private String fileDate;
    private String fileName;
    private String filePath;
    private String fileSize;

    public FilesModelClass(String fileName, String fileSize, String filePath, String fileDate) {
        Intrinsics.checkNotNullParameter(fileName, "fileName");
        Intrinsics.checkNotNullParameter(fileSize, "fileSize");
        Intrinsics.checkNotNullParameter(filePath, "filePath");
        Intrinsics.checkNotNullParameter(fileDate, "fileDate");
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.fileDate = fileDate;
    }

    public final String getFileName() {
        return this.fileName;
    }

    public final void setFileName(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.fileName = str;
    }

    public final String getFileSize() {
        return this.fileSize;
    }

    public final void setFileSize(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.fileSize = str;
    }

    public final String getFilePath() {
        return this.filePath;
    }

    public final void setFilePath(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.filePath = str;
    }

    public final String getFileDate() {
        return this.fileDate;
    }

    public final void setFileDate(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.fileDate = str;
    }
}
