package com.example.thegreekgoodies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperStatus extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "status.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_STATUS = "status";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LOGGEDIN = "loggedin";
    private static final String COLUMN_ROLE = "role";

    public DBHelperStatus(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStatusTableSql = "CREATE TABLE " + TABLE_STATUS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LOGGEDIN + " INTEGER, "
                + COLUMN_ROLE + " TEXT)";
        db.execSQL(createStatusTableSql);
        Log.i("info", "created tables");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);
        onCreate(db);

    }

    public String getStatus() {

        String status = "false";

        String selectQuery = "SELECT " + COLUMN_LOGGEDIN + " FROM " + TABLE_STATUS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Integer loggedin = cursor.getInt(0); //0 means false, 1 means true
                if (loggedin == 0){
                    status = "false";
                } else {
                    status = "true";
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return status;
    }

    public String getRole() {

        String roleGotten = null;

        String selectQuery = "SELECT " + COLUMN_ROLE + " FROM " + TABLE_STATUS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String role = cursor.getString(0); //0 means false, 1 means true
                roleGotten = role;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return roleGotten;
    }

    public int updateStatus(String keyword, String role){ //when signing out/signing in
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (keyword.equalsIgnoreCase("loggedin") == true){
            values.put(COLUMN_LOGGEDIN, 1);
        } else {
            values.put(COLUMN_LOGGEDIN, 0);
        }

        values.put(COLUMN_ROLE, role);
        String condition = COLUMN_ID + "= 1";
        int result = db.update(TABLE_STATUS, values, condition, null);

        db.close();
        return result;
    }



}
