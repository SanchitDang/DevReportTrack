package com.sanapplications.devreporttrack.Models;

import java.util.List;

public class NewsApiModel {
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
