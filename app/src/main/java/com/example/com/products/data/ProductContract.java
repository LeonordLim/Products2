package com.example.com.products.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Vishal Bhati on 3/25/2017.
 */

public class ProductContract {

    public static final String CONTENT_AUTHORITY="com.example.com.products";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH="products";
    public static final class ProductEntry implements BaseColumns{
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH);
        public static final String TABLE_NAME="products";
        public static final String _ID=BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME="name";
        public static final String COLUMN_PRODUCT_QUANTITY="quantity";
        public static final String COLUMN_PRODUCT_PRICE="price";
        public static final String COLUMN_PRODUCT_SUPPLIER="supplier";
        public static final String COLUMN_PRODUCT_IMAGE="image";
    }
}
