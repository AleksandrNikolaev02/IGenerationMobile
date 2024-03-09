package com.example.igenerationmobile.model.ExpandableListModel;

public class Stage {
    private String title = "";
    private Float rating;

    public Stage(String title, Float rating) {
        this.title = title;
        this.rating = rating;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
