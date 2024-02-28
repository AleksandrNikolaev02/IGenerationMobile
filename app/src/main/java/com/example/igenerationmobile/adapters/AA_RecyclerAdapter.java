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
import com.example.igenerationmobile.model.RecyclerModel;

import java.util.ArrayList;
import java.util.List;

public class AA_RecyclerAdapter extends RecyclerView.Adapter<AA_RecyclerAdapter.AdapterHolder> {

    private Context context;

    public List<RecyclerModel> list;

    public AA_RecyclerAdapter(Context context, ArrayList<RecyclerModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AA_RecyclerAdapter.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new AA_RecyclerAdapter.AdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AA_RecyclerAdapter.AdapterHolder holder, int position) {
        holder.image.setImageBitmap(list.get(position).getImage());
        holder.title.setText(list.get(position).getTitle());
        holder.date.setText(list.get(position).getDate());
        holder.role.setText(list.get(position).getRole());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class AdapterHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView role, date, title;

        public AdapterHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            role = itemView.findViewById(R.id.role);
            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.title);
        }
    }
}
