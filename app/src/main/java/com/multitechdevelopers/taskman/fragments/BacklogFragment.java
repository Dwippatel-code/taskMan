package com.multitechdevelopers.taskman.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.multitechdevelopers.taskman.R;
import com.multitechdevelopers.taskman.activities.ProjectDetailActivity;
import com.multitechdevelopers.taskman.activities.SprintDetailActivity;
import com.multitechdevelopers.taskman.adapter.BacklogAdapter;
import com.multitechdevelopers.taskman.adapter.SprintListAdapter;
import com.multitechdevelopers.taskman.databinding.FragmentBacklogBinding;
import com.multitechdevelopers.taskman.databinding.FragmentUserStoryBinding;
import com.multitechdevelopers.taskman.interfaces.SprintClick;
import com.multitechdevelopers.taskman.models.BacklogModel;
import com.multitechdevelopers.taskman.models.SprintListModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class BacklogFragment extends Fragment {
    private FragmentBacklogBinding binding;
    private final String sprintId;
    private BacklogAdapter backlogAdapter;

    public BacklogFragment(String sprintId) {
        this.sprintId = sprintId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBacklogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setup();
        onClickListeners();
    }
    private void init() {

    }

    private void setup() {
       Query query = FirebaseDatabase.getInstance().getReference().child("backlogs").getRef().orderByChild("sprintId").equalTo(sprintId);

        FirebaseRecyclerOptions<BacklogModel> options = new FirebaseRecyclerOptions.Builder<BacklogModel>()
                .setQuery(query, BacklogModel.class)
                .build();

        binding.rcvBacklog.setLayoutManager(new LinearLayoutManager(getContext()));
        backlogAdapter = new BacklogAdapter(options);

        binding.rcvBacklog.setAdapter(backlogAdapter);
    }

    private void onClickListeners() {
        binding.txtAddBacklog.setOnClickListener(view -> {
            openCustomDialogBox();
        });


    }

    private void openCustomDialogBox() {
        EditText edtDefectName,edtDeveloperName,edtStatus,edtDescription;
        Button btnAddBacklog,btnCancel;

        Dialog dialog = new Dialog(getContext());
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.cd_add_backlog);
        dialog.setCancelable(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        edtDefectName = dialog.findViewById(R.id.edtDefectName);
        edtDeveloperName = dialog.findViewById(R.id.edtDeveloperName);
        edtStatus = dialog.findViewById(R.id.edtStatus);
        edtDescription = dialog.findViewById(R.id.edtDescription);
        btnAddBacklog = dialog.findViewById(R.id.btnAddBacklog);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        btnAddBacklog.setOnClickListener(view -> {
            if (edtDefectName.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Please enter Defect Name", Toast.LENGTH_SHORT).show();
            } else if (edtDeveloperName.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Please enter Developer Name", Toast.LENGTH_SHORT).show();
            } else if (edtStatus.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Please enter Status", Toast.LENGTH_SHORT).show();
            } else if (edtDescription.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Please enter Description", Toast.LENGTH_SHORT).show();
            } else {
                addDataToFirebase(edtDefectName.getText().toString().trim(),edtDeveloperName.getText().toString().trim(),edtStatus.getText().toString().trim(),edtDescription.getText().toString().trim());
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    private void addDataToFirebase(String defectName, String developerName, String status, String description) {
        Map<String, String> map = new HashMap<>();
        map.put("sprintId", sprintId);
        map.put("defectName", defectName);
        map.put("developerName", developerName);
        map.put("status", status);
        map.put("description", description);
        FirebaseDatabase.getInstance().getReference().child("backlogs").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Backlog added Successfully!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        backlogAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        backlogAdapter.stopListening();
    }



}