package com.example.igenerationmobile.dto;

public class CreateProjectDto {
    private int project_id;
    private String title;
    private int track_id;

    public CreateProjectDto(int project_id, String title, int track_id) {
        this.project_id = project_id;
        this.title = title;
        this.track_id = track_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public String getTitle() {
        return title;
    }

    public int getTrack_id() {
        return track_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTrack_id(int track_id) {
        this.track_id = track_id;
    }
}
