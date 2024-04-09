package com.example.igenerationmobile.pages;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.adapters.AA_RecyclerAdapter;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.RecyclerModel;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class YourProjects extends AppCompatActivity implements RecyclerInterface {

    private Toolbar toolbar;
    private final ObjectMapper mapper = new ObjectMapper();
    private Token token;
    private RecyclerView recyclerView;
    private AA_RecyclerAdapter adapter;
    private Integer user_id;
    private Integer status;
    private JSONArray myProjects;
    private boolean getSessionsFirstTimes = true;
    private ArrayAdapter<CharSequence> sessionAdapter;
    private Spinner sessions;
    LinearLayout linearLayout;
    private String defaultSession;

    public YourProjects(){

    }

    @Override
    @SuppressWarnings({"deprecation"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_projects);

        // init data
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        try {
            token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
            user_id = sharedPreferences.getInt("id", -1);
            status = sharedPreferences.getInt("status", 1);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        defaultSession = getResources().getStringArray(R.array.sessions)[getResources().getStringArray(R.array.sessions).length - 1];

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.allProjectRecyclerView);
        sessions = findViewById(R.id.spinner);
        linearLayout = findViewById(R.id.container);
        adapter = new AA_RecyclerAdapter(this, new ArrayList<>(), this);

        sessionAdapter = ArrayAdapter.createFromResource(this,
                R.array.sessions, android.R.layout.simple_spinner_item);
        sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessions.setAdapter(sessionAdapter);
        sessions.setSelection(sessionAdapter.getCount() - 1);

        sessions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                defaultSession = (String) parent.getItemAtPosition(position);

                if (!getSessionsFirstTimes) {
                    adapter.list.clear();

                    loadData();

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(toolbar);

        new getProjects().execute();

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

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, MyProjectPage.class);
        System.out.println(adapter.list.get(position));

        SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("project_id", adapter.list.get(position).getProject_id());
        editor.putInt("author_id", adapter.list.get(position).getAuthor_id());

        editor.apply();

        startActivity(intent);
    }

    public void loadData() {
        try {

            for (int i = 0; i < myProjects.length(); i++) {
                JSONObject project = myProjects.getJSONObject(i);

                String year = defaultSession.split(" ")[0]; // 2023
                String month = defaultSession.split(" ")[1]; // весна

                String created_at = project.getString("created_at");

                String[] date = created_at.split(" ")[0].split("-");

                boolean confirmed = false;

                if (!created_at.contains(year)) continue;

                int m1 = Integer.parseInt(date[1]);

                if (month.equals("весна")) {
                    if (m1 >= 1 && m1 <= 7) confirmed = true;
                } else {
                    if (m1 >= 8 && m1 <= 12) confirmed = true;
                }

                if (confirmed) {
                    String img_file = project.getString("img_file");

                    String roleInProject;

                    if (user_id == project.getInt("author_id")) roleInProject = "Автор";
                    else {
                        switch (status) {
                            case 1:
                                roleInProject = "Менеджер";
                                break;
                            case 3:
                                roleInProject = "Наставник";
                                break;
                            case 4:
                                roleInProject = "Эксперт";
                                break;
                            case 5:
                                roleInProject = "Администратор";
                                break;
                            case 7:
                                roleInProject = "Заказчик";
                                break;
                            default:
                                roleInProject = "Другой";
                                break;
                        }
                    }

                    String projectImageURL = img_file.isEmpty() ? HTTPMethods.urlIGN + "/img/no_icon.png" :
                            HTTPMethods.urlApi + "/image/" + img_file.replaceAll("\\\\/", "/");

                    int index_elem = adapter.getItemCount();

                    adapter.list.add(new RecyclerModel(
                            projectImageURL,
                            roleInProject,
                            StringEscapeUtils.unescapeJava(project.getString("title")),
                            project.getString("created_at").split(" ")[0],
                            project.getInt("project_id"),
                            project.getInt("author_id")
                    ));

                    adapter.notifyItemInserted(index_elem);
                }

            }
            if (adapter.list.isEmpty()) {

//                TextView name = new TextView(YourProjects.this);
//                name.setText("Связей с проектами не обнаружено");
//                name.setTextColor(getResources().getColor(R.color.participant));
//                name.setBackgroundColor(getResources().getColor(R.color.mentor));
//
//                linearLayout.addView(linearLayout);
            }
        } catch (JSONException e) {
            Log.e("ParseJSON", "Ошибка при обработке JSON", e);
            Toast.makeText(YourProjects.this, "Ошибка при обработке данных. Пожалуйста, попробуйте еще раз.", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    private class getProjects extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                return HTTPMethods.MyProjects(token, user_id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                getSessionsFirstTimes = false;
                myProjects = new JSONArray(response);

                loadData();
            } catch (JSONException e) {
                Log.e("ParseJSON", "Ошибка при обработке JSON", e);
                Toast.makeText(YourProjects.this, "Ошибка при обработке данных. Пожалуйста, попробуйте еще раз.", Toast.LENGTH_LONG).show();
            }
        }
    }
}