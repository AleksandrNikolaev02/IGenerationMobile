package com.example.igenerationmobile.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.databinding.ActivityMyProjectPageBinding;
import com.example.igenerationmobile.fragments.myProject.EmptyProject;
import com.example.igenerationmobile.fragments.myProject.TrajectoryProject;
import com.example.igenerationmobile.fragments.myProject.UsersProject;
import com.example.igenerationmobile.fragments.myProject.ViewProject;

public class MyProjectPage extends AppCompatActivity {

    private String token;
    private Integer project_id;

    private Integer author_id;

    private Integer track_id;

    private ActivityMyProjectPageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProjectPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        project_id = sharedPreferences.getInt("project_id", -1);
        author_id = sharedPreferences.getInt("author_id", -1);


        ReplaceFragment(new ViewProject(token, project_id));

        binding.bottomNavigationView2.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.viewProject:
                    ReplaceFragment(new ViewProject(token, project_id));
                    break;
                case R.id.usersProject:
                    ReplaceFragment(new UsersProject(project_id, author_id));
                    break;
                case R.id.trajectoryProject:
                    SharedPreferences track = getApplicationContext().getSharedPreferences("pages.my_project_page", Context.MODE_PRIVATE);
                    track_id = track.getInt("track_id", -1);
                    if (track_id == -1) {
                        ReplaceFragment(new EmptyProject());
                    } else {
                        ReplaceFragment(new TrajectoryProject(token, project_id, track_id));
                    }
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