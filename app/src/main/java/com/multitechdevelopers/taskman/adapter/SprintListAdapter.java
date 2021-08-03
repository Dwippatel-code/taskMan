package com.multitechdevelopers.taskman.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.multitechdevelopers.taskman.activities.ProjectDetailActivity;
import com.multitechdevelopers.taskman.interfaces.SprintClick;
import com.multitechdevelopers.taskman.models.ProjectListModel;
import com.multitechdevelopers.taskman.models.SprintListModel;

import java.util.HashMap;
import java.util.Map;

public class SprintListAdapter extends FirebaseRecyclerAdapter<SprintListModel, SprintListAdapter.SprintListHolder> {

    private final SprintClick sprintClick;

    public SprintListAdapter(@NonNull FirebaseRecyclerOptions<SprintListModel> options, SprintClick sprintClick) {
        super(options);
        this.sprintClick = sprintClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull SprintListHolder holder, int position, @NonNull SprintListModel model) {
        holder.txtTaskName.setText(model.getTaskName());
        holder.txtAssignedPersonName.setText(model.getAssignedPerson());
        holder.txtDeadline.setText(model.getDeadline());
        holder.txtDescription.setText(model.getDescription());

        holder.crdSprintList.setOnClickListener(view -> {
            sprintClick.getSprintPosition(model.getSprintId());
        });

        holder.imgEdit.setOnClickListener(view -> {
            openCustomDialog(holder, model, position);
        });
        holder.imgDelete.setOnClickListener(view -> {
            openAlertDialog(holder, position);
        });

    }

    private void openAlertDialog(SprintListHolder holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.txtTaskName.getContext());
        builder.setTitle("Delete Sprint");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            FirebaseDatabase.getInstance().getReference().child("sprints").child(getRef(position).getKey()).removeValue();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.cancel();
        });
        builder.show();
    }

    private void openCustomDialog(SprintListHolder holder, SprintListModel model, int position) {

        EditText edtTaskName,edtAssignedPerson,edtDeadline, edtDescription;
        Button btnUpdateSprint, btnCancel;

        Dialog dialog = new Dialog(holder.txtTaskName.getContext());
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.cd_update_sprint);
        dialog.setCancelable(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        edtTaskName = dialog.findViewById(R.id.edtTaskName);
        edtAssignedPerson = dialog.findViewById(R.id.edtAssignedPerson);
        edtDeadline = dialog.findViewById(R.id.edtDeadline);
        edtDescription = dialog.findViewById(R.id.edtDescription);
        btnUpdateSprint = dialog.findViewById(R.id.btnUpdateSprint);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        edtTaskName.setText(model.getTaskName());
        edtAssignedPerson.setText(model.getAssignedPerson());
        edtDeadline.setText(model.getDeadline());
        edtDescription.setText(model.getDescription());

        btnUpdateSprint.setOnClickListener(view -> {
            if (edtTaskName.getText().toString().trim().isEmpty()) {
                Toast.makeText(holder.txtTaskName.getContext(), "Please enter Task Name", Toast.LENGTH_SHORT).show();
            } else if (edtAssignedPerson.getText().toString().trim().isEmpty()){
                Toast.makeText(holder.txtTaskName.getContext(), "Please enter Assigned Person", Toast.LENGTH_SHORT).show();
            } else if (edtDeadline.getText().toString().trim().isEmpty()){
                Toast.makeText(holder.txtTaskName.getContext(), "Please enter Deadline", Toast.LENGTH_SHORT).show();
            } else if (edtDescription.getText().toString().trim().isEmpty()){
                Toast.makeText(holder.txtTaskName.getContext(), "Please enter Description", Toast.LENGTH_SHORT).show();
            } else {
                updateDataToFirebase(model.getSprintId(),edtTaskName.getText().toString().trim(),edtAssignedPerson.getText().toString().trim(),edtDeadline.getText().toString().trim(),edtDescription.getText().toString().trim(), position, holder);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    private void updateDataToFirebase(String sprintId,String taskName, String assignedPerson, String deadline, String description, int position, SprintListHolder holder) {
        Map<String, Object> map = new HashMap<>();
        map.put("sprintId", sprintId);
        map.put("taskName", taskName);
        map.put("assignedPerson", assignedPerson);
        map.put("deadline", deadline);
        map.put("description", description);

        FirebaseDatabase.getInstance().getReference().child("sprints").child(getRef(position).getKey()).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(holder.txtTaskName.getContext(), "Sprint Updated Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.txtTaskName.getContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @NonNull
    @Override
    public SprintListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_sprint_list,parent,false);
        return new SprintListHolder(view);
    }

    public class SprintListHolder extends RecyclerView.ViewHolder {
        CardView crdSprintList;
        TextView txtTaskName, txtAssignedPersonName, txtDeadline,txtDescription;
        ImageView imgEdit, imgDelete;

        public SprintListHolder(@NonNull View itemView) {
            super(itemView);

            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            crdSprintList = itemView.findViewById(R.id.crdSprintList);
            txtTaskName = itemView.findViewById(R.id.txtTaskName);
            txtAssignedPersonName = itemView.findViewById(R.id.txtAssignedPersonName);
            txtDeadline = itemView.findViewById(R.id.txtDeadline);
            txtDescription = itemView.findViewById(R.id.txtDescription);

        }
    }
}
