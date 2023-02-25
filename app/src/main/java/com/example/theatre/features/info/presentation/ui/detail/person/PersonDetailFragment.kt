package com.example.theatre.features.info.presentation.ui.detail.person

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.theatre.R
import com.example.theatre.core.domain.model.common.agent.Agent
import com.example.theatre.core.presentation.ext.EMPTY
import com.example.theatre.core.presentation.ext.deleteHTML
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.handleContents
import com.example.theatre.databinding.FragmentDetailCommonArghBinding
import com.example.theatre.features.Constants.BundleConstants.BUNDlE_KEY_PERSON
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент с подробностями об актере
 *
 * @author Tamerlan Mamukhov
 */

class PersonDetailFragment : Fragment(R.layout.fragment_detail_common_argh) {

    private val binding: FragmentDetailCommonArghBinding by viewBinding(
        FragmentDetailCommonArghBinding::bind
    )

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
            with(personDetails) {
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
                textName.text = title
                textDescription.text = description.orEmpty().deleteHTML().trimEnd()
                textBody.text = bodyText.orEmpty().deleteHTML().trimEnd()
                textTagline.visibility = View.GONE
            }
        }
    }
}