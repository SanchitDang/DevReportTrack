package com.sanapplications.devreporttrack.Services.Api;

import com.sanapplications.devreporttrack.Models.NewsApiModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET("v2/top-headlines")
    Call<NewsApiModel> getTopHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );
}
