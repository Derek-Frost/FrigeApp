package com.example.frigeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.util.ArrayList;

public class ShopListAddDialog extends DialogFragment {

    private String products;
    private  int itemProduct;
    private String product;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String products = getArguments().getString("products");
        final String[] productNamesArray = products.split(" ");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выберите категорию")
                // добавляем переключатели
                .setSingleChoiceItems(productNamesArray, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                Toast.makeText(
                                        getActivity(),
                                        "выбранная категория: "
                                                + productNamesArray[item],
                                        Toast.LENGTH_SHORT).show();
                                itemProduct = item;
                                product = productNamesArray[item];

                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((ShopListActivity) getActivity()).okClickedCategory(product);
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }


}
