package com.example.theatre.features.info.presentation.ui.detail.person

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.theatre.R
import com.example.theatre.core.domain.model.common.agent.Agent
import com.example.theatre.core.presentation.ext.toListOfPerformances
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.handleContents
import com.example.theatre.databinding.FragmentInfoCommonArghBinding
import com.example.theatre.features.Constants.BundleConstants.BUNDlE_KEY_PERSON
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент с отображением подробностей об актере
 *
 * @author Tamerlan Mamukhov
 */

class PersonInfoFragment : Fragment(R.layout.fragment_info_common_argh) {

    private val binding: FragmentInfoCommonArghBinding by viewBinding(FragmentInfoCommonArghBinding::bind)
    private val personViewModel by sharedViewModel<PersonViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.run { personViewModel.getPersonById(getInt(BUNDlE_KEY_PERSON)) }
        personViewModel.personDetails.observe(viewLifecycleOwner, ::handleInfo)
    }

    private fun handleInfo(contentResultState: ContentResultState) {
        contentResultState.handleContents(
            onStateSuccess = {
                setDetails(it as Agent)
            },
            onStateError = {
                // TODO: Добавить обработку ошибки (например сообщение)
            }
        )
    }

    private fun setDetails(personDetails: Agent) {
        with(binding) {
            textDatetime.text = getString(R.string.person_participations)
            textPlaceTitle.visibility = View.GONE
            textPlaceSubway.visibility = View.GONE
            textPlaceAddress.visibility = View.GONE
            textPlacePhone.visibility = View.GONE
            textPlaceSite.visibility = View.GONE
            with(personDetails) {
                textEventStartEnd.text = participations?.toListOfPerformances(requireContext())
            }
        }
    }
}
