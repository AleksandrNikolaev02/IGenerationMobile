package com.example.igenerationmobile.fragments.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;

public class Profile extends Fragment {

    private String token;

    private final ObjectMapper mapper = new ObjectMapper();
    private TextView fio;
    private TextView achievements_sum;
    private ImageView avatar;
    private User user;

    // FIXME при повороте экрана приложения на этой странице, приложение падает!
    public Profile(String token) {
        this.token = token;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new HTTPProcess().execute(token);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fio = view.findViewById(R.id.fio_new);
        achievements_sum = view.findViewById(R.id.achievements_sum_new);
        avatar = view.findViewById(R.id.avatar_new);

        return view;
    }

    private class HTTPProcess extends AsyncTask<String, String, String> {
        private boolean isProcessed = false;

        @Override
        protected String doInBackground(String... strings) {
            try {
                Token token = mapper.readValue(strings[0], Token.class);
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
                    user = mapper.readValue(result, User.class);


                    if (getActivity() != null) {
                        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("Params", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("user_id", user.getId());
                        editor.apply();
                    }



                    System.out.println(StringEscapeUtils.unescapeJava(user.getFname()));

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
            if (strings[0].isEmpty()) {
                return HTTPMethods.getDefaultImage();
            }
            return HTTPMethods.getImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            avatar.setImageBitmap(bitmap);
        }
    }
}