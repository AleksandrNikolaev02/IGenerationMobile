package com.example.igenerationmobile.pages;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.databinding.ActivityMyProjectPageBinding;
import com.example.igenerationmobile.fragments.myProject.EmptyProject;
import com.example.igenerationmobile.fragments.myProject.TrajectoryProject;
import com.example.igenerationmobile.fragments.myProject.UsersProject;
import com.example.igenerationmobile.fragments.myProject.ViewProject;

public class MyProjectPage extends AppCompatActivity {

    private Integer track_id;
    private ActivityMyProjectPageBinding binding;
    private Toolbar toolbar;
    private int state;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProjectPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ReplaceFragment(new ViewProject(), "ViewProject");
        state = R.id.viewProject;

        binding.bottomNavigationView2.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.viewProject:
                    if (state == R.id.viewProject) break;
                    ReplaceFragment(new ViewProject(), "ViewProject");
                    state = R.id.viewProject;
                    break;
                case R.id.usersProject:
                    if (state == R.id.usersProject) break;
                    ReplaceFragment(new UsersProject(), "UsersProject");
                    state = R.id.usersProject;
                    break;
                case R.id.trajectoryProject:
                    if (state == R.id.trajectoryProject) break;
                    state = R.id.trajectoryProject;
                    SharedPreferences track = getApplicationContext().getSharedPreferences("pages.my_project_page", Context.MODE_PRIVATE);
                    track_id = track.getInt("track_id", -1);
                    if (track_id == -1) {
                        ReplaceFragment(new EmptyProject(), "EmptyProject");
                    } else {
                        ReplaceFragment(new TrajectoryProject(), "TrajectoryProject");
                    }
                    break;
            }

            return true;
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.edit_project);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        boolean isAuthor = sharedPreferences.getBoolean("isAuthor", false);

        if (!isAuthor) item.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.edit_project) {

            Intent intent = new Intent(MyProjectPage.this, EditProjectPage.class);

            startActivity(intent);

            return true;
        }

        if (item.getItemId() == R.id.refresh_project) {
            switch (state) {
                case R.id.viewProject:
                    ViewProject viewProject = (ViewProject) getSupportFragmentManager().findFragmentByTag("ViewProject");

                    if (viewProject != null) viewProject.update();

                    break;
                case R.id.usersProject:
                    UsersProject usersProject = (UsersProject) getSupportFragmentManager().findFragmentByTag("UsersProject");

                    if (usersProject != null) usersProject.update();

                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void ReplaceFragment(Fragment fragment, String tag) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, tag);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}