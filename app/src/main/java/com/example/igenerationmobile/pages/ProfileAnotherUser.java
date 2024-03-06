package com.example.igenerationmobile.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.databinding.ActivityProfileAnotherUserBinding;
import com.example.igenerationmobile.fragments.profileAnotherUser.AnotherProfile;
import com.example.igenerationmobile.fragments.profileAnotherUser.AnotherResume;

public class ProfileAnotherUser extends AppCompatActivity {

    private String token;
    private Integer user_id;

    private ActivityProfileAnotherUserBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileAnotherUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            token = extras.getString("token");
            user_id = extras.getInt("user_id");
            //byte[] imageAsBytes = Base64.decode(codedImage.getBytes(), Base64.DEFAULT);
            //avatar = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        }

        ReplaceFragment(new AnotherProfile(token, user_id));

        binding.bottomNavigationView3.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.anotherProfile:
                    ReplaceFragment(new AnotherProfile(token, user_id));
                    break;
                case R.id.anotherResume:
                    ReplaceFragment(new AnotherResume());
                    break;
            }

            return true;
        });

    }

    private void ReplaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutAnotherUser, fragment);
        fragmentTransaction.commit();
    }
}