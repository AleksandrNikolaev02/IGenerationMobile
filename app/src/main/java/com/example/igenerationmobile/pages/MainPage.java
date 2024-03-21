package com.example.igenerationmobile.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
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

import java.io.IOException;

public class MainPage extends AppCompatActivity {

    private ActivityMainPageBinding binding;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new getUserID().execute();

        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ReplaceFragment(new AllProjects());

        binding.bottomMainPageView.setOnItemSelectedListener(v -> {
            switch(v.getItemId()) {
                case R.id.allProjects:
                    ReplaceFragment(new AllProjects());
                    break;
                case R.id.allUsers:
                    ReplaceFragment(new AllUsers());
                    break;
                case R.id.yourProfile:
                    ReplaceFragment(new YourProfile());
                    break;
            }
            return true;
        });
    }

    private void ReplaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainPage, fragment);
        fragmentTransaction.commit();
    }

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
                editor.apply();

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}