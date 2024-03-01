package com.example.igenerationmobile.fragments.myProject;

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
import com.example.igenerationmobile.model.ProjectID;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ViewProject extends Fragment {

    private ObjectMapper mapper = new ObjectMapper();
    private ImageView projectImage;

    private TextView Created;

    private TextView Place;

    private TextView Rating;

    private String token;

    private Bitmap image;

    private Integer project_id;

    public ViewProject(String token, Bitmap image, Integer project_id) {
        this.token = token;
        this.image = image;
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

        projectImage.setImageBitmap(image);

        return view;
    }

    private class HTTPProcess extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
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

                Created.setText(project.getCreated_at());
                Place.setText(project.getPlace());
                Rating.setText(project.getTracks_title().isEmpty() ? "-" :
                        (double) project.getTracks_title().get(0).getRating() / 10.0 +
                        " - по траектории " + project.getTracks_title().get(0).getTitle());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}