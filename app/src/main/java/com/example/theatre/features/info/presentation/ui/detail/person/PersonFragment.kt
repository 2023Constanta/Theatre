package com.example.theatre.features.info.presentation.ui.detail.person

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.theatre.R
import com.example.theatre.core.domain.model.common.agent.Agent
import com.example.theatre.core.presentation.ext.EMPTY
import com.example.theatre.core.presentation.ext.toAgent
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.refreshPage
import com.example.theatre.core.presentation.viewpager.NewPagerAdapter
import com.example.theatre.core.presentation.viewpager.prepareAdapter
import com.example.theatre.databinding.FragmentDetailCommonBinding
import com.example.theatre.features.Constants.BundleConstants.BUNDlE_KEY_PERSON
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Фрагмент с отображением детальной информации об актере
 *
 * @author Tamerlan Mamukhov
 */
class PersonFragment : Fragment(R.layout.fragment_detail_common) {

    private val binding: FragmentDetailCommonBinding by viewBinding(FragmentDetailCommonBinding::bind)
    private val personViewModel by sharedViewModel<PersonViewModel>()
    private lateinit var adapter: NewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)

        arguments?.run { personViewModel.getPersonById(getInt(BUNDlE_KEY_PERSON)) }
        prepareViewPager()
        personViewModel.personDetails.observe(viewLifecycleOwner, ::handleInfo)
    }


    private fun prepareViewPager() = with(binding.content) {
        adapter = NewPagerAdapter(
            fragments = listOf(PersonDetailFragment(), PersonInfoFragment()),
            fragmentManager = requireActivity().supportFragmentManager,
            lifecycle = lifecycle
        )
        adapter.prepareAdapter(
            tabs, viewPager,
            resources.getStringArray(R.array.details_info).toList()
        )
    }

    private fun handleInfo(contentResultState: ContentResultState) =
        with(binding) {
            contentResultState.refreshPage(
                viewToShow = contentDetails,
                progressBar = progressBar,
                onStateSuccess = { setDetails(it as Agent) }
            )
        }

    private fun setDetails(personDetails: Agent) {
        with(binding.content) {
            with(personDetails) {
                textPrice.text = getString(agentType.orEmpty().toAgent())
                textName.text = title.replaceFirstChar { it.uppercaseChar() }
                val imageURL =
                    if (images?.isNotEmpty() == true) images.first().imageURL.orEmpty() else String.EMPTY
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

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> requireActivity().findNavController(R.id.navHostFragment)
                .popBackStack()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}
