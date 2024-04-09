package com.example.igenerationmobile.fragments.projectAnotherUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.ProjectID;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class ViewProjectAnotherUser extends Fragment {

    private final ObjectMapper mapper = new ObjectMapper();
    private ImageView projectImage;
    private TextView Created;
    private TextView Place;
    private TextView Rating;
    private TextView nameProject;
    private TextView Description;
    private Toolbar toolbar;
    private Token token;
    private Integer project_id;

    public ViewProjectAnotherUser() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    @SuppressWarnings({"deprecation"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_project_another_user, container, false);

        projectImage = view.findViewById(R.id.projectImage);
        Created = view.findViewById(R.id.Created);
        Place = view.findViewById(R.id.Place);
        Rating = view.findViewById(R.id.Rating);
        nameProject = view.findViewById(R.id.nameProject);
        Description = view.findViewById(R.id.Description);


        SharedPreferences sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);


        if (getActivity() != null) {
            toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Обзор");
        }

        project_id = sharedPreferences.getInt("project_id", -1);

        try {
            token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        new HTTPProcess().execute();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    private class HTTPProcess extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                System.out.println(token);
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
                nameProject.setText(project.getTitle());

                Spanned spannedText;
                String description = project.getDescription();

                if (description == null) {
                    Description.setText("...");

                } else {
                    spannedText = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT);

                    Description.setText(spannedText.toString());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}