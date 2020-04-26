package com.shlsoft.fooddelivery.app;

import android.app.Application;

import com.shlsoft.fooddelivery.service.ConnectivityReceiver;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return MyApplication.mInstance;
    }
}
