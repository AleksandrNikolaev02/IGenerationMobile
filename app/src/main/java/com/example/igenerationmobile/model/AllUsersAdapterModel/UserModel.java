package com.example.igenerationmobile.model.AllUsersAdapterModel;

import java.time.LocalDateTime;

public class UserModel {

    private String imageURL;
    private String name;
    private int rating;
    private int status;
    private LocalDateTime dateTime;
    private int user_id;

    public UserModel(String imageURL, String name, int rating, int status, LocalDateTime dateTime, int users_id) {
        this.imageURL = imageURL;
        this.name = name;
        this.rating = rating;
        this.status = status;
        this.dateTime = dateTime;
        this.user_id = users_id;
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

    public int getUser_id() {
        return user_id;
    }
}
