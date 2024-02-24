package com.example.igenerationmobile.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class Options extends Fragment {

    private String token;

    private Switch viewingMode;

    private ObjectMapper mapper = new ObjectMapper();

    public Options(String token) {
        this.token = token;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            viewingMode = getActivity().findViewById(R.id.viewingMode);

            if (viewingMode != null) {
                viewingMode.setOnClickListener(item -> {
                    boolean checked = ((Switch) item).isChecked();
                    new HTTPUpdate().execute(checked);
                });
            } else {
                Log.e("OptionsFragment", "viewingMode is null");
            }
        } else {
            Log.e("OptionsFragment", "Activity is null");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        viewingMode = view.findViewById(R.id.viewingMode);

        new HTTPProcess().execute();

        viewingMode.setOnClickListener(item -> {
            boolean checked = ((Switch) item).isChecked();
            new HTTPUpdate().execute(checked);
        });

        return view;
    }

    private class HTTPProcess extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            try {
                Token tokenObj = (Token) mapper.readValue(token, Token.class);
                return HTTPMethods.user(tokenObj);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                User user = (User) mapper.readValue(response, User.class);
                int mode = user.getMode();
                viewingMode.setChecked(mode != 1);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class HTTPUpdate extends AsyncTask<Boolean, Void, String> {
        @Override
        protected String doInBackground(Boolean... strings) {

            try {
                Token tokenObj = (Token) mapper.readValue(token, Token.class);
                HTTPMethods.update(strings[0], tokenObj);
                return "ok";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}