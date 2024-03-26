package com.example.igenerationmobile.fragments.myProject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.ProjectID;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;


import java.io.IOException;


public class ViewProject extends Fragment {

    private final ObjectMapper mapper = new ObjectMapper();
    private ImageView projectImage;

    private TextView Created;

    private TextView Place;

    private TextView Rating;
    private BottomNavigationView bottomNavigationView;

    private String token;

    private Integer project_id;

    public ViewProject(String token, Integer project_id) {
        this.token = token;
        this.project_id = project_id;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new HTTPProcess().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_project, container, false);

        projectImage = view.findViewById(R.id.projectImage);
        Created = view.findViewById(R.id.Created);
        Place = view.findViewById(R.id.Place);
        Rating = view.findViewById(R.id.Rating);
        if (getActivity() != null) {
            bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView2);
        }

        return view;
    }

    private class HTTPProcess extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                System.out.println(token);
                Token tokenObj = mapper.readValue(token, Token.class);
                return HTTPMethods.projectID(tokenObj, project_id);
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

                Picasso.get()
                        .load(imagePath.isEmpty() ? HTTPMethods.urlIGN + "/img/no_icon.png" :
                                HTTPMethods.urlApi + "/image/" + imagePath.replaceAll("\\\\/", "/"))
                        .fit()
                        .centerInside()
                        .into(projectImage);

                Created.setText(project.getCreated_at());
                Place.setText(project.getPlace());
                Rating.setText(project.getTracks_title().isEmpty() ? "-" :
                        (double) project.getTracks_title().get(0).getRating() / 10.0 +
                        " - по траектории " + project.getTracks_title().get(0).getTitle());

                if (!project.getTracks_title().isEmpty()) {
                    MenuItem trajectory = bottomNavigationView.getMenu().findItem(R.id.trajectoryProject);

                    trajectory.setTitle(project.getTracks_title().get(0).getTitle());
                }

                // save track_id
                if (getActivity() != null) {
                    SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("pages.my_project_page", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putInt("track_id", project.getTracks_title().isEmpty() ? -1 : project.getTracks_title().get(0).getTrack_id());
                    editor.putInt("project_id", project.getProject_id());
                    editor.apply();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}