package com.example.igenerationmobile.fragments.mainPage;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class AllUsers extends Fragment implements RecyclerInterface {

    private final ObjectMapper mapper = new ObjectMapper();
    private Token token;
    private int user_id;
    private RecyclerView recyclerView;
    private AllUsersAdapter adapter;
    private NestedScrollView nestedSV;
    private ProgressBar loadingPB;
    private Spinner role;
    private Spinner sort;
    private ArrayAdapter<CharSequence> roleAdapter;
    private ArrayAdapter<CharSequence> sortAdapter;
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
        role = view.findViewById(R.id.role);
        sort = view.findViewById(R.id.sort);

        roleAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.role, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(roleAdapter);
        role.setSelection(getResources().getStringArray(R.array.role).length - 1);

        sortAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort.setAdapter(sortAdapter);
        sort.setSelection(0);

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
    @TargetApi(26)
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

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                List<UserModel> notConfirmedUsers = new ArrayList<>();

                for (int i = 0; i < usersJSON.length(); i++) {
                    JSONObject user = usersJSON.getJSONObject(i);

                    String imageURL = user.getString("img_file").isEmpty() ? HTTPMethods.urlIGN + "/img/avatar_00.png"
                                        : HTTPMethods.urlApi + "/image/" + user.getString("img_file").replaceAll("\\\\/", "/");
                    String name = StringEscapeUtils.unescapeJava(user.getString("fname")) + " " +
                            StringEscapeUtils.unescapeJava(user.getString("iname")) + " " +
                            StringEscapeUtils.unescapeJava(user.getString("oname"));
                    int rating = user.getInt("rating");
                    int status = user.getInt("status");
                    if (user.getString("email_verified_at").equals("null")) {
                        notConfirmedUsers.add(new UserModel(imageURL, name, rating, status, null));
                    } else {
                        LocalDateTime dateTime = LocalDateTime.parse(user.getString("email_verified_at"), formatter);

                        users.add(new UserModel(imageURL, name, rating, status, dateTime));
                    }
                }
                usersFiltered = new ArrayList<>(users);

                usersFiltered.sort(Comparator.comparing(UserModel::getDateTime));

                usersFiltered.addAll(notConfirmedUsers);

                loadMoreProjects();

                nestedSV.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        loadMoreProjects();
                        if (usersFiltered.size() == adapter.getItemCount()) loadingPB.setVisibility(View.INVISIBLE);
                    }
                });

                role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    @SuppressLint("NotifyDataSetChanged")
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sortAndFilteredData();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    @SuppressLint("NotifyDataSetChanged")
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sortAndFilteredData();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
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

    @SuppressLint("NotifyDataSetChanged")
    public void sortAndFilteredData() {
        usersFiltered.clear();

        String strStatus = sort.getSelectedItem().toString();
        String selectedRole = role.getSelectedItem().toString();

        if (!selectedRole.equals("Не выбрано")) {
            int status = -1;
            switch (selectedRole) {
                case "Наставник":
                    status = 3;
                    break;
                case "Эксперт":
                    status = 4;
                    break;
                case "Администратор":
                    status = 5;
                    break;
                case "Заказчик":
                    status = 7;
                    break;
            }

            int finalStatus = status;
            usersFiltered = users.stream()
                    .filter(user -> user.getStatus() == finalStatus)
                    .collect(Collectors.toList());
        }
        if (strStatus.equals("По дате регистрации")) {
            usersFiltered.sort(Comparator.comparing(UserModel::getDateTime));
        } else {
            usersFiltered.sort(Comparator.comparing(UserModel::getRating).reversed());
        }

        adapter.users.clear();
        adapter.notifyDataSetChanged();

        loadMoreProjects();
    }

}