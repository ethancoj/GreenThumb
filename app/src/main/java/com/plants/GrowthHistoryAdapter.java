package com.plants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class GrowthHistoryAdapter : RecyclerView.Adapter<GrowthHistoryAdapter.ViewHolder>() {
    private val historyList = mutableListOf<Growth>()

    override fun getItemCount() = historyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val growth = historyList[position]
        val dateFormat = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())

        holder.apply {
            date.text = dateFormat.format(growth.timestamp.toDate())
            height.text = "${growth.height.toInt()} cm"
            leafCount.text = growth.leafCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_growth_history, parent, false)
        )
    }

    fun updateHistory(history: List<Growth>) {
        historyList.clear()
        historyList.addAll(history)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.tvDate)
        val height: TextView = view.findViewById(R.id.tvHistoryHeight)
        val leafCount: TextView = view.findViewById(R.id.tvHistoryLeafCount)
    }
}