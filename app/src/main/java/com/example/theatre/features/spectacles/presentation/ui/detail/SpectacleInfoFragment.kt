package com.example.theatre.features.spectacles.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.theatre.R
import com.example.theatre.core.domain.model.common.performance.Performance
import com.example.theatre.core.domain.model.common.performance.PerformancePlace
import com.example.theatre.core.domain.model.common.performance.PerformancePlaceLocation
import com.example.theatre.core.presentation.PerformanceDateFormatter
import com.example.theatre.core.presentation.ext.EMPTY
import com.example.theatre.core.presentation.ext.toListOfActorsInPerformance
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.handleContents
import com.example.theatre.databinding.FragmentInfoCommonArghBinding
import com.example.theatre.features.Constants.BundleConstants.BUNDlE_KEY_PERFORMANCE
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент с отображением деталей места постановки
 *
 * @author Tamerlan Mamukhov
 */

class SpectacleInfoFragment : Fragment(R.layout.fragment_info_common_argh) {

    private val binding: FragmentInfoCommonArghBinding by viewBinding(FragmentInfoCommonArghBinding::bind)
    private val spectacleViewModel by sharedViewModel<SpectacleViewModel>()
    private val dateFormatter by inject<PerformanceDateFormatter>()
    private lateinit var cityName: String
    private lateinit var gaps: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gaps = getString(R.string.gaps)
        with(spectacleViewModel) {
            arguments?.run { getSpectacleDetails(getInt(BUNDlE_KEY_PERFORMANCE)) }
            spectacleDetailLoaded.observe(viewLifecycleOwner, ::handleSpecDetails)

            cityLoaded.observe(viewLifecycleOwner, ::handleSpecCity)
            placeLoaded.observe(viewLifecycleOwner, ::handleSpecPlace)
        }
    }

    private fun handleSpecDetails(contentResultState: ContentResultState) =
        contentResultState.handleContents(
            onStateSuccess = {
                with(it as Performance) {
                    val placeId = this.place?.id
                    val citySlug = this.location?.slug
                    setDetails(this)
                    placeId?.let { it1 -> spectacleViewModel.getPlace(it1) }
                    citySlug?.let { it1 -> spectacleViewModel.getCity(it1) }
                }
            },
            onStateError = {
                // TODO: Добавить обработку ошибки (например сообщение)
            }
        )


    private fun handleSpecCity(contentResultState: ContentResultState) =
        contentResultState.handleContents(
            onStateSuccess = { setCity(it as PerformancePlaceLocation) },
            onStateError = {
                // TODO: Добавить обработку ошибки (например сообщение)
            }
        )

    private fun handleSpecPlace(contentResultState: ContentResultState) =
        contentResultState.handleContents(
            onStateSuccess = { setPlace(it as PerformancePlace) },
            onStateError = {
                // TODO: Добавить обработку ошибки (например сообщение)
            }
        )

    private fun setDetails(eventDetails: Performance) {
        with(binding) {
            textDatetime.text = getString(R.string.event_date_time)
            textPlace.text = getString(R.string.place)
            textParticipants.text = getString(R.string.actors)
            with(eventDetails) {
                textAgeRestriction.text = ageRestriction
                textEventStartEnd.text = dateFormatter.getUpcomingPerformanceDates(dates)
                textParticipantsList.text =
                    participants?.toListOfActorsInPerformance(requireContext())
            }
        }
    }

    private fun setCity(city: PerformancePlaceLocation) {
        cityName = city.name.toString()
    }

    private fun setPlace(place: PerformancePlace) {
        with(binding) {
            with(place) {
                textPlaceTitle.text = title.orEmpty().replaceFirstChar { it.uppercaseChar() }
                textPlaceSubway.text = subway
                textPlaceAddress.text = "$cityName$gaps$address"
                textPlacePhone.text = phone
                val url = if (foreignUrl?.isNotEmpty() == true) foreignUrl else String.EMPTY
                textPlaceSite.text = url
                if (isClosed == true) {
                    textPlaceIsclosed.text = getString(R.string.place_is_closed)
                }
            }
        }
    }
}
