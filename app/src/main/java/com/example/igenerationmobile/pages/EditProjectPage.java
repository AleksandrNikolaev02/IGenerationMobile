package com.example.igenerationmobile.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.adapters.TrajectoryAdapter;
import com.example.igenerationmobile.dto.TrackDto;
import com.example.igenerationmobile.fragments.myProject.ProjectDto;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.ApiService;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.Project;
import com.example.igenerationmobile.model.ProjectID;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.model.Track;
import com.example.igenerationmobile.model.TrajectoryModel.Trajectory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class EditProjectPage extends AppCompatActivity implements RecyclerInterface {

    private TrajectoryAdapter adapter;
    private RecyclerView recyclerView;
    private Token token;
    private final ObjectMapper mapper = new ObjectMapper();
    private int project_id;
    private ApiService apiService;
    private String tokenStr;
    private Toolbar toolbar;
    private AppCompatEditText edit_description;
    private AppCompatEditText edit_title;
    private Button save_description;
    private Button save_title;

    @Override
    @SuppressWarnings({"deprecation"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project_page);

        recyclerView = findViewById(R.id.listTrajectory);
        toolbar = findViewById(R.id.toolbar);
        edit_description = findViewById(R.id.edit_description);
        edit_title = findViewById(R.id.edit_title);
        save_description = findViewById(R.id.save_description);
        save_title = findViewById(R.id.save_title);

        SharedPreferences userData = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        SharedPreferences my_project = getApplicationContext().getSharedPreferences("pages.my_project_page", Context.MODE_PRIVATE);

        project_id = my_project.getInt("project_id", -1);

        try {
            token = mapper.readValue(userData.getString("token", ""), Token.class);
            tokenStr = token.getTokenType() + " " + token.getAccessToken();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        save_description.setOnClickListener(l -> {
            String description = edit_description.getText().toString();

            new saveDescription().execute(description);
        });

        save_title.setOnClickListener(l -> {
            String title = edit_title.getText().toString();

            new saveTitle().execute(title);
        });

        new getDescription().execute();

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        adapter = new TrajectoryAdapter(this, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HTTPMethods.urlApi + "/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        apiService = retrofit.create(ApiService.class);

        tracks();
    }

    private void tracks() {
        TrackDto trackDto = new TrackDto(1, true);

        Call<List<Track>> call = apiService.tracks(tokenStr, trackDto);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Track>> call, @NonNull Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    List<Track> tracks = response.body();

                    if (tracks != null) {
                        for (Track track : tracks) {
                            int index_elem = adapter.getItemCount();

                            adapter.trajectories.add(new Trajectory(
                                    track.getTitle(),
                                    track.getDescription(),
                                    track.getId()
                            ));

                            adapter.notifyItemInserted(index_elem);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Track>> call, @NonNull Throwable t) {
                Toast.makeText(EditProjectPage.this, "Выбор траектории произошло с ошибкой", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setTrack(int track_id) {
        ProjectDto projectDto = new ProjectDto(project_id, track_id);

        Call<Project> call = apiService.setTrack(tokenStr, projectDto);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Project> call, @NonNull Response<Project> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProjectPage.this, "Выбор траектории сохранен!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Project> call, @NonNull Throwable t) {
                Toast.makeText(EditProjectPage.this, "Выбор траектории произошло с ошибкой", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Trajectory trajectory = adapter.trajectories.get(position);

        setTrack(trajectory.getTrack_id());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    private class saveDescription extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            String description = strings[0];
            try {
                return HTTPMethods.updateDescriptionProject(token, description, project_id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(Integer status) {
            super.onPostExecute(status);

            if (status == 200) {
                Toast.makeText(EditProjectPage.this, "Новое описание сохранено!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(EditProjectPage.this, "Не удалось сохранить описание!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    private class saveTitle extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            String title = strings[0];
            try {
                return HTTPMethods.updateTitleProject(token, title, project_id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(Integer status) {
            super.onPostExecute(status);

            if (status == 200) {
                Toast.makeText(EditProjectPage.this, "Новое название сохранено!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(EditProjectPage.this, "Не удалось сохранить название!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    private class getDescription extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            System.out.println(project_id);
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

                Spanned spannedText;
                String description = project.getDescription();
                String title = project.getTitle();

                if (description == null) {
                    edit_description.setText("...");

                } else {
                    spannedText = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT);

                    edit_description.setText(spannedText.toString());
                }

                edit_title.setText(title);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
