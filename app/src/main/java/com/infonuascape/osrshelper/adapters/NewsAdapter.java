package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.OSRSNews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{
    private static final String TAG = "NewsAdapter";

    private Context context;
    private List<OSRSNews> osrsNews;
    private RecyclerItemClickListener listener;
    private DateFormat dateFormat;

    public NewsAdapter(final Context context, final List<OSRSNews> osrsNews, final RecyclerItemClickListener listener) {
        this.context = context;
        this.osrsNews = osrsNews;
        this.listener = listener;
        dateFormat = SimpleDateFormat.getDateInstance(DateFormat.LONG);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder:");
        return new NewsViewHolder(View.inflate(context, R.layout.news_list_item, null));
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder:");
        OSRSNews news = getItem(position);
        holder.title.setText(news.title);
        holder.description.setText(news.description);
        holder.category.setText(news.category);
        Glide.with(holder.image).load(news.imageUrl).into(holder.image);
        holder.publicationDate.setText(dateFormat.format(new Date(news.publicationDate)));
    }

    @Override
    public int getItemCount() {
        return osrsNews.size();
    }

    public OSRSNews getItem(int position) {
        return osrsNews.get(position);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView description;
        TextView category;
        TextView publicationDate;

        public NewsViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            description = itemView.findViewById(R.id.news_description);
            category = itemView.findViewById(R.id.news_category);
            publicationDate = itemView.findViewById(R.id.news_date);

            itemView.findViewById(R.id.news_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) {
                        listener.onItemClicked(getAdapterPosition());
                    }
                }
            });
        }
    }
}
