package com.example.igenerationmobile.fragments.mainPage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.adapters.AllProjectsAdapter;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.AllProjectAdapter.ProjectModel;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AllProjects extends Fragment implements RecyclerInterface {

    private Token token;

    private RecyclerView recyclerView;

    private Spinner sessions;
    private Spinner trajectory;
    private String defaultSession;
    private String defaultTrajectory;

    private AllProjectsAdapter adapter;

    private ArrayAdapter<CharSequence> sessionAdapter;
    private ArrayAdapter<CharSequence> trajectoryAdapter;

    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    private ObjectMapper mapper = new ObjectMapper();

    List<ProjectModel> allProjects = new ArrayList<>();

    private final int limit = 10;
    public AllProjects() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_projects, container, false);

        // get token from preferences
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            try {
                token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        defaultSession = getResources().getStringArray(R.array.sessions)[getResources().getStringArray(R.array.sessions).length - 1];
        defaultTrajectory = "";

        // init out views
        recyclerView = view.findViewById(R.id.allProjectRecyclerView);
        loadingPB = view.findViewById(R.id.idPBLoading);
        nestedSV = view.findViewById(R.id.idNestedSV);
        sessions = view.findViewById(R.id.spinner);
        trajectory = view.findViewById(R.id.spinnerTrajectory);


        adapter = new AllProjectsAdapter(getActivity(), this);
        sessionAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sessions, android.R.layout.simple_spinner_item);
        sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        trajectoryAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.trajectory, android.R.layout.simple_spinner_item);
        trajectoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        trajectory.setAdapter(trajectoryAdapter);
        sessions.setAdapter(sessionAdapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        sessions.setSelection(sessionAdapter.getCount() - 1);

        sessions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                defaultSession = (String) parent.getItemAtPosition(position);
                allProjects.clear();
                adapter.projects.clear();
                adapter.notifyDataSetChanged();
                new getAllProjects().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        trajectory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        new getAllProjects().execute();

        return view;
    }

    @Override
    public void onItemClick(int position) {

    }

    private class getAllProjects extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                return HTTPMethods.projects(token, defaultSession);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject project = jsonArray.getJSONObject(i);

                    String img_file = project.getString("img_file");

                    if (project.getJSONArray("tracks_title").length() > 0) {
                        allProjects.add(new ProjectModel(
                                img_file.isEmpty() ? HTTPMethods.urlIGN + "/img/no_icon.png" :
                                        HTTPMethods.urlApi + "/image/" + img_file.replaceAll("\\\\/", "/"),
                                project.getJSONArray("tracks_title").getJSONObject(0).getInt("rating"),
                                StringEscapeUtils.unescapeJava(project.getString("title"))
                        ));
                    }

                }

                allProjects.sort((o1, o2) -> o2.getRating() - o1.getRating());

                loadMoreProjects();

                nestedSV.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        loadMoreProjects();
                        if (allProjects.size() == adapter.getItemCount()) loadingPB.setVisibility(View.INVISIBLE);
                    }
                });


            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }

        private void loadMoreProjects() {
            int index = adapter.getItemCount();
            int end = Math.min(index + limit, allProjects.size());
            for (int i = index; i < end; i++) {
                adapter.projects.add(allProjects.get(i));
                adapter.notifyItemInserted(i);
            }
        }
    }
}