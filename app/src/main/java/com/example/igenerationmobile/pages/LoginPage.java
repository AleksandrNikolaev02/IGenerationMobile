package com.example.igenerationmobile.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;

import java.io.IOException;

public class LoginPage extends AppCompatActivity {

    private Button loginButton;
    private EditText emailField;

    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        loginButton.setOnClickListener(v -> {
            if (emailField.getText().toString().isEmpty()) {
                Toast.makeText(LoginPage.this, R.string.empty_email, Toast.LENGTH_LONG).show();
            } else if (passwordField.getText().toString().isEmpty()) {
                Toast.makeText(LoginPage.this, R.string.empty_password, Toast.LENGTH_LONG).show();
            } else {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                new HTTPProcess().execute(email, password);
            }

        });

    }

    private class HTTPProcess extends AsyncTask<String, String, String> {
        private boolean isProcessed = false;

        @Override
        protected String doInBackground(String... strings) {
            try {
                return HTTPMethods.login(strings[0], strings[1]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String token) {
            super.onPostExecute(token);

            if (!isProcessed) {
                isProcessed = true;

                if (token.equals("Unauthorized")) {
                    System.out.println("Unauthorized");
                    Toast.makeText(LoginPage.this, R.string.error_login, Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(LoginPage.this, MainPage.class);
                    System.out.println("Authorised");

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.apply();

                    startActivity(intent);
                }

            }
        }
    }


}