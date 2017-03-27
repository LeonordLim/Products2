package com.example.com.products;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.products.data.ProductContract;

/**
 * Created by Vishal Bhati on 3/25/2017.
 */

public class ProductCursorAdapter extends CursorAdapter {
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_todo,parent,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView= (TextView) view.findViewById(R.id.item_name);
        final TextView quantityTextView= (TextView) view.findViewById(R.id.item_quantity);
        TextView priceTextView= (TextView) view.findViewById(R.id.item_price);
        quantityTextView.setText("Quantity = ");
        priceTextView.setText("Rs");
        final Context context1=context;
        nameTextView.setText(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME)));
        final int quantityNumber=cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
        quantityTextView.append(String.valueOf(quantityNumber));
        priceTextView.append(String.valueOf(cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE))));
        priceTextView.append("/-");
        Button button= (Button) view.findViewById(R.id.button);
        final int id=view.getId();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity=quantityNumber;
                --quantity;
                Toast.makeText(context1, "abc"+id, Toast.LENGTH_SHORT).show();
                ContentValues values=new ContentValues();
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,quantity);
                String selection= ProductContract.ProductEntry._ID+"=?";
                String[] selectionArgs={String.valueOf(0)};
                context1.getContentResolver().update(ProductContract.ProductEntry.CONTENT_URI,values,selection,selectionArgs);
            }
        });
    }



}
