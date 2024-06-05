package com.example.igenerationmobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Project {

    @JsonProperty("id")
    private int id;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("project_id")
    private int projectId;

    @JsonProperty("logo_file")
    private String logoFile;

    @JsonProperty("monochrome")
    private int monochrome;

    @JsonProperty("img_file")
    private String imgFile;

    @JsonProperty("icon_file")
    private String iconFile;

    @JsonProperty("color_base")
    private String colorBase;

    @JsonProperty("title_short")
    private String titleShort;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("conf")
    private String conf;

    @JsonProperty("domain1")
    private String domain1;

    @JsonProperty("domain2")
    private String domain2;

    @JsonProperty("domain3")
    private String domain3;

    @JsonProperty("hidden")
    private int hidden;

    @JsonProperty("created_at")
    private String createdAt;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getLogoFile() {
        return logoFile;
    }

    public int getMonochrome() {
        return monochrome;
    }

    public String getImgFile() {
        return imgFile;
    }

    public String getIconFile() {
        return iconFile;
    }

    public String getColorBase() {
        return colorBase;
    }

    public String getTitleShort() {
        return titleShort;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getConf() {
        return conf;
    }

    public String getDomain1() {
        return domain1;
    }

    public String getDomain2() {
        return domain2;
    }

    public String getDomain3() {
        return domain3;
    }

    public int getHidden() {
        return hidden;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}

