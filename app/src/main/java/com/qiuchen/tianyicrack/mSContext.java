package com.qiuchen.tianyicrack;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.qiuchen.tianyicrack.Presenter.DB;

/**
 * Created by qiuchen on 2018/2/6.
 */

public class mSContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = getApplicationContext();
        mDB = new DB(getApplicationContext(), "QIUCHENDB", null, 1);
    }

    private static Context ctx;
    private static DB mDB;

    public static Context getCtx() {
        return ctx;
    }

    public static DB getDB() {
        return mDB;
    }

    public static Boolean hasNet() {
        ConnectivityManager conn = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            return conn.getActiveNetworkInfo() != null && conn.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
