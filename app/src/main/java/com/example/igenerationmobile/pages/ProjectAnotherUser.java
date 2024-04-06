package com.example.igenerationmobile.pages;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.databinding.ActivityProjectAnotherUserBinding;
import com.example.igenerationmobile.fragments.projectAnotherUser.UsersProjectAnotherUser;
import com.example.igenerationmobile.fragments.projectAnotherUser.ViewProjectAnotherUser;

public class ProjectAnotherUser extends AppCompatActivity {

    private Toolbar toolbar;
    private ActivityProjectAnotherUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProjectAnotherUserBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            System.out.println("ActionBar not is null");
        } else {
            System.out.println("ActionBar is null");
        }

        if (bundle != null) {
            toolbar.setTitle(bundle.getString("nameProject"));
        }

        ReplaceFragment(new ViewProjectAnotherUser());

        binding.bottomMainPageView.setOnItemSelectedListener(v -> {

            switch(v.getItemId()) {
                case R.id.viewProject:
                    ReplaceFragment(new ViewProjectAnotherUser());
                    break;
                case R.id.usersProject:
                    ReplaceFragment(new UsersProjectAnotherUser());
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