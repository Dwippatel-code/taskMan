package com.multitechdevelopers.taskman.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.multitechdevelopers.taskman.R;
import com.multitechdevelopers.taskman.databinding.ActivitySprintDetailBinding;
import com.multitechdevelopers.taskman.fragments.BacklogFragment;
import com.multitechdevelopers.taskman.fragments.MOMFragment;
import com.multitechdevelopers.taskman.fragments.UserStoryFragment;

public class SprintDetailActivity extends AppCompatActivity {
    private ActivitySprintDetailBinding binding;
    private String sprintId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySprintDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setup();
        onClickListeners();
    }

    private void init() {
        sprintId = getIntent().getStringExtra("sprintId");
    }

    private void setup() {
        setBacklogFragment();
    }

    private void onClickListeners() {
        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.txtBacklog.setOnClickListener(view -> {
            setBacklogFragment();
        });

        binding.txtUserStory.setOnClickListener(view -> {
            setUserStoryFragment();
        });

        binding.txtMOM.setOnClickListener(view -> {
            setMOMFragment();
        });
    }

    private void setBacklogFragment() {

        binding.viewBacklog.setVisibility(View.VISIBLE);
        binding.viewUserStory.setVisibility(View.GONE);
        binding.viewMOM.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new BacklogFragment(sprintId)).commit();
    }

    private void setUserStoryFragment() {

        binding.viewBacklog.setVisibility(View.GONE);
        binding.viewUserStory.setVisibility(View.VISIBLE);
        binding.viewMOM.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new UserStoryFragment(sprintId)).commit();
    }

    private void setMOMFragment() {

        binding.viewBacklog.setVisibility(View.GONE);
        binding.viewUserStory.setVisibility(View.GONE);
        binding.viewMOM.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MOMFragment(sprintId)).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SprintDetailActivity.this, ProjectDetailActivity.class)
                .putExtra("projectId", getIntent().getStringExtra("projectId")));
        finish();
    }
}