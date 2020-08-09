package com.example.kaido.videoslibrary.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.kaido.videoslibrary.fragments.PlaylistYoutubeFragment;
import com.example.kaido.videoslibrary.fragments.VideoFragment;

public class ViewPaperAdapter extends FragmentStateAdapter {
    public ViewPaperAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new VideoFragment();
        }
        return new PlaylistYoutubeFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
