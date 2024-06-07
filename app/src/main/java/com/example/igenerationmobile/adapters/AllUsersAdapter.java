package com.example.igenerationmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igenerationmobile.http.HTTPMethods;
import com.example.igenerationmobile.interfaces.RecyclerInterface;
import com.example.igenerationmobile.model.AllUsersAdapterModel.UserModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.example.igenerationmobile.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.AdapterHolder>{

    private Context context;

    private final RecyclerInterface recyclerInterface;

    public List<UserModel> users = new ArrayList<>();

    public AllUsersAdapter(Context context, RecyclerInterface recyclerInterface) {
        this.context = context;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public AllUsersAdapter.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_row_in_all_users, parent, false);
        return new AllUsersAdapter.AdapterHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AllUsersAdapter.AdapterHolder holder, int position) {
        UserModel model = users.get(position);

        if (model.getImageURL().contains(HTTPMethods.urlApi)) {
            Picasso.get()
                    .load(model.getImageURL())
                    .fit()
                    .centerInside()
                    .into(holder.image);
        } else {
            Picasso.get()
                    .load(R.drawable.avatar_00)
                    .fit()
                    .centerInside()
                    .into(holder.image);
        }

        holder.name.setText(model.getName());
        holder.name.setTextColor(context.getColor(R.color.white));
        holder.rating.setText(String.valueOf(model.getRating()));

        int status = model.getStatus();

        switch (status) {
            case 3:
                holder.user_card.setCardBackgroundColor(context.getColor(R.color.mentor));
                holder.image.setBackgroundColor(context.getColor(R.color.mentor));
                break;
            case 4:
                holder.user_card.setCardBackgroundColor(context.getColor(R.color.expert));
                holder.image.setBackgroundColor(context.getColor(R.color.expert));
                break;
            case 5:
                holder.user_card.setCardBackgroundColor(context.getColor(R.color.administrator));
                holder.image.setBackgroundColor(context.getColor(R.color.administrator));
                break;
            case 7:
                holder.user_card.setCardBackgroundColor(context.getColor(R.color.customer));
                holder.image.setBackgroundColor(context.getColor(R.color.customer));
                break;
            default:
                holder.user_card.setCardBackgroundColor(context.getColor(R.color.participant));
                holder.image.setBackgroundColor(context.getColor(R.color.participant));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class AdapterHolder extends RecyclerView.ViewHolder {

        ShapeableImageView image;
        TextView name;
        TextView rating;
        CardView user_card;

        public AdapterHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
            super(itemView);
            image = itemView.findViewById(R.id.user_image);
            name = itemView.findViewById(R.id.user_name);
            rating = itemView.findViewById(R.id.user_rating);
            user_card = itemView.findViewById(R.id.user_card);

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
