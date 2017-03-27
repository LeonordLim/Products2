package com.example.com.products.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Vishal Bhati on 3/25/2017.
 */

public class ProductProvider extends ContentProvider {

    static UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    public static final int PRODUCTS=0;
    public static final int PATH_Product=1;
    ProductSQLite productSQLite;
    static {
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY,ProductContract.PATH,PRODUCTS);
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY,ProductContract.PATH+"/#",PATH_Product);
    }
    @Override
    public boolean onCreate() {
        productSQLite=new ProductSQLite(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db=productSQLite.getReadableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case PRODUCTS:
                cursor=db.query(ProductContract.ProductEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PATH_Product:
                selection= ProductContract.ProductEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(ProductContract.ProductEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default: throw new IllegalArgumentException(uri.toString());
        }

        cursor.setNotificationUri(getContext().getContentResolver(),ProductContract.ProductEntry.CONTENT_URI);
        return cursor;
    }
    public void useful(){
//        switch (uriMatcher.match(uri)){
//            case PRODUCTS:
//                break;
//            case PATH_Product:
//                break;
//            default:
//        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db=productSQLite.getWritableDatabase();
        long rows;
        switch (uriMatcher.match(uri)){
            case PRODUCTS:
                rows=db.insert(ProductContract.ProductEntry.TABLE_NAME,null,values);
                break;
            default: throw new IllegalArgumentException(uri.toString());
        }
        if(rows>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return ContentUris.withAppendedId(uri,rows);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db=productSQLite.getWritableDatabase();
        int rows;
        switch (uriMatcher.match(uri)){
            case PRODUCTS:
              rows= db.delete(ProductContract.ProductEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case PATH_Product:
                selection= ProductContract.ProductEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rows=db.delete(ProductContract.ProductEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default: throw new IllegalArgumentException(uri.toString());
        }
        if(rows>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db=productSQLite.getWritableDatabase();
        int rows=0;
        switch (uriMatcher.match(uri)){
            case PRODUCTS:
                rows=db.update(ProductContract.ProductEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case PATH_Product:
                selection= ProductContract.ProductEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rows=db.update(ProductContract.ProductEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default: throw new IllegalArgumentException(uri.toString());
        }
        if(rows>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rows;
    }
}
