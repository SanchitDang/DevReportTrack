package com.sanapplications.devreporttrack.Activities;

import static com.sanapplications.devreporttrack.R.id.newsRecyclerView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sanapplications.devreporttrack.Adapters.NewsCardAdapter;
import com.sanapplications.devreporttrack.Models.NewsArticleModel;
import com.sanapplications.devreporttrack.R;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class NewsActivity extends AppCompatActivity {

    private NewsCardAdapter newsAdapter;
    private List<NewsArticleModel> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        RecyclerView newsRecyclerView = findViewById(R.id.newsRecyclerView);
        newsList = new ArrayList<>();
        newsAdapter = new NewsCardAdapter(this, newsList);
        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Make API request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService newsApi = retrofit.create(NewsApiService.class);
        Call<NewsResponse> call = newsApi.getTopHeadlines("us", "f89a0a23e2e4410b98f9b4a18447128e");
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse newsResponse = response.body();
                    List<NewsArticleModel> articles = newsResponse.getArticles();

                    if (articles != null) {
                        newsList.addAll(articles);
                        newsAdapter.notifyDataSetChanged();
                    }
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }
}

 interface NewsApiService {
    @GET("v2/top-headlines")
    Call<NewsResponse> getTopHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );
}


 class NewsResponse {
    private String status;
    private int totalResults;
    private List<NewsArticleModel> articles;

    // Constructors, getters, and setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<NewsArticleModel> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsArticleModel> articles) {
        this.articles = articles;
    }
}
