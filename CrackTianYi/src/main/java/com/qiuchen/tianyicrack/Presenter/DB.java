package com.qiuchen.tianyicrack.Presenter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qiuchen.tianyicrack.Bean.DB_PhoneInfoBean;

import java.util.ArrayList;

/**
 * Created by qiuchen on 2018/2/6.
 */

public class DB extends SQLiteOpenHelper {
    /**
     * 字段设计:
     * 表名称: mDB_NumList
     * 字段:
     * id int
     * phoneNum TEXT 手机号码
     * phonePass TEXT 手机号码服务密码
     * loginSession TEXT 二次登录用的鉴权Session
     */

    private static String NUMLIST = "mDB_List";

    public DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void initDB(SQLiteDatabase db) {
        String exec = "create table if not exists " + NUMLIST + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "phoneNum TEXT NOT NULL," +
                "phonePass TEXT NOT NULL," +
                "loginSession TEXT," +
                "areaCode TEXT);";
        db.execSQL(exec);
    }

    public void addPhone(String num, String pass) {
        String exec = "insert into " + NUMLIST + "(phoneNum,phonePass)" +
                "values('" + num + "','" + pass + "');";
        this.getWritableDatabase().execSQL(exec);
    }

    public String getNumPass(String num) {
        String exec = "select phonePass from " + NUMLIST + " where phoneNum = '" + num + "';";
        Cursor cursor = getReadableDatabase().rawQuery(exec, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return "";
    }

    public void removePhone(String num) {
        String exec = "delete from " + NUMLIST + " where phoneNum='" + num + "';";
        this.getWritableDatabase().execSQL(exec);
    }

    public void updateAccount(String oldAccount, String newUser, String newPass) {
        String exec = "update " + NUMLIST + " set phoneNum = '" + newUser + "',phonePass = '" + newPass + "' where phoneNum = '" + oldAccount + "';";
        getWritableDatabase().execSQL(exec);
    }

    public void saveSession(String num, String session, String areaCode) {
        String exec = "update " + NUMLIST + " set loginSession = '" + session + "',areaCode = '" + areaCode + "' where phoneNum='" + num + "';";
        getWritableDatabase().execSQL(exec);
    }

    public ArrayList<DB_PhoneInfoBean> AllPhone() {
        ArrayList<DB_PhoneInfoBean> array = new ArrayList<>();
        String exec = "select * from " + NUMLIST;
        Cursor cursor = this.getReadableDatabase().rawQuery(exec, null);
        if (cursor.moveToFirst()) {
            /*
            2018.2.7日 QiuChenly 修复数据库读取数据问题
             */
            while (true) {
                DB_PhoneInfoBean a = new DB_PhoneInfoBean();
                a.user = cursor.getString(1);
                a.pass = cursor.getString(2);
                a.token = cursor.getString(3);
                a.areaCode = cursor.getString(4);
                array.add(a);
                if (cursor.isLast()) {
                    break;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return array;
    }
}
