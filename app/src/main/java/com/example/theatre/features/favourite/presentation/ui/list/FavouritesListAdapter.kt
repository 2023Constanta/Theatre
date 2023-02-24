package com.example.theatre.features.favourite.presentation.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theatre.R
import com.example.theatre.core.domain.model.common.performance.Performance
import com.example.theatre.databinding.ItemPersonBinding

class FavouritesListAdapter(
    private val onItemClick: (id: Int) -> Unit
) : RecyclerView.Adapter<FavouritesListAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root)

    var favourites: MutableList<Performance> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemPersonBinding.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            with(favourites[position]) {
                textName.text = title
                root.setOnClickListener {
                    onItemClick(id)
                }
            }

        }


    }

    override fun getItemCount(): Int = favourites.size


}