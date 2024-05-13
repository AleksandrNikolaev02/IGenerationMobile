package com.example.igenerationmobile.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.databinding.ActivityMainPageBinding;
import com.example.igenerationmobile.fragments.mainPage.AllProjects;
import com.example.igenerationmobile.fragments.mainPage.AllUsers;
import com.example.igenerationmobile.fragments.mainPage.YourProfile;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class MainPage extends AppCompatActivity {

    private ActivityMainPageBinding binding;
    BottomNavigationView view;
    private final ObjectMapper mapper = new ObjectMapper();
    private int state;

    @SuppressLint("NonConstantResourceId")
    @SuppressWarnings({"deprecation"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new getUserID().execute();

        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        view = findViewById(R.id.bottomMainPageView);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            int fragmentID = bundle.getInt("fragmentID");
            ReplaceFragment(new YourProfile());
            view.setSelectedItemId(R.id.yourProfile);
            state = R.id.yourProfile;
        } else {
            ReplaceFragment(new AllProjects());
            state = R.id.allProjects;
        }

        binding.bottomMainPageView.setOnItemSelectedListener(v -> {
            switch(v.getItemId()) {
                case R.id.allProjects:
                    if (state == R.id.allProjects) break;
                    ReplaceFragment(new AllProjects());
                    state = R.id.allProjects;
                    break;
                case R.id.allUsers:
                    if (state == R.id.allUsers) break;
                    ReplaceFragment(new AllUsers());
                    state = R.id.allUsers;
                    break;
                case R.id.yourProfile:
                    if (state == R.id.yourProfile) break;
                    ReplaceFragment(new YourProfile());
                    state = R.id.yourProfile;
                    break;
                case R.id.yourProjects:
                    Intent intent = new Intent(this, YourProjects.class);
                    startActivityForResult(intent, 200);
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                view.setSelectedItemId(state);
            }
        }
    }

    private void ReplaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainPage, fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    private class getUserID extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            try {
                Token token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
                return HTTPMethods.user(token);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                User user = mapper.readValue(response, User.class);

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id", user.getId());
                editor.putInt("status", user.getStatus());
                editor.putInt("mode", user.getMode());
                editor.apply();

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}