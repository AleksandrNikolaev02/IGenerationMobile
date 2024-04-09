package com.example.igenerationmobile.fragments.myProject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.adapters.StagesAdapter;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.ExpandableListModel.Stage;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrajectoryProject extends Fragment {

    private ExpandableListView expandableListView;
    private Toolbar toolbar;
    private StagesAdapter adapter;
    private Integer project_id;
    private Integer track_id;
    private Token token;
    private final ObjectMapper mapper = new ObjectMapper();

    public TrajectoryProject() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressWarnings({"deprecation"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trajectory_project, container, false);

        if (getActivity() != null) {
            SharedPreferences userData = getActivity().getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences track = getActivity().getApplicationContext().getSharedPreferences("pages.my_project_page", Context.MODE_PRIVATE);

            try {
                token = mapper.readValue(userData.getString("token", ""), Token.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            project_id = track.getInt("project_id", -1);
            track_id = track.getInt("track_id", -1);

            toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle(track.getString("name_trajectory", ""));
        }

        new getSections().execute();

        expandableListView = view.findViewById(R.id.ExpandableListView);

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    private class getSections extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                return HTTPMethods.sections(token, track_id, project_id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            List<String> stages = new ArrayList<>();

            Map<String, List<Stage>> childs = new HashMap<>();

            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject section = jsonArray.getJSONObject(i);

                    String stage = StringEscapeUtils.unescapeJava(section.getString("title"));
                    int level = section.getInt("level");

                    if (level == 0) {
                        stages.add(stage);

                        JSONArray criteria_rel = section.getJSONArray("criteria_rel");

                        List<Stage> tmp = new ArrayList<>();
                        for (int j = 0; j < criteria_rel.length(); j++) {
                            JSONObject criteria = criteria_rel.getJSONObject(j);

                            String title = StringEscapeUtils.unescapeJava(criteria.getString("title"));

                            JSONArray rates = criteria.getJSONArray("rates");

                            int value = 0;

                            for (int x = 0; x < rates.length(); x++) {
                                JSONObject rate = rates.getJSONObject(x);
                                value += rate.getInt("value");
                            }

                            tmp.add(new Stage(title, (float) (value) / (float) rates.length()));
                        }
                        childs.put(stage, tmp);
                    }
                }

                adapter = new StagesAdapter(requireActivity().getApplicationContext(), stages, childs);

                expandableListView.setAdapter(adapter);

                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int lastExpandedPosition = -1;
                    @Override
                    public void onGroupExpand(int i) {
                        if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                            expandableListView.collapseGroup(lastExpandedPosition);
                        }
                        lastExpandedPosition = i;
                    }
                });

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }
}