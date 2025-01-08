package com.plants;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.plants.databinding.FragmentGrowthBinding;

public class GrowthFragment extends Fragment {
    private GrowthAdapter adapter;
    private FragmentGrowthBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new GrowthAdapter();
        binding = FragmentGrowthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (binding != null) {
            // Setup RecyclerView
            binding.rvFragmentGrowth.setAdapter(adapter);
            binding.rvFragmentGrowth.setLayoutManager(new LinearLayoutManager(getContext()));

            // Setup Toolbar
            binding.mtFragmentGrowth.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.mFragmentGrowthAdd) {
                    startActivity(new Intent(getContext(), GrowthActivity.class));
                    return true;
                }
                return false;
            });

            // Setup Firestore listener
            Model.getGrowth()
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .addSnapshotListener((snapshot, e) -> {
                        if (snapshot != null) {
                            adapter.removeAll();
                            for (DocumentSnapshot document : snapshot.getDocuments()) {
                                Growth growth = document.toObject(Growth.class);
                                if (growth != null) {
                                    growth.setId(document.getId());
                                    adapter.add(growth);
                                }
                            }

                            binding.tvFragmentGrowth.setVisibility(
                                    adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE
                            );
                        }
                    });
        }
    }
}