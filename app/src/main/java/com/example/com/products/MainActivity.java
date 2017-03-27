package com.example.com.products;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.com.products.data.ProductContract;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    View emptyView ;
    ProductCursorAdapter adapter;
    ListView listView;
    public static final int LOADER_ID1=1;
    public final String LOG_TAG=MainActivity.this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView= (ListView) findViewById(R.id.list_id);
        FloatingActionButton floatingActionButton= (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this,DetailsActivity.class);
                startActivity(i);
            }
        });
        emptyView=findViewById(R.id.empty_view);
        adapter=new ProductCursorAdapter(this,null);
        listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(MainActivity.this,DetailsActivity.class);
                i.setData(ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI,id));
                Log.v(LOG_TAG,ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI,id).toString());
                Log.v(LOG_TAG, ProductContract.ProductEntry.CONTENT_URI.toString());
                startActivity(i);
            }
        });

        getLoaderManager().initLoader(LOADER_ID1,null,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.dummy_data:
                insertPet();
                return true;
            case R.id.delete_all:
                deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {
        getContentResolver().delete(ProductContract.ProductEntry.CONTENT_URI,null,null);
    }

    private void insertPet() {
        ContentValues values=new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,"Product");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,10);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,12);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER,"abc");
        getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI,values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
        };
        return new CursorLoader(this,ProductContract.ProductEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
