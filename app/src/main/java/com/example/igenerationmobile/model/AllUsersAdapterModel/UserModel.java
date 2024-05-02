package com.example.igenerationmobile.model.AllUsersAdapterModel;

import java.time.LocalDateTime;

public class UserModel {

    private String imageURL;
    private String name;
    private int rating;
    private int status;
    private LocalDateTime dateTime;

    public UserModel(String imageURL, String name, int rating, int status, LocalDateTime dateTime) {
        this.imageURL = imageURL;
        this.name = name;
        this.rating = rating;
        this.status = status;
        this.dateTime = dateTime;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
