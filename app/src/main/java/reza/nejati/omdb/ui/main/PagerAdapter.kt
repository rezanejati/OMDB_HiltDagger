package reza.nejati.omdb.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 * Adapter for view pager
 *
 * @property mFragmentList
 * @constructor
 *
 * @param context
 */
class PagerAdapter @Inject constructor(

    @ActivityContext context: Context,
    private val mFragmentList: MutableList<Pair<Fragment, String>>
) : FragmentStateAdapter(context as AppCompatActivity) {

    override fun getItemCount(): Int = mFragmentList.size

    fun getCurrentFragments(): MutableList<Pair<Fragment, String>> = mFragmentList

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position].first
    }
}