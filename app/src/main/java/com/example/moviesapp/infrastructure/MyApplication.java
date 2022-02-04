package com.example.moviesapp.infrastructure;

import android.app.Application;
import android.content.Context;


/**
 * Created by Dell on 11/28/2017.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


    }




    public static MyApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }
}
