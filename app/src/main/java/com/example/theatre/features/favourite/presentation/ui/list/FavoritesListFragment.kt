package com.example.theatre.features.favourite.presentation.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.theatre.R
import com.example.theatre.core.domain.model.common.performance.Performance
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.handleContents
import com.example.theatre.databinding.FragmentFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент для показа списка избранных афиш
 *
 * @constructor Create empty Favorites list fragment
 */
class FavoritesListFragment : Fragment(R.layout.fragment_favorite) {

    private val favouriteListViewModel by sharedViewModel<FavouriteListViewModel>()

    private val binding: FragmentFavoriteBinding by viewBinding(FragmentFavoriteBinding::bind)
    private lateinit var adapter: FavouritesListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        handleAdapter()
        favouriteListViewModel.getAll()
    }

    private fun handleAdapter() {
        binding.rvFavourites.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        adapter = FavouritesListAdapter { id -> onFavClick(id) }
        binding.rvFavourites.adapter = adapter
    }

    private fun initObserver() {
        favouriteListViewModel.favouriteContent.observe(viewLifecycleOwner, ::handleFavourites)
    }

    private fun handleFavourites(contentResultState: ContentResultState) =
        contentResultState.handleContents(onStateSuccess = {
            adapter.favourites = ((it as List<Performance>).toMutableList())
        }, onStateError = {

        })

    private fun onFavClick(id: Int) = findNavController().navigate(
        R.id.action_favourite_to_favouriteFragment, bundleOf("fav_id" to id)
    )

}