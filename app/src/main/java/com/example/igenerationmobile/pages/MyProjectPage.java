package com.example.igenerationmobile.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.databinding.ActivityMyProjectPageBinding;
import com.example.igenerationmobile.fragments.myProject.TrajectoryProject;
import com.example.igenerationmobile.fragments.myProject.UsersProject;
import com.example.igenerationmobile.fragments.myProject.ViewProject;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyProjectPage extends AppCompatActivity {

    private String token;
    private Integer project_id;

    private Integer author_id;

    private Integer track_id;

    private ActivityMyProjectPageBinding binding;

    private Bitmap projectImage;

    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProjectPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            token = extras.getString("token");
            project_id = extras.getInt("project_id");
            author_id = extras.getInt("author_id");
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Params", Context.MODE_PRIVATE);
            String encoded = sharedPreferences.getString("project_image", "");
            byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
            projectImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        }

        ReplaceFragment(new ViewProject(token, projectImage, project_id));

        binding.bottomNavigationView2.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.viewProject:
                    ReplaceFragment(new ViewProject(token, projectImage, project_id));
                    break;
                case R.id.usersProject:
                    ReplaceFragment(new UsersProject(token, project_id, author_id));
                    break;
                case R.id.trajectoryProject:
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pages.my_project_page", Context.MODE_PRIVATE);
                    track_id = sharedPreferences.getInt("track_id", -1);
                    ReplaceFragment(new TrajectoryProject(token, project_id, track_id));
                    break;
            }

            return true;
        });

    }

    private void ReplaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}