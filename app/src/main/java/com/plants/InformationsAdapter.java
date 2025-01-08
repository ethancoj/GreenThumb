package com.plants;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.plants.databinding.AdapterInformationsBinding;
import java.util.ArrayList;
import java.util.List;

public class InformationsAdapter extends RecyclerView.Adapter<InformationsAdapter.ViewHolder> {
    private static final String TAG = "InformationsAdapter";
    private final List<Information> informations = new ArrayList<>();

    @Override
    public int getItemCount() {
        return informations.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Information information = informations.get(position);
        holder.binding.tvAdapterInformations.setText(information.getName());
        holder.binding.tvAdapterInformationsDescription.setText(information.getDescription());

        holder.binding.mcvAdapterInformations.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), InformationActivity.class);
            Information clickedInfo = informations.get(position);
            // Convert the complete object to JSON
            String completeJson = Utils.getGson().toJson(clickedInfo);
            Log.d(TAG, "Sending complete JSON to activity: " + completeJson);
            intent.putExtra("information", completeJson);
            v.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterInformationsBinding binding = AdapterInformationsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    public void add(Information information) {
        // Log the complete information object being added
        String json = Utils.getGson().toJson(information);
        Log.d(TAG, "Adding complete information: " + json);
        informations.add(information);
        notifyItemInserted(informations.indexOf(information));
    }

    public void removeAll() {
        informations.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterInformationsBinding binding;

        ViewHolder(AdapterInformationsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}