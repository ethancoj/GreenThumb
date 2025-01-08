package com.plants;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.List;

public class GrowthAdapter extends RecyclerView.Adapter<GrowthAdapter.ViewHolder> {
    private final List<Growth> growthList = new ArrayList<>();

    @Override
    public int getItemCount() {
        return growthList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Growth growth = growthList.get(position);
        holder.name.setText(growth.getPlantName());

        // Handle item click for editing
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), GrowthActivity.class);
            intent.putExtra("growth", Utils.getGson().toJson(growth));
            intent.putExtra("isEdit", true);
            v.getContext().startActivity(intent);
        });

        // Handle delete button click
        holder.deleteButton.setOnClickListener(v -> {
            String id = growth.getId();
            if (id != null) {
                new MaterialAlertDialogBuilder(v.getContext())
                        .setTitle("Delete Growth Record")
                        .setMessage("Are you sure you want to delete this record?")
                        .setPositiveButton("Delete", (dialog, which) ->
                                Model.getGrowth().document(id).delete())
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_growth, parent, false);
        return new ViewHolder(view);
    }

    public void add(Growth growth) {
        growthList.add(growth);
        notifyItemInserted(growthList.indexOf(growth));
    }

    public void removeAll() {
        growthList.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final MaterialCardView cardView;
        final TextView name;
        final ImageButton deleteButton;

        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.mcvAdapterGrowth);
            name = view.findViewById(R.id.tvAdapterGrowth);
            deleteButton = view.findViewById(R.id.btnDelete);
        }
    }
}