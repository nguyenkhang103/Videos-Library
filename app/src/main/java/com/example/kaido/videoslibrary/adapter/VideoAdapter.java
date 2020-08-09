package com.example.kaido.videoslibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kaido.videoslibrary.R;
import com.example.kaido.videoslibrary.activities.PlayerActivity;
import com.example.kaido.videoslibrary.activities.VideoPlayerActivity;
import com.example.kaido.videoslibrary.models.VideoModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    List<VideoModel> videos;
    Context context;

    public VideoAdapter(List<VideoModel> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        holder.textSource.setText("Google");
        holder.textVideoTitle.setText(videos.get(position).getTitle());
        Picasso.get().load(videos.get(position).getThumbnail()).into(holder.imageThumbnail);
        holder.textAuthor.setText(videos.get(position).getAuthor());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("video", videos.get(position));
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class VideoHolder extends RecyclerView.ViewHolder{
        TextView textVideoTitle, textAuthor, textSource;
        ImageView imageThumbnail;
        View view;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textAuthor = itemView.findViewById(R.id.textAuthor);
            textSource = itemView.findViewById(R.id.textSource);
            imageThumbnail = itemView.findViewById(R.id.imageVideo);
            view = itemView;

        }
    }
}
