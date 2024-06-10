package com.example.igenerationmobile.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.adapters.EditStagePageAdapter;
import com.example.igenerationmobile.dto.FieldDto;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.ApiService;
import com.example.igenerationmobile.model.ExpandableListModel.Task;
import com.example.igenerationmobile.model.ExpandableListModel.Title;
import com.example.igenerationmobile.model.Field;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class EditStagePage extends AppCompatActivity {
    private Toolbar toolbar;
    private ExpandableListView expandableListView;
    private final ObjectMapper mapper = new ObjectMapper();
    private Token token;
    private int track_id;
    private int project_id;
    private int StageID;
    private EditStagePageAdapter adapter;
    private Map<Integer, Boolean> stateGroup = new HashMap<>();
    private ApiService apiService;

    @Override
    @SuppressWarnings({"deprecation"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stage_page);

        toolbar = findViewById(R.id.toolbar);
        expandableListView = findViewById(R.id.ExpandableListView);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            track_id = bundle.getInt("track_id");
            project_id = bundle.getInt("project_id");
            StageID = bundle.getInt("StageID");
        }

        SharedPreferences userData = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        try {
            token = mapper.readValue(userData.getString("token", ""), Token.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HTTPMethods.urlApi + "/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        apiService = retrofit.create(ApiService.class);

        new getSections().execute();

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


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
    private class getSections extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                return HTTPMethods.sections(token, track_id, project_id);
            } catch (IOException e) {
                Toast.makeText(EditStagePage.this, "Загрузка произошла с ошибкой", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            List<Title> stages = new ArrayList<>();

            Map<String, List<Task>> childs = new HashMap<>();

            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {

                    if (jsonArray.getJSONObject(i).getInt("id") == StageID) {
                        for (int j = i + 1; j < jsonArray.length(); j++) {
                            JSONObject section = jsonArray.getJSONObject(j);

                            if (section.getInt("level") == 0) break;

                            JSONArray fields_edited = section.getJSONArray("fields_edited");

                            if (fields_edited.length() > 0) {
                                String title = section.getString("title");
                                int id = section.getInt("id");

                                childs.put(title, new ArrayList<>());

                                stages.add(new Title(title, id));

                                stateGroup.put(stages.size() - 1, false);
                            }
                        }

                    }
                }

                adapter = new EditStagePageAdapter(EditStagePage.this, stages, childs);

                expandableListView.setAdapter(adapter);

                expandableListView.setOnGroupExpandListener(i -> {

                    if (stateGroup.get(i).equals(true)) return;

                    fieldsWithContent(i);
                });
            } catch (JSONException e) {
                Toast.makeText(EditStagePage.this, "Обработка JSON произошла с ошибкой", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void fieldsWithContent(int i) {
        Title title = adapter.groupData.get(i);
        int id = title.getId();

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

                    stateGroup.put(i, true);

                    List<Task> tasksAll = adapter.childData.get(title.getTitle());

                    List<String> titles = new ArrayList<>();
                    List<String> tasks = new ArrayList<>();

                    if (fields != null) {
                        for (int i = 0; i < fields.size(); i++) {
                            Field field = fields.get(i);
                            if (field.getType() == 6 || field.getType() == 7) break;

                            if (field.getType() == 1) {
                                titles.add(field.getContent_default());
                                continue;
                            }

                            if (field.getType() == 2) {
                                tasks.add(field.getContent());
                                continue;
                            }

                            if (field.getType() == 3) {
                                tasks.add("TODO");
                            }

                        }
                    }

                    for (int i = 0; i < titles.size(); i++) {
                        Task task = new Task(titles.get(i), tasks.get(i));

                        tasksAll.add(task);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Field>> call, @NonNull Throwable t) {
                Toast.makeText(EditStagePage.this, "Загрузка комментариев произошла с ошибкой", Toast.LENGTH_LONG).show();
            }
        });
    }

}