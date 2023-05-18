package com.sanapplications.devreporttrack.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sanapplications.devreporttrack.Models.NewsArticleModel;
import com.sanapplications.devreporttrack.R;

import java.util.List;

public class NewsCardAdapter extends RecyclerView.Adapter<NewsCardAdapter.ViewHolder>{
    private final Context context;
    private final List<NewsArticleModel> newsList;

    public NewsCardAdapter(Context context, List<NewsArticleModel> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView newsImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            newsImageView = itemView.findViewById(R.id.newsImageView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_news_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsArticleModel news = newsList.get(position);

        Glide.with(context).load(news.getUrlToImage()).into(holder.newsImageView);
        holder.titleTextView.setText(news.getTitle());
        holder.descriptionTextView.setText(news.getNewsDesc());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
