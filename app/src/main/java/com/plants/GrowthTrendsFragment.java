package com.plants;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GrowthTrendsFragment extends Fragment {
    private LineChart heightChart;
    private TextView tvAverageGrowthRate;
    private TextView tvTotalHeightGain;
    private TextView tvLeafGrowthRate;
    private List<Growth> growthHistory;

    public static GrowthTrendsFragment newInstance(List<Growth> history) {
        GrowthTrendsFragment fragment = new GrowthTrendsFragment();
        fragment.growthHistory = new ArrayList<>(history);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_growth_trends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        heightChart = view.findViewById(R.id.heightChart);
        tvAverageGrowthRate = view.findViewById(R.id.tvAverageGrowthRate);
        tvTotalHeightGain = view.findViewById(R.id.tvTotalHeightGain);
        tvLeafGrowthRate = view.findViewById(R.id.tvLeafGrowthRate);

        setupChart();
        calculateAndDisplayStatistics();
    }

    private void setupChart() {
        // Configure chart appearance
        heightChart.getDescription().setEnabled(false);
        heightChart.setTouchEnabled(true);
        heightChart.setDragEnabled(true);
        heightChart.setScaleEnabled(true);
        heightChart.setPinchZoom(true);
        heightChart.setDrawGridBackground(false);

        // Configure X axis
        XAxis xAxis = heightChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new DateAxisValueFormatter());

        // Configure Y axis
        heightChart.getAxisLeft().setDrawGridLines(true);
        heightChart.getAxisRight().setEnabled(false);

        // Create and set data
        List<Entry> heightEntries = new ArrayList<>();
        long firstTimestamp = growthHistory.get(0).getTimestamp().getSeconds() * 1000L;

        for (Growth growth : growthHistory) {
            float x = (growth.getTimestamp().getSeconds() * 1000L - firstTimestamp) / (24 * 60 * 60 * 1000f); // Days since first measurement
            heightEntries.add(new Entry(x, (float) growth.getHeight()));
        }

        LineDataSet heightDataSet = new LineDataSet(heightEntries, "Height (cm)");
        heightDataSet.setColor(Color.GREEN);
        heightDataSet.setCircleColor(Color.GREEN);
        heightDataSet.setLineWidth(2f);
        heightDataSet.setCircleRadius(4f);
        heightDataSet.setDrawValues(false);

        LineData lineData = new LineData(heightDataSet);
        heightChart.setData(lineData);
        heightChart.invalidate();
    }

    private void calculateAndDisplayStatistics() {
        if (growthHistory.size() < 2) {
            return;
        }

        // Calculate growth rates
        double totalHeightGain = growthHistory.get(growthHistory.size() - 1).getHeight() -
                growthHistory.get(0).getHeight();

        double totalDays = (growthHistory.get(growthHistory.size() - 1).getTimestamp().toDate().getTime() -
                growthHistory.get(0).getTimestamp().toDate().getTime()) / (1000.0 * 60 * 60 * 24);

        double averageGrowthRate = totalHeightGain / totalDays;

        // Calculate leaf growth rate
        int totalLeafGain = growthHistory.get(growthHistory.size() - 1).getLeafCount() -
                growthHistory.get(0).getLeafCount();
        double leafGrowthRate = totalLeafGain / totalDays;

        // Display statistics
        tvAverageGrowthRate.setText(String.format(Locale.getDefault(), "%.2f cm/day", averageGrowthRate));
        tvTotalHeightGain.setText(String.format(Locale.getDefault(), "%.1f cm", totalHeightGain));
        tvLeafGrowthRate.setText(String.format(Locale.getDefault(), "%.2f leaves/day", leafGrowthRate));
    }

    private static class DateAxisValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.format(Locale.getDefault(), "Day %.0f", value);
        }
    }
}