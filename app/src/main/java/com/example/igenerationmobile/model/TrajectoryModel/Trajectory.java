package com.example.igenerationmobile.model.TrajectoryModel;

public class Trajectory {

    private String trajectory;
    private String description;
    private int track_id;

    public Trajectory(String trajectory, String description, int track_id) {
        this.trajectory = trajectory;
        this.description = description;
        this.track_id = track_id;
    }

    public String getTrajectory() {
        return trajectory;
    }

    public String getDescription() {
        return description;
    }

    public int getTrack_id() {
        return track_id;
    }
}
