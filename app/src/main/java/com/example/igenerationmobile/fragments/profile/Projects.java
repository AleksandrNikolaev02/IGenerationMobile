package com.example.igenerationmobile.fragments.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.example.igenerationmobile.adapters.AA_RecyclerAdapter;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.MyProject;
import com.example.igenerationmobile.model.Token;
import com.example.igenerationmobile.model.RecyclerModel;
import com.example.igenerationmobile.pages.MyProjectPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Projects extends Fragment implements RecyclerInterface {

    private final ObjectMapper mapper = new ObjectMapper();
    private String token;

    private RecyclerView recyclerView;

    private AA_RecyclerAdapter adapter;
    private Integer user_id;

    public Projects(String token) {
        this.token = token;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("Params", Context.MODE_PRIVATE);
            user_id = sharedPreferences.getInt("user_id", -1);
            System.out.println(user_id);
            new HTTPProcess().execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_projects, container, false);

        recyclerView = view.findViewById(R.id.mRecyclerView);

        adapter = new AA_RecyclerAdapter(getActivity(), new ArrayList<>(), this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), MyProjectPage.class);
        System.out.println(adapter.list.get(position));

        intent.putExtra("project_id", adapter.list.get(position).getProject_id());
        intent.putExtra("author_id", adapter.list.get(position).getAuthor_id());
        intent.putExtra("token", token);
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("Params", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            adapter.list.get(position).getImage().compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String encoded = Base64.encodeToString(b, Base64.DEFAULT);
            editor.putString("project_image", encoded);
            editor.apply();
        }

        startActivity(intent);
    }

    private class HTTPProcess extends AsyncTask<String, String, HashMap<MyProject, Bitmap>> {
        @Override
        protected HashMap<MyProject, Bitmap> doInBackground(String... strings) {
            try {
                Token tokenObj = mapper.readValue(token, Token.class);

                String response = HTTPMethods.MyProjects(tokenObj, user_id);
                List<MyProject> myProjects = mapper.readValue(response, new TypeReference<>() {});

                HashMap<MyProject, Bitmap> images = new HashMap<>();

                for (MyProject project : myProjects) {
                    Bitmap image;
                    if (project.getImg_file().isEmpty()) {
                        image = Bitmap.createBitmap(100, 100,
                                Bitmap.Config.ARGB_8888);
                        image.eraseColor(Color.BLUE);
                    } else {
                        image = HTTPMethods.getImage(project.getImg_file());
                    }
                    images.put(project, image);
                }

                return images;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(HashMap<MyProject, Bitmap> images) {
            super.onPostExecute(images);

            for (Map.Entry<MyProject, Bitmap> entry : images.entrySet()) {
                // TODO чтобы определить наставника или эксперта, смотри на status
                String roleInProject = user_id == entry.getKey().getAuthor_id() ? "Автор" : "Менеджер";

                int index_elem = adapter.getItemCount();

                adapter.list.add(new RecyclerModel(entry.getValue(),
                        roleInProject,
                        StringEscapeUtils.unescapeJava(entry.getKey().getTitle()),
                        entry.getKey().getCreated_at().split(" ")[0],
                        entry.getKey().getProject_id(),
                        entry.getKey().getAuthor_id()));

                adapter.notifyItemInserted(index_elem);

            }
        }
    }
}