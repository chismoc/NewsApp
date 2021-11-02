package com.example.android.newsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.viewHolder> {
    private ArrayList<NewsArticle> news = new ArrayList<>();
    private Context context;


    public NewsAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);

        return new viewHolder(view);
    }

    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title_textView.setText(news.get(position).getTitle());
        holder.description_textView.setText(news.get(position).getDescription());
        holder.date_textView.setText(news.get(position).getDate());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebsiteActivity.class);
                intent.putExtra("url", news.get(position).getLink());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void setNews(ArrayList<NewsArticle> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        //instatiate Views
        private TextView title_textView, description_textView, date_textView;
        private CardView parent;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            //find textViews with corresponding id
            title_textView = itemView.findViewById(R.id.title_textView);
            description_textView = itemView.findViewById(R.id.description_textView);
            date_textView = itemView.findViewById(R.id.date_textView);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
