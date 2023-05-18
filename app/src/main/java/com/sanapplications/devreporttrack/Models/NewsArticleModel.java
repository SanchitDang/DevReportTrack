package com.sanapplications.devreporttrack.Models;

public class NewsArticleModel {
    private String title;
    private String description;
    private String urlToImage;
    private String url;

    public NewsArticleModel(){

    }

    public NewsArticleModel(String newsTitle, String newsDesc, String newsImageUrl, String newsArticleUrl) {
        this.title = newsTitle;
        this.description = newsDesc;
        this.urlToImage = newsImageUrl;
        this.url = newsArticleUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewsDesc() {
        return description;
    }

    public void setNewsDesc(String newsDesc) {
        this.description = newsDesc;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
