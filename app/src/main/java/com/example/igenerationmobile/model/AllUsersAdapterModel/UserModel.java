package com.example.igenerationmobile.model.AllUsersAdapterModel;

public class UserModel {

    private String imageURL;
    private String name;
    private int rating;
    private int status;

    public UserModel(String imageURL, String name, int rating, int status) {
        this.imageURL = imageURL;
        this.name = name;
        this.rating = rating;
        this.status = status;
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
}
