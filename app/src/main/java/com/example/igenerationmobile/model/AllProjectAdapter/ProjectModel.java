package com.example.igenerationmobile.model.AllProjectAdapter;

public class ProjectModel {

    private String projectImageURL;

    private int rating;

    private String projectName;

    private String title;

    public ProjectModel(String projectImageURL, int rating, String projectName, String title) {
        this.projectImageURL = projectImageURL;
        this.rating = rating;
        this.projectName = projectName;
        this.title = title;
    }

    public String getProjectImage() {
        return projectImageURL;
    }

    public void setProjectImage(String projectImageURL) {
        this.projectImageURL = projectImageURL;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectImageURL() {
        return projectImageURL;
    }

    public void setProjectImageURL(String projectImageURL) {
        this.projectImageURL = projectImageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
