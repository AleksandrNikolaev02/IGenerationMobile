package com.example.igenerationmobile.fragments.mainPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.adapters.AllUsersAdapter;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.AllUsersAdapterModel.UserModel;
import com.example.igenerationmobile.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AllUsers extends Fragment implements RecyclerInterface {

    private final ObjectMapper mapper = new ObjectMapper();
    private Token token;
    private int user_id;
    private RecyclerView recyclerView;
    private AllUsersAdapter adapter;
    private NestedScrollView nestedSV;
    private ProgressBar loadingPB;
    private List<UserModel> users = new ArrayList<>();
    private List<UserModel> usersFiltered;
    private final int limit = 16;


    public AllUsers() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    @SuppressWarnings({"deprecation"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_users, container, false);

        // get token from preferences
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            try {
                token = mapper.readValue(sharedPreferences.getString("token", ""), Token.class);
                user_id = sharedPreferences.getInt("id", -1);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        recyclerView = view.findViewById(R.id.allUsersRecyclerView);
        nestedSV = view.findViewById(R.id.idNestedSV);
        loadingPB = view.findViewById(R.id.idPBLoading);

        adapter = new AllUsersAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        new getAllUsers().execute();

        return view;
    }

    @Override
    public void onItemClick(int position) {

    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings({"deprecation"})
    private class getAllUsers extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                return HTTPMethods.users(token);
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Не удалось получить данные с сервера!", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response == null) return;
            try {
                JSONArray usersJSON = new JSONArray(response);

                for (int i = 0; i < usersJSON.length(); i++) {
                    JSONObject user = usersJSON.getJSONObject(i);

                    String imageURL = user.getString("img_file").isEmpty() ? HTTPMethods.urlIGN + "/img/avatar_00.png"
                                        : HTTPMethods.urlApi + "/image/" + user.getString("img_file").replaceAll("\\\\/", "/");
                    String name = StringEscapeUtils.unescapeJava(user.getString("fname")) + " " +
                            StringEscapeUtils.unescapeJava(user.getString("iname")) + " " +
                            StringEscapeUtils.unescapeJava(user.getString("oname"));
                    int rating = user.getInt("rating");
                    int status = user.getInt("status");

                    users.add(new UserModel(imageURL, name, rating, status));

                }
                usersFiltered = new ArrayList<>(users);

                loadMoreProjects();

                nestedSV.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        loadMoreProjects();
                        if (usersFiltered.size() == adapter.getItemCount()) loadingPB.setVisibility(View.INVISIBLE);
                    }
                });
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Не удалось распарсить данные!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void loadMoreProjects() {
        if (adapter.getItemCount() < usersFiltered.size()) {
            int index = adapter.getItemCount();
            int end = Math.min(index + limit, usersFiltered.size());
            for (int i = index; i < end; i++) {
                adapter.users.add(usersFiltered.get(i));
                adapter.notifyItemInserted(i);
            }
        }
    }

}