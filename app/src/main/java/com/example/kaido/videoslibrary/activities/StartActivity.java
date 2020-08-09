package com.example.kaido.videoslibrary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kaido.videoslibrary.R;
import com.example.kaido.videoslibrary.adapter.ViewPaperAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    TextView textUsername;
    Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ViewPager2 viewPager = findViewById(R.id.viewPaper);
        viewPager.setAdapter(new ViewPaperAdapter(this));
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
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout,viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 1) {
                    tab.setText("Videos");
                    tab.setIcon(R.drawable.ic_video);
                } else {
                    tab.setText("Playlist Youtube");
                    tab.setIcon(R.drawable.ic_youtube);
                }
            }
        });
        tabLayoutMediator.attach();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}