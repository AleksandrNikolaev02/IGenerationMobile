package com.example.igenerationmobile.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;


public class Profile extends AppCompatActivity {

    private final ObjectMapper mapper = new ObjectMapper();
    private TextView fio;
    private TextView achievements_sum;
    private ImageView avatar;
    private User user;
    private String token;

    private Button resume;
    private Button rating;
    private Button options;
    private Button projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");

            new HTTPProcess().execute(token);
        }

        resume = findViewById(R.id.resume);
        rating = findViewById(R.id.rating);
        options = findViewById(R.id.options);
        projects = findViewById(R.id.projects);

        resume.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, Resume.class);
            intent.putExtra("token", token);
            startActivity(intent);
        });

    }

    private class HTTPProcess extends AsyncTask<String, String, String> {
        private boolean isProcessed = false;

        @Override
        protected String doInBackground(String... strings) {
            try {
                Token token = (Token) mapper.readValue(strings[0], Token.class);
                return HTTPMethods.user(token);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!isProcessed) {
                isProcessed = true;

                try {
                    user = (User) mapper.readValue(result, User.class);
                    System.out.println(StringEscapeUtils.unescapeJava(user.getFname()));

                    fio = findViewById(R.id.fio);
                    achievements_sum = findViewById(R.id.achievements_sum);

                    String name = StringEscapeUtils.unescapeJava(user.getFname()) + " " +
                                  StringEscapeUtils.unescapeJava(user.getIname()) + " " +
                                  StringEscapeUtils.unescapeJava(user.getOname());

                    String rating = "Рейтинг: " + user.getRating().getAchievements_sum();

                    fio.setText(name);
                    achievements_sum.setText(rating);

                    new HTTPImage().execute(user.getImg_file());



                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    private class HTTPImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            return HTTPMethods.getImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            runOnUiThread(() -> {
                avatar = findViewById(R.id.avatar);
                avatar.setImageBitmap(bitmap);
            });
        }
    }

}