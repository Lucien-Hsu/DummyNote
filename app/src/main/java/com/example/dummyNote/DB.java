package com.example.dummyNote;

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

    //資料庫變數
    private SQLiteDatabase mdb;
    //DBHelper
    private DatabaseHelper dbHelper;

    //定義 DBHelper 內部類別
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
        //以dbHelper創建WritableDatabase給資料庫變數
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

    //插入資料
    public long create(String item) {
        //建立要插入的欄位之鍵值對
        ContentValues values = new ContentValues();
        //欄位0為ID，不設定
        //欄位一放傳入的字串
        values.put(DATABASE_COLUMN_01, item);
        //欄位二放時間
        values.put(DATABASE_COLUMN_02, new Date(System.currentTimeMillis()).toString());
        //插入資料
        return mdb.insert("notes", null, values);
    }

    //刪除一筆資料
    public boolean delete(long id){
        //刪除指令
        //若失敗則回傳 -1，因此為判斷式為false
        return mdb.delete(DATABASE_TABLE, DATABASE_COLUMN_00 + "=" + id , null) > 0;
    }

    //修改一筆資料
    public boolean update(long id, String item){
        //建立要插入的欄位之鍵值對
        ContentValues values = new ContentValues();
        //欄位0為ID，不設定
        //欄位一放傳入的字串
        values.put(DATABASE_COLUMN_01, item);
        //欄位二放時間
        values.put(DATABASE_COLUMN_02, new Date(System.currentTimeMillis()).toString());
        //更新指令
        //若失敗則回傳 -1，因此為判斷式為false
        return mdb.update(DATABASE_TABLE, values, DATABASE_COLUMN_00 + "=" + id , null) > 0;
    }
}
