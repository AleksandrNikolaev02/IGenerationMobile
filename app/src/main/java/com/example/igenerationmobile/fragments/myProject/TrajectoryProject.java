package com.example.igenerationmobile.fragments.myProject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.util.Pair;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.adapters.StagesAdapter;
import com.example.igenerationmobile.dto.FieldDto;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.ApiService;
import com.example.igenerationmobile.model.ExpandableListModel.Stage;
import com.example.igenerationmobile.model.Field;
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
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class TrajectoryProject extends Fragment {

    private ExpandableListView expandableListView;
    private Toolbar toolbar;
    private StagesAdapter adapter;
    private Integer project_id;
    private Integer track_id;
    private Token token;
    private final ObjectMapper mapper = new ObjectMapper();
    private Map<Integer, List<Integer>> comments = new HashMap<>();
    private Map<Integer, String> numericStages = new HashMap<>();
    private Map<Integer, Boolean> stateGroup = new HashMap<>();

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
                        tmp.add(new Stage("Оценки:", null, true, false));

                        for (int j = 0; j < criteria_rel.length(); j++) {
                            JSONObject criteria = criteria_rel.getJSONObject(j);

                            String title = StringEscapeUtils.unescapeJava(criteria.getString("title"));

                            JSONArray rates = criteria.getJSONArray("rates");

                            int value = 0;

                            for (int x = 0; x < rates.length(); x++) {
                                JSONObject rate = rates.getJSONObject(x);
                                value += rate.getInt("value");
                            }

                            tmp.add(new Stage(title, (float) (value) / (float) rates.length(), false, false));
                        }
                        childs.put(stage, tmp);

                        numericStages.put(stages.size() - 1, stage);
                        stateGroup.put(stages.size() - 1, false);
                    } else if (level == 1) {
                        JSONArray fields_edited = section.getJSONArray("fields_edited");

                        if (fields_edited.length() != 0) {
                            int id = section.getInt("id");

                            List<Integer> local_comments = comments.computeIfAbsent(stages.size() - 1, k -> new ArrayList<>());
                            local_comments.add(id);
                        }
                    }
                }

                adapter = new StagesAdapter(requireActivity().getApplicationContext(), stages, childs);

                expandableListView.setAdapter(adapter);

                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int lastExpandedPosition = -1;
                    @Override
                    public void onGroupExpand(int i) {
                        if (stateGroup.get(i).equals(true)) return;

                        new getField().execute(i);
                    }
                });

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    public class getField extends AsyncTask<Integer, String, Pair<List<String>, Integer>> {

        @Override
        protected Pair<List<String>, Integer> doInBackground(Integer... strings) {
            int i = strings[0];

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HTTPMethods.urlApi + "/")
                    .addConverterFactory(JacksonConverterFactory.create(mapper))
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);

            List<Integer> group_comments = comments.get(i);

            List<String> expert_comments = new ArrayList<>();

            if (group_comments != null) {
                CountDownLatch latch = new CountDownLatch(group_comments.size());

                for (Integer id : group_comments) {
                    FieldDto dto = new FieldDto();

                    dto.setProject_id(project_id);
                    dto.setSection_id(id);

                    String tokenStr = token.getTokenType() + " " + token.getAccessToken();

                    Call<List<Field>> call = apiService.fieldsWithContent(tokenStr, dto);

                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Field>> call, @NonNull Response<List<Field>> response) {
                            if (response.isSuccessful()) {
                                List<Field> fields = response.body();

                                if (fields != null) {
                                    for (Field field : fields) {
                                        if (field.getType() == 7 && field.getContent() != null) {
                                            expert_comments.add(field.getContent());
                                        }
                                    }
                                }
                            }
                            latch.countDown();
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Field>> call, @NonNull Throwable t) {
                            Toast.makeText(getActivity(), "Загрузка комментариев произошла с ошибкой", Toast.LENGTH_LONG).show();
                            latch.countDown();
                        }
                    });
                }

                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return new Pair<>(expert_comments, i);
        }

        @Override
        protected void onPostExecute(Pair<List<String>, Integer> pair) {
            super.onPostExecute(pair);

            List<String> expert_comments = pair.first;
            int i = pair.second;

            String stage = numericStages.get(i);
            List<Stage> stage_childs = adapter.childData.get(stage);
            stage_childs.add(new Stage("Комментарии:", null, true, false));

            System.out.println(expert_comments);

            if (expert_comments.isEmpty()) {
                stage_childs.add(new Stage("Комментариев нет", null, false, true));
            } else {
                for (String comment : expert_comments) {
                    stage_childs.add(new Stage(comment, null, false, true));
                }
            }

            stateGroup.put(i, true);

            adapter.notifyDataSetChanged();
        }
    }

}