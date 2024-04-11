package com.example.igenerationmobile.pages;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class YourOptions extends AppCompatActivity {

    private Token token;
    private final ObjectMapper mapper = new ObjectMapper();
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch viewingMode;
    private Toolbar toolbar;

    @Override
    @SuppressWarnings({"deprecation"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_options);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        try {
            token = mapper.readValue(sharedPreferences.getString("token",""), Token.class);
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

        int mode = sharedPreferences.getInt("mode", -1);
        viewingMode.setChecked(mode != 1);

        viewingMode.setOnClickListener(item -> {
            boolean checked = ((Switch) item).isChecked();
            new HTTPUpdate().execute(checked);
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

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    private class HTTPUpdate extends AsyncTask<Boolean, Void, Void> {
        @Override
        protected Void doInBackground(Boolean... strings) {

            try {
                HTTPMethods.update(strings[0], token);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

    }
}