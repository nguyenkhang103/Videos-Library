package com.example.kaido.videoslibrary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kaido.videoslibrary.R;
import com.example.kaido.videoslibrary.adapter.VideoAdapter;
import com.example.kaido.videoslibrary.models.VideoModel;
import com.example.kaido.videoslibrary.utils.ConfigUtil;
import com.example.kaido.videoslibrary.utils.DateTimeUtil;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.*;

public class MainActivity extends YouTubeBaseActivity {

    RecyclerView recyclerView;
    VideoAdapter videoAdapter;
    private List<VideoModel> videos;


    String ID_PLAYLIST = "";
    String URL_YOUTUBE_API = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.listVideo);
        videos = new ArrayList<>();
        videoAdapter = new VideoAdapter(videos, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(videoAdapter);

        Intent intent = getIntent();
        ID_PLAYLIST = intent.getStringExtra("idPlaylist");
        URL_YOUTUBE_API = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + ID_PLAYLIST + "&key=" + ConfigUtil.API_KEY + "&maxResults=" + ConfigUtil.MAX_RESULTS;
        parseJsonYoutubeAPI(URL_YOUTUBE_API);
    }

    private void parseJsonYoutubeAPI(String url_youtube_api) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, url_youtube_api, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonItem = jsonArray.getJSONObject(i);
                            JSONObject jsonSnippet = jsonItem.getJSONObject("snippet");
                            JSONObject jsonThumbnails = jsonSnippet.getJSONObject("thumbnails");
                            JSONObject jsonThumbnailMed = jsonThumbnails.getJSONObject("medium");
                            JSONObject jsonResourceID = jsonSnippet.getJSONObject("resourceId");
                            VideoModel video = new VideoModel();
                            video.setTitle(jsonSnippet.getString("title"));
                            video.setAuthor(jsonSnippet.getString("channelTitle"));
                            String dateStr = jsonSnippet.getString("publishedAt");
                            video.setDateTime(DateTimeUtil.formatDateTime(dateStr));
                            video.setThumbnail(jsonThumbnailMed.getString("url"));
                            video.setId(jsonResourceID.getString("videoId"));

                            videos.add(video);
                        }
                        videoAdapter.notifyDataSetChanged();
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace);
        requestQueue.add(jsonObject);
    }
}