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
import com.multitechdevelopers.taskman.models.MOMModel;
import com.multitechdevelopers.taskman.models.UserStoryModel;

import java.util.HashMap;
import java.util.Map;

public class UserStoryAdapter extends FirebaseRecyclerAdapter<UserStoryModel,UserStoryAdapter.UserStoryHolder> {

    public UserStoryAdapter(@NonNull FirebaseRecyclerOptions<UserStoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserStoryAdapter.UserStoryHolder holder, int position, @NonNull UserStoryModel model) {
        holder.txtUserName.setText(model.getUserName());
        holder.txtAcceptanceCriteria.setText(model.getAcceptanceCriteria());
        holder.txtDeveloper.setText(model.getDeveloper());
        holder.txtPriority.setText(model.getPriority());
        holder.txtPriority.setText(model.getPriority());
        holder.txtStatus.setText(model.getStatus());
        holder.txtDescription.setText(model.getDescription());

        holder.imgEdit.setOnClickListener(view -> {
            openCustomDialog(holder, model, position);
        });
        holder.imgDelete.setOnClickListener(view -> {
            openAlertDialog(holder, position);
        });
    }

    private void openAlertDialog(UserStoryAdapter.UserStoryHolder holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.txtUserName.getContext());
        builder.setTitle("Delete User Story");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            FirebaseDatabase.getInstance().getReference().child("userStory").child(getRef(position).getKey()).removeValue();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.cancel();
        });
        builder.show();
    }

    private void openCustomDialog(UserStoryAdapter.UserStoryHolder holder, UserStoryModel model, int position) {

        EditText edtUserName,edtAcceptanceCriteria,edtDeveloper,edtPriority,edtStatus,edtDescription;
        Button btnUpdateUserStory,btnCancel;


        Dialog dialog = new Dialog(holder.txtUserName.getContext());
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.cd_update_user_story);
        dialog.setCancelable(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        edtUserName = dialog.findViewById(R.id.edtUserName);
        edtAcceptanceCriteria = dialog.findViewById(R.id.edtAcceptanceCriteria);
        edtDeveloper = dialog.findViewById(R.id.edtDeveloper);
        edtPriority = dialog.findViewById(R.id.edtPriority);
        edtStatus = dialog.findViewById(R.id.edtStatus);
        edtDescription = dialog.findViewById(R.id.edtDescription);

        btnUpdateUserStory = dialog.findViewById(R.id.btnUpdateUserStory);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        edtUserName.setText(model.getUserName());
        edtAcceptanceCriteria.setText(model.getAcceptanceCriteria());
        edtDeveloper.setText(model.getDeveloper());
        edtPriority.setText(model.getPriority());
        edtStatus.setText(model.getStatus());
        edtDescription.setText(model.getDescription());

        btnUpdateUserStory.setOnClickListener(view -> {

            if (edtUserName.getText().toString().trim().isEmpty()) {
                Toast.makeText(holder.txtUserName.getContext(), "Please enter User Name", Toast.LENGTH_SHORT).show();
            } else if (edtAcceptanceCriteria.getText().toString().trim().isEmpty()){
                Toast.makeText(holder.txtUserName.getContext(), "Please enter Acceptance Criteria", Toast.LENGTH_SHORT).show();
            }else if (edtDeveloper.getText().toString().trim().isEmpty()){
                Toast.makeText(holder.txtUserName.getContext(), "Please enter Developer", Toast.LENGTH_SHORT).show();
            } else if (edtPriority.getText().toString().trim().isEmpty()){
                Toast.makeText(holder.txtUserName.getContext(), "Please enter priority", Toast.LENGTH_SHORT).show();
            } else if (edtStatus.getText().toString().trim().isEmpty()){
                Toast.makeText(holder.txtUserName.getContext(), "Please enter Status", Toast.LENGTH_SHORT).show();
            } else if (edtDescription.getText().toString().trim().isEmpty()){
                Toast.makeText(holder.txtUserName.getContext(), "Please enter Description", Toast.LENGTH_SHORT).show();
            }
            else {
                updateDataToFirebase(edtUserName.getText().toString().trim(),edtAcceptanceCriteria.getText().toString().trim()
                        ,edtDeveloper.getText().toString().trim(),edtPriority.getText().toString().trim(),edtStatus.getText().toString().trim()
                        ,edtDescription.getText().toString().trim(),holder,position);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    private void updateDataToFirebase(String userName, String acceptanceCriteria, String developer, String priority, String status, String description, UserStoryHolder holder, int position) {

        Map<String, Object> map = new HashMap<>();
        //map.put("sprintId", sprintId);
        map.put("userName", userName);
        map.put("acceptanceCriteria", acceptanceCriteria);
        map.put("developer", developer);
        map.put("priority", priority);
        map.put("status", status);
        map.put("description", description);


        FirebaseDatabase.getInstance().getReference().child("userStory").child(getRef(position).getKey()).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(holder.txtUserName.getContext(), "User Story Updated Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.txtUserName.getContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @NonNull
    @Override
    public UserStoryAdapter.UserStoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_user_story,parent,false);
        return new UserStoryHolder(view);
    }

    public class UserStoryHolder extends RecyclerView.ViewHolder {
        CardView crdUserStory;
        TextView txtUserName,txtAcceptanceCriteria,txtDeveloper,txtPriority,txtStatus,txtDescription;
        ImageView imgEdit, imgDelete;

        public UserStoryHolder(@NonNull View itemView) {
            super(itemView);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            crdUserStory = itemView.findViewById(R.id.crdUserStory);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtAcceptanceCriteria = itemView.findViewById(R.id.txtAcceptanceCriteria);
            txtDeveloper = itemView.findViewById(R.id.txtDeveloper);
            txtPriority = itemView.findViewById(R.id.txtPriority);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtDescription = itemView.findViewById(R.id.txtDescription);
        }
    }
}
