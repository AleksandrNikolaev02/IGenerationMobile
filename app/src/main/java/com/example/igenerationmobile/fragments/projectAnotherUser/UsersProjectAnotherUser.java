package com.example.igenerationmobile.fragments.projectAnotherUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.adapters.ProjectsAdapter;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.model.UserProject;
import com.example.igenerationmobile.pages.MainPage;
import com.example.igenerationmobile.pages.ProfileAnotherUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UsersProjectAnotherUser extends Fragment implements RecyclerInterface {

    private Token token;
    private Integer project_id;
    private Integer user_id;
    private Integer author_id;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ProjectsAdapter adapter;
    private final ObjectMapper mapper = new ObjectMapper();

    public UsersProjectAnotherUser() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    @SuppressWarnings({"deprecation"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_project_another_user, container, false);

        recyclerView = view.findViewById(R.id.listProjects);

        if (getActivity() != null) {
            toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Участники");
        }

        adapter = new ProjectsAdapter(getActivity(), this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        user_id = sharedPreferences.getInt("id", -1);
        project_id = sharedPreferences.getInt("project_id", -1);
        author_id = sharedPreferences.getInt("author_id", -1);
        try {
            token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        new HTTPProcess().execute();

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent;
        if (adapter.users.get(position).getId().equals(user_id)) {
            intent = new Intent(getActivity(), MainPage.class);

            intent.putExtra("fragmentID", R.id.yourProfile);

        } else {
            intent = new Intent(requireActivity(), ProfileAnotherUser.class);

            intent.putExtra("user_id", adapter.users.get(position).getId());
        }
        startActivity(intent);
    }

    @SuppressWarnings({"deprecation"})
    @SuppressLint("StaticFieldLeak")
    private class HTTPProcess extends AsyncTask<String, List<String>, List<String>> {

        private JSONArray jsonArray;
        @Override
        protected List<String> doInBackground(String... strings) {
            try {
                String usersString = HTTPMethods.projectUsers(token, project_id);
                jsonArray = new JSONArray(usersString);

                List<String> imageUrls = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject user = jsonArray.getJSONObject(i);

                    String imageUrl = user.getString("img_file").isEmpty() ? HTTPMethods.urlIGN + "/img/avatar_00.png" :
                            HTTPMethods.urlApi + "/image/" + user.getString("img_file").replaceAll("\\\\/", "/");

                    imageUrls.add(imageUrl);
                }

                return imageUrls;
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(List<String> imageUrls) {
            super.onPostExecute(imageUrls);

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject user = jsonArray.getJSONObject(i);

                    String fileName = user.getString("img_file").isEmpty() ? "avatar_00.png" :
                            HTTPMethods.urlApi + "/image/" + user.getString("img_file").replaceAll("\\\\/", "/");

                    String roleInProject = author_id.equals(user.getInt("user_id")) ? "Автор" : "Менеджер";

                    int index_elem = adapter.getItemCount();

                    adapter.users.add(new UserProject(
                            null,
                            StringEscapeUtils.unescapeJava(user.getString("fname")) +
                                    " " +
                                    StringEscapeUtils.unescapeJava(user.getString("iname")).charAt(0) +
                                    ". " +
                                    StringEscapeUtils.unescapeJava(user.getString("oname")).charAt(0) +
                                    ".",
                            roleInProject,
                            user.getInt("user_id"),
                            fileName,
                            user.getInt("status")
                    ));

                    adapter.notifyItemInserted(index_elem);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}