package com.example.igenerationmobile.http;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.igenerationmobile.model.Login;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

public class HTTPMethods {
    private static final String urlApi = "https://innostor.unn.ru/api";

    private static final String urlIGN = "https://i-generation.unn.ru/static/icons";

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String login(String email, String password) throws IOException {
        Login login = new Login(email, password);

        String request = mapper.writeValueAsString(login);

        URL url = new URL(urlApi + "/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Accept-Language", "ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Length", String.valueOf(request.length()));
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Host", "innostor.unn.ru");

        connection.setDoOutput(true);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());

        outputStreamWriter.write(request);
        outputStreamWriter.flush();
        outputStreamWriter.close();

        connection.connect();

        System.out.println(request);

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) return connection.getResponseMessage();

        InputStream inputStream = new GZIPInputStream(connection.getInputStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        StringBuilder response = new StringBuilder();


        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
        }

        connection.disconnect();
        reader.close();

        return response.toString();
    }

    public static String user(Token token) throws IOException {
        URL url = new URL(urlApi + "/user");

        String request = "{\"project_id\":1}";

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Accept-Language", "ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7");
        connection.setRequestProperty("Authorization", token.getTokenType() + " " + token.getAccessToken());
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Length", String.valueOf(request.length()));
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Host", "innostor.unn.ru");

        connection.setDoOutput(true);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());

        outputStreamWriter.write(request);
        outputStreamWriter.flush();
        outputStreamWriter.close();

        connection.connect();

        System.out.println(connection.getResponseCode());

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) return connection.getResponseMessage();

        InputStream inputStream = new GZIPInputStream(connection.getInputStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        StringBuilder response = new StringBuilder();


        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
        }

        connection.disconnect();
        reader.close();

        return response.toString();

    }

    public static String achievements(Token token) throws IOException {
        URL url = new URL(urlApi + "/achievements");

        String request = "{}";

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Accept-Language", "ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7");
        connection.setRequestProperty("Authorization", token.getTokenType() + " " + token.getAccessToken());
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Length", String.valueOf(request.length()));
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Host", "innostor.unn.ru");

        connection.setDoOutput(true);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());

        outputStreamWriter.write(request);
        outputStreamWriter.flush();
        outputStreamWriter.close();

        connection.connect();

        System.out.println(connection.getResponseCode());

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) return connection.getResponseMessage();

        InputStream inputStream = new GZIPInputStream(connection.getInputStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        StringBuilder response = new StringBuilder();


        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
        }

        connection.disconnect();
        reader.close();

        return response.toString();
    }


    public static String myAchievements(Token token) throws IOException {
        URL url = new URL(urlApi + "/my-achievements");

        String request = "{\"project_id\":1}";

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Accept-Language", "ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7");
        connection.setRequestProperty("Authorization", token.getTokenType() + " " + token.getAccessToken());
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Length", String.valueOf(request.length()));
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Host", "innostor.unn.ru");

        connection.setDoOutput(true);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());

        outputStreamWriter.write(request);
        outputStreamWriter.flush();
        outputStreamWriter.close();

        connection.connect();

        System.out.println(connection.getResponseCode());

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) return connection.getResponseMessage();

        InputStream inputStream = new GZIPInputStream(connection.getInputStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        StringBuilder response = new StringBuilder();


        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
        }

        connection.disconnect();
        reader.close();

        return response.toString();

    }

    public static void update(boolean checked, Token token) throws IOException {
        URL url = new URL(urlApi + "/profile/update");

        String request = checked ? "{\"mode\":false}" : "{\"mode\":true}";

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PATCH");
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Accept-Language", "ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7");
        connection.setRequestProperty("Authorization", token.getTokenType() + " " + token.getAccessToken());
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Length", String.valueOf(request.length()));
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Host", "innostor.unn.ru");

        connection.setDoOutput(true);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());

        outputStreamWriter.write(request);
        outputStreamWriter.flush();
        outputStreamWriter.close();

        connection.connect();

        System.out.println(connection.getResponseCode());

        connection.disconnect();
    }

    public static String MyProjects(Token token, Integer id) throws IOException {
        URL url = new URL(urlApi + "/my-projects");

        String request = String.format(Locale.US,"{\"project_id\":1,\"user_id\":%d}", id);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Accept-Language", "ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7");
        connection.setRequestProperty("Authorization", token.getTokenType() + " " + token.getAccessToken());
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Length", String.valueOf(request.length()));
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Host", "innostor.unn.ru");

        connection.setDoOutput(true);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());

        outputStreamWriter.write(request);
        outputStreamWriter.flush();
        outputStreamWriter.close();

        connection.connect();

        System.out.println(connection.getResponseCode());

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) return connection.getResponseMessage();

        InputStream inputStream = new GZIPInputStream(connection.getInputStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        StringBuilder response = new StringBuilder();


        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
        }

        connection.disconnect();
        reader.close();

        return response.toString();

    }


    public static Bitmap getImage(String nameFile) {
        Bitmap bm = null;
        try {
            URL url = new URL(urlApi + "/image/" + nameFile.replaceAll("\\\\/", "/"));
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }

    public static String getSVGImage(String nameFile) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(urlIGN + "/" + nameFile);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}
