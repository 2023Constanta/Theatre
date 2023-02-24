package com.example.theatre.features.info.presentation.ui.detail.theatre

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
import com.example.theatre.databinding.FragmentDetailCommonArghBinding
import com.example.theatre.features.info.domain.model.theatre.Theatre
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент с подробностями о театре
 *
 * @author Tamerlan Mamukhov
 */

class TheatreDetailFragment : Fragment(R.layout.fragment_detail_common_argh) {

    companion object {
        const val DESCRIPTION_TAB = 0
        const val INFO = "Информация"
        const val theatre_id = "id"
        fun newInstance(): TheatreDetailFragment {
            return TheatreDetailFragment()
        }
    }

    private val binding: FragmentDetailCommonArghBinding by viewBinding(
        FragmentDetailCommonArghBinding::bind
    )
    private val theatreViewModel by sharedViewModel<TheatreViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run { theatreViewModel.getTheatreById(getInt(theatre_id)) }
        theatreViewModel.theatreDetailsContent.observe(viewLifecycleOwner, ::handleContent)
    }


    private fun handleContent(contentResultState: ContentResultState) =

        contentResultState.handleContents(
            onStateSuccess = {
                setDetails(it as Theatre)
            },
            onStateError = {
                // TODO: Добавить обработку ошибки (например сообщение)
            }
        )


    private fun setDetails(theatreDetails: Theatre) {
        with(binding) {
            with(theatreDetails) {
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
                textDescription.text = description.orEmpty().deleteHTML().trimEnd()
                textBody.text = bodyText.orEmpty().deleteHTML().trimEnd()
                textTagline.visibility = View.GONE
            }
        }
    }
}
