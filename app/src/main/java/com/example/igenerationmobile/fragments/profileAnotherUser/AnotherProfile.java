package com.example.igenerationmobile.fragments.profileAnotherUser;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.imageview.ShapeableImageView;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AnotherProfile extends Fragment {

    private String token;

    private Integer user_id;
    private ShapeableImageView shapeableImageView;
    private TextView fio;
    private TextView rating;

    private ObjectMapper mapper = new ObjectMapper();

    public AnotherProfile(String token, Integer user_id) {
        this.token = token;
        this.user_id = user_id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_another_profile, container, false);

        shapeableImageView = view.findViewById(R.id.ShapeableImageView);
        fio = view.findViewById(R.id.name);
        rating = view.findViewById(R.id.ratingAnotherUser);

        new HTTPProcess().execute();

        return view;
    }

    private class HTTPProcess extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                Token tokenObj = mapper.readValue(token, Token.class);
                return HTTPMethods.userID(tokenObj, user_id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                JSONObject user = new JSONObject(response);

                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("image.public.profile_imgs", Context.MODE_PRIVATE);

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
}