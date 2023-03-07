package com.example.theatre.features.poster.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.theatre.R
import com.example.theatre.core.presentation.ext.EMPTY
import com.example.theatre.core.presentation.ext.deleteHTML
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.handleContents
import com.example.theatre.databinding.FragmentPosterDescriptionBinding
import com.example.theatre.features.Constants.BundleConstants.BUNDlE_KEY_POSTER
import com.example.theatre.features.poster.domain.model.PosterDetails
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент описания афиши
 * @author Tamerlan Mamukhov on 2022-05-28
 */
class PosterDetailFragment : Fragment(R.layout.fragment_poster_description) {
    private val viewModel by sharedViewModel<PosterViewModel>()


    private val binding: FragmentPosterDescriptionBinding by viewBinding(
        FragmentPosterDescriptionBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run { viewModel.getPoster(getInt(BUNDlE_KEY_POSTER)) }
        viewModel.posterDetailedLoaded.observe(viewLifecycleOwner, ::handlePosterDetails)
    }

    private fun handlePosterDetails(contentResultState: ContentResultState) =
        contentResultState.handleContents(
            onStateSuccess = { setDetails(it as PosterDetails) },
            onStateError = {}
        )

    private fun setDetails(posterDetails: PosterDetails) =
        with(binding) {
            with(posterDetails) {

                val imageURL =
                    if (this.images?.isNotEmpty() == true) images.first().imageURL.orEmpty() else String.EMPTY
                if (imageURL.isNotEmpty()) {
                    context?.let {
                        Glide
                            .with(it)
                            .load(imageURL)
                            .into(imageThumbnail)
                    }

                }

                textName.text = title.replaceFirstChar { it.uppercaseChar() }
                textDescription.text = description.orEmpty().deleteHTML()
                textTagline.text = tagline
                textBody.text = bodyText.orEmpty().deleteHTML()

            }
        }

}