package com.example.igenerationmobile.model.AllProjectAdapter;

public class ProjectModel {

    private String projectImageURL;

    private int rating;

    private String projectName;

    public ProjectModel(String projectImageURL, int rating, String projectName) {
        this.projectImageURL = projectImageURL;
        this.rating = rating;
        this.projectName = projectName;
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
}
