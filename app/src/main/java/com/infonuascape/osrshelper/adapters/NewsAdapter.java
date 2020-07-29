package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.News;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by marc_ on 2018-01-20.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{
    private static final String TAG = "NewsAdapter";

    private Context context;
    private List<News> news;
    private RecyclerItemClickListener listener;
    private DateFormat dateFormat;
    private SimpleDateFormat simpleDateFormat;

    public NewsAdapter(final Context context, final RecyclerItemClickListener listener) {
        this.context = context;
        this.news = new ArrayList<>();
        this.listener = listener;
        dateFormat = SimpleDateFormat.getDateInstance(DateFormat.LONG);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public void addNews(List<News> news) {
        int sizeBefore = this.news.size();
        this.news.addAll(news);
        notifyItemRangeInserted(sizeBefore, news.size());
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(context, R.layout.news_list_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new NewsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News news = getItem(position);
        holder.title.setText(news.title);
        holder.description.setText(news.description);
        holder.category.setText(news.category);
        Glide.with(holder.image).asBitmap().load(news.imageUrl).transform((new BlurTransformation(5))).into(holder.image);
        try {
            holder.publicationDate.setText(dateFormat.format(simpleDateFormat.parse(news.publicationDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public News getItem(int position) {
        return news.get(position);
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
