package com.healthy_lifestyle.aip_lifestyle.video_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.MenuItem;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthy_lifestyle.aip_lifestyle.R;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity implements VideoListView {

    VideoListAdapter videoListAdapter;
    RecyclerView mRecyclerView;
    List<MeditationModel> meditationVideoList;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
        showActionBar();
        findById();
        setLayoutManager();
        onLoadingStarted();


        Bundle mBundle = getIntent().getExtras();
        String difficultyTag = mBundle.getString("difficultyTag");
        meditationVideoList = new ArrayList<>();
        videoListAdapter = new VideoListAdapter(getApplicationContext(), meditationVideoList);
        mRecyclerView.setAdapter(videoListAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Meditation");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                meditationVideoList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    MeditationModel meditationVideoData = itemSnapshot.getValue(MeditationModel.class);
                    if (meditationVideoData.getTag().equals( difficultyTag)){
                        meditationVideoList.add(meditationVideoData);
                    }
                }
                videoListAdapter = new VideoListAdapter(getApplicationContext(), meditationVideoList);
                mRecyclerView.setAdapter(videoListAdapter);
                videoListAdapter.notifyDataSetChanged();
                onLoadingFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onLoadingFinished();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#C9EEDD"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Video List");
    }

    @Override
    public void onLoadingStarted() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void onLoadingFinished() {
        progressDialog.dismiss();
    }

    private void findById() {
        mRecyclerView = findViewById(R.id.rv_meditation);
    }

    private void setLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }
}