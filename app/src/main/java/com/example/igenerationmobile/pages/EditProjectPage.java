package com.example.igenerationmobile.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.adapters.TrajectoryAdapter;
import com.example.igenerationmobile.dto.TrackDto;
import com.example.igenerationmobile.fragments.myProject.ProjectDto;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.ApiService;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.Project;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.model.Track;
import com.example.igenerationmobile.model.TrajectoryModel.Trajectory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project_page);

        recyclerView = findViewById(R.id.listTrajectory);
        toolbar = findViewById(R.id.toolbar);

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

        SharedPreferences userData = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        SharedPreferences my_project = getApplicationContext().getSharedPreferences("pages.my_project_page", Context.MODE_PRIVATE);

        project_id = my_project.getInt("project_id", -1);

        try {
            token = mapper.readValue(userData.getString("token", ""), Token.class);
            tokenStr = token.getTokenType() + " " + token.getAccessToken();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

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
}