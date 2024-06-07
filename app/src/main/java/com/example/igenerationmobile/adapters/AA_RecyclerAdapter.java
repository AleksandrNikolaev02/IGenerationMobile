package com.example.igenerationmobile.adapters;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.RecyclerModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AA_RecyclerAdapter extends RecyclerView.Adapter<AA_RecyclerAdapter.AdapterHolder> {

    private Context context;

    private final RecyclerInterface recyclerInterface;

    public List<RecyclerModel> list;

    public AA_RecyclerAdapter(Context context, ArrayList<RecyclerModel> list, RecyclerInterface recyclerInterface) {
        this.context = context;
        this.list = list;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public AA_RecyclerAdapter.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new AA_RecyclerAdapter.AdapterHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AA_RecyclerAdapter.AdapterHolder holder, int position) {
        String url = list.get(position).getImage();

        if (url.contains(HTTPMethods.urlApi)) {
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerInside()
                    .into(holder.image);
        } else {
            Picasso.get()
                    .load(R.drawable.no_icon)
                    .into(holder.image);
        }

        holder.title.setText(list.get(position).getTitle());
        holder.date.setText(list.get(position).getDate());
        holder.role.setText(list.get(position).getRole());

        switch (list.get(position).getRole()) {
            case "Автор":
                holder.role.setBackgroundColor(Color.GREEN);
                break;
            case "Наставник":
                holder.role.setBackgroundColor(ContextCompat.getColor(context, R.color.mentor));
                break;
            case "Эксперт":
                holder.role.setBackgroundColor(ContextCompat.getColor(context, R.color.expert));
                break;
            case "Администратор":
                holder.role.setBackgroundColor(ContextCompat.getColor(context, R.color.administrator));
                break;
            case "Заказчик":
                holder.role.setBackgroundColor(ContextCompat.getColor(context, R.color.customer));
                break;
            default:
                holder.role.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class AdapterHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView role, date, title;

        public AdapterHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            role = itemView.findViewById(R.id.role);
            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.title);

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
