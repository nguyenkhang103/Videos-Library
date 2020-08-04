package com.example.kaido.videoslibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kaido.videoslibrary.R;
import com.example.kaido.videoslibrary.activities.MainActivity;
import com.example.kaido.videoslibrary.models.PlaylistModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder> {
    List<PlaylistModel> playlists;
    Context context;

    public PlaylistAdapter(List<PlaylistModel> playlists, Context context) {
        this.playlists = playlists;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new PlaylistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistHolder holder, int position) {
        holder.textTitle.setText(playlists.get(position).getTitlePlaylist());
        holder.textDateTime.setText(playlists.get(position).getDateTimePlaylist());
        Picasso.get().load(playlists.get(position).getThumnails()).into(holder.imagePlaylist);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("idPlaylist", playlists.get(position).getId());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class PlaylistHolder extends RecyclerView.ViewHolder{
        TextView textTitle, textDateTime;
        ImageView imagePlaylist;
        View view;

        public PlaylistHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitlePlaylist);
            textDateTime = itemView.findViewById(R.id.textDateTimePlaylist);
            imagePlaylist = itemView.findViewById(R.id.imagePlaylist);
            view = itemView;
        }
    }
}
