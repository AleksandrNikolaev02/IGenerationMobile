package com.example.igenerationmobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InfoProject {

    @JsonProperty("created_at")
    String created_at;
    @JsonProperty("id")
    int id;
    @JsonProperty("project_id")
    int project_id;
    @JsonProperty("title")
    String title;
    @JsonProperty("user_id")
    int user_id;
}
