package com.example.igenerationmobile.dto;

public class UserDto {
    private int project_id;

    public UserDto(int project_id) {
        this.project_id = project_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }
}
