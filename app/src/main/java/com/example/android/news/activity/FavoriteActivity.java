package com.example.android.news.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.news.DataBase.Contract;
import com.example.android.news.DataBase.DbHelper;
import com.example.android.news.R;
import com.example.android.news.adapter.NewsStoryAdapter;
import com.example.android.news.model.NewsItem;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private ProgressBar loadingIndicator;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview_all_rootview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsStoryAdapter(this, new ArrayList<NewsItem>());
        recyclerView.setAdapter(adapter);

        loadingIndicator = findViewById(R.id.loading_indicator);

        connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        DbHelper dbhelper = new DbHelper(this);
        SQLiteDatabase sqLiteDatabase = dbhelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Contract.Save.TABLE_NAME , null);
        if (cursor.moveToFirst()) {
            ArrayList<NewsItem> Items = new ArrayList<>();
            do {
                String title = cursor.getString(cursor.getColumnIndex(Contract.Save.COLUMN_WEB_TITLE));
                String url = cursor.getString(cursor.getColumnIndex(Contract.Save.COLUMN_WEBURL));
                String date = cursor.getString(cursor.getColumnIndex(Contract.Save.COLUMN_WEB_PUBLICATION_DATE));
                String section = cursor.getString(cursor.getColumnIndex(Contract.Save.COLUMN_SECTION));
                String author = cursor.getString(cursor.getColumnIndex(Contract.Save.COLUMN_AUTHOR));
                String thumbnail = cursor.getString(cursor.getColumnIndex(Contract.Save.COLUMN_THUMBNAIL_IMAGE));
                Items.add(new NewsItem(title, section, author, date, url, thumbnail));
            } while (cursor.moveToNext());
            loadingIndicator.setVisibility(View.GONE);
            adapter = new NewsStoryAdapter(this, Items);
            recyclerView.setAdapter(adapter);
        }
    }


}
