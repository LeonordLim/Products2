package com.example.com.products.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Vishal Bhati on 3/25/2017.
 */

public class ProductSQLite extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="products.db";
    public static final int DATABASE_VERSIOIN=1;
    public ProductSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSIOIN);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onDowngrade(db,oldVersion,newVersion);
        onCreate(db);
    }

    private static final String CREATE_DATABASE="CREATE TABLE "+ ProductContract.ProductEntry.TABLE_NAME+"("+
            ProductContract.ProductEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            ProductContract.ProductEntry.COLUMN_PRODUCT_NAME+" TEXT NOT NULL,"+
            ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY+" INTEGER,"+
            ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE+" INTEGER NOT NULL,"+
            ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER+" TEXT NOT NULL,"+
            ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE+" TEXT );";
}
