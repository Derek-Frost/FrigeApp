package com.example.frigeapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Product> products;
    private final ArrayList<String> dataDelete;
    private Context context;
    private SQLiteDatabase mDb;


    ProductAdapter(Context context, List<Product> states, SQLiteDatabase mDb) {
        this.products = states;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        dataDelete = new ArrayList<>();
        this.mDb = mDb;
    }

    public ArrayList<String> getDataDelete() {
        return dataDelete;
    }



    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rowlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        Product state = products.get(position);
        holder.nameView.setText(state.getName());
        holder.countView.setText(state.getCount());
        holder.weightView.setText((String.format("%.2f kg", Double.parseDouble((state.getWeight())))));
        holder.dataView.setText(state.getData());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, countView, weightView, dataView;
        public CheckBox selectionState;
        public ImageButton button;

        ViewHolder(View view){
            super(view);
            button = (ImageButton) view.findViewById(R.id.buttonMinus);
            nameView = (TextView) view.findViewById(R.id.Name);
            countView = (TextView) view.findViewById(R.id.Count);
            weightView = (TextView) view.findViewById(R.id.Weight);
            dataView = (TextView) view.findViewById(R.id.Data);
            selectionState = (CheckBox) view.findViewById(R.id.checkBox3);

            selectionState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        dataDelete.add((String) dataView.getText());
                    } else {
                        //TODO
                    }
                }
            });

            button.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ContentValues cv = new ContentValues();
                    String c = String.valueOf(countView.getText());
                    if(!c.equals("-")) {
                    int i = Integer.parseInt(c);
                    if(i != 0) {
                        String dataThis = String.valueOf(dataView.getText());
                        i--;
                        cv.put("quantity", String.valueOf(i));
                        countView.setText(String.valueOf(i));

                        Product finded = products
                                .stream()
                                .filter(test -> (test.getData() == dataThis))
                                .findFirst()
                                .orElse(null);
                        Objects.requireNonNull(finded).setCount(String.valueOf(i));
                        weightView.setText(String.format("%.2f kg", Double.parseDouble(finded.getWeight())));
                        cv.put("weight", finded.getWeight());
                        mDb.update("productItems", cv, "sell_by = " + "'" + dataThis + "'", null);
                        if (i == 0) dataDelete.add((String) dataView.getText());
                    }

                    }

                }
            });

            nameView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    String thisLong = "";
                    String idProduct = "";
                    String thisLat = "";
                    Intent intent = new Intent();
                    intent.setClass(context, MapsActivity.class);

                   /* Cursor category = mDb.rawQuery("SELECT id FROM products WHERE title = " + "'" + dataView.getText() + "'", null);
                    category.moveToFirst();
                    while(!category.isAfterLast()) {
                        idProduct = category.getString(0);
                        category.moveToNext();
                    }
                    category.close();*/

                    Cursor category = mDb.rawQuery("SELECT longtitude, latitude FROM productItems WHERE sell_by = " + "'" + dataView.getText() + "'", null);
                    category.moveToFirst();
                    while(!category.isAfterLast()) {
                        thisLong = category.getString(0);
                        thisLat = category.getString(1);
                        category.moveToNext();
                    }
                    category.close();

                    if(thisLong!=null)
                    {
                    intent.putExtra("longtitude", thisLong);
                    intent.putExtra("latitude", thisLat);
                    intent.putExtra("ProductName", nameView.getText());
                    context.startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(context, "Товар положен дома", Toast.LENGTH_SHORT).show();
                    }
                }
            });






        }
    }
}
