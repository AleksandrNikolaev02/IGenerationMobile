package com.example.igenerationmobile.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.dto.CreateProjectDto;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.ApiService;
import com.example.igenerationmobile.model.InfoProject;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CreateProject extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText name_project;
    private Button add_project;
    private Token token;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        try {
            token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        toolbar = findViewById(R.id.toolbar);
        name_project = findViewById(R.id.name_project);
        add_project = findViewById(R.id.add);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        add_project.setOnClickListener(l -> {
            String name = name_project.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(CreateProject.this, R.string.empty_name_project, Toast.LENGTH_LONG).show();
            } else {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(HTTPMethods.urlApi + "/")
                        .addConverterFactory(JacksonConverterFactory.create(mapper))
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);

                String tokenStr = token.getTokenType() + " " + token.getAccessToken();

                System.out.println(tokenStr);

                CreateProjectDto createProjectDto = new CreateProjectDto(1, name, 0);

                Call<InfoProject> call = apiService.addProject(tokenStr, createProjectDto);

                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<InfoProject> call, @NonNull Response<InfoProject> response) {
                        if (response.isSuccessful()) {
                            InfoProject infoProject = response.body();

                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<InfoProject> call, @NonNull Throwable t) {
                        Toast.makeText(CreateProject.this, "Создание проекта произошло с ошибкой", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}