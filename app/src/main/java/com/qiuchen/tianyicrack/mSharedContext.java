package com.qiuchen.tianyicrack;

import android.app.Application;
import android.content.Context;

import com.qiuchen.tianyicrack.Presenter.*;

/**
 * Created by qiuchen on 2018/2/6.
 */

public class mSharedContext extends Application {

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
}
