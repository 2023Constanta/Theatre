package com.example.theatre.features.spectacles.presentation.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.theatre.R
import com.example.theatre.core.domain.model.common.performance.Performance
import com.example.theatre.core.domain.model.common.performance.PerformancePlaceLocation
import com.example.theatre.core.presentation.ext.EMPTY
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.refreshPage
import com.example.theatre.core.presentation.viewpager.NewPagerAdapter
import com.example.theatre.core.presentation.viewpager.prepareAdapter
import com.example.theatre.databinding.FragmentSpectacleDetailBinding
import com.example.theatre.features.favourite.presentation.ui.detail.FavouriteViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент с отображением деталей события
 *
 * @author Tamerlan Mamukhov
 */
class SpectacleFragment : Fragment(R.layout.fragment_spectacle_detail) {

    companion object {
        const val event_id = "id"
    }

    private var specId = 0
    private lateinit var perToSave: Performance
    private val binding: FragmentSpectacleDetailBinding by viewBinding(
        FragmentSpectacleDetailBinding::bind
    )

    private val spectacleViewModel by sharedViewModel<SpectacleViewModel>()
    private val favouriteViewModel by sharedViewModel<FavouriteViewModel>()

    private var eventURL: String? = null
    private lateinit var adapter: NewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)

        arguments?.run {
            specId = getInt(event_id)
            spectacleViewModel.getSpectacleDetails(getInt(event_id))
        }
        prepareViewPager()

        spectacleViewModel.spectacleDetailLoaded.observe(viewLifecycleOwner, ::handleContent)
        spectacleViewModel.cityLoaded.observe(viewLifecycleOwner, ::handleContent)
    }


    private fun prepareViewPager() = with(binding.content) {
        adapter = NewPagerAdapter(
            fragments = listOf(SpectacleDetailFragment(), SpectacleInfoFragment()),
            fragmentManager = requireActivity().supportFragmentManager,
            lifecycle = lifecycle
        )
        adapter.prepareAdapter(
            tabs, viewPager,
            resources.getStringArray(R.array.details_info).toList()
        )
    }

    private fun handleContent(contentResultState: ContentResultState) =
        with(binding) {
            contentResultState.refreshPage(
                viewToShow = contentDetailzz,
                progressBar = progressBar6,
                onStateSuccess = {
                    when (it) {
                        is Performance -> {
                            val slug = it.location?.slug
                            setDetails(it)
                            perToSave = it
                            slug?.let { it1 -> spectacleViewModel.getCity(it1) }
                        }
                        is PerformancePlaceLocation -> {
                            setCity(it)
                        }
                    }
                }
            )
        }

    private fun setDetails(eventDetails: Performance) {
        eventURL = eventDetails.siteUrl
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

    private fun setCity(city: PerformancePlaceLocation) {
        binding.content.textVenue.text = city.name
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_event, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> requireActivity().onBackPressed()
            R.id.action_event -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(eventURL))
                startActivity(browserIntent)
            }
            R.id.action_add_fav -> {
                Toast.makeText(activity, "Boo", Toast.LENGTH_SHORT).show()
                favouriteViewModel.saveFav(
                    perToSave
                )
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }
}