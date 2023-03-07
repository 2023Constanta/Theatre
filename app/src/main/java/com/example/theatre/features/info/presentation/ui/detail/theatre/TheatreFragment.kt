package com.example.theatre.features.info.presentation.ui.detail.theatre

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.theatre.R
import com.example.theatre.core.presentation.ext.EMPTY
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.refreshPage
import com.example.theatre.core.presentation.viewpager.NewPagerAdapter
import com.example.theatre.core.presentation.viewpager.prepareAdapter
import com.example.theatre.databinding.FragmentDetailCommonBinding
import com.example.theatre.features.Constants.BundleConstants.BUNDlE_KEY_THEATRE
import com.example.theatre.features.info.domain.model.theatre.Theatre
import com.example.theatre.features.info.domain.model.theatre.TheatreLocation
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент с отображением детальной информации о театре
 *
 * @author Tamerlan Mamukhov
 */

class TheatreFragment : Fragment(R.layout.fragment_detail_common) {

    private val binding: FragmentDetailCommonBinding by viewBinding(FragmentDetailCommonBinding::bind)
    private val theatreViewModel by sharedViewModel<TheatreViewModel>()
    private lateinit var adapter: NewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)

        arguments?.run { theatreViewModel.getTheatreById(getInt(BUNDlE_KEY_THEATRE)) }
        prepareViewPager()

        theatreViewModel.theatreDetailsContent.observe(viewLifecycleOwner, ::handleContent)
        theatreViewModel.cityContent.observe(viewLifecycleOwner, ::handleContent)
    }


    private fun prepareViewPager() = with(binding.content) {
        adapter = NewPagerAdapter(
            fragments = listOf(TheatreDetailFragment(), TheatreInfoFragment()),
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
                viewToShow = contentDetails,
                progressBar = progressBar,
                onStateSuccess = {
                    when (it) {
                        is Theatre -> setDetails(it)
                        is TheatreLocation -> setCity(it)
                    }
                }
            )
        }

    private fun setDetails(theatreDetails: Theatre) {
        with(binding.content) {
            with(theatreDetails) {
                textName.text = shortTitle.orEmpty().replaceFirstChar { it.uppercaseChar() }
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
                textVenue.text = subway
            }
        }
    }

    private fun setCity(city: TheatreLocation) {
        binding.content.textPrice.text = city.name
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> requireActivity().findNavController(R.id.navHostFragment)
                .popBackStack()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}
