package com.example.moviesapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.moviesapp.R;
import com.example.moviesapp.infrastructure.MyApplication;
import com.example.moviesapp.storage.MySharedPreferences;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class Utils {

    private static final String TAG = "UTILS";

    public static boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable()
                && networkInfo.isConnected();
    }

    public static void showRedSnackbar(View layoutForSnacbar, String message) {
        Snackbar snack = Snackbar.make(layoutForSnacbar, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.colorAccent));
        snack.show();
    }

    public static void makeToast(String message) {
        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void parsingErrorToast() {
        Toast.makeText(MyApplication.getAppContext(), R.string.oops_something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    public static void noInternetToast() {
        Toast.makeText(MyApplication.getAppContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    public static void showPB(View pb, View parent ) {
        parent.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
    }

    public static void hidePB( View pb,View parent) {
        parent.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
    }


    public static String getTime(String time) {
        long ts = Long.parseLong(time);
        Date date = new Date(ts * 1000); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        return formatter.format(date);
    }

    public static String getDate(String time) {

        long ts = Long.parseLong(time);
        Date date = new Date(ts * 1000); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy "); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

        return sdf.format(date);
    }

    public static String getDay(String timestamp) {

        long ts = (Long.parseLong(timestamp)) * 1000;
        Date date = new Date(ts); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

        return sdf.format(date);
    }

    public static String getMonth(String timestamp) {

        try {
            long ts = (Long.parseLong(timestamp)) * 1000;
            Date date = new Date(ts); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("MMM"); // the format of your date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

            return sdf.format(date);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.d(TAG, "MUR NUM ERROR: " + e.getLocalizedMessage());
            return "";

        }

    }

    public static String getYear(String timestamp) {

        try {
            long ts = (Long.parseLong(timestamp)) * 1000;
            Date date = new Date(ts); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("yyy"); // the format of your date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

            return sdf.format(date);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.d(TAG, "MUR NUM ERROR: " + e.getLocalizedMessage());
            return "";

        }
    }



    public static void showLoader(ViewGroup parent, ViewGroup loader) {
        parent.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

    public static void hideLoader(ViewGroup parent, ViewGroup loader) {
        parent.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
    }




    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
