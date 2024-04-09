package com.example.igenerationmobile.pages;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.databinding.ActivityProjectAnotherUserBinding;
import com.example.igenerationmobile.fragments.projectAnotherUser.UsersProjectAnotherUser;
import com.example.igenerationmobile.fragments.projectAnotherUser.ViewProjectAnotherUser;

public class ProjectAnotherUser extends AppCompatActivity {

    private Toolbar toolbar;
    private ActivityProjectAnotherUserBinding binding;
    private int state;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProjectAnotherUserBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ReplaceFragment(new ViewProjectAnotherUser());
        state = R.id.viewProject;

        binding.bottomMainPageView.setOnItemSelectedListener(v -> {

            switch(v.getItemId()) {
                case R.id.viewProject:
                    if (state == R.id.viewProject) break;
                    ReplaceFragment(new ViewProjectAnotherUser());
                    state = R.id.viewProject;
                    break;
                case R.id.usersProject:
                    if (state == R.id.usersProject) break;
                    ReplaceFragment(new UsersProjectAnotherUser());
                    state = R.id.usersProject;
                    break;
            }

            return true;
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ReplaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainPage, fragment);
        fragmentTransaction.commit();
    }
}