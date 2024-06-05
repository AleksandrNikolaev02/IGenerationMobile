package com.example.igenerationmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.TrajectoryModel.Trajectory;

import java.util.ArrayList;
import java.util.List;

public class TrajectoryAdapter extends RecyclerView.Adapter<TrajectoryAdapter.AdapterHolder>{

    private Context context;

    private final RecyclerInterface recyclerInterface;

    public List<Trajectory> trajectories = new ArrayList<>();

    public TrajectoryAdapter(Context context, RecyclerInterface recyclerInterface) {
        this.context = context;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public TrajectoryAdapter.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trajectory_row, parent, false);
        return new TrajectoryAdapter.AdapterHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TrajectoryAdapter.AdapterHolder holder, int position) {
        Trajectory trajectory = trajectories.get(position);

        holder.trajectory.setText(trajectory.getTrajectory());
//        holder.description.setText(trajectory.getDescription());
    }

    @Override
    public int getItemCount() {
        return trajectories.size();
    }

    public static class AdapterHolder extends RecyclerView.ViewHolder {

        TextView trajectory, description;
        Button button;

        public AdapterHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
            super(itemView);

            trajectory = itemView.findViewById(R.id.trajectory);
            description = itemView.findViewById(R.id.description);
            button = itemView.findViewById(R.id.add);

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
