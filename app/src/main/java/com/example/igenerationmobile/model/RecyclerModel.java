package com.example.igenerationmobile.model;

import android.graphics.Bitmap;

public class RecyclerModel {
    private Bitmap image;
    private String role;
    private String title;
    private String date;

    public RecyclerModel(Bitmap image, String role, String title, String date) {
        this.image = image;
        this.role = role;
        this.title = title;
        this.date = date;
    }

    public Bitmap getImage() {
        return image;
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
}
