package com.plants

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.plants.databinding.FragmentGrowthBinding

class GrowthFragment : Fragment() {
    private lateinit var adapter: GrowthAdapter
    private var binding: FragmentGrowthBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = GrowthAdapter()
        binding = FragmentGrowthBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            getGrowth().orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, _ ->
                    adapter.removeAll()

                    snapshot?.forEach {
                        it.toObject(Growth::class.java).apply { id = it.id }.also { growth ->
                            adapter.add(growth)
                        }
                    }

                    tvFragmentGrowth.visibility = if (adapter.itemCount > 0) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
                }

            mtFragmentGrowth.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.mFragmentGrowthAdd -> {
                        startActivity(Intent(context, GrowthActivity::class.java))
                        true
                    }
                    else -> false
                }
            }

            rvFragmentGrowth.also {
                it.adapter = adapter
                it.layoutManager = LinearLayoutManager(context)
            }
        }
    }
}