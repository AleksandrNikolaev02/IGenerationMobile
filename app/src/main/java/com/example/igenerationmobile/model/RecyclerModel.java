package com.example.igenerationmobile.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public class RecyclerModel {
    private String imageUrl;
    private String role;
    private String title;
    private String date;

    private int project_id;

    private int author_id;


    public RecyclerModel(String imageUrl, String role, String title, String date, int project_id, int author_id) {
        this.imageUrl = imageUrl;
        this.role = role;
        this.title = title;
        this.date = date;
        this.project_id = project_id;
        this.author_id = author_id;
    }

    public String getImage() {
        return imageUrl;
    }

    public String getRole() {
        return role;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getProject_id() {
        return project_id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    @NonNull
    @Override
    public String toString() {
        return "RecyclerModel{" +
                "image=" + imageUrl +
                ", role='" + role + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
