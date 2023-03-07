package com.example.theatre.features.info.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.theatre.R
import com.example.theatre.core.presentation.viewpager.NewPagerAdapter
import com.example.theatre.core.presentation.viewpager.prepareAdapter
import com.example.theatre.databinding.FragmentInfoBinding
import com.example.theatre.features.info.presentation.ui.list.person.PersonsListFragment
import com.example.theatre.features.info.presentation.ui.list.theatre.TheatresListFragment

/**
 * Фрагмент с вкладками, отображающими список театров и список актеров
 *
 * @author Tamerlan Mamukhov
 */

class InfoFragment : Fragment(R.layout.fragment_info) {

    private val binding: FragmentInfoBinding by viewBinding(FragmentInfoBinding::bind)
    private lateinit var adapter: NewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareViewPager()
    }

    private fun prepareViewPager() = with(binding) {
        adapter = NewPagerAdapter(
            fragments = listOf(TheatresListFragment(), PersonsListFragment()),
            fragmentManager = requireActivity().supportFragmentManager,
            lifecycle = lifecycle
        )
        adapter.prepareAdapter(
            tabs, viewPager,
            resources.getStringArray(R.array.theaters_persons).toList()
        )
    }
}