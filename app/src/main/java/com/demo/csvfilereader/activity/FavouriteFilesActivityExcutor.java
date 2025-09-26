package com.demo.csvfilereader.activity;


public final class FavouriteFilesActivityExcutor implements Runnable {
    public final FavouriteFilesActivity favouriteFilesActivity;

    public FavouriteFilesActivityExcutor(FavouriteFilesActivity favouriteFilesActivity) {
        this.favouriteFilesActivity = favouriteFilesActivity;
    }

    @Override
    public void run() {
        FavouriteFilesActivity.FavouriteFilesActivityExecutorperform(this.favouriteFilesActivity);
    }
}
