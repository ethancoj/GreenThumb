package com.plants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GrowthHistoryAdapter extends RecyclerView.Adapter<GrowthHistoryAdapter.ViewHolder> {
    private final List<Growth> historyList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault());

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Growth growth = historyList.get(position);

        holder.date.setText(dateFormat.format(growth.getTimestamp().toDate()));
        holder.height.setText(String.format(Locale.getDefault(), "%d cm", (int) growth.getHeight()));
        holder.leafCount.setText(String.valueOf(growth.getLeafCount()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_growth_history, parent, false);
        return new ViewHolder(view);
    }

    public void updateHistory(List<Growth> history) {
        historyList.clear();
        historyList.addAll(history);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView date;
        final TextView height;
        final TextView leafCount;

        ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.tvDate);
            height = view.findViewById(R.id.tvHistoryHeight);
            leafCount = view.findViewById(R.id.tvHistoryLeafCount);
        }
    }
}