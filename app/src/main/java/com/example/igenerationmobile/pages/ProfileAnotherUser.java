package com.example.igenerationmobile.pages;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
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
    private TextView IGNRole;
    private Toolbar toolbar;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_another_user);

        shapeableImageView = findViewById(R.id.ShapeableImageView);
        fio = findViewById(R.id.name);
        rating = findViewById(R.id.ratingAnotherUser);
        countProjectsAnotherUser = findViewById(R.id.countProjectsAnotherUser);
        IGNRole = findViewById(R.id.IGNRole);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        try {
            token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getInt("user_id");
        }

        new HTTPProcess().execute();
        new getCountProjects().execute();

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

                int status = user.getInt("status");

                switch (status) {
                    case 1:
                        IGNRole.setText("Участник");
                        IGNRole.setBackgroundColor(getColor(R.color.participant));
                        IGNRole.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                        break;
                    case 3:
                        IGNRole.setText("Наставник");
                        IGNRole.setBackgroundColor(getColor(R.color.mentor));
                        break;
                    case 4:
                        IGNRole.setText("Эксперт");
                        IGNRole.setBackgroundColor(getColor(R.color.expert));
                        break;
                    case 5:
                        IGNRole.setText("Администратор");
                        IGNRole.setBackgroundColor(getColor(R.color.administrator));
                        break;
                    case 7:
                        IGNRole.setText("Заказчик");
                        IGNRole.setBackgroundColor(getColor(R.color.customer));
                        break;
                    default:
                        IGNRole.setText("Другой");
                        IGNRole.setBackgroundColor(getColor(R.color.participant));
                        break;
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