package com.example.frigeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private ListView ProductList;
    private ArrayList<String> productArray;
    String resultFrige;
    String resultFreeze;
    public void OnBack(View view){
        Intent intent = new Intent();
        intent.setClass(SearchActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public boolean search(ArrayList<String> productArray, String product)
    {
        return productArray.contains(product);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activty);
        ProductList = findViewById(R.id.ProductList);
        productArray = new ArrayList<>();
        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        Cursor category = mDb.rawQuery("SELECT title FROM productItems JOIN products ON productItems.productId = products.id", null);
        category.moveToFirst();
        String product;
        while(!category.isAfterLast()){
            product = category.getString(0);
            if(!search(productArray, product)) productArray.add(product);
            category.moveToNext();
        }
        category.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productArray);


        ProductList.setAdapter(adapter);
        ProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor categoryFrige = mDb.rawQuery("SELECT SUM(quantity) FROM productItems JOIN products ON productItems.productId = products.id " +
                                "WHERE refrigerator_id = 1 AND products.title =  " + "'" +
                                ProductList.getItemAtPosition(position) + "'",
                        null);
                categoryFrige.moveToFirst();
                resultFrige = categoryFrige.getString(0);
                categoryFrige.close();
                Cursor categoryFreeze = mDb.rawQuery("SELECT SUM(quantity) FROM productItems JOIN products ON productItems.productId = products.id " +
                                "WHERE refrigerator_id = 2 AND products.title =  " + "'" +
                                ProductList.getItemAtPosition(position) + "'",
                        null);
                categoryFreeze.moveToFirst();
                resultFreeze = categoryFreeze.getString(0);
                categoryFrige.close();


                Snackbar.make(view, "Холодильник: " + resultFrige + " Морозильник: " + resultFreeze, Snackbar.LENGTH_LONG)
                        .show();
            }
        });

    }
}