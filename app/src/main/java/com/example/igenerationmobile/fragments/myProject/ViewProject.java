package com.example.igenerationmobile.fragments.myProject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.UpdateUI;
import com.example.igenerationmobile.model.ProjectID;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;


import java.io.IOException;


public class ViewProject extends Fragment implements UpdateUI {

    private final ObjectMapper mapper = new ObjectMapper();
    private ImageView projectImage;
    private TextView Created;
    private TextView Place;
    private TextView Rating;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private TextView nameProject;
    private TextView Description;
    private Token token;
    private Integer project_id;

    public ViewProject() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    @SuppressWarnings({"deprecation"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_project, container, false);

        projectImage = view.findViewById(R.id.projectImage);
        Created = view.findViewById(R.id.Created);
        Place = view.findViewById(R.id.Place);
        Rating = view.findViewById(R.id.Rating);
        nameProject = view.findViewById(R.id.nameProject);
        Description = view.findViewById(R.id.Description);

        if (getActivity() != null) {
            bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView2);

            toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Обзор");
        }

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        try {
            token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        project_id = sharedPreferences.getInt("project_id", -1);

        new HTTPProcess().execute();

        return view;
    }

    @Override
    @SuppressWarnings({"deprecation"})
    public void update() {
        new HTTPProcess().execute();
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    private class HTTPProcess extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                return HTTPMethods.projectID(token, project_id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                ProjectID project = mapper.readValue(response, ProjectID.class);

                String imagePath = project.getImg_file();

                if (imagePath.isEmpty()) {
                    Picasso.get()
                            .load(R.drawable.no_icon)
                            .into(projectImage);
                } else {
                    Picasso.get()
                            .load(HTTPMethods.urlApi + "/image/" + imagePath.replaceAll("\\\\/", "/"))
                            .fit()
                            .centerInside()
                            .into(projectImage);
                }

                Created.setText(project.getCreated_at());
                Place.setText(project.getPlace());
                Rating.setText(project.getTracks_title().isEmpty() ? "-" :
                        (double) project.getTracks_title().get(0).getRating() / 10.0 +
                        " - по траектории " + project.getTracks_title().get(0).getTitle());
                nameProject.setText(project.getTitle());

                Spanned spannedText;
                String description = project.getDescription();

                if (description == null) {
                    Description.setText("...");

                } else {
                    spannedText = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT);

                    Description.setText(spannedText.toString());
                }

                if (!project.getTracks_title().isEmpty()) {
                    MenuItem trajectory = bottomNavigationView.getMenu().findItem(R.id.trajectoryProject);

                    trajectory.setTitle(project.getTracks_title().get(0).getTitle());
                }

                // save track_id
                if (getActivity() != null) {
                    SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("pages.my_project_page", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putInt("track_id", project.getTracks_title().isEmpty() ? -1 : project.getTracks_title().get(0).getTrack_id());
                    editor.putInt("project_id", project.getId());
                    editor.putString("name_trajectory", project.getTracks_title().isEmpty() ? "Траектория" : project.getTracks_title().get(0).getTitle());
                    editor.apply();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}