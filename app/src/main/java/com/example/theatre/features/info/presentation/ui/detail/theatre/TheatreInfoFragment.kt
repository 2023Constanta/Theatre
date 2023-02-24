package com.example.theatre.features.info.presentation.ui.detail.theatre

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.theatre.R
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.handleContents
import com.example.theatre.databinding.FragmentInfoCommonArghBinding
import com.example.theatre.features.info.domain.model.theatre.Theatre
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент с отображением подробностей о театре
 *
 * @author Tamerlan Mamukhov
 */

class TheatreInfoFragment : Fragment(R.layout.fragment_info_common_argh) {

    companion object {
        const val DETAILS_TAB = 1
        const val DETAILS = "Детали"
        const val theatre_id = "id"
        fun newInstance(): TheatreInfoFragment {
            return TheatreInfoFragment()
        }
    }

    private val binding: FragmentInfoCommonArghBinding by viewBinding(FragmentInfoCommonArghBinding::bind)
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
            textDatetime.text = getString(R.string.theatre_schedule)
            textPlace.text = getString(R.string.theatre_address)
            with(theatreDetails) {
                textEventStartEnd.text = timetable
                textPlaceTitle.text = title.replaceFirstChar { it.uppercaseChar() }
                textPlaceSubway.text = subway
                textPlaceAddress.text = address
                textPlacePhone.text = phone
                textPlaceSite.text = foreignUrl
                if (isClosed == true) {
                    textPlaceIsclosed.text = getString(R.string.place_is_closed)
                }
            }
        }
    }


}


