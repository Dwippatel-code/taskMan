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
import com.multitechdevelopers.taskman.adapter.BacklogAdapter;
import com.multitechdevelopers.taskman.adapter.MOMAdapter;
import com.multitechdevelopers.taskman.adapter.UserStoryAdapter;
import com.multitechdevelopers.taskman.databinding.FragmentMOMBinding;
import com.multitechdevelopers.taskman.databinding.FragmentUserStoryBinding;
import com.multitechdevelopers.taskman.models.SprintListModel;
import com.multitechdevelopers.taskman.models.UserStoryModel;

import java.util.HashMap;
import java.util.Map;

public class UserStoryFragment extends Fragment {
    private FragmentUserStoryBinding binding;
    private String sprintId;
    private UserStoryAdapter userStoryAdapter;

    public UserStoryFragment(String sprintId) {
        this.sprintId = sprintId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserStoryBinding.inflate(inflater, container, false);
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
        Query query = FirebaseDatabase.getInstance().getReference().child("userStory").getRef().orderByChild("sprintId").equalTo(sprintId);

        FirebaseRecyclerOptions<UserStoryModel> options = new FirebaseRecyclerOptions.Builder<UserStoryModel>()
                .setQuery(query, UserStoryModel.class)
                .build();
        binding.rcvUserStory.setLayoutManager(new LinearLayoutManager(getContext()));
        userStoryAdapter = new UserStoryAdapter(options);

        binding.rcvUserStory.setAdapter(userStoryAdapter);
    }

    private void onClickListeners() {
        binding.txtAddUserStory.setOnClickListener(view -> {
            openCustomDialogBox();
        });
    }

    private void openCustomDialogBox() {
        EditText edtUserName,edtAcceptanceCriteria,edtDeveloper,edtPriority,edtStatus,edtDescription;
        Button btnAddUserStory,btnCancel;

        Dialog dialog = new Dialog(getContext());
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.cd_add_user_story);
        dialog.setCancelable(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        edtUserName = dialog.findViewById(R.id.edtUserName);
        edtAcceptanceCriteria = dialog.findViewById(R.id.edtAcceptanceCriteria);
        edtDeveloper = dialog.findViewById(R.id.edtDeveloper);
        edtPriority = dialog.findViewById(R.id.edtPriority);
        edtStatus = dialog.findViewById(R.id.edtStatus);
        edtDescription = dialog.findViewById(R.id.edtDescription);
        btnAddUserStory = dialog.findViewById(R.id.btnAddUserStory);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        btnAddUserStory.setOnClickListener(view -> {
            if (edtUserName.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Please enter User Name", Toast.LENGTH_SHORT).show();
            } else if (edtAcceptanceCriteria.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Please enter Acceptance Criteria", Toast.LENGTH_SHORT).show();
            }else if (edtDeveloper.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Please enter Developer", Toast.LENGTH_SHORT).show();
            } else if (edtPriority.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Please enter priority", Toast.LENGTH_SHORT).show();
            } else if (edtStatus.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Please enter Status", Toast.LENGTH_SHORT).show();
            } else if (edtDescription.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Please enter Description", Toast.LENGTH_SHORT).show();
            } else {
                addDataToFirebase(edtUserName.getText().toString().trim(),edtAcceptanceCriteria.getText().toString().trim(),edtDeveloper.getText().toString().trim(), edtPriority.getText().toString().trim(),edtStatus.getText().toString().trim(),edtDescription.getText().toString().trim());
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    private void addDataToFirebase(String userName, String acceptanceCriteria, String developer, String priority, String status, String description) {
        Map<String, String> map = new HashMap<>();
        map.put("sprintId", sprintId);
        map.put("userName", userName);
        map.put("acceptanceCriteria", acceptanceCriteria);
        map.put("developer", developer);
        map.put("priority", priority);
        map.put("status", status);
        map.put("description", description);
        FirebaseDatabase.getInstance().getReference().child("userStory").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "User Story added Successfully!", Toast.LENGTH_SHORT).show();
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
        userStoryAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        userStoryAdapter.stopListening();
    }


}