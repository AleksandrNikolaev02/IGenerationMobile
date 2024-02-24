package com.example.igenerationmobile.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.model.Achievement;
import com.example.igenerationmobile.model.MyAchievement;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Rating extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String token;

    private TextView achievements;

    private ObjectMapper mapper = new ObjectMapper();

    public Rating(String token) {
        this.token = token;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new HTTPProcess().execute();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);

        achievements = view.findViewById(R.id.achievements);

        return view;
    }

    private class HTTPProcess extends AsyncTask<String, String, HashMap<Achievement, String>> {


        @Override
        protected HashMap<Achievement, String> doInBackground(String... strings) {
            try {
                Token tokenObj = (Token) mapper.readValue(token, Token.class);
                String response = HTTPMethods.myAchievements(tokenObj);
                List<MyAchievement> myAchievements = mapper.readValue(response, new TypeReference<>() {});

                if (myAchievements.isEmpty()) return new HashMap<>();
                else {
                    String allAchievements = HTTPMethods.achievements(tokenObj);

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
            LinearLayout imageContainer = getActivity().findViewById(R.id.imageContainer);

            for (Map.Entry<Achievement, String> entry : strings.entrySet()) {
                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                ImageView image = new ImageView(getActivity());
                Bitmap bitmap = getBitmapFromSvgData(entry.getValue());
                image.setImageBitmap(bitmap);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                image.setLayoutParams(layoutParams);
                image.setBackgroundColor(Color.parseColor(entry.getKey().getColor()));

                TextView value = new TextView(getActivity());
                value.setText(String.valueOf(entry.getKey().getValue()));

                TextView name = new TextView(getActivity());
                name.setText(StringEscapeUtils.unescapeJava(entry.getKey().getName()));
                name.setTextColor(Color.parseColor("#ffffff"));


                linearLayout.addView(image);
                linearLayout.addView(name);

                imageContainer.addView(linearLayout);
                imageContainer.addView(value);
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