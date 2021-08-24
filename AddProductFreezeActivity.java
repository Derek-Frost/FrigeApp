package com.example.frigeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class AddProductFreezeActivity extends AppCompatActivity {

    private boolean checkProductParam = false;
    private boolean checkCountParam = false;
    private boolean checkWeightParam = false;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private int id_product;
    private String id_count;
    EditText weightText;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_freeze);
        Bundle args = getIntent().getExtras();
        String name = args.get("ProductsFromDB").toString();
        ArrayList<String> products = new ArrayList<>(Arrays.asList(name.split(" ")));
        ListView ProductView = (ListView) findViewById(R.id.ListViewProducts);
        ListView CountView = (ListView) findViewById(R.id.ListViewCount);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        ProductView.setAdapter(adapter);
        ArrayList<String> count= new ArrayList<>();
        count.add("-");
        int elementh = 0;
        for (int i = 0; i < 51; i++) {
            count.add(String.valueOf(elementh));
            elementh++;
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, count);
        CountView.setAdapter(adapter1);
        weightText = (EditText) findViewById(R.id.WeightText);
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

        Button button = (Button) findViewById(R.id.buttonAdd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCountParam && checkProductParam && weightText.length() > 0)
                {

                    Intent intent = new Intent(AddProductFreezeActivity.this, FreezeActivity.class);
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                    String date = df.format(Calendar.getInstance().getTime());
                    ContentValues values = new ContentValues();
                    values.put("productId", id_product);
                    values.put("quantity", id_count);
                    values.put("weight", weightText.getText().toString().replace(',', '.'));
                    values.put("sell_by", date);
                    values.put("refrigerator_id", "2");
                    mDb.insert("productItems", "productId, quantity, weight, sell_by, refrigerator_id",values);

                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(v, "Error weight", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
        //region ViewListeners
        ProductView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id_product = position;
                checkProductParam = true;

            }
        });
        CountView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id_count = (String) CountView.getItemAtPosition(position);
                checkCountParam = true;
            }
        });
        //endregion




    }
}