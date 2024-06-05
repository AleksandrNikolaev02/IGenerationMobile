package com.example.igenerationmobile.fragments.myProject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.adapters.TrajectoryAdapter;
import com.example.igenerationmobile.dto.TrackDto;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.ApiService;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.Project;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.model.Track;
import com.example.igenerationmobile.model.TrajectoryModel.Trajectory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class EmptyProject extends Fragment implements RecyclerInterface {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private TrajectoryAdapter adapter;
    private RecyclerView recyclerView;
    private Token token;
    private final ObjectMapper mapper = new ObjectMapper();
    private int project_id;
    private ApiService apiService;
    private String tokenStr;

    public EmptyProject() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_empty_project, container, false);

        recyclerView = view.findViewById(R.id.listTrajectory);

        adapter = new TrajectoryAdapter(getActivity(), this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HTTPMethods.urlApi + "/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        apiService = retrofit.create(ApiService.class);

        if (getActivity() != null) {
            SharedPreferences userData = getActivity().getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

            SharedPreferences my_project = getActivity().getApplicationContext().getSharedPreferences("pages.my_project_page", Context.MODE_PRIVATE);

            try {
                token = mapper.readValue(userData.getString("token", ""), Token.class);
                tokenStr = token.getTokenType() + " " + token.getAccessToken();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            project_id = my_project.getInt("project_id", -1);

            bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView2);

            toolbar = getActivity().findViewById(R.id.toolbar);

            MenuItem trajectory = bottomNavigationView.getMenu().findItem(R.id.trajectoryProject);

            toolbar.setTitle(trajectory.getTitle());

            tracks();
        }

        return view;
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
                Toast.makeText(getActivity(), "Выбор траектории произошло с ошибкой", Toast.LENGTH_LONG).show();
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
                    Project project = response.body();

                    if (project != null && getActivity() != null) {
                        getActivity().recreate();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Project> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Выбор траектории произошло с ошибкой", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Trajectory trajectory = adapter.trajectories.get(position);

        setTrack(trajectory.getTrack_id());
    }
}