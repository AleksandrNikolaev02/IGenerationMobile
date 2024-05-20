package com.example.igenerationmobile.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.ApiService;
import com.example.igenerationmobile.model.Login;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class LoginPage extends AppCompatActivity {

    private Button loginButton;
    private EditText emailField;
    private EditText passwordField;
    private ApiService apiService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HTTPMethods.urlApi + "/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        apiService = retrofit.create(ApiService.class);

        loginButton.setOnClickListener(v -> {
            if (emailField.getText().toString().isEmpty()) {
                Toast.makeText(LoginPage.this, R.string.empty_email, Toast.LENGTH_LONG).show();
            } else if (passwordField.getText().toString().isEmpty()) {
                Toast.makeText(LoginPage.this, R.string.empty_password, Toast.LENGTH_LONG).show();
            } else {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                login(email, password);
            }

        });

    }

    private void login(String email, String password) {
        Login loginDto = new Login(email, password);

        Call<Token> call = apiService.login(loginDto);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Intent intent = new Intent(LoginPage.this, MainPage.class);

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", response.body().toString());
                        editor.apply();

                        System.setProperty("https.protocols", "TLSv1.2");

                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(LoginPage.this, R.string.error_login, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                Toast.makeText(LoginPage.this, "Загрузка данных произошла с ошибкой", Toast.LENGTH_LONG).show();
            }
        });
    }

}