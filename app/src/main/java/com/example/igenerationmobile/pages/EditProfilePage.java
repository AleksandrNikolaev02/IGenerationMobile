package com.example.igenerationmobile.pages;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EditProfilePage extends AppCompatActivity {

    private AppCompatEditText fname;
    private AppCompatEditText iname;
    private AppCompatEditText oname;
    private AppCompatEditText organization;
    private ShapeableImageView avatar;
    private Toolbar toolbar;
    private ImageButton confirm;
    private Bundle bundle;
    private Token token;
    final private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        bundle = getIntent().getExtras();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        try {
            token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        fname = findViewById(R.id.fname);
        iname = findViewById(R.id.iname);
        oname = findViewById(R.id.oname);
        organization = findViewById(R.id.organization);
        avatar = findViewById(R.id.ShapeableImageView);
        toolbar = findViewById(R.id.toolbar);
        confirm = findViewById(R.id.confirm);

        confirm.setOnClickListener(v -> {
            try {
                JSONObject json = new JSONObject();
                if (bundle != null) {
                    if (!bundle.getString("name").split(" ")[0].equals(fname.getText().toString())) {
                        json.put("fname", StringEscapeUtils.escapeJava(fname.getText().toString()));
                    }
                    if (!bundle.getString("name").split(" ")[1].equals(iname.getText().toString())) {
                        json.put("iname", StringEscapeUtils.escapeJava(iname.getText().toString()));
                    }
                    if (!bundle.getString("name").split(" ")[2].equals(oname.getText().toString())) {
                        json.put("oname", StringEscapeUtils.escapeJava(oname.getText().toString()));
                    }
                    if (json.length() == 0) {
                        Toast.makeText(EditProfilePage.this, R.string.send, Toast.LENGTH_LONG).show();
                    } else {
                        new updateProfile().execute(json);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("fname", fname.getText().toString());
                        editor.putString("iname", iname.getText().toString());
                        editor.putString("oname", oname.getText().toString());

                        editor.apply();
                    }
                }
            } catch (JSONException e) {
                Log.e("ErrorInFname", "Ошибка при отправке данных");
            }

        });

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (bundle != null) {
            String name = bundle.getString("name", "");
            organization.setText(bundle.getString("organization"));
            String path = bundle.getString("path");

            Picasso.get()
                    .load(path)
                    .placeholder(R.drawable.loading_animation_profile)
                    .fit()
                    .centerInside()
                    .into(avatar);

            String[] tokens = name.split(" ");
            fname.setText(tokens[0]);
            iname.setText(tokens[1]);
            oname.setText(tokens[2]);
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

    private class updateProfile extends AsyncTask<JSONObject, String, String> {

        @Override
        protected String doInBackground(JSONObject... jsonObjects) {
            try {
                System.out.println(jsonObjects[0]);
                return HTTPMethods.updateProfile(token, jsonObjects[0]);
            } catch (IOException e) {
                Log.e("UpdateProfileError", "Ошибка при отправке данных");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if (response != null) {
                Toast.makeText(EditProfilePage.this, R.string.success, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}