package com.example.igenerationmobile.model.ExpandableListModel;

public class Stage {
    private String title = "";
    private Float rating;
    private boolean isTitle;
    private boolean isComment;
    private boolean isButton;

    public Stage(String title, Float rating, boolean isTitle, boolean isComment, boolean isButton) {
        this.title = title;
        this.rating = rating;
        this.isTitle = isTitle;
        this.isComment = isComment;
        this.isButton = isButton;
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

    public boolean isTitle() {
        return isTitle;
    }

    public boolean isComment() {
        return isComment;
    }

    public boolean isButton() {
        return isButton;
    }
}
