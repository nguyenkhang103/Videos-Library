package com.example.kaido.videoslibrary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kaido.videoslibrary.R;
import com.example.kaido.videoslibrary.adapter.PlaylistAdapter;
import com.example.kaido.videoslibrary.models.PlaylistModel;
import com.example.kaido.videoslibrary.utils.ConfigUtil;
import com.example.kaido.videoslibrary.utils.DateTimeUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PlaylistAdapter playlistAdapter;

    String URL_PLAYLIST_YOUTUBE_API = "https://www.googleapis.com/youtube/v3/playlists?part=snippet&channelId=" + ConfigUtil.CHANNEL_ID + "&key=" + ConfigUtil.API_KEY + "&maxResults=" + ConfigUtil.MAX_RESULTS;

    List<PlaylistModel> playlistModels;

    TextView textUsername;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        recyclerView = findViewById(R.id.listPlaylist);
        playlistModels = new ArrayList<>();
        playlistAdapter = new PlaylistAdapter(playlistModels, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(playlistAdapter);
        textUsername = findViewById(R.id.textUsername);

        GoogleSignInAccount googleSignIn = GoogleSignIn.getLastSignedInAccount(this);
        if(googleSignIn != null) {
            textUsername.setText(googleSignIn.getDisplayName());
        }

        btnLogout = findViewById(R.id.buttonLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        getPlaylistFromJSONData(URL_PLAYLIST_YOUTUBE_API);
    }

    private void getPlaylistFromJSONData(String URL_PLAYLIST_YOUTUBE_API) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_PLAYLIST_YOUTUBE_API, null,
                response -> {
                    try {
                        JSONArray jsonItems = response.getJSONArray("items");
                        for (int i = 0; i < jsonItems.length(); i++) {
                            JSONObject jsonItem = jsonItems.getJSONObject(i);
                            JSONObject jsonSnippet = jsonItem.getJSONObject("snippet");
                            JSONObject jsonThumbnails = jsonSnippet.getJSONObject("thumbnails");
                            JSONObject jsonThumbnailMed = jsonThumbnails.getJSONObject("medium");
                            PlaylistModel playlist = new PlaylistModel();
                            playlist.setTitlePlaylist(jsonSnippet.getString("title"));
                            playlist.setId(jsonItem.getString("id"));
                            playlist.setThumnails(jsonThumbnailMed.getString("url"));
                            String dateStr = jsonSnippet.getString("publishedAt");
                            playlist.setDateTimePlaylist(DateTimeUtil.formatDateTime(dateStr));
                            playlistModels.add(playlist);
                        }
                        playlistAdapter.notifyDataSetChanged();
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
    }
}