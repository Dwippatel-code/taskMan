package com.multitechdevelopers.taskman.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.multitechdevelopers.taskman.R;
import com.multitechdevelopers.taskman.adapter.BacklogAdapter;
import com.multitechdevelopers.taskman.adapter.MOMAdapter;
import com.multitechdevelopers.taskman.databinding.FragmentMOMBinding;
import com.multitechdevelopers.taskman.models.MOMModel;


import java.util.HashMap;
import java.util.Map;

public class MOMFragment extends Fragment {
    private FragmentMOMBinding binding;
    private String sprintId;
    private MOMAdapter momAdapter;

    public MOMFragment(String sprintId) {
        this.sprintId = sprintId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMOMBinding.inflate(inflater, container, false);
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
        Query query = FirebaseDatabase.getInstance().getReference().child("mom").getRef().orderByChild("sprintId").equalTo(sprintId);

        FirebaseRecyclerOptions<MOMModel> options = new FirebaseRecyclerOptions.Builder<MOMModel>()
                .setQuery(query, MOMModel.class)
                .build();
        binding.rcvMOM.setLayoutManager(new LinearLayoutManager(getContext()));
        momAdapter = new MOMAdapter(options);

        binding.rcvMOM.setAdapter(momAdapter);
    }

    private void onClickListeners() {
        binding.txtAddMOM.setOnClickListener(view -> {
            openCustomDialogBox();
        });
    }

    private void openCustomDialogBox() {
        EditText edtMOMDetail;
        Button btnAddMOM,btnCancel;

        Dialog dialog = new Dialog(getContext());
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.cd_mom);
        dialog.setCancelable(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        edtMOMDetail = dialog.findViewById(R.id.edtMOMDetail);
        btnAddMOM = dialog.findViewById(R.id.btnAddMOM);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        btnAddMOM.setOnClickListener(view -> {
            if (edtMOMDetail.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Please enter MOM Detail", Toast.LENGTH_SHORT).show();
            } else {
                addDataToFirebase(edtMOMDetail.getText().toString().trim());
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    private void addDataToFirebase(String momDetail) {
        Map<String, String> map = new HashMap<>();
        map.put("sprintId", sprintId);
        map.put("momDetail", momDetail);

        FirebaseDatabase.getInstance().getReference().child("mom").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Detail added Successfully!", Toast.LENGTH_SHORT).show();
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
        momAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        momAdapter.stopListening();
    }


}