package com.example.igenerationmobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Track {
    @JsonProperty("description")
    String description;
    @JsonProperty("id")
    int id;
    @JsonProperty("title")
    String title;
    @JsonProperty("user_id")
    int user_id;

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getUser_id() {
        return user_id;
    }
}
