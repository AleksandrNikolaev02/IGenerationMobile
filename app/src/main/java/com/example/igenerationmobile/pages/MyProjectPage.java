package com.example.igenerationmobile.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.model.RecyclerModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyProjectPage extends AppCompatActivity {

    private String token;
    private String model;

    private ImageView image;

    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_project_page);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            token = extras.getString("token");
            model = extras.getString("model");
        }

        image = findViewById(R.id.test_image);



    }
}