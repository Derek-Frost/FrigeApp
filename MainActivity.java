package com.example.frigeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public void OnFrige(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, FrigeActivity.class);
        startActivity(intent);
    }

    public void OnFreeze(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, FreezeActivity.class);
        startActivity(intent);
    }

    public void OnShopList(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ShopListActivity.class);
        startActivity(intent);
    }


    public void OnSearch(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}