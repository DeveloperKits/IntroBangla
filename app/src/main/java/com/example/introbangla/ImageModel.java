package com.example.introbangla;

import com.google.gson.annotations.SerializedName;

public class ImageModel {
    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("url")
    private String url;

    @SerializedName("download_url")
    private String downloadUrl;

    // getters
    public String getId() { return id; }
    public String getAuthor() { return author; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getUrl() { return url; }
    public String getDownloadUrl() { return downloadUrl; }
}
