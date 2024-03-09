package com.example.igenerationmobile.fragments.myProject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.adapters.ProjectsAdapter;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.model.UserProject;
import com.example.igenerationmobile.pages.ProfileAnotherUser;
import com.example.igenerationmobile.pages.ProfileNew;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class UsersProject extends Fragment implements RecyclerInterface {

    private String token;

    private Integer project_id;

    private Integer user_id;

    private Integer author_id;

    private RecyclerView recyclerView;

    private ProjectsAdapter adapter;

    private ObjectMapper mapper = new ObjectMapper();

    public UsersProject(String token, Integer project_id, Integer author_id) {
        this.token = token;
        this.project_id = project_id;
        this.author_id = author_id;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("Params", Context.MODE_PRIVATE);
            user_id = sharedPreferences.getInt("user_id", -1);
            System.out.println(user_id);
            System.out.println(author_id);
        }

        new HTTPProcess().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_project, container, false);

        recyclerView = view.findViewById(R.id.listProjects);

        adapter = new ProjectsAdapter(getActivity(), this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent;
        if (adapter.users.get(position).getId().equals(user_id)) {
            intent = new Intent(getActivity(), ProfileNew.class);

            intent.putExtra("token", token);

        } else {
            intent = new Intent(requireActivity(), ProfileAnotherUser.class);

            // encoded image
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            adapter.users.get(position).getAvatar().compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String encoded = Base64.encodeToString(b, Base64.DEFAULT);

            // save image in shared preference
            if (getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("image.public.profile_imgs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(adapter.users.get(position).getImg_file(), encoded);
                editor.apply();
            }

            intent.putExtra("token", token);
            intent.putExtra("user_id", adapter.users.get(position).getId());
        }
        startActivity(intent);
    }

    private class HTTPProcess extends AsyncTask<String, ConcurrentMap<String, Bitmap>, ConcurrentMap<String, Bitmap>> {

        private JSONArray jsonArray;
        @Override
        protected ConcurrentMap<String, Bitmap> doInBackground(String... strings) {
            try {
                Token tokenObj = mapper.readValue(token, Token.class);
                String usersString = HTTPMethods.projectUsers(tokenObj, project_id);
                jsonArray = new JSONArray(usersString);

                List<Thread> threads = new ArrayList<>();
                ConcurrentMap<String, Bitmap> images = new ConcurrentHashMap<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject user = jsonArray.getJSONObject(i);
                    Thread thread = new Thread(() -> {
                        try {
                            Bitmap bitmap;
                            if (user.getString("img_file").isEmpty()) {
                                bitmap = HTTPMethods.getDefaultImage();
                            } else {
                                bitmap = HTTPMethods.getImage(user.getString("img_file"));
                            }
                            images.put(user.getString("img_file"), bitmap);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    threads.add(thread);
                }

                for (Thread thread : threads) {
                    thread.start();
                }

                for (Thread thread : threads) {
                    thread.join();
                }
                return images;
            } catch (IOException | JSONException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(ConcurrentMap<String, Bitmap> map) {
            super.onPostExecute(map);

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject user = jsonArray.getJSONObject(i);
                    String fileName = user.getString("img_file");
                    String roleInProject = author_id.equals(user.getInt("user_id")) ? "Автор" : "Менеджер";

                    int index_elem = adapter.getItemCount();

                    adapter.users.add(new UserProject(
                            map.get(fileName),
                            new StringBuilder()
                                    .append(StringEscapeUtils.unescapeJava(user.getString("fname")))
                                    .append(" ")
                                    .append(StringEscapeUtils.unescapeJava(user.getString("iname")).charAt(0))
                                    .append(". ")
                                    .append(StringEscapeUtils.unescapeJava(user.getString("oname")).charAt(0))
                                    .append(".").toString(),
                            roleInProject,
                            user.getInt("user_id"),
                            fileName
                    ));



                    adapter.notifyItemInserted(index_elem);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}