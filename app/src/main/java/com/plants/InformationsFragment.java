package com.plants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.plants.databinding.FragmentInformationsBinding;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class InformationsFragment extends Fragment {
    private InformationsAdapter adapter;
    private FragmentInformationsBinding binding;
    private List<Integer> checkedChipIds;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new InformationsAdapter();
        binding = FragmentInformationsBinding.inflate(inflater, container, false);
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
            checkedChipIds = binding.cgFragmentInformations.getCheckedChipIds();
            populateRecyclerView();

            binding.cgFragmentInformations.setOnCheckedStateChangeListener(
                    (group, checkedIds) -> {
                        checkedChipIds = checkedIds;
                        populateRecyclerView();
                    });

            binding.rvFragmentDiscussions.setAdapter(adapter);
            binding.rvFragmentDiscussions.setLayoutManager(
                    new LinearLayoutManager(getContext()));
        }
    }

    private void populateRecyclerView() {
        adapter.removeAll();

        if (getContext() != null && !checkedChipIds.isEmpty()) {
            String fileName;
            int chipId = checkedChipIds.get(0);

            if (chipId == R.id.cFragmentInformationsDesert) {
                fileName = "desert.json";
            } else if (chipId == R.id.cFragmentInformationsTemperate) {
                fileName = "temperate.json";
            } else if (chipId == R.id.cFragmentInformationsWetland) {
                fileName = "wetland.json";
            } else {
                fileName = "tropical.json";
            }

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(getContext().getAssets().open(fileName)));
                String jsonContent = reader.lines().collect(Collectors.joining("\n"));
                reader.close();

                Information[] informations = Utils.getGson()
                        .fromJson(jsonContent, Information[].class);

                for (Information information : informations) {
                    adapter.add(information);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (binding != null) {
            binding.tvFragmentDiscussions.setVisibility(
                    adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
        }
    }
}