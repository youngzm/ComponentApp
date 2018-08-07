package com.young.componentapp.application;

import android.app.Application;

public class GApp extends Application {

    private static GApp mGApp = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mGApp = this;
    }

    public static GApp getInstance() {
        return mGApp;
    }
}
