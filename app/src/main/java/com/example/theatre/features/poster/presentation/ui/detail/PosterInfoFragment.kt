package com.example.theatre.features.poster.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.theatre.R
import com.example.theatre.core.presentation.PerformanceDateFormatter
import com.example.theatre.core.presentation.ext.toListOfActorsInPerformance
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.handleContents
import com.example.theatre.databinding.FragmentPosterReviewBinding
import com.example.theatre.features.poster.domain.model.PosterDetails
import com.example.theatre.features.poster.presentation.ui.detail.PosterFragment.Companion.poster_id
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * @author Tamerlan Mamukhov
 * @created 2022-08-25
 */
class PosterInfoFragment : Fragment(R.layout.fragment_poster_review) {

    private val binding: FragmentPosterReviewBinding by viewBinding(FragmentPosterReviewBinding::bind)
    private val viewModel by sharedViewModel<PosterViewModel>()

    private val dateFormatter by inject<PerformanceDateFormatter>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.run { viewModel.getPoster(getInt(poster_id)) }

        viewModel.posterDetailedLoaded.observe(viewLifecycleOwner, ::handlePosterDetails)
    }

    private fun handlePosterDetails(contentResultState: ContentResultState) =
        contentResultState.handleContents(
            onStateSuccess = { setDetails(it as PosterDetails) },
            onStateError = {}
        )

    private fun setDetails(posterDetails: PosterDetails) =
        with(binding) {
            textDatetime.text = getString(R.string.event_date_time)
            textPlace.text = getString(R.string.place)
            textParticipants.text = getString(R.string.actors)
            with(posterDetails) {
                textAgeRestriction.text = ageRestriction
                textEventStartEnd.text = dateFormatter.getUpcomingPerformanceDates(dates)
                textParticipantsList.text =
                    participants?.toListOfActorsInPerformance(requireContext())
                textPlaceSite.text = siteUrl
            }
        }
}