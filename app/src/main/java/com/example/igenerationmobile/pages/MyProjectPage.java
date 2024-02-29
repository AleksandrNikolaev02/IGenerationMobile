package com.example.igenerationmobile.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.example.igenerationmobile.R;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyProjectPage extends AppCompatActivity {

    private String token;
    private Integer project_id;

    private ImageView image;

    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_project_page);

        Bundle extras = getIntent().getExtras();

        image = findViewById(R.id.test_image);

        if (extras != null) {
            token = extras.getString("token");
            project_id = extras.getInt("project_id");
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Params", Context.MODE_PRIVATE);
            String encoded = sharedPreferences.getString("project_image", "");
            byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
            image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }

    }
}