package com.example.igenerationmobile.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.UserProject;

import java.util.ArrayList;
import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.AdapterHolder> {

    private Context context;

    private final RecyclerInterface recyclerInterface;

    public List<UserProject> users = new ArrayList<>();

    public ProjectsAdapter(Context context, RecyclerInterface recyclerInterface) {
        this.context = context;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public ProjectsAdapter.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_project_row, parent, false);
        return new ProjectsAdapter.AdapterHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsAdapter.AdapterHolder holder, int position) {
        holder.image.setImageBitmap(users.get(position).getAvatar());
        holder.role.setText(users.get(position).getRole());
        holder.fio.setText(users.get(position).getFio());
        if (users.get(position).getRole().equals("Автор")) holder.role.setBackgroundColor(Color.GREEN);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public static class AdapterHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView fio, role;

        public AdapterHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
            super(itemView);

            image = itemView.findViewById(R.id.imgProject);
            fio = itemView.findViewById(R.id.fio);
            role = itemView.findViewById(R.id.roleProject);

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
