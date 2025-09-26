package com.demo.csvfilereader.activity;


public final class FavouriteFilesActivityGetdata implements Runnable {
    public final FavouriteFilesActivity favouriteFilesActivity;

    public FavouriteFilesActivityGetdata(FavouriteFilesActivity favouriteFilesActivity) {
        this.favouriteFilesActivity = favouriteFilesActivity;
    }

    @Override
    public void run() {
        FavouriteFilesActivity.FavouriteFilesActivityGetdata(this.favouriteFilesActivity);
    }
}
