package com.example.igenerationmobile.fragments.mainPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.adapters.AllProjectsAdapter;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.AllProjectAdapterModel.ProjectModel;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.pages.MyProjectPage;
import com.example.igenerationmobile.pages.ProjectAnotherUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AllProjects extends Fragment implements RecyclerInterface {

    private Token token;
    private int user_id;
    private RecyclerView recyclerView;

    private Spinner sessions;
    private Set<String> uniqSessions = new HashSet<>();
    private Spinner trajectory;
    private String defaultSession;
    private String defaultTrajectory;

    private AllProjectsAdapter adapter;

    private ArrayAdapter<CharSequence> sessionAdapter;
    private ArrayAdapter<String> trajectoryAdapter;

    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    private final ObjectMapper mapper = new ObjectMapper();

    List<ProjectModel> allProjects = new ArrayList<>();
    private final int limit = 10;

    private boolean loadTrajectory = false;
    public AllProjects() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    @SuppressWarnings({"deprecation"})
    @SuppressLint("NotifyDataSetChanged")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_projects, container, false);

        // get token from preferences
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            try {
                token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
                user_id = sharedPreferences.getInt("id", -1);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        defaultSession = getResources().getStringArray(R.array.sessions)[getResources().getStringArray(R.array.sessions).length - 1];
        defaultTrajectory = "Не выбрано";

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

//        trajectoryAdapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.trajectory, android.R.layout.simple_spinner_item);
//        trajectoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        trajectory.setAdapter(trajectoryAdapter);
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
                defaultTrajectory = (String) parent.getItemAtPosition(position);
                allProjects.clear();
                adapter.projects.clear();
                adapter.notifyDataSetChanged();
                new getAllProjects().execute();
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
        Intent intent;
        SharedPreferences sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("project_id", adapter.projects.get(position).getProject_id());
        editor.putInt("author_id", adapter.projects.get(position).getAuthor_id());

        if (adapter.projects.get(position).getUsers().contains(user_id)) {
            intent = new Intent(getActivity(), MyProjectPage.class);

        } else {
            intent = new Intent(getActivity(), ProjectAnotherUser.class);
            intent.putExtra("nameProject", adapter.projects.get(position).getProjectName());
        }

        editor.apply();
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
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
                        String projectImageURL = img_file.isEmpty() ? HTTPMethods.urlIGN + "/img/no_icon.png" :
                                HTTPMethods.urlApi + "/image/" + img_file.replaceAll("\\\\/", "/");

                        uniqSessions.add(StringEscapeUtils.unescapeJava(project.getJSONArray("tracks_title").getJSONObject(0).getString("title")));

                        ArrayList<Integer> users = new ArrayList<>();

                        JSONArray userCard = project.getJSONArray("users_card");

                        for (int j = 0; j < userCard.length(); j++) {
                            users.add(userCard.getJSONObject(j).getInt("user_id"));
                        }

                        if (defaultTrajectory.equals("Не выбрано")) {
                            allProjects.add(new ProjectModel(
                                    projectImageURL,
                                    project.getJSONArray("tracks_title").getJSONObject(0).getInt("rating"),
                                    StringEscapeUtils.unescapeJava(project.getString("title")),
                                    project.getJSONArray("tracks_title").getJSONObject(0).getString("title"),
                                    project.getInt("id"),
                                    users,
                                    project.getInt("user_id")
                            ));    
                        } else {
                            if (defaultTrajectory.equals(StringEscapeUtils.unescapeJava(project.getJSONArray("tracks_title").getJSONObject(0).getString("title")))) {
                                allProjects.add(new ProjectModel(
                                        projectImageURL,
                                        project.getJSONArray("tracks_title").getJSONObject(0).getInt("rating"),
                                        StringEscapeUtils.unescapeJava(project.getString("title")),
                                        project.getJSONArray("tracks_title").getJSONObject(0).getString("title"),
                                        project.getInt("id"),
                                        users,
                                        project.getInt("user_id")
                                ));
                            }
                        }
                        
                    }

                }

                uniqSessions.add("без траектории");
                uniqSessions.add("Не выбрано");

                System.out.println(uniqSessions);

                if (!loadTrajectory) {
                    List<String> listSessions = new ArrayList<>(uniqSessions);

                    trajectoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listSessions);
                    trajectoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    trajectory.setAdapter(trajectoryAdapter);
                    trajectory.setSelection(listSessions.indexOf("Не выбрано"));
                    loadTrajectory = true;
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
                Log.e("ParseJSON", "Ошибка при обработке JSON", e);
                Toast.makeText(getActivity(), "Ошибка при обработке данных. Пожалуйста, попробуйте еще раз.", Toast.LENGTH_LONG).show();
            }

        }

        public void loadMoreProjects() {
            if (adapter.getItemCount() < allProjects.size()) {
                int index = adapter.getItemCount();
                int end = Math.min(index + limit, allProjects.size());
                for (int i = index; i < end; i++) {
                    adapter.projects.add(allProjects.get(i));
                    adapter.notifyItemInserted(i);
                }
            }
        }
    }
}