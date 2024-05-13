package com.example.igenerationmobile.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.dto.ModeDto;
import com.example.igenerationmobile.dto.UserDto;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.ApiService;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class YourOptions extends AppCompatActivity {

    private Token token;
    private final ObjectMapper mapper = new ObjectMapper();
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch viewingMode;
    private Toolbar toolbar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_options);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        try {
            token = mapper.readValue(sharedPreferences.getString("token",""), Token.class);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HTTPMethods.urlApi + "/")
                    .addConverterFactory(JacksonConverterFactory.create(mapper))
                    .build();

             apiService = retrofit.create(ApiService.class);

            getMode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        viewingMode = findViewById(R.id.viewingMode);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        viewingMode.setOnClickListener(item -> {
            boolean checked = ((Switch) item).isChecked();
            updateMode(!checked);
        });

    }

    private void getMode() {
        String tokenStr = token.getTokenType() + " " + token.getAccessToken();

        UserDto userDto = new UserDto(1);

        Call<User> call = apiService.user(tokenStr, userDto);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();

                    if (user != null) {
                        int mode = user.getMode();
                        viewingMode.setChecked(mode != 1);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(YourOptions.this, "Загрузка настроек произошла с ошибкой", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateMode(boolean mode) {
        String tokenStr = token.getTokenType() + " " + token.getAccessToken();

        ModeDto modeDto = new ModeDto(mode);

        Call<Void> call = apiService.update(tokenStr, modeDto);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {}

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(YourOptions.this, "Обновление данных произошло с ошибкой", Toast.LENGTH_LONG).show();
            }
        });
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