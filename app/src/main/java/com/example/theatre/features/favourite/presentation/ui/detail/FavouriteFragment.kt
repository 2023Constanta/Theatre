package com.example.theatre.features.favourite.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.theatre.R
import com.example.theatre.core.domain.model.common.performance.Performance
import com.example.theatre.core.presentation.ext.EMPTY
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.refreshPage
import com.example.theatre.core.presentation.viewpager.NewPagerAdapter
import com.example.theatre.core.presentation.viewpager.prepareAdapter
import com.example.theatre.databinding.FragmentFavouriteDetailBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент с отображением деталей избранного события
 *
 * @author Tamerlan Mamukhov
 */
class FavouriteFragment : Fragment(R.layout.fragment_favourite_detail) {

    private lateinit var adapter: NewPagerAdapter
    private val binding: FragmentFavouriteDetailBinding by viewBinding(
        FragmentFavouriteDetailBinding::bind
    )
    private val favouriteViewModel by sharedViewModel<FavouriteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run {
            favouriteViewModel.getFavouritesById(getInt(fav_id))
        }

        prepareViewPager()
        favouriteViewModel.favouriteContent.observe(viewLifecycleOwner, ::handleFav)
    }

    private fun prepareViewPager() = with(binding.content) {
        adapter = NewPagerAdapter(
            fragments = listOf(FavouriteDetailFragment(), FavouriteInfoFragment()),
            fragmentManager = requireActivity().supportFragmentManager,
            lifecycle = lifecycle
        )
        adapter.prepareAdapter(
            tabs, viewPager,
            resources.getStringArray(R.array.details_info).toList()
        )
    }

    private fun handleFav(contentResultState: ContentResultState) =
        with(binding) {
            contentResultState.refreshPage(
                onStateSuccess = {
                    setDetails((it as Performance))
                },
                viewToShow = detailsContent,
                progressBar = progressBar2
            )
        }

    private fun setDetails(eventDetails: Performance) {
        with(binding.content) {
            with(eventDetails) {
                textName.text = shortTitle.orEmpty().replaceFirstChar { it.uppercaseChar() }
                if (isFree == true) {
                    textPrice.text = context?.getString(R.string.free)
                } else {
                    textPrice.text = price
                }
                val imageURL =
                    if (images?.isNotEmpty() == true) images.first().imageURL.orEmpty() else String.EMPTY
                if (imageURL.isNotEmpty()) {
                    context?.let {
                        Glide
                            .with(it)
                            .load(imageURL)
                            .into(imageThumbnail)

                        Glide
                            .with(it)
                            .load(imageURL)
                            .into(binding.imageLarge)
                    }
                }
            }
        }
    }

    companion object {
        const val fav_id = "fav_id"
    }
}