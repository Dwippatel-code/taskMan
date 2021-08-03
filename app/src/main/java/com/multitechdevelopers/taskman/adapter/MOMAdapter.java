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
import com.multitechdevelopers.taskman.models.MOMModel;

import java.util.HashMap;
import java.util.Map;

public class MOMAdapter extends FirebaseRecyclerAdapter<MOMModel,MOMAdapter.MOMHolder> {

    public MOMAdapter(@NonNull FirebaseRecyclerOptions<MOMModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MOMAdapter.MOMHolder holder, int position, @NonNull MOMModel model) {
        holder.txtMOM.setText(model.getMomDetail());

        holder.imgEdit.setOnClickListener(view -> {
            openCustomDialog(holder, model, position);
        });
        holder.imgDelete.setOnClickListener(view -> {
            openAlertDialog(holder, position);
        });
    }

    private void openAlertDialog(MOMAdapter.MOMHolder holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.txtMOM.getContext());
        builder.setTitle("Delete MOM");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            FirebaseDatabase.getInstance().getReference().child("mom").child(getRef(position).getKey()).removeValue();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.cancel();
        });
        builder.show();
    }

    private void openCustomDialog(MOMAdapter.MOMHolder holder, MOMModel model, int position) {

        EditText edtMOMDetail;
        Button btnUpdateMOM,btnCancel;

        Dialog dialog = new Dialog(holder.txtMOM.getContext());
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.cd_update_mom);
        dialog.setCancelable(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        edtMOMDetail = dialog.findViewById(R.id.edtMOMDetail);

        btnUpdateMOM = dialog.findViewById(R.id.btnUpdateMOM);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        edtMOMDetail.setText(model.getMomDetail());


        btnUpdateMOM.setOnClickListener(view -> {
            if (edtMOMDetail.getText().toString().trim().isEmpty()) {
                Toast.makeText(holder.txtMOM.getContext(), "Please enter MOM Detail", Toast.LENGTH_SHORT).show();
            } else {
                updateDataToFirebase(edtMOMDetail.getText().toString().trim(),holder,position);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    private void updateDataToFirebase(String momDetail, MOMAdapter.MOMHolder holder, int position) {

        Map<String, Object> map = new HashMap<>();
        //map.put("sprintId", sprintId);
        map.put("momDetail", momDetail);


        FirebaseDatabase.getInstance().getReference().child("mom").child(getRef(position).getKey()).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(holder.txtMOM.getContext(), "Details Updated Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.txtMOM.getContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @NonNull
    @Override
    public MOMAdapter.MOMHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_mom,parent,false);
        return new MOMHolder(view);
    }

    public class MOMHolder extends RecyclerView.ViewHolder {
        CardView crdMOM;
        TextView txtMOM;
        ImageView imgEdit, imgDelete;

        public MOMHolder(@NonNull View itemView) {
            super(itemView);

            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            crdMOM = itemView.findViewById(R.id.crdMOM);
            txtMOM = itemView.findViewById(R.id.txtMOM);
        }
    }
}
