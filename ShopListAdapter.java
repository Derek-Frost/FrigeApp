package com.example.frigeapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final ArrayList<Product> products;
    private final ArrayList<String> dataDelete;
    Location location;
    private Context context;
    private SQLiteDatabase mDb;
    String lat;
    String lon;
    String idProduct;
    String count, weight;



    public ArrayList<Product> getListProducts() {return  products;}

    public Product getProduct(int position) {
        return products.get(position);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    ShopListAdapter(Context context, ArrayList<Product> states, SQLiteDatabase mDb) {
        this.products = states;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        dataDelete = new ArrayList<>();
        this.mDb = mDb;
    }


    @Override
    public ShopListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.shoplistlayout, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ShopListAdapter.ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.nameView.setText(product.getName());
        //holder.countView.setText(product.getLatitude());
        //holder.weightView.setText((String.format("%.2f kg", Double.parseDouble((product.getWeight())))));

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, countView, weightView;
        public CheckBox FrigeSelect, FreezeSelect;
        public Button button;

        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.ProductName);
            countView = (TextView) view.findViewById(R.id.ProductCount);
            weightView = (TextView) view.findViewById(R.id.ProductWeight);
            FrigeSelect = (CheckBox) view.findViewById(R.id.checkBox);
            FreezeSelect = (CheckBox) view.findViewById(R.id.checkBox2);

            FrigeSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        //Product testProduct = getProduct(getAdapterPosition());
                        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                        String date = df.format(Calendar.getInstance().getTime());
                        lat = String.format(
                                "%.4f",location.getLatitude());
                        lon = String.format(
                                "%.4f", location.getLongitude());
                        count = countView.getText().toString();
                        weight = weightView.getText().toString();
                        products.get(getAdapterPosition()).setWeight(weight);
                        products.get(getAdapterPosition()).setCountCustom(count);
                        products.get(getAdapterPosition()).setLongtitude(lon);
                        products.get(getAdapterPosition()).setLatitude(lat);
                        products.get(getAdapterPosition()).setWeight_1_piece();
                        products.get(getAdapterPosition()).setRefrigerator_id("1");
                        products.get(getAdapterPosition()).setData(date);
                        ContentValues cv = new ContentValues();
                        cv.put("longtitude", lon);
                        cv.put("latitude", lat);
                        cv.put("count", count);
                        cv.put("weight", weight);
                        Cursor category = mDb.rawQuery("SELECT id FROM products WHERE title = " + "'" + nameView.getText() + "'", null);
                        category.moveToFirst();
                        while(!category.isAfterLast()) {
                            idProduct = category.getString(0);
                            category.moveToNext();
                        }
                        category.close();

                        mDb.update("productShopList", cv, "productId = " + "'" + idProduct + "'", null);
                    } else {
                        //TODO
                    }
                }
            });
            FreezeSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        //Product testProduct = getProduct(getAdapterPosition());
                        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                        String date = df.format(Calendar.getInstance().getTime());
                        lat = String.format(
                                "%.4f",location.getLatitude());
                        lon = String.format(
                                "%.4f", location.getLongitude());
                        count = countView.getText().toString();
                        weight = weightView.getText().toString();
                        products.get(getAdapterPosition()).setWeight(weight);
                        products.get(getAdapterPosition()).setCountCustom(count);
                        products.get(getAdapterPosition()).setLongtitude(lon);
                        products.get(getAdapterPosition()).setLatitude(lat);
                        products.get(getAdapterPosition()).setWeight_1_piece();
                        products.get(getAdapterPosition()).setRefrigerator_id("2");
                        products.get(getAdapterPosition()).setData(date);
                        ContentValues cv = new ContentValues();
                        cv.put("longtitude", lon);
                        cv.put("latitude", lat);
                        cv.put("count", count);
                        cv.put("weight", weight);
                        Cursor category = mDb.rawQuery("SELECT id FROM products WHERE title = " + "'" + nameView.getText() + "'", null);
                        category.moveToFirst();
                        while(!category.isAfterLast()) {
                            idProduct = category.getString(0);
                            category.moveToNext();
                        }
                        category.close();

                        mDb.update("productShopList", cv, "productId = " + "'" + idProduct + "'", null);
                    } else {
                        //TODO
                    }
                }
            });





        }
    }
}
