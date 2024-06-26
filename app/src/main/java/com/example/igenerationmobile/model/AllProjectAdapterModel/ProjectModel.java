package com.example.igenerationmobile.model.AllProjectAdapterModel;

import java.util.ArrayList;
import java.util.List;

public class ProjectModel {

    private String projectImageURL;
    private int rating;
    private String projectName;
    private String title;
    private int project_id;
    private List<Integer> users;
    private int author_id;


    public ProjectModel(String projectImageURL,
                        int rating,
                        String projectName,
                        String title,
                        int project_id,
                        ArrayList<Integer> users,
                        int author_id) {
        this.projectImageURL = projectImageURL;
        this.rating = rating;
        this.projectName = projectName;
        this.title = title;
        this.project_id = project_id;
        this.users = users;
        this.author_id = author_id;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    public int getAuthor_id() {
        return author_id;
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

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }
}
