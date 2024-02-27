package com.example.igenerationmobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyProject {
    @JsonProperty("id")
    private int id;
    @JsonProperty("project_id")
    private int project_id;
    @JsonProperty("user_id")
    private int user_id;
    @JsonProperty("status")
    private int status;
    @JsonProperty("confirmed")
    private int confirmed;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("author_id")
    private int author_id;
    @JsonProperty("logo_file")
    private String logo_file;
    @JsonProperty("img_file")
    private String img_file;
    @JsonProperty("icon_file")
    private String icon_file;
    @JsonProperty("color_base")
    private String color_base;
    @JsonProperty("hidden")
    private int hidden;
    @JsonProperty("created_at")
    private String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getLogo_file() {
        return logo_file;
    }

    public void setLogo_file(String logo_file) {
        this.logo_file = logo_file;
    }

    public String getImg_file() {
        return img_file;
    }

    public void setImg_file(String img_file) {
        this.img_file = img_file;
    }

    public String getIcon_file() {
        return icon_file;
    }

    public void setIcon_file(String icon_file) {
        this.icon_file = icon_file;
    }

    public String getColor_base() {
        return color_base;
    }

    public void setColor_base(String color_base) {
        this.color_base = color_base;
    }

    public int getHidden() {
        return hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

