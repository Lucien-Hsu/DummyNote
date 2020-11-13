package com.example.dummylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;

public class DB {
    private static final String DATABASE_NAME = "notes.db";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_COLUMN_00 = "_id";
    private static final String DATABASE_COLUMN_01 = "note";
    private static final String DATABASE_COLUMN_02 = "created";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            "notes(_id INTEGER PRIMARY KEY, note TEXT NOT NULL, created INTEGER);";

    private Context context;

    //資料庫
    private SQLiteDatabase mdb;
    //DBHelper
    private DatabaseHelper dbHelper;

    //定義DBHelper
    //需定義建構子並覆寫兩方法
    class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    //建構子，需指定context
    public DB(Context context) {
        this.context = context;
    }

    //創建資料庫
    public void open() {
        dbHelper = new DatabaseHelper(context);
        mdb = dbHelper.getWritableDatabase();
    }

    //關閉資料庫
    public void close() {
//        if(mdb != null){
//            mdb.close();
//        }
        dbHelper.close();
    }

    //查詢資料庫
    public Cursor getAll() {
        return mdb.rawQuery("SELECT * FROM notes;", null);
    }

    public long create(String item) {
        //建立要插入的欄位之鍵值對
        ContentValues values = new ContentValues();
        values.put(DATABASE_COLUMN_01, item);
        values.put(DATABASE_COLUMN_02, new Date(System.currentTimeMillis()).toString());

        return mdb.insert("notes", null, values);
    }
}
