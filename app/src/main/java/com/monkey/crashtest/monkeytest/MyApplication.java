package com.monkey.crashtest.monkeytest;

import android.app.Application;

public class MyApplication extends Application {


    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(SaveException.getInstance());

    }
}
