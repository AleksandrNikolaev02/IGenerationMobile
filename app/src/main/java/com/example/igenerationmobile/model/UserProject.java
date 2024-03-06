package com.example.igenerationmobile.model;

import android.graphics.Bitmap;

public class UserProject {

    private Integer id;
    private Bitmap avatar;
    private String img_file;

    private String fio;

    private String role;

    public UserProject(Bitmap avatar, String fio, String role, Integer id, String img_file) {
        this.avatar = avatar;
        this.fio = fio;
        this.role = role;
        this.id = id;
        this.img_file = img_file;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg_file() {
        return img_file;
    }

    public void setImg_file(String img_file) {
        this.img_file = img_file;
    }
}
