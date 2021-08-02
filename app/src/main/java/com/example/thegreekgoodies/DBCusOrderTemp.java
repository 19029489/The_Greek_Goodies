package com.example.thegreekgoodies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBCusOrderTemp extends SQLiteOpenHelper {
    //--------------------Setup---------------------
    private static final String DATABASE_NAME = "customerorder.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_LIST = "orderlist";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PNAME = "productname";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_PRICE = "price";
    //--------------------Setup---------------------

    public DBCusOrderTemp(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}


    //-----------------OnCreate--------------
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createItemTableSql = "CREATE TABLE " + TABLE_LIST + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PNAME + " TEXT,"
                + COLUMN_QUANTITY + " INTEGER, "
                + COLUMN_PRICE + " FLOAT )";

        db.execSQL(createItemTableSql);

        Log.i("info", "created tables");
    }
    //-----------------OnCreate--------------

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //--------------------------Insert----------------------
    public long insertCustomer(String pname, int quantity, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PNAME, pname);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_PRICE, price);
        long result = db.insert(TABLE_LIST, null, values);
        db.close();
        Log.d("SQL Insert","ID:"+ result); //id returned, shouldn’t be -1
        return result;
    }
    //--------------------------Insert----------------------

    //-----------------Retrieve--------------
    public ArrayList<CustomerOrder> getCustomerData() {
        ArrayList<CustomerOrder> customer = new ArrayList<CustomerOrder>();

        String selectQuery = "SELECT " + COLUMN_ID + ","
                + COLUMN_PNAME + ","+COLUMN_QUANTITY+ ","+COLUMN_PRICE  + " FROM " + TABLE_LIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String pname = cursor.getString(1);
                int quantity = cursor.getInt(2);
                double price = cursor.getDouble(3);
                CustomerOrder newCus = new CustomerOrder(id, pname, quantity, price);
                customer.add(newCus);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return customer;
    }
    //-----------------Retrieve--------------

    //-----------------Delete--------------
    public int deleteCusItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};

        int result = db.delete(TABLE_LIST, condition, args);
        db.close();
        return result;
    }
    //-----------------Delete--------------


}
