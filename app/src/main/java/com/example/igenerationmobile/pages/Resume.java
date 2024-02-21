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

public class Resume extends AppCompatActivity {

    private String token;
    private ObjectMapper mapper = new ObjectMapper();
    private User user;
    private ImageView avatar;
    private TextView login;
    private TextView fname;
    private TextView iname;
    private TextView oname;
    private TextView placeName;

    private Button profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");
            new HTTPProcess().execute(token);
        }

        profile = findViewById(R.id.profile);

        profile.setOnClickListener(v -> {

            Intent intent = new Intent(Resume.this, Profile.class);
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

                    new HTTPImage().execute(user.getImg_file());

                    login = findViewById(R.id.login);
                    fname = findViewById(R.id.fname);
                    iname = findViewById(R.id.iname);
                    oname = findViewById(R.id.oname);
                    placeName = findViewById(R.id.placeName);

                    login.setText(user.getName());
                    fname.setText(StringEscapeUtils.unescapeJava(user.getFname()));
                    iname.setText(StringEscapeUtils.unescapeJava(user.getIname()));
                    oname.setText(StringEscapeUtils.unescapeJava(user.getOname()));
                    placeName.setText(StringEscapeUtils.unescapeJava(user.getPlace_name()));


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