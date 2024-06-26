package com.example.igenerationmobile.fragments.mainPage;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.Achievement;
import com.example.igenerationmobile.model.MyAchievement;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.pages.EditProfilePage;
import com.example.igenerationmobile.pages.LoginPage;
import com.example.igenerationmobile.pages.NotificationPage;
import com.example.igenerationmobile.pages.YourOptions;
import com.example.igenerationmobile.pages.YourProjects;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YourProfile extends Fragment {

    private AppCompatButton projects;
    private AppCompatButton settings;
    private AppCompatButton notifications;
    private ImageButton edit;
    private ImageButton exit;
    private ShapeableImageView avatar;
    private String pathToAvatar;
    private TextView name;
    private TextView IGNRole;
    private TextView ratingUser;
    private TextView countProjectsUser;
    private TextView login;
    private TextView organization;
    private Token token;
    private int user_id;
    private final ObjectMapper mapper = new ObjectMapper();

    public YourProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

            String fname = sharedPreferences.getString("fname", "");
            String iname = sharedPreferences.getString("iname", "");
            String oname = sharedPreferences.getString("oname", "");

            if (!fname.equals("") && !iname.equals("") && !oname.equals("")) {
                String newName = fname + " " + iname + " " + oname;
                name.setText(newName);
            }

        }

    }

    @Override
    @SuppressWarnings({"deprecation"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_your_profile, container, false);

        SharedPreferences sharedPreferences;

        if (getActivity() != null) {
            sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            try {
                token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
                user_id = sharedPreferences.getInt("id", -1);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            sharedPreferences = null;
        }

        avatar = view.findViewById(R.id.ShapeableImageView);
        name = view.findViewById(R.id.name);
        IGNRole = view.findViewById(R.id.IGNRole);
        ratingUser = view.findViewById(R.id.ratingUser);
        countProjectsUser = view.findViewById(R.id.countProjectsUser);
        projects = view.findViewById(R.id.projects);
        settings = view.findViewById(R.id.settings);
        notifications = view.findViewById(R.id.notifications);
        login = view.findViewById(R.id.login);
        organization = view.findViewById(R.id.organization);
        edit = view.findViewById(R.id.edit);
        exit = view.findViewById(R.id.exit);

        edit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfilePage.class);

            intent.putExtra("name", name.getText());
            intent.putExtra("organization", organization.getText());
            intent.putExtra("path", pathToAvatar);

            startActivity(intent);
        });

        exit.setOnClickListener(v -> {
            if (sharedPreferences != null) {
                Intent intent = new Intent(getActivity(), LoginPage.class);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("auth", false);
                editor.apply();

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });

        notifications.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationPage.class);

            startActivity(intent);
        });

        new getCnt().execute();

        new getAchievements().execute();

        projects.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), YourProjects.class);

            startActivity(intent);
        });

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), YourOptions.class);

            startActivity(intent);
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = HTTPMethods.urlApi + "/user";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("project_id", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                user -> {
                    try {
                        String imagePath = user.getString("img_file");

                        pathToAvatar = imagePath.isEmpty() ? HTTPMethods.urlIGN + "/img/avatar_00.png" :
                                HTTPMethods.urlApi + "/image/" + imagePath.replaceAll("\\\\/", "/");

                        if (imagePath.isEmpty()) {
                            Picasso.get()
                                    .load(R.drawable.avatar_00)
                                    .placeholder(R.drawable.loading_animation_profile)
                                    .fit()
                                    .centerInside()
                                    .into(avatar);
                        } else {
                            Picasso.get()
                                    .load(pathToAvatar)
                                    .placeholder(R.drawable.loading_animation_profile)
                                    .fit()
                                    .centerInside()
                                    .into(avatar);
                        }

                        String nameUser = StringEscapeUtils.unescapeJava(user.getString("fname")) + " " +
                                StringEscapeUtils.unescapeJava(user.getString("iname")) + " " +
                                StringEscapeUtils.unescapeJava(user.getString("oname"));
                        name.setText(nameUser);
                        ratingUser.setText(String.valueOf(user.getJSONObject("rating").getInt("achievements_sum")));

                        login.setText(user.getString("name"));
                        organization.setText(StringEscapeUtils.unescapeJava(user.getString("place_name")));

                        int status = user.getInt("status");

                        switch (status) {
                            case 1:
                                IGNRole.setText("Участник");
                                IGNRole.setBackgroundColor(getActivity().getColor(R.color.participant));
                                IGNRole.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                                break;
                            case 3:
                                IGNRole.setText("Наставник");
                                IGNRole.setBackgroundColor(getActivity().getColor(R.color.mentor));
                                break;
                            case 4:
                                IGNRole.setText("Эксперт");
                                IGNRole.setBackgroundColor(getActivity().getColor(R.color.expert));
                                break;
                            case 5:
                                IGNRole.setText("Администратор");
                                IGNRole.setBackgroundColor(getActivity().getColor(R.color.administrator));
                                break;
                            case 7:
                                IGNRole.setText("Заказчик");
                                IGNRole.setBackgroundColor(getActivity().getColor(R.color.customer));
                                break;
                            default:
                                IGNRole.setText("Другой");
                                IGNRole.setBackgroundColor(getActivity().getColor(R.color.participant));
                                break;
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> Toast.makeText(getActivity(), "Fail to get data..", Toast.LENGTH_SHORT).show()
        ) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", token.getTokenType() + " " + token.getAccessToken());
                return headers;
            }
        };

        queue.add(postRequest);

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    private class getCnt extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                return HTTPMethods.cnt(token, user_id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                JSONObject json = new JSONObject(response);

                countProjectsUser.setText(String.valueOf(json.getInt("cnt")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static class SvgSoftwareLayerSetter implements RequestListener<PictureDrawable> {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<PictureDrawable> target, boolean isFirstResource) {
            ImageView view = ((ImageViewTarget<?>) target).getView();
            view.setLayerType(ImageView.LAYER_TYPE_NONE, null);
            return false;
        }

        @Override
        public boolean onResourceReady(@NonNull PictureDrawable resource, Object model, Target<PictureDrawable> target, DataSource dataSource, boolean isFirstResource) {
            ImageView view = ((ImageViewTarget<?>) target).getView();
            view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null);
            return false;
        }
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    private class getAchievements extends AsyncTask<String, String, Pair<List<MyAchievement>, String>> {


        @Override
        protected Pair<List<MyAchievement>, String> doInBackground(String... strings) {
            try {
                String response = HTTPMethods.myAchievements(token);

                List<MyAchievement> myAchievements = mapper.readValue(response, new TypeReference<>() {});

                if (myAchievements.isEmpty()) return new Pair<>(myAchievements, "");
                else return new Pair<>(myAchievements, HTTPMethods.achievements(token));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(Pair<List<MyAchievement>, String> response) {
            super.onPostExecute(response);

            LinearLayout imageContainer = requireActivity().findViewById(R.id.linear_layout_id);

            if (!response.second.isEmpty()) {
                try {
                    List<Achievement> achievements = mapper.readValue(response.second, new TypeReference<>() {});
                    List<MyAchievement> myAchievements = response.first;

                    for (MyAchievement myAchievement : myAchievements) {
                        for (Achievement achievement : achievements) {
                            if (myAchievement.getAchievementId() == achievement.getId()) {
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                                int rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                                layoutParams.setMargins(0, 0, rightMargin, 0);

                                ImageView image = new ImageView(getActivity());

                                String name = achievement.getIcon().replace("-", "_").substring(4).replace(".svg", "");

                                int resourceID = getActivity().getResources().getIdentifier(name, "drawable", getActivity().getPackageName());

                                Picasso.get()
                                        .load(resourceID)
                                        .fit()
                                        .centerInside()
                                        .into(image, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                System.out.println("Изображение загружено успешно!");
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                System.out.println("Загрузка изображения произошла с ошибкой!");
                                            }
                                        });

                                image.setLayoutParams(layoutParams);
                                image.setBackgroundColor(Color.parseColor(achievement.getColor()));

                                imageContainer.addView(image);
                            }
                        }
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }


            }
        }

        public Bitmap getBitmapFromSvgData(String svgData) {
            try {
                SVG svg = SVG.getFromString(svgData);

                Bitmap bitmap = Bitmap.createBitmap((int) svg.getDocumentWidth(), (int) svg.getDocumentHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                svg.renderToCanvas(canvas);

                return bitmap;
            } catch (SVGParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}