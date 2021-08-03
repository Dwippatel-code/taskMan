package com.multitechdevelopers.taskman.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.multitechdevelopers.taskman.R;
import com.multitechdevelopers.taskman.interfaces.ProjectNameClick;
import com.multitechdevelopers.taskman.models.ProjectListModel;

import java.util.HashMap;
import java.util.Map;

public class ProjectListAdapter extends FirebaseRecyclerAdapter<ProjectListModel, ProjectListAdapter.ProjectListHolder> {

    private final ProjectNameClick projectNameClick;

    public ProjectListAdapter(@NonNull FirebaseRecyclerOptions<ProjectListModel> options, ProjectNameClick projectNameClick) {
        super(options);
        this.projectNameClick = projectNameClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProjectListHolder holder, final int position, @NonNull ProjectListModel model) {
        holder.txtProjectName.setText(model.getProjectName());

        holder.imgEdit.setOnClickListener(view -> {
            openCustomDialog(holder, model, position);
        });
        holder.imgDelete.setOnClickListener(view -> {
            openAlertDialog(holder, position);
        });

        holder.crdProjectList.setOnClickListener(view -> {
            projectNameClick.getProjectNamePosition(model.getProjectId());
        });
    }

    private void openAlertDialog(ProjectListHolder holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.txtProjectName.getContext());
        builder.setTitle("Delete Project");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            FirebaseDatabase.getInstance().getReference().child("projects").child(getRef(position).getKey()).removeValue();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.cancel();
        });
        builder.show();
    }

    private void openCustomDialog(ProjectListHolder holder, ProjectListModel model, int position) {

        EditText edtProjectName;
        Button btnUpdateProjectName, btnCancel;

        Dialog dialog = new Dialog(holder.txtProjectName.getContext());
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.cd_update_project);
        dialog.setCancelable(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        edtProjectName = dialog.findViewById(R.id.edtProjectName);
        btnUpdateProjectName = dialog.findViewById(R.id.btnUpdateProjectName);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        edtProjectName.setText(model.getProjectName());

        btnUpdateProjectName.setOnClickListener(view -> {
            if (edtProjectName.getText().toString().trim().isEmpty()) {
                Toast.makeText(holder.txtProjectName.getContext(), "Please enter Project Name", Toast.LENGTH_SHORT).show();
            } else {
                updateDataToFirebase(edtProjectName.getText().toString().trim(), model.getProjectId(), position, holder);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    private void updateDataToFirebase(String projectName, String projectId, int position, ProjectListHolder holder) {
        Map<String, Object> map = new HashMap<>();
        map.put("projectId", projectId);
        map.put("projectName", projectName);

        FirebaseDatabase.getInstance().getReference().child("projects").child(getRef(position).getKey()).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(holder.txtProjectName.getContext(), "Project Updated Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.txtProjectName.getContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @NonNull
    @Override
    public ProjectListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_project_list, parent, false);
        return new ProjectListHolder(view);
    }

    public static class ProjectListHolder extends RecyclerView.ViewHolder {
        TextView txtProjectName;
        ImageView imgEdit, imgDelete;
        CardView crdProjectList;

        public ProjectListHolder(@NonNull View itemView) {
            super(itemView);
            txtProjectName = itemView.findViewById(R.id.txtProjectName);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            crdProjectList = itemView.findViewById(R.id.crdProjectList);
        }
    }
}
