package com.example.android.news.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.news.DataBase.Contract;
import com.example.android.news.DataBase.DbHelper;
import com.example.android.news.R;
import com.example.android.news.model.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by acer on 1/31/2018.
 */

public class NewsStoryAdapter extends RecyclerView.Adapter<NewsStoryAdapter.NewsStoryViewHolder> {

    private Context context;
    private List<NewsItem> newsItemArrayList;

     public NewsStoryAdapter(Context context, List<NewsItem> newsItemArrayList) {
        this.context = context;
        this.newsItemArrayList = newsItemArrayList;
    }

    @Override
    public NewsStoryAdapter.NewsStoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_newsstory, parent, false);
        return new NewsStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsStoryViewHolder holder, final int position) {
        final String titleString = newsItemArrayList.get(position).getTitle();
        final String sectionString = newsItemArrayList.get(position).getSection();
        final String authorString = newsItemArrayList.get(position).getAuthor();
        final String dateString = getDateOnly(newsItemArrayList.get(position).getDate());
        final String thumbnailURL = newsItemArrayList.get(position).getThumbnail();  //FISAL
        final String webURL = newsItemArrayList.get(position).getWebUrl();

        holder.title.setText(titleString);
        holder.section.setText(sectionString);
        holder.author.setText(authorString);
        holder.date.setText(dateString);
        if (!(thumbnailURL == null)) {
            // FISAL Coding line start here
            Picasso.with(context)
                    .load(thumbnailURL)
                    //.resize(50, 50)
                    //.centerCrop()
                    .into(holder.thumbnail);
            // FISAL Coding line end here
        } else {
            holder.thumbnail.setImageResource(R.drawable.no_image);
        }
        DbHelper dbHelper = new DbHelper(context);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsItemArrayList.get(position).setIsFavored();
                if (newsItemArrayList.get(position).getIsFavored()) {
                    holder.favorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                    ContentValues values = new ContentValues();
                    values.put(Contract.Save.COLUMN_WEB_TITLE, titleString);
                    values.put(Contract.Save.COLUMN_WEBURL, webURL);
                    values.put(Contract.Save.COLUMN_SECTION, sectionString);
                    values.put(Contract.Save.COLUMN_AUTHOR, authorString);
                    values.put(Contract.Save.COLUMN_WEB_PUBLICATION_DATE, dateString);
                    values.put(Contract.Save.COLUMN_THUMBNAIL_IMAGE, thumbnailURL);  //FISAL
                    db.insert(Contract.Save.TABLE_NAME, null, values);
                } else {
                    holder.favorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                }
            }
        });

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(newsItemArrayList.get(position).getWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context , R.string.no_internet_connection , Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(newsItemArrayList.get(position).getWebUrl());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                intent.putExtra(Intent.EXTRA_TEXT, uri.toString());
                context.startActivity(Intent.createChooser(intent, "Share URL"));

            }
        });

    }

    @Override
    public int getItemCount() {
        return newsItemArrayList.size();
    }

    class NewsStoryViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout container ;
        TextView title;
        TextView section;
        TextView author;
        TextView date;
        ImageView thumbnail; //FISAL
        ImageButton favorite;
        ImageButton share;

        NewsStoryViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.view_container);
            title = itemView.findViewById(R.id.textview_all_title);
            section = itemView.findViewById(R.id.textview_all_section);
            author = itemView.findViewById(R.id.textview_all_author);
            date = itemView.findViewById(R.id.textview_all_date);
            thumbnail = itemView.findViewById(R.id.imageview_all_photo); //FISAL
            favorite = itemView.findViewById(R.id.imagebutton_all_favorite);
            share = itemView.findViewById(R.id.imagebutton_all_share);

           share.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
        }
    }


    private String getDateOnly(String date) {
        String[] parts = date.split("T");
        return parts[0];
    }
}