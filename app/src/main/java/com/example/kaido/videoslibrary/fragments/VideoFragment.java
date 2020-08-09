package com.example.kaido.videoslibrary.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kaido.videoslibrary.R;
import com.example.kaido.videoslibrary.activities.LoginActivity;
import com.example.kaido.videoslibrary.adapter.PlaylistAdapter;
import com.example.kaido.videoslibrary.adapter.VideoAdapter;
import com.example.kaido.videoslibrary.models.PlaylistModel;
import com.example.kaido.videoslibrary.models.VideoModel;
import com.example.kaido.videoslibrary.utils.ConfigUtil;
import com.example.kaido.videoslibrary.utils.DateTimeUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {

    RecyclerView recyclerView;
    VideoAdapter videoAdapter;

    String URL_VIDEO_API = "https://raw.githubusercontent.com/bikashthapa01/myvideos-android-app/master/data.json";

    List<VideoModel> allVideos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_video, container, false);
        recyclerView = view.findViewById(R.id.listVideo);
        allVideos = new ArrayList<>();
        videoAdapter = new VideoAdapter(allVideos, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(videoAdapter);


        getPlaylistFromJSONData(URL_VIDEO_API);
        return view;
    }
    private void getPlaylistFromJSONData(String URL_VIDEO_API) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_VIDEO_API, null,
                response -> {
                    try {
                        JSONArray categories = response.getJSONArray("categories");
                        JSONObject categoriesData = categories.getJSONObject(0);
                        JSONArray videos = categoriesData.getJSONArray("videos");

                        for (int i = 0; i < videos.length(); i++) {
                            JSONObject video = videos.getJSONObject(i);
                            VideoModel v = new VideoModel();
                            v.setTitle(video.getString("title"));
                            v.setAuthor(video.getString("subtitle"));
                            v.setThumbnail(video.getString("thumb"));
                            JSONArray videoUrl = video.getJSONArray("sources");
                            v.setId(videoUrl.getString(0));
                            allVideos.add(v);
                        }
                        videoAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
    }
}