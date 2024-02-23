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


public class Resume extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String token;
    private ObjectMapper mapper = new ObjectMapper();
    private User user;
    private ImageView avatar;
    private TextView login;
    private TextView fname;
    private TextView iname;
    private TextView oname;
    private TextView placeName;

    public Resume(String token) {
        this.token = token;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HTTPProcess().execute(token);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume, container, false);

        avatar = view.findViewById(R.id.avatar);
        login = view.findViewById(R.id.login);
        fname = view.findViewById(R.id.fname);
        iname = view.findViewById(R.id.iname);
        oname = view.findViewById(R.id.oname);
        placeName = view.findViewById(R.id.placeName);

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

                    new HTTPImage().execute(user.getImg_file());

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
            avatar.setImageBitmap(bitmap);
        }
    }
}