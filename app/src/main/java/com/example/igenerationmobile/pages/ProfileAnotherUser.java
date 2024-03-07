package com.example.igenerationmobile.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.imageview.ShapeableImageView;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ProfileAnotherUser extends AppCompatActivity {

    private Token token;
    private Integer user_id;
    private ShapeableImageView shapeableImageView;
    private TextView fio;
    private TextView rating;
    private TextView countProjectsAnotherUser;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_another_user);

        shapeableImageView = findViewById(R.id.ShapeableImageView);
        fio = findViewById(R.id.name);
        rating = findViewById(R.id.ratingAnotherUser);
        countProjectsAnotherUser = findViewById(R.id.countProjectsAnotherUser);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            try {
                token = mapper.readValue(extras.getString("token"), Token.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            user_id = extras.getInt("user_id");
        }

        new HTTPProcess().execute();
        new getCountProjects().execute();
    }

    private class HTTPProcess extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                return HTTPMethods.userID(token, user_id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                JSONObject user = new JSONObject(response);

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("image.public.profile_imgs", Context.MODE_PRIVATE);

                String result = sharedPreferences.getString(user.getString("img_file"), "");

                if (result.isEmpty()) {
                    new HTTPImage().execute(user.getString("img_file"));
                } else {
                    byte[] imageAsBytes = Base64.decode(result.getBytes(), Base64.DEFAULT);
                    shapeableImageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                }

                String builder = StringEscapeUtils.unescapeJava(user.getString("fname")) + " " +
                        StringEscapeUtils.unescapeJava(user.getString("iname")) + " " +
                        StringEscapeUtils.unescapeJava(user.getString("oname"));

                fio.setText(builder);

                String score = String.valueOf(user.getJSONObject("rating").getInt("achievements_sum"));

                rating.setText(score);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class HTTPImage extends AsyncTask<String, Bitmap, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            if (strings[0].isEmpty()) {
                return HTTPMethods.getDefaultImage();
            }
            return HTTPMethods.getImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            shapeableImageView.setImageBitmap(bitmap);
        }
    }

    private class getCountProjects extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                return HTTPMethods.cnt(token, user_id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                JSONObject cnt = new JSONObject(response);
                countProjectsAnotherUser.setText(String.valueOf(cnt.getInt("cnt")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

}