package com.multitechdevelopers.taskman.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.multitechdevelopers.taskman.R;
import com.multitechdevelopers.taskman.adapter.SprintListAdapter;
import com.multitechdevelopers.taskman.databinding.ActivityProjectDetailBinding;
import com.multitechdevelopers.taskman.interfaces.SprintClick;
import com.multitechdevelopers.taskman.models.SprintListModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProjectDetailActivity extends AppCompatActivity {
    private ActivityProjectDetailBinding binding;
    private SprintListAdapter sprintListAdapter;
    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProjectDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setup();
        onClickListeners();
    }

    private void init() {
        projectId = getIntent().getStringExtra("projectId");
    }

    private void setup() {
        Query query = FirebaseDatabase.getInstance().getReference().child("sprints").getRef().orderByChild("projectId").equalTo(projectId);

        FirebaseRecyclerOptions<SprintListModel> options = new FirebaseRecyclerOptions.Builder<SprintListModel>()
                .setQuery(query, SprintListModel.class)
                .build();

        binding.rcvSprintList.setLayoutManager(new LinearLayoutManager(this));
        sprintListAdapter = new SprintListAdapter(options, new SprintClick() {
            @Override
            public void getSprintPosition(String sprintId) {

                moveToSprintDetailScreen(sprintId);
            }

        });

        binding.rcvSprintList.setAdapter(sprintListAdapter);

    }

    private void moveToSprintDetailScreen(String sprintId) {

        startActivity(new Intent(ProjectDetailActivity.this, SprintDetailActivity.class)
                .putExtra("sprintId", sprintId)
                .putExtra("projectId", projectId));
        finish();
    }

    private void onClickListeners() {
        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.txtAddSprint.setOnClickListener(view -> {
            openCustomDialogBox();
        });
    }

    private void openCustomDialogBox() {
        EditText edtTaskName, edtAssignedPerson, edtDeadline, edtDescription;
        Button btnAddSprint, btnCancel;

        Dialog dialog = new Dialog(ProjectDetailActivity.this);
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.cd_add_sprint);
        dialog.setCancelable(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        edtTaskName = dialog.findViewById(R.id.edtTaskName);
        edtAssignedPerson = dialog.findViewById(R.id.edtAssignedPerson);
        edtDeadline = dialog.findViewById(R.id.edtDeadline);
        edtDescription = dialog.findViewById(R.id.edtDescription);
        btnAddSprint = dialog.findViewById(R.id.btnAddSprint);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        btnAddSprint.setOnClickListener(view -> {
            if (edtTaskName.getText().toString().trim().isEmpty()) {
                Toast.makeText(ProjectDetailActivity.this, "Please enter Task Name", Toast.LENGTH_SHORT).show();
            } else if (edtAssignedPerson.getText().toString().trim().isEmpty()) {
                Toast.makeText(ProjectDetailActivity.this, "Please enter Assigned Person", Toast.LENGTH_SHORT).show();
            } else if (edtDeadline.getText().toString().trim().isEmpty()) {
                Toast.makeText(ProjectDetailActivity.this, "Please enter Deadline", Toast.LENGTH_SHORT).show();
            } else if (edtDescription.getText().toString().trim().isEmpty()) {
                Toast.makeText(ProjectDetailActivity.this, "Please enter Description", Toast.LENGTH_SHORT).show();
            } else {
                addDataToFirebase(edtTaskName.getText().toString().trim(), edtAssignedPerson.getText().toString().trim(), edtDeadline.getText().toString().trim(), edtDescription.getText().toString().trim());
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    private void addDataToFirebase(String taskName, String assignedPerson, String deadline, String description) {
        Map<String, String> map = new HashMap<>();
        map.put("projectId", projectId);
        map.put("sprintId", UUID.randomUUID().toString() + "ss");
        map.put("taskName", taskName);
        map.put("assignedPerson", assignedPerson);
        map.put("deadline", deadline);
        map.put("description", description);
        FirebaseDatabase.getInstance().getReference().child("sprints").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProjectDetailActivity.this, "Sprint added Successfully!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProjectDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sprintListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sprintListAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProjectDetailActivity.this, DashboardActivity.class));
        finish();
    }
}