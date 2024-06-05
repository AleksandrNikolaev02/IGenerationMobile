package com.example.igenerationmobile.dto;

public class TrackDto {
    private int project_id;
    private boolean show_hiddens;

    public TrackDto(int project_id, boolean show_hiddens) {
        this.project_id = project_id;
        this.show_hiddens = show_hiddens;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public boolean isShow_hiddens() {
        return show_hiddens;
    }

    public void setShow_hiddens(boolean show_hiddens) {
        this.show_hiddens = show_hiddens;
    }
}
