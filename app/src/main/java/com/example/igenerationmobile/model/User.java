package com.example.igenerationmobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("fname")
    private String fname;
    @JsonProperty("iname")
    private String iname;
    @JsonProperty("oname")
    private String oname;
    @JsonProperty("login1")
    private String login1;
    @JsonProperty("img_file")
    private String img_file;
    @JsonProperty("sex")
    private int sex;
    @JsonProperty("status")
    private int status;
    @JsonProperty("mode")
    private int mode;
    @JsonProperty("place_id")
    private int place_id;
    @JsonProperty("place_name")
    private String place_name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("email_verified_at")
    private String email_verified_at;
    @JsonProperty("created_at")
    private String created_at;
    @JsonProperty("updated_at")
    private String updated_at;
    @JsonProperty("rating")
    private Rating rating;
    @JsonProperty("classifier")
    private Object classifier;
    @JsonProperty("cnt_msgs_unread")
    private int cnt_msgs_unread;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getIname() {
        return iname;
    }

    public void setIname(String iname) {
        this.iname = iname;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getLogin1() {
        return login1;
    }

    public void setLogin1(String login1) {
        this.login1 = login1;
    }

    public String getImg_file() {
        return img_file;
    }

    public void setImg_file(String img_file) {
        this.img_file = img_file;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Object getClassifier() {
        return classifier;
    }

    public void setClassifier(Object classifier) {
        this.classifier = classifier;
    }

    public int getCnt_msgs_unread() {
        return cnt_msgs_unread;
    }

    public void setCnt_msgs_unread(int cnt_msgs_unread) {
        this.cnt_msgs_unread = cnt_msgs_unread;
    }

    public static class Rating {
        @JsonProperty("achievements_sum")
        private int achievements_sum;
        @JsonProperty("trophys_sum")
        private int trophys_sum;
        @JsonProperty("penaltys_sum")
        private int penaltys_sum;

        public int getAchievements_sum() {
            return achievements_sum;
        }

        public void setAchievements_sum(int achievements_sum) {
            this.achievements_sum = achievements_sum;
        }

        public int getTrophys_sum() {
            return trophys_sum;
        }

        public void setTrophys_sum(int trophys_sum) {
            this.trophys_sum = trophys_sum;
        }

        public int getPenaltys_sum() {
            return penaltys_sum;
        }

        public void setPenaltys_sum(int penaltys_sum) {
            this.penaltys_sum = penaltys_sum;
        }
    }
}
