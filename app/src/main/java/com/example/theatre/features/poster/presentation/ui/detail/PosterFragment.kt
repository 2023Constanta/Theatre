package com.example.theatre.features.poster.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.theatre.R
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.refreshPage
import com.example.theatre.core.presentation.viewpager.NewPagerAdapter
import com.example.theatre.core.presentation.viewpager.prepareAdapter
import com.example.theatre.databinding.FragmentPosterDetailBinding
import com.example.theatre.features.Constants.BundleConstants.BUNDlE_KEY_POSTER
import com.example.theatre.features.poster.domain.model.PosterDetails
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


/**
 * Фрагмент деталей афиши
 *
 * @author Tamerlan Mamukhov on 2022-05-28
 */
class PosterFragment : Fragment(R.layout.fragment_poster_detail) {

    private val binding: FragmentPosterDetailBinding by viewBinding(FragmentPosterDetailBinding::bind)
    private lateinit var adapter: NewPagerAdapter
    private lateinit var fragmentsList: ArrayList<Fragment>

    private val viewModel by sharedViewModel<PosterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentsList = arrayListOf(
            PosterDetailFragment(),
            PosterInfoFragment()
        )

        adapter = NewPagerAdapter(
            fragmentsList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        with(binding.content) {
            adapter.prepareAdapter(
                tabs, viewPager, resources.getStringArray(R.array.details_info).toList()
            )
        }

        arguments?.run { viewModel.getPoster(getInt(BUNDlE_KEY_POSTER)) }

        viewModel.posterDetailedLoaded.observe(viewLifecycleOwner, ::handlePoster)
    }


    private fun handlePoster(contentResultState: ContentResultState) =
        with(binding) {
            contentResultState.refreshPage(
                viewToShow = detailsContent,
                progressBar = progressBar2,
                onStateSuccess = { setDetails(it as PosterDetails) }
            )
        }

    private fun setDetails(posterDetails: PosterDetails) {

        with(binding.content) {
            with(posterDetails) {
                textName.text =
                    posterDetails.shortTitle.orEmpty().replaceFirstChar { it.uppercaseChar() }

                val imageURL = images?.first()?.imageURL.orEmpty()

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

}