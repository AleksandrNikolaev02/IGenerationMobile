package com.example.igenerationmobile.fragments;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.MyProject;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Projects extends Fragment {

    private ObjectMapper mapper = new ObjectMapper();
    private String token;

    private Integer user_id;

    public Projects(String token) {
        this.token = token;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("Params", Context.MODE_PRIVATE);
            user_id = sharedPreferences.getInt("user_id", -1);
            System.out.println(user_id);
            new HTTPProcess().execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    private class HTTPProcess extends AsyncTask<String, String, HashMap<MyProject, Bitmap>> {
        @Override
        protected HashMap<MyProject, Bitmap> doInBackground(String... strings) {
            try {
                Token tokenObj = mapper.readValue(token, Token.class);

                String response = HTTPMethods.MyProjects(tokenObj, user_id);
                List<MyProject> myProjects = mapper.readValue(response, new TypeReference<>() {});

                HashMap<MyProject, Bitmap> images = new HashMap<>();

                for (MyProject project : myProjects) {
                    images.put(project, HTTPMethods.getImage(project.getImg_file()));
                }

                return images;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(HashMap<MyProject, Bitmap> images) {
            super.onPostExecute(images);
            LinearLayout imageContainer = getActivity().findViewById(R.id.imageContainer);
            for (Map.Entry<MyProject, Bitmap> entry : images.entrySet()) {
                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                ImageView image = new ImageView(getActivity());
                image.setImageBitmap(entry.getValue());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                image.setLayoutParams(layoutParams);
                linearLayout.addView(image);

                TextView text = new TextView(getActivity());
                text.setText(user_id == entry.getKey().getAuthor_id() ? "Автор" : "Менеджер");

                TextView title = new TextView(getActivity());
                text.setText(StringEscapeUtils.unescapeJava(entry.getKey().getTitle()));

                imageContainer.addView(linearLayout);
                imageContainer.addView(text);
                imageContainer.addView(title);
            }
        }
    }
}