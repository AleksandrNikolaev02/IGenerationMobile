package com.example.igenerationmobile.model;

import java.util.List;

public class Pair {

    private int i;
    private List<String> expert_comments;

    public Pair(List<String> expert_comments, int i) {
        this.expert_comments = expert_comments;
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public List<String> getExpert_comments() {
        return expert_comments;
    }
}
