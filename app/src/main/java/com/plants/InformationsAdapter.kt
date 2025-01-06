package com.plants

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

internal class InformationsAdapter : RecyclerView.Adapter<InformationsAdapter.ViewHolder>() {
    private val informations = mutableListOf<Information>()

    override fun getItemCount() = informations.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.description.text = informations[position].description
        viewHolder.name.text = informations[position].name

        viewHolder.itemView.setOnClickListener {
            viewHolder.itemView.context.apply {
                viewHolder.cardView.setOnClickListener {
                    startActivity(Intent(this, InformationActivity::class.java).apply {
                        putExtra("information", getGson().toJson(informations[position]))
                    })
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_informations, parent, false
            )
        )
    }

    fun add(information: Information) {
        informations.add(information)
        notifyItemInserted(informations.indexOf(information))
    }

    fun removeAll() {
        (0).also {
            if (informations.isNotEmpty()) {
                informations.removeAt(it)
                notifyItemRemoved(it)
                removeAll()
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView = view.findViewById<MaterialCardView>(R.id.mcvAdapterInformations)
        val description: TextView = view.findViewById(R.id.tvAdapterInformationsDescription)
        val name: TextView = view.findViewById(R.id.tvAdapterInformations)
    }
}