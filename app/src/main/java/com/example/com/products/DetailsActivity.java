package com.example.com.products;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.products.data.ProductContract.ProductEntry;

import com.example.com.products.data.ProductContract;
import com.example.com.products.data.ProductSQLite;

public class DetailsActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{

    EditText name;
    EditText quantity;
    EditText price;
    EditText supplier;
    boolean hasDataChanged;
    Uri uri;
    boolean editMode;
    public static final int LOADER_ID1=1;

    View.OnTouchListener listener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            hasDataChanged=true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
       uri=getIntent().getData();
        if(uri==null){
            editMode=false;
        }else
        {
            editMode=true;
        }

        name=(EditText)findViewById(R.id.name);
        quantity=(EditText)findViewById(R.id.quantity);
        price=(EditText)findViewById(R.id.price);
        supplier=(EditText)findViewById(R.id.supplier);

        name.setOnTouchListener(listener);
        quantity.setOnTouchListener(listener);
        price.setOnTouchListener(listener);
        supplier.setOnTouchListener(listener);


        getLoaderManager().initLoader(LOADER_ID1,null,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details,menu);
        return true;
    }

    public void showWarning(DialogInterface.OnClickListener discardButtonClickListener){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Warning").setMessage("Do you want to exit?");
        builder.setPositiveButton("Discard",discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog!=null)
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!hasDataChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showWarning(discardButtonClickListener);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
         super.onPrepareOptionsMenu(menu);
        if(!editMode){
            MenuItem menuItem=menu.findItem(R.id.delete_one);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_one:
                insertProduct();
                finish();
                return true;
            case R.id.delete_one:delete();
                finish();
                return true;
            case android.R.id.home:
                if (!hasDataChanged) {
                    NavUtils.navigateUpFromSameTask(DetailsActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(DetailsActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showWarning(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void delete() {
        getContentResolver().delete(uri,null,null);
    }


    private void insertProduct() {
        ContentValues values=new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,name.getText().toString());
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,Integer.parseInt(price.getText().toString()));
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,Integer.parseInt(quantity.getText().toString()));
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER,name.getText().toString());
        if(editMode){
            getContentResolver().update(uri,values,null,null);
        }else {
            getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (editMode) {
            String[] projection = {ProductEntry._ID,
                    ProductEntry.COLUMN_PRODUCT_NAME,
                    ProductEntry.COLUMN_PRODUCT_QUANTITY,
                    ProductEntry.COLUMN_PRODUCT_PRICE,
                    ProductEntry.COLUMN_PRODUCT_SUPPLIER};
            String selection = ProductEntry._ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            return new CursorLoader(this, ProductEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToNext()){
            name.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME)));
            supplier.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER)));
            quantity.setText(String.valueOf(data.getInt(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY))));
            price.setText(String.valueOf(data.getInt(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE))));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    

/////////////////////////////////////////////////////////////
}