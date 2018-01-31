package com.example.android.news.model;

public class NewsItem {
    private String title;
    private String section;
    private String author;
    private String date;
    private String webUrl;
    private String thumbnail;  //FISAL
    private boolean isFavored;

    public NewsItem(String title, String section, String author, String date, String webUrl,  /* FISAL */String thumbnail) {
        this.title = title;
        this.section = section;
        this.author = author;
        this.date = date;
        this.webUrl = webUrl;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getWebUrl() {
        return webUrl;
    }

    /**
     * FISAL
     */
    public String getThumbnail() {
        return thumbnail;
    }

    public void setIsFavored(){
        this.isFavored = !this.isFavored;
    }

    public boolean getIsFavored(){
         return isFavored;
    }

}
