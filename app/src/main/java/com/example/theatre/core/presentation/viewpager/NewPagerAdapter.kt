package com.example.theatre.core.presentation.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Адапотер, работает с [ViewPager2]
 *
 * @property fragments      список фрагментов
 * @param fragmentManager   [FragmentManager]
 * @param lifecycle         [Lifecycle]
 * @author Tamerlan Mamukhov on 2023-02-18
 */
class NewPagerAdapter(
    private val fragments: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}

fun FragmentStateAdapter.prepareAdapter(
    tabLayout: TabLayout,
    viewPager: ViewPager2,
    titles: List<String>
) {
    viewPager.adapter = this
    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
        tab.text = titles[position]
    }.attach()
}