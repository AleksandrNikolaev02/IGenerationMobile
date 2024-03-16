package com.example.igenerationmobile.model;

import android.graphics.Bitmap;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class Pair {

    public Map<String, Bitmap> images;
    public List<JSONObject> jsons;

    public Pair(Map<String, Bitmap> images, List<JSONObject> jsons) {
        this.images = images;
        this.jsons = jsons;
    }
}
