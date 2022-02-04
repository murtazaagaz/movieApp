package com.example.moviesapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    public static SharedPreferences mSharedPreferences;
    public static MySharedPreferences mInstance;
    public static Context mContext;
    private String sp_name = "MovieApp";
    private String DEFAULT = "";

    public MySharedPreferences() {
        mSharedPreferences = mContext.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
    }

    public static MySharedPreferences getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new MySharedPreferences();
        }
        return mInstance;
    }


    public void addMovies(String value) {
        mSharedPreferences.edit().putString("movies", value).apply();

    }

    public String getMovies() {
        return mSharedPreferences.getString("movies", DEFAULT);
    }


    public void addMoviesIds(String value) {
        mSharedPreferences.edit().putString("ids", value).apply();

    }


    public String getMoviesIds() {
        return mSharedPreferences.getString("ids", DEFAULT);
    }






    public void clearData() {
        mSharedPreferences.edit().clear().apply();
    }
}
