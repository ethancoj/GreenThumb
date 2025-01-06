package com.plants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.plants.databinding.FragmentInformationsBinding
import okio.buffer
import okio.source

class InformationsFragment : Fragment() {
    private lateinit var adapter: InformationsAdapter
    private var binding: FragmentInformationsBinding? = null
    private var checkedChipIds: List<Int> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = InformationsAdapter()
        binding = FragmentInformationsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            checkedChipIds = cgFragmentInformations.checkedChipIds
            populateRecyclerView()

            cgFragmentInformations.setOnCheckedStateChangeListener { _, checkedIds ->
                checkedChipIds = checkedIds
                populateRecyclerView()
            }

            rvFragmentDiscussions.also {
                it.adapter = adapter
                it.layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private fun populateRecyclerView() {
        adapter.removeAll()

        when (checkedChipIds[0]) {
            R.id.cFragmentInformationsDesert -> "desert.json"
            R.id.cFragmentInformationsTemperate -> "temperate.json"
            R.id.cFragmentInformationsWetland -> "wetland.json"
            else -> "tropical.json"
        }.also {
            context?.apply {
                getGson().fromJson(
                    assets.open(it).source().buffer().readUtf8(),
                    Array<Information>::class.java
                ).forEach { adapter.add(it) }
            }
        }

        binding?.tvFragmentDiscussions?.visibility = if (adapter.itemCount > 0) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}