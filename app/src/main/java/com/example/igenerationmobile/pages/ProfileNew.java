package com.example.igenerationmobile.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.databinding.ActivityProfileNewBinding;
import com.example.igenerationmobile.fragments.Options;
import com.example.igenerationmobile.fragments.Profile;
import com.example.igenerationmobile.fragments.Projects;
import com.example.igenerationmobile.fragments.Rating;
import com.example.igenerationmobile.fragments.Resume;

public class ProfileNew extends AppCompatActivity {

    private ActivityProfileNewBinding binding;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            token = extras.getString("token");
        }

        ReplaceFragment(new Profile(token));


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // cringe
            if (id == R.id.profile_new) ReplaceFragment(new Profile(token));
            if (id == R.id.resume_new) ReplaceFragment(new Resume(token));
            if (id == R.id.rating_new) ReplaceFragment(new Rating(token));
            if (id == R.id.options_new) ReplaceFragment(new Options(token));
            if (id == R.id.projects_new) ReplaceFragment(new Projects());

            return true;
        });

    }

    private void ReplaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}