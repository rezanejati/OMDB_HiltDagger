package reza.nejati.omdb.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import com.ferfalk.simplesearchview.SimpleSearchView
import com.shreyaspatil.MaterialDialog.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import reza.nejati.omdb.R
import reza.nejati.omdb.data.model.search.SearchWords
import reza.nejati.omdb.databinding.ActivityMainBinding
import reza.nejati.omdb.ui.base.BaseActivity
import reza.nejati.omdb.ui.main.fragment.MovieFragment
import reza.nejati.omdb.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import reza.nejati.omdb.ui.main.adapter.SearchWordsAdapter
import javax.inject.Inject

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {


    @Inject
    lateinit var searchAdapterAdapter: SearchWordsAdapter

    @Inject
    lateinit var pagerAdapter: PagerAdapter

    override val mViewModel: MainViewModel by viewModels()


    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)


    override fun onCreated(savedInstanceState: Bundle?) {
        setupViewModel()
        initView()
        setupViewPager()
        setupLastSearchAdapter()
        observeData()

    }


    private fun observeData() {
        with(mViewModel) {
            observe(allWords, ::lastSearches)
            observe(search, ::onSubmitSearch)
        }
    }


    private fun onSubmitSearch(pair: Pair<String, Int>?) {
        pair?.let {
            mViewBinding.searchView.setQuery(pair.first, false)
            (pagerAdapter.getCurrentFragments()[mViewBinding.tabLayout.selectedTabPosition].first
                    as MovieFragment).submitSearch(it)
        }

    }

    private fun lastSearches(list: List<SearchWords>?) {
        list?.let {
            if (it.isNotEmpty()) {
                searchAdapterAdapter.collection = list
            }
        }

    }


    private fun setupViewModel() {
        mViewBinding.viewModel = mViewModel
    }

    private fun setupViewPager() {
        with(mViewBinding.vpSearch) {
            adapter = pagerAdapter
            TabLayoutMediator(mViewBinding.tabLayout, this) { tab, position ->
                tab.text = pagerAdapter.getCurrentFragments()[position].second
            }.attach()

        }

    }

    private fun setupLastSearchAdapter() {
        mViewBinding.moviesRecyclerView.adapter = searchAdapterAdapter

    }


    private fun initView() {
        setSupportActionBar(mViewBinding.toolbar)
        searchAdapterAdapter.clickListener = { onItemClicked(it) }

    }

    private fun onItemClicked(words: SearchWords) {
        mViewModel.setCurrentSearch(Pair(words.searchedWords, mViewBinding.vpSearch.currentItem))
        mViewBinding.moviesRecyclerView.hide()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        setupSearchView(menu)
        return true
    }

    private fun setupSearchView(menu: Menu) = with(mViewBinding) {
        val item = menu.findItem(R.id.action_search)
        searchView.setMenuItem(item)
        searchView.setTabLayout(tabLayout)
        searchView.setKeepQuery(true)

        searchView.setOnSearchViewListener(object : SimpleSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                mViewBinding.moviesRecyclerView.hide()
            }

            override fun onSearchViewClosedAnimation() {
            }

            override fun onSearchViewShown() {

                mViewModel.viewModelScope.launch {
                    delay(SimpleAnimationUtils.ANIMATION_DURATION_DEFAULT.toLong() + 250)
                    if (searchAdapterAdapter.collection.isNotEmpty())
                        mViewBinding.moviesRecyclerView.show()
                }
            }

            override fun onSearchViewShownAnimation() {
            }
        })

        searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                return if (query.length > 2) {
                    mViewModel.setCurrentSearch(Pair(query, mViewBinding.vpSearch.currentItem))
                    false
                } else {
                    showToast(getString(R.string.error_count_character))
                    true

                }


            }

            override fun onQueryTextCleared(): Boolean {
                return false
            }
        })

        val revealCenter = searchView.revealAnimationCenter
        revealCenter?.let {
            it.x -= convertDpToPx(EXTRA_REVEAL_CENTER_PADDING, this@MainActivity)
        }

    }

    override fun onBackPressed() {
        if (mViewBinding.moviesRecyclerView.visibility.equals(View.VISIBLE)) {
            mViewBinding.searchView.onBackPressed()
        } else {
            MaterialDialog.Builder(this)
                .setTitle(getString(R.string.exit_dialog_title))
                .setMessage(getString(R.string.exit_dialog_message))
                .setPositiveButton(getString(R.string.option_yes)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    super.onBackPressed()
                }
                .setNegativeButton(getString(R.string.option_no)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .build()
                .show()
        }
    }

    override fun onNetworkChanges(isConnected: Boolean) {
        if (!isConnected) {
            mViewBinding.textViewNetworkStatus.text =
                getString(R.string.text_no_connectivity)
            mViewBinding.networkStatusLayout.apply {
                show()
                setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
            }
        } else {
            mViewBinding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
            mViewBinding.networkStatusLayout.apply {
                setBackgroundColor(getColorRes(R.color.colorStatusConnected))
                animate()
                    .alpha(1f)
                    .setStartDelay(MainActivity.ANIMATION_DURATION)
                    .setDuration(MainActivity.ANIMATION_DURATION)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            hide()
                        }
                    })
            }
        }
    }

    companion object {
        const val ANIMATION_DURATION = 1000L
        const val EXTRA_REVEAL_CENTER_PADDING = 40
    }


}
