package com.example.theatre.features.info.presentation.ui.list.person

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.theatre.R
import com.example.theatre.core.domain.model.common.agent.Agent
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.refreshPage
import com.example.theatre.databinding.FragmentPersonsBinding
import com.example.theatre.features.info.presentation.adapters.PersonsListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Фрагмент со списком актеров
 *
 * @author Tamerlan Mamukhov
 */

class PersonsListFragment : Fragment(R.layout.fragment_persons) {

    companion object {
        const val DETAILS_TAB = 1
        const val PERSON = "Персона"
        fun newInstance(): PersonsListFragment {
            return PersonsListFragment()
        }
    }

    private val binding: FragmentPersonsBinding by viewBinding(FragmentPersonsBinding::bind)
    private lateinit var personsAdapter: PersonsListAdapter
    private val personsViewModel by viewModel<PersonsListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        personsViewModel.init()
        prepareAdapter()
    }

    private fun prepareAdapter() = with(binding) {
        listPersons.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        personsAdapter = PersonsListAdapter { id ->
            onPersonClick(id)
        }
        listPersons.adapter = personsAdapter
    }

    private fun initObservers() =
        personsViewModel.personLoaded.observe(viewLifecycleOwner, ::handlePersons)


    private fun handlePersons(contentResultState: ContentResultState) {
        contentResultState.refreshPage(
            viewToShow = binding.listPersons,
            progressBar = binding.progressBar4,
            onStateSuccess = {
                personsAdapter.persons = ((it as List<Agent>).toMutableList())
            }
        )
    }

    private fun tryAgain() {
        binding?.errorLayout?.root?.visibility = View.INVISIBLE
        personsViewModel.init()
        initObservers()
    }

    private fun onPersonClick(id: Int) {
        requireActivity().findNavController(R.id.navHostFragment)
            .navigate(R.id.action_info_to_personFragment, bundleOf("id" to id))
    }
}