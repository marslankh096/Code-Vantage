package com.demo.csvfilereader;

import android.app.Application;

public class App extends Application {
    private static App fire_base_app;

    public static synchronized App getInstance() {
        App fireBaseInitializeApp;
        synchronized (App.class) {
            fireBaseInitializeApp = fire_base_app;
        }
        return fireBaseInitializeApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fire_base_app = this;
    }
}
