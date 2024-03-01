package com.example.igenerationmobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProjectID {

    @JsonProperty("id")
    private int id;

    @JsonProperty("user_id")
    private int user_id;

    @JsonProperty("project_id")
    private int project_id;
    @JsonProperty("logo_file")
    private String logo_file;
    @JsonProperty("monochrome")
    private int monochrome;
    @JsonProperty("img_file")
    private String img_file;
    @JsonProperty("icon_file")
    private String icon_file;
    @JsonProperty("color_base")
    private String color_base;
    @JsonProperty("title_short")
    private String title_short;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("conf")
    private Object conf;
    @JsonProperty("domain1")
    private String domain1;
    @JsonProperty("domain2")
    private String domain2;
    @JsonProperty("domain3")
    private String domain3;
    @JsonProperty("hidden")
    private int hidden;
    @JsonProperty("created_at")
    private String created_at;
    @JsonProperty("place")
    private String place;
    @JsonProperty("place_id")
    private int place_id;
    @JsonProperty("tracks_title")
    private List<Track> tracks_title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getLogo_file() {
        return logo_file;
    }

    public void setLogo_file(String logo_file) {
        this.logo_file = logo_file;
    }

    public int getMonochrome() {
        return monochrome;
    }

    public void setMonochrome(int monochrome) {
        this.monochrome = monochrome;
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

    public String getTitle_short() {
        return title_short;
    }

    public void setTitle_short(String title_short) {
        this.title_short = title_short;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getConf() {
        return conf;
    }

    public void setConf(Object conf) {
        this.conf = conf;
    }

    public String getDomain1() {
        return domain1;
    }

    public void setDomain1(String domain1) {
        this.domain1 = domain1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomain2() {
        return domain2;
    }

    public void setDomain2(String domain2) {
        this.domain2 = domain2;
    }

    public String getDomain3() {
        return domain3;
    }

    public void setDomain3(String domain3) {
        this.domain3 = domain3;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public List<Track> getTracks_title() {
        return tracks_title;
    }

    public void setTracks_title(List<Track> tracks_title) {
        this.tracks_title = tracks_title;
    }

    public static class Track {
        @JsonProperty("title")
        private String title;
        @JsonProperty("track_id")
        private int track_id;
        @JsonProperty("rating")
        private int rating;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTrack_id() {
            return track_id;
        }

        public void setTrack_id(int track_id) {
            this.track_id = track_id;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }
    }

}
