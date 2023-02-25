package com.example.theatre.features.spectacles.presentation.ui.list

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.theatre.R
import com.example.theatre.core.domain.model.common.performance.Performance
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.refreshPage
import com.example.theatre.databinding.FragmentSpectaclesBinding
import com.example.theatre.features.Constants.BundleConstants.BUNDlE_KEY_PERFORMANCE
import com.example.theatre.features.spectacles.presentation.adapters.EventListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Фрагмент со списком постановок
 *
 * @author Tamerlan Mamukhov
 */

class SpectaclesListFragment : Fragment(R.layout.fragment_spectacles) {

    private val binding: FragmentSpectaclesBinding by viewBinding(FragmentSpectaclesBinding::bind)
    private lateinit var performancesAdapter: EventListAdapter
    private val spectaclesListViewModel by viewModel<SpectaclesListViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareAdapter()
        initObservers()
        spectaclesListViewModel.loadPerformances()
        prepareSearch()
    }

    private fun prepareAdapter() = with(binding) {
        listEvent.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        performancesAdapter = EventListAdapter() { id ->
            onSpectacleClick(id)
        }
        listEvent.adapter = performancesAdapter
    }

    private fun initObservers() {
        spectaclesListViewModel.spectacleLoaded.observe(viewLifecycleOwner, ::handleSpectacles)
    }

    private fun prepareSearch() =
        with(binding) {
            tilSearch.apply {
                editText?.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        val word = editText?.text.toString()
                        Log.d("SpecList", "prepareSearch: $word")
                        spectaclesListViewModel.getPerformancesByQuery(word)

                        initObserversForSearched()
                    }
                    true
                }
            }
        }

    private fun initObserversForSearched() {
        spectaclesListViewModel.spectacleSearched.observe(viewLifecycleOwner, ::handleSpectacles)
    }

    private fun handleSpectacles(contentResultState: ContentResultState) = with(binding) {
        contentResultState.refreshPage(
            onStateSuccess = {
                Log.d("SpecList", "handleSpectacles: ${it as List<Performance>}")
                performancesAdapter.spectacles = (it.toMutableList())
            },
            tryAgainAction = { tryAgain() },
            viewToShow = content,
            progressBar = progressBar,
            errorLayout = errorLayout
        )
    }

    private fun tryAgain() {
        spectaclesListViewModel.getPerformances()
    }

    private fun onSpectacleClick(id: Int) =
        requireActivity().findNavController(R.id.navHostFragment)
            .navigate(
                R.id.action_performance_to_eventFragment,
                bundleOf(BUNDlE_KEY_PERFORMANCE to id)
            )
}
