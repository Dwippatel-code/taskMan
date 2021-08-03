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
import com.multitechdevelopers.taskman.models.BacklogModel;
import com.multitechdevelopers.taskman.models.SprintListModel;

import java.util.HashMap;
import java.util.Map;

public class BacklogAdapter extends FirebaseRecyclerAdapter<BacklogModel,BacklogAdapter.BacklogHolder> {



    public BacklogAdapter(@NonNull FirebaseRecyclerOptions<BacklogModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BacklogAdapter.BacklogHolder holder, int position, @NonNull BacklogModel model) {
        holder.txtDefectName.setText(model.getDefectName());
        holder.txtDeveloperName.setText(model.getDeveloperName());
        holder.txtStatus.setText(model.getStatus());
        holder.txtDescription.setText(model.getDescription());

        holder.imgEdit.setOnClickListener(view -> {
            openCustomDialog(holder, model, position);
        });
        holder.imgDelete.setOnClickListener(view -> {
            openAlertDialog(holder, position);
        });
    }
    private void openAlertDialog(BacklogAdapter.BacklogHolder holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.txtDefectName.getContext());
        builder.setTitle("Delete Backlog");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            FirebaseDatabase.getInstance().getReference().child("backlogs").child(getRef(position).getKey()).removeValue();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.cancel();
        });
        builder.show();
    }

    private void openCustomDialog(BacklogHolder holder, BacklogModel model, int position) {

        EditText edtDefectName,edtDeveloperName,edtStatus,edtDescription;
        Button btnUpdateBacklog,btnCancel;

        Dialog dialog = new Dialog(holder.txtDefectName.getContext());
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.cd_update_backlog);
        dialog.setCancelable(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        edtDefectName = dialog.findViewById(R.id.edtDefectName);
        edtDeveloperName = dialog.findViewById(R.id.edtDeveloperName);
        edtStatus = dialog.findViewById(R.id.edtStatus);
        edtDescription = dialog.findViewById(R.id.edtDescription);
        btnUpdateBacklog = dialog.findViewById(R.id.btnUpdateBacklog);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        edtDefectName.setText(model.getDefectName());
        edtDeveloperName.setText(model.getDeveloperName());
        edtStatus.setText(model.getStatus());
        edtDescription.setText(model.getDescription());


        btnUpdateBacklog.setOnClickListener(view -> {
            if (edtDefectName.getText().toString().trim().isEmpty()) {
                Toast.makeText(holder.txtDefectName.getContext(), "Please enter Defect Name", Toast.LENGTH_SHORT).show();
            } else if (edtDeveloperName.getText().toString().trim().isEmpty()){
                Toast.makeText(holder.txtDefectName.getContext(), "Please enter Developer Name", Toast.LENGTH_SHORT).show();
            } else if (edtStatus.getText().toString().trim().isEmpty()){
                Toast.makeText(holder.txtDefectName.getContext(), "Please enter Status", Toast.LENGTH_SHORT).show();
            } else if (edtDescription.getText().toString().trim().isEmpty()){
                Toast.makeText(holder.txtDefectName.getContext(), "Please enter Description", Toast.LENGTH_SHORT).show();
            } else {
                updateDataToFirebase(edtDefectName.getText().toString().trim(),edtDeveloperName.getText().toString().trim(),edtStatus.getText().toString().trim(),edtDescription.getText().toString().trim(),holder,position);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    private void updateDataToFirebase(String defectName, String developerName, String status, String description, BacklogHolder holder, int position) {

        Map<String, Object> map = new HashMap<>();
        //map.put("sprintId", sprintId);
        map.put("defectName", defectName);
        map.put("developerName", developerName);
        map.put("status", status);
        map.put("description", description);

        FirebaseDatabase.getInstance().getReference().child("backlogs").child(getRef(position).getKey()).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(holder.txtDefectName.getContext(), "Backlogs Updated Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.txtDefectName.getContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @NonNull
    @Override
    public BacklogAdapter.BacklogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_backlog,parent,false);
        return new BacklogHolder(view);
    }

    public class BacklogHolder extends RecyclerView.ViewHolder {
        CardView crdBacklog;
        TextView txtDefectName, txtDeveloperName, txtStatus,txtDescription;
        ImageView imgEdit, imgDelete;

        public BacklogHolder(@NonNull View itemView) {
            super(itemView);

            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            crdBacklog = itemView.findViewById(R.id.crdBacklog);
            txtDefectName = itemView.findViewById(R.id.txtDefectName);
            txtDeveloperName = itemView.findViewById(R.id.txtDeveloperName);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtDescription = itemView.findViewById(R.id.txtDescription);
        }
    }
}
