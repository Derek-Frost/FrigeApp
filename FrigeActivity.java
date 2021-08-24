package com.example.frigeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class FrigeActivity extends AppCompatActivity {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private ArrayList<Product> products = new ArrayList<>();
    private ProductAdapter adapter;
    ArrayList<String> array;


    TextView textView;



    @Override
    protected void onResume() {
        super.onResume();


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

        //region Load Frige List
        products.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        Cursor category = mDb.rawQuery("SELECT title, quantity, weight, sell_by FROM productItems JOIN products ON productItems.productId = products.id WHERE productItems.refrigerator_id = 1", null);
        category.moveToFirst();

        while(!category.isAfterLast()){
            products.add(new Product(category.getString(0), (category.getString(1)),
                    (category.getString(2)), category.getString(3)));
            category.moveToNext();
        }
        category.close();
        adapter = new ProductAdapter(this, products, mDb);
        recyclerView.setAdapter(adapter);

        //endregion
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frige);

        //region connect DB
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
        //endregion





        findViewById(R.id.fab1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String products = "";
                Intent intent = new Intent();
                intent.setClass(FrigeActivity.this, AddProductActivity.class);
                Cursor category = mDb.rawQuery("SELECT title FROM products", null);
                category.moveToFirst();
                while(!category.isAfterLast()){
                    products += category.getString(0) + ' ';
                    category.moveToNext();
                }
                category.close();
                intent.putExtra("ProductsFromDB", products);
                startActivity(intent);

            }
        });

        findViewById(R.id.fab2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                array = adapter.getDataDelete();
                if(array.size() > 0) {
                    for (int i = 0; i < array.size(); i++) {
                        mDb.delete("productItems", "sell_by = " + "'" + array.get(i) + "'", null);
                    }
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });



    }
}