package com.example.igenerationmobile.fragments.mainPage;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.Achievement;
import com.example.igenerationmobile.model.MyAchievement;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YourProfile extends Fragment {

    private ShapeableImageView avatar;
    private TextView name;
    private TextView IGNRole;
    private TextView ratingUser;
    private TextView countProjectsUser;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_your_profile, container, false);

        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            try {
                token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
                user_id = sharedPreferences.getInt("id", -1);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        avatar = view.findViewById(R.id.ShapeableImageView);
        name = view.findViewById(R.id.name);
        IGNRole = view.findViewById(R.id.IGNRole);
        ratingUser = view.findViewById(R.id.ratingUser);
        countProjectsUser = view.findViewById(R.id.countProjectsUser);


        new getCnt().execute();

        new getAchivments().execute();

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

                        Picasso.get()
                                .load(imagePath.isEmpty() ? HTTPMethods.urlIGN + "/img/avatar_00.png" :
                                        HTTPMethods.urlApi + "/image/" + imagePath.replaceAll("\\\\/", "/"))
                                .placeholder(R.drawable.loading_animation_profile)
                                .fit()
                                .centerInside()
                                .into(avatar);

                        String nameUser = StringEscapeUtils.unescapeJava(user.getString("fname")) + " " +
                                StringEscapeUtils.unescapeJava(user.getString("iname")) + " " +
                                StringEscapeUtils.unescapeJava(user.getString("oname"));
                        name.setText(nameUser);
                        ratingUser.setText(String.valueOf(user.getJSONObject("rating").getInt("achievements_sum")));

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

    private class getAchivments extends AsyncTask<String, String, HashMap<Achievement, String>> {


        @Override
        protected HashMap<Achievement, String> doInBackground(String... strings) {
            try {
                String response = HTTPMethods.myAchievements(token);
                List<MyAchievement> myAchievements = mapper.readValue(response, new TypeReference<>() {});

                if (myAchievements.isEmpty()) return new HashMap<>();
                else {
                    String allAchievements = HTTPMethods.achievements(token);

                    HashMap<Achievement, String> result = new HashMap<>();
                    List<Achievement> achievements = mapper.readValue(allAchievements, new TypeReference<>() {});

                    for (MyAchievement myAchievement : myAchievements) {
                        for (Achievement achievement : achievements) {
                            if (myAchievement.getAchievementId() == achievement.getId()) {
                                result.put(achievement, HTTPMethods.getSVGImage(achievement.getIcon()));
                            }
                        }
                    }

                    return result;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(HashMap<Achievement, String> strings) {
            super.onPostExecute(strings);
            LinearLayout imageContainer = getActivity().findViewById(R.id.linear_layout_id);

            for (Map.Entry<Achievement, String> entry : strings.entrySet()) {

                ImageView image = new ImageView(getActivity());
                Bitmap bitmap = getBitmapFromSvgData(entry.getValue());
                image.setImageBitmap(bitmap);

                image.setOnClickListener(l -> {
                    System.out.println(l.getId());
                });

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                int rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                layoutParams.setMargins(0, 0, rightMargin, 0);

                image.setLayoutParams(layoutParams);
                image.setBackgroundColor(Color.parseColor(entry.getKey().getColor()));

                imageContainer.addView(image);
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