package com.personal.android.wallpaper.models;

import com.google.gson.annotations.SerializedName;

public class Collection {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("total_photos")
    private int totalPhotos;
    @SerializedName("cover_photo")
    private Photo coverPhotos = new Photo();
    @SerializedName("user")
    private User user = new User();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(int totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public Photo getCoverPhotos() {
        return coverPhotos;
    }

    public void setCoverPhotos(Photo coverPhotos) {
        this.coverPhotos = coverPhotos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
