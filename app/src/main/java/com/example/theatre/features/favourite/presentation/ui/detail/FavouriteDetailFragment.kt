package com.example.theatre.features.favourite.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.theatre.R
import com.example.theatre.core.domain.model.common.performance.Performance
import com.example.theatre.core.presentation.ext.deleteHTML
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.handleContents
import com.example.theatre.databinding.FragmentDetailCommonArghBinding
import com.example.theatre.features.Constants.BundleConstants.BUNDlE_KEY_FAVOURITE
import com.example.theatre.features.spectacles.presentation.ui.detail.SpectacleViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент с подробным описанием события
 *
 * @author Tamerlan Mamukhov
 */

class FavouriteDetailFragment : Fragment(R.layout.fragment_detail_common_argh) {

    private val binding: FragmentDetailCommonArghBinding by viewBinding(
        FragmentDetailCommonArghBinding::bind
    )
    private val spectacleViewModel by sharedViewModel<SpectacleViewModel>()
    private val favouriteViewModel by sharedViewModel<FavouriteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.run {
            favouriteViewModel.getFavouritesById(getInt(BUNDlE_KEY_FAVOURITE))
        }

        favouriteViewModel.favouriteContent.observe(viewLifecycleOwner, ::handleInfo)
    }

    private fun handleInfo(contentResultState: ContentResultState) {
        contentResultState.handleContents(
            onStateSuccess = {
                when (it) {
                    is Performance -> {
                        setDetails(it)
                    }
                }
            },
            onStateError = {
                // TODO: Добавить обработку ошибки (например сообщение)
            }
        )
    }

    private fun setDetails(eventDetails: Performance) {
        with(binding) {
            with(eventDetails) {
                textName.text = title.replaceFirstChar { it.uppercaseChar() }
                textDescription.text = description.orEmpty().deleteHTML()
                textTagline.text = tagline
                textBody.text = bodyText.orEmpty().deleteHTML()
            }
        }
    }
}
