package com.example.igenerationmobile.model;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Field {
    @JsonProperty("content")
    private String content;

    @JsonProperty("content_default")
    private String content_default;

    @JsonProperty("id")
    private int id;

    @JsonProperty("type")
    private int type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_default() {
        return content_default;
    }

    public void setContent_default(String content_default) {
        this.content_default = content_default;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return "Field{" +
                "content='" + content + '\'' +
                ", content_default='" + content_default + '\'' +
                ", id=" + id +
                ", type=" + type +
                '}';
    }
}
