package com.example.theatre.features.info.presentation.ui.list.theatre

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.theatre.R
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.refreshPage
import com.example.theatre.databinding.FragmentTheatresBinding
import com.example.theatre.features.info.domain.model.theatre.Theatre
import com.example.theatre.features.info.presentation.adapters.TheatresListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Фрагмент со списком театров
 *
 * @author Tamerlan Mamukhov
 */

class TheatresListFragment : Fragment(R.layout.fragment_theatres) {

    companion object {
        const val DESCRIPTION_TAB = 0
        const val THEATRE = "Театр"
        fun newInstance(): TheatresListFragment {
            return TheatresListFragment()
        }
    }

    private val binding: FragmentTheatresBinding by viewBinding(FragmentTheatresBinding::bind)
    private lateinit var theatresAdapter: TheatresListAdapter
    private lateinit var recyclerView: RecyclerView
    private val theatresListViewModel by viewModel<TheatresListViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareAdapter()
        initObservers()
        theatresListViewModel.getTheatres()
    }

    private fun prepareAdapter() = with(binding) {
        listTheatre.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        theatresAdapter = TheatresListAdapter { id ->
            onTheatreClick(id)
        }
        listTheatre.adapter = theatresAdapter
    }

    private fun initObservers() {
        theatresListViewModel.theatresContent.observe(viewLifecycleOwner, ::handleTheatres)
    }

    private fun handleTheatres(contentResultState: ContentResultState) {
        contentResultState.refreshPage(
            viewToShow = binding?.listTheatre!!,
            progressBar = binding?.progressBar5!!,

            onStateSuccess = {
                theatresAdapter.theatres = ((it as List<Theatre>).toMutableList())
                binding.listTheatre.adapter = theatresAdapter
            })

    }

    private fun tryAgain() {
        binding?.errorLayout?.root?.visibility = View.INVISIBLE
        theatresListViewModel.getTheatres()
        initObservers()
    }

    private fun onTheatreClick(id: Int) {
        val bundle = bundleOf("id" to id)
        requireActivity().findNavController(R.id.navHostFragment)
            .navigate(R.id.action_info_to_theatreFragment, bundle)
    }
}