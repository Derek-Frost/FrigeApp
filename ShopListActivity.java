package com.example.frigeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class ShopListActivity extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private ShopListAdapter adapter;

    private ArrayList<Product> products = new ArrayList<>();
    LocationManager locationManager;
    LocationProvider _locationProvider;
    RecyclerView recyclerView;


    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }
    };


    private void showLocation(Location location) {
        if (location == null)
            return;
        Toast.makeText(this, "LocationUpdated", Toast.LENGTH_SHORT).show();
        adapter.setLocation(location);
        //adapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000*10, 0, locationListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        _locationProvider = locationManager
                .getProvider(LocationManager.NETWORK_PROVIDER);




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

        recyclerView = (RecyclerView) findViewById(R.id.ShopRecyclerView);
        Cursor category = mDb.rawQuery("SELECT title, count, weight FROM productShopList JOIN products ON productShopList.productId = products.id", null);
        category.moveToFirst();

        while (!category.isAfterLast()) {
            products.add(new Product(category.getString(0), (category.getString(1)),
                    (category.getString(2))));
            category.moveToNext();
        }
        category.close();
        adapter = new ShopListAdapter(this, products, mDb);
        recyclerView.setAdapter(adapter);


        /*findViewById(R.id.fab1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String products = "";
                FragmentManager manager = getSupportFragmentManager();
                FireMissilesDialogFragment dialogFragment = new FireMissilesDialogFragment();
                Bundle args = new Bundle();
                Cursor category = mDb.rawQuery("SELECT title FROM products", null);
                category.moveToFirst();
                while (!category.isAfterLast()) {
                    products += category.getString(0) + ' ';
                    category.moveToNext();
                }
                category.close();
                args.putString("products", products);
                dialogFragment.setArguments(args);
                dialogFragment.show(manager, "fe");

            }
        });*/

        findViewById(R.id.fab1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String products = "";
                FragmentManager manager = getSupportFragmentManager();
                ShopListAddDialog dialogFragment = new ShopListAddDialog();
                Bundle args = new Bundle();
                Cursor category = mDb.rawQuery("SELECT title FROM categories", null);
                category.moveToFirst();
                while (!category.isAfterLast()) {
                    products += category.getString(0) + ' ';
                    category.moveToNext();
                }
                category.close();
                args.putString("products", products);
                dialogFragment.setArguments(args);
                dialogFragment.show(manager, "fe");

            }
        });



    }

    public void ClickFinish(View view){
        Toast.makeText(this, "WOW", Toast.LENGTH_LONG).show();
        Product thisProduct;
        String ProductName, ProductCount, ProductWeight, ProductLongtitude, ProductLatitude, idProduct = "", date, Ref_Id;
        products = adapter.getListProducts();
        for (int i = 0; i < products.size(); i++) {
            thisProduct = products.get(i);
            ProductName = thisProduct.getName();
            ProductCount = thisProduct.getCount();
            ProductWeight = thisProduct.getWeight();
            ProductLongtitude = thisProduct.getLongtitude();
            ProductLatitude = thisProduct.getLatitude();
            Ref_Id = thisProduct.getRefrigerator_id();
            date = thisProduct.getData();
            Cursor category = mDb.rawQuery("SELECT id FROM products WHERE title = " + "'" + ProductName + "'", null);
            category.moveToFirst();
            while(!category.isAfterLast()) {
                idProduct = category.getString(0);
                category.moveToNext();
            }
            category.close();

            ContentValues values = new ContentValues();
            values.put("productId", idProduct);
            values.put("quantity", ProductCount);
            values.put("weight", ProductWeight);
            values.put("sell_by", date);
            values.put("refrigerator_id", Ref_Id);
            values.put("longtitude", ProductLongtitude);
            values.put("latitude", ProductLatitude);
            mDb.insert("productItems", "productId, quantity, weight, sell_by, refrigerator_id, longtitude, latitude",values);


        }

        mDb.delete("productShopList", "count != 'NULL'", null);
        products.clear();
        Cursor category = mDb.rawQuery("SELECT title, count, weight FROM productShopList JOIN products ON productShopList.productId = products.id", null);
        category.moveToFirst();

        while (!category.isAfterLast()) {
            products.add(new Product(category.getString(0), (category.getString(1)),
                    (category.getString(2))));
            category.moveToNext();
        }
        category.close();
        adapter = new ShopListAdapter(this, products, mDb);
        recyclerView.setAdapter(adapter);
    }

    public void okClicked(String s) {

        String idProduct = "";
        ContentValues values = new ContentValues();
        products.add(new Product(s, "", ""));
        Cursor category = mDb.rawQuery("SELECT id FROM products WHERE title = " + "'" + s + "'", null);
        category.moveToFirst();
        while(!category.isAfterLast()) {
            idProduct = category.getString(0);
            category.moveToNext();
        }
        category.close();
        values.put("productId", Integer.parseInt(idProduct));
        mDb.insert("productShopList", "productId", values);
        adapter.notifyDataSetChanged();
        /*ContentValues values = new ContentValues();
        values.put("productId", s);
        mDb.insert("productShopList", "productId", values);
        products.clear();
        Cursor category = mDb.rawQuery("SELECT title, count, weight, latitude, longtitude FROM productShopList JOIN products ON productShopList.productId = products.id", null);
        category.moveToFirst();

        while (!category.isAfterLast()) {
            products.add(new Product(category.getString(0), (category.getString(1)),
                    (category.getString(2)), category.getString(3), category.getString(4)));
            category.moveToNext();
        }
        category.close();
        adapter.notifyDataSetChanged();*/

    }

    public void okClickedCategory(String S)
    {

        String idProduct = "";
        Cursor category = mDb.rawQuery("SELECT id FROM categories WHERE title = " + "'" + S + "'", null);
        category.moveToFirst();
        while(!category.isAfterLast()) {
            idProduct = category.getString(0);
            category.moveToNext();
        }
        category.close();

        category = mDb.rawQuery("SELECT title FROM products WHERE category_id = " + "'" + idProduct + "'", null);
        idProduct = "";
        category.moveToFirst();
        while(!category.isAfterLast()) {
            idProduct += category.getString(0) + " ";
            category.moveToNext();
        }
        category.close();
        FragmentManager manager = getSupportFragmentManager();
        ShopListAddDialog2 dialogFragment = new ShopListAddDialog2();
        Bundle args = new Bundle();
        args.putString("products", idProduct);
        dialogFragment.setArguments(args);
        dialogFragment.show(manager, "fe");

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }



}