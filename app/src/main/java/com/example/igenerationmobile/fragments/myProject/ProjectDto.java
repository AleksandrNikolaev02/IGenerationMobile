package com.example.igenerationmobile.fragments.myProject;

public class ProjectDto {

    private int project_id;

    private int track_id;

    public ProjectDto(int project_id, int track_id) {
        this.project_id = project_id;
        this.track_id = track_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getTrack_id() {
        return track_id;
    }

    public void setTrack_id(int track_id) {
        this.track_id = track_id;
    }
}
