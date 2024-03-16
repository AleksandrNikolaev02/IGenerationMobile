package com.example.igenerationmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.AllProjectAdapter.ProjectModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllProjectsAdapter extends RecyclerView.Adapter<AllProjectsAdapter.AdapterHolder> {

    private Context context;

    private final RecyclerInterface recyclerInterface;

    public List<ProjectModel> projects = new ArrayList<>();

    public AllProjectsAdapter(Context context, RecyclerInterface recyclerInterface) {
        this.context = context;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public AllProjectsAdapter.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.project_row_in_all_projects, parent, false);
        return new AllProjectsAdapter.AdapterHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProjectsAdapter.AdapterHolder holder, int position) {

        ProjectModel model = projects.get(position);

        Picasso.get()
                .load(model.getProjectImage())
                .fit()
                .centerInside()
                .into(holder.image);

        holder.projectName.setText(model.getProjectName());
        holder.projectRating.setText(String.valueOf((float) model.getRating() / 10));
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public static class AdapterHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView projectName, projectRating;

        public AdapterHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
            super(itemView);

            image = itemView.findViewById(R.id.imageProject);
            projectName = itemView.findViewById(R.id.projectName);
            projectRating = itemView.findViewById(R.id.projectRating);

            itemView.setOnClickListener(v -> {
                if (recyclerInterface != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        recyclerInterface.onItemClick(position);
                    }
                }
            });
        }
    }
}
