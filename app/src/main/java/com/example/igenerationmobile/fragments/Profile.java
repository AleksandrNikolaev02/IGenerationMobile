package com.example.igenerationmobile.fragments;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String token;

    private final ObjectMapper mapper = new ObjectMapper();
    private TextView fio;
    private TextView achievements_sum;
    private ImageView avatar;
    private User user;

    public Profile(String token) {
        this.token = token;
    }

    // TODO: Rename and change types and number of parameters
//    public static Profile newInstance(String param1, String param2) {
//        Profile fragment = new Profile(token);
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println(token);

        new HTTPProcess().execute(token);



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
            avatar.setImageBitmap(bitmap);
        }
    }
}