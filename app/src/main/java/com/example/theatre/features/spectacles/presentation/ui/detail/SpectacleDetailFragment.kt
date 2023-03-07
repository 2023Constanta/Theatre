package com.example.theatre.features.spectacles.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.theatre.R
import com.example.theatre.core.domain.model.common.performance.Performance
import com.example.theatre.core.presentation.ext.EMPTY
import com.example.theatre.core.presentation.ext.deleteHTML
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.handleContents
import com.example.theatre.databinding.FragmentDetailCommonArghBinding
import com.example.theatre.features.Constants.BundleConstants.BUNDlE_KEY_PERFORMANCE
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент с подробным описанием события
 *
 * @author Tamerlan Mamukhov
 */

class SpectacleDetailFragment : Fragment(R.layout.fragment_detail_common_argh) {

    private val binding: FragmentDetailCommonArghBinding by viewBinding(
        FragmentDetailCommonArghBinding::bind
    )
    private val spectacleViewModel by sharedViewModel<SpectacleViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.run { spectacleViewModel.getSpectacleDetails(getInt(BUNDlE_KEY_PERFORMANCE)) }
        spectacleViewModel.spectacleDetailLoaded.observe(viewLifecycleOwner, ::handleInfo)
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
                val imageURL =
                    if (images?.isNotEmpty() == true) images.first().imageURL.orEmpty() else String.EMPTY
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
}
