package com.example.theatre.features.poster.presentation.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.theatre.R
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.core.presentation.model.refreshPage
import com.example.theatre.databinding.FragmentPosterBinding
import com.example.theatre.features.Constants.BundleConstants.BUNDlE_KEY_POSTER
import com.example.theatre.features.poster.domain.model.PosterBriefItem
import com.example.theatre.features.poster.presentation.adapters.PosterBriefItemAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Фрагмент со списком афиш
 *
 * @author Tamerlan Mamukhov on 2022-05-28
 */
class PostersListFragment : Fragment(R.layout.fragment_poster) {

    private lateinit var adapter: PosterBriefItemAdapter
    private val viewModel by viewModel<PostersListViewModel>()
    private val binding: FragmentPosterBinding by viewBinding(FragmentPosterBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareAdapter()
        initObservers()
        viewModel.getPosters()
    }

    private fun onItemClicked(id: Int) =
        findNavController()
            .navigate(R.id.action_home_to_posterDetailFragment, bundleOf(BUNDlE_KEY_POSTER to id))

    private fun prepareAdapter() = with(binding) {

        rvPosters.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        adapter = PosterBriefItemAdapter { id: Int ->
            onItemClicked(id)
        }
        rvPosters.adapter = adapter
    }

    private fun initObservers() {
        viewModel.postersBrief.observe(viewLifecycleOwner, ::handlePosters)
    }

    private fun handlePosters(contentResultState: ContentResultState) =
        with(binding) {
            contentResultState.refreshPage(
                onStateSuccess = {
                    adapter.setData(it as List<PosterBriefItem>)
                },
                tryAgainAction = { tryAgain() },
                viewToShow = rvPosters,
                progressBar = progressBar,
                errorLayout = errorLayout
            )

        }

    private fun tryAgain() = viewModel.getPosters()
}