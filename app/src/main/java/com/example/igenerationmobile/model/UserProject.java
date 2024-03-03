package com.example.igenerationmobile.model;

import android.graphics.Bitmap;

public class UserProject {
    private Bitmap avatar;

    private String fio;

    private String role;

    public UserProject(Bitmap avatar, String fio, String role) {
        this.avatar = avatar;
        this.fio = fio;
        this.role = role;
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
}
