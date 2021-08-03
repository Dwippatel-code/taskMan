package com.multitechdevelopers.taskman.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.multitechdevelopers.taskman.R;
import com.multitechdevelopers.taskman.adapter.ProjectListAdapter;
import com.multitechdevelopers.taskman.databinding.ActivityDashboardBinding;
import com.multitechdevelopers.taskman.interfaces.ProjectNameClick;
import com.multitechdevelopers.taskman.models.ProjectListModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DashboardActivity extends AppCompatActivity {
    FirebaseRecyclerOptions<ProjectListModel> options = null;
    private ActivityDashboardBinding binding;
    private SharedPreferences sharedPreferences;
    private GoogleSignInClient mGoogleSignInClient;
    private ProjectListAdapter projectListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setup();
        onClickListeners();
    }

    private void init() {

        sharedPreferences = getSharedPreferences("prefManager", MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setup() {

        Query query = FirebaseDatabase.getInstance().getReference().child("projects");

        options = new FirebaseRecyclerOptions.Builder<ProjectListModel>()
                .setQuery(query, ProjectListModel.class)
                .build();


        binding.rcvProjectList.setLayoutManager(new LinearLayoutManager(this));
        projectListAdapter = new ProjectListAdapter(options, new ProjectNameClick() {
            @Override
            public void getProjectNamePosition(String projectId) {
                moveToProjectDetailScreen(projectId);
            }
        });

        binding.rcvProjectList.setAdapter(projectListAdapter);

    }

    private void moveToProjectDetailScreen(String projectId) {
        startActivity(new Intent(DashboardActivity.this, ProjectDetailActivity.class).putExtra("projectId", projectId));
        finish();
    }

    private void onClickListeners() {
        binding.btnLogOut.setOnClickListener(view -> {
            logOut();
        });

        binding.txtAddProject.setOnClickListener(view -> {
            openCustomDialogBox();
        });

    }

    private void openCustomDialogBox() {
        EditText edtProjectName;
        Button btnAddProjectName, btnCancel;

        Dialog dialog = new Dialog(DashboardActivity.this);
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.cd_add_project);
        dialog.setCancelable(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        edtProjectName = dialog.findViewById(R.id.edtProjectName);
        btnAddProjectName = dialog.findViewById(R.id.btnAddProjectName);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        btnAddProjectName.setOnClickListener(view -> {
            if (edtProjectName.getText().toString().trim().isEmpty()) {
                Toast.makeText(DashboardActivity.this, "Please enter Project Name", Toast.LENGTH_SHORT).show();
            } else {
                addDataToFirebase(edtProjectName.getText().toString().trim());
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    private void addDataToFirebase(String projectName) {
        Map<String, String> map = new HashMap<>();
        map.put("projectId", UUID.randomUUID().toString() + "pp");
        map.put("projectName", projectName);
        FirebaseDatabase.getInstance().getReference().child("projects").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DashboardActivity.this, "Project added Successfully!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        projectListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        projectListAdapter.stopListening();
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();

        sharedPreferences.edit().clear().apply();
        finish();

        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(DashboardActivity.this, "Signed Out Successfully!", Toast.LENGTH_SHORT).show();

            }
        });

        startActivity(new Intent(DashboardActivity.this, SplashScreenActivity.class));
        finish();
    }

}