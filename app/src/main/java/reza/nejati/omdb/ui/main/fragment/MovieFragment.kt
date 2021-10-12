package reza.nejati.omdb.ui.main.fragment

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shreyaspatil.MaterialDialog.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import reza.nejati.omdb.R
import reza.nejati.omdb.data.model.response.Search
import reza.nejati.omdb.data.model.response.SearchResponse
import reza.nejati.omdb.data.remote.State
import reza.nejati.omdb.databinding.FragmentMovieBinding
import reza.nejati.omdb.def.Const
import reza.nejati.omdb.ui.base.BaseFragment
import reza.nejati.omdb.ui.details.MovieDetailsActivity
import reza.nejati.omdb.ui.main.adapter.MovieResultAdapter
import reza.nejati.omdb.utils.EndlessScrollViewListener
import reza.nejati.omdb.utils.observe
import reza.nejati.omdb.utils.setSubTitle
import reza.nejati.omdb.utils.showToast
import javax.inject.Inject

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MovieFragment : BaseFragment<MovieViewModel, FragmentMovieBinding>() {

    @Inject
    lateinit var viewModelFactory: MovieViewModel.SearchIdMovieViewModelFactory

    @Inject
    lateinit var mAdapter: MovieResultAdapter

    private val pageStart: Int = 1
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var currentPage: Int = pageStart

    override val mViewModel: MovieViewModel by viewModels() {
        val searchType = arguments?.getInt(SEARCH_TYPE)
            ?: throw IllegalArgumentException("`search type` must be non-null")
        MovieViewModel.provideFactory(viewModelFactory, searchType)
    }

    override fun getViewBinding(): FragmentMovieBinding =
        FragmentMovieBinding.inflate(layoutInflater)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setupBinding()
        observeData()
        setupAdapter()
        setupRecyclerView()
        mViewModel.getAllSearch()
        onWaitingData()
    }

    private fun setupBinding() {
        mViewBinding.lifecycleOwner = this
        mViewBinding.viewModel = mViewModel
    }

    private fun observeData() {
        with(mViewModel) {
            observe(currentSearch, ::onFirstSearch)
            observe(searchResultFromDb, ::renderDbData)
            observe(apiResponse, ::renderApiData)
        }

    }

    private fun setupAdapter() {
        mAdapter.collection = mViewModel.currentResult
        mAdapter.clickListener = { search, imageView ->
            onItemClicked(search, imageView)
        }
    }

    fun submitSearch(searchValue: Pair<String, Int>) {
        mViewModel.submitSearch(searchValue)
    }

    private fun setupRecyclerView() {
        with(mViewBinding.moviesRecyclerview) {
            addOnScrollListener(object :
                EndlessScrollViewListener(mViewBinding.moviesRecyclerview.layoutManager as LinearLayoutManager) {
                override fun loadMoreItems() {
                    if (mViewModel.currentSearch.value?.first.isNullOrEmpty()) return
                    mViewModel.totalItem.value?.let {
                        isLastPage = it <= mAdapter.collection.size
                    } ?: return
                    loadMore()
                }

                override fun getTotalPageCount(): Int {
                    return totalPages
                }

                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading() = mViewModel.isLoading.value ?: false
            })
            adapter = mAdapter
        }
    }

    fun loadMore() {
        mViewModel.setLoadingStatus(true)
        currentPage += 1
        mViewModel.getMovieResult(
            mViewModel.currentSearch.value?.first,
            currentPage,
            mViewModel.currentSearch.value?.second
        )
    }


    private fun renderApiData(state: State<SearchResponse?>?) {
        when (state) {
            is State.Loading -> {
                mViewModel.setLoadingStatus(int = true)
            }
            is State.Success -> {
                with(state.data?.error) {
                    if (this.isNullOrEmpty()) {
                        renderData(state.data?.search)
                        mViewModel.setTotalItem(state.data?.totalResults ?: 0)
                    } else {
                        requireActivity().showToast(
                            message = if (mViewModel.currentResult.size > 0)
                                getString(
                                R.string.total_result_is,
                                mViewModel.totalItem.value.toString()
                            )
                            else
                                this
                        )

                    }
                }
                onDataReady()
            }
            is State.Error -> {
                onDataReady()
                MaterialDialog.Builder(requireActivity())
                    .setTitle(getString(R.string.error))
                    .setMessage(getString(R.string.netwotk_error))
                    .setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .build()
                    .show()
            }


        }
    }

    private fun onWaitingData() {
        if (mAdapter.collection.isEmpty()) {
            mViewModel.setWaitingForSearch(true)
        } else {
            mViewModel.setWaitingForSearch(false)
        }
    }

    private fun onDataReady() {
        mViewModel.setLoadingStatus(int = false)
        if (mAdapter.collection.isEmpty()) {
            mViewModel.setWaitingForSearch(true)
        } else {
            mViewModel.setWaitingForSearch(false)
        }
    }

    private fun renderDbData(data: List<Search>?) {
        data?.let {
            if (it.isNotEmpty()) {
                (activity as AppCompatActivity).setSubTitle(
                    getString(
                        R.string.offline,
                        data.size.toString()
                    )
                )
                mViewModel.currentResult.addAll(data)
            }
        }
        onDataReady()
    }

    private fun onFirstSearch(searchValue: Pair<String, Int>?) {
        currentPage = 1
        mAdapter.notifyItemRangeRemoved(0, mViewModel.currentResult.size)
        mViewModel.currentResult.clear()
        searchValue?.let {
            mViewModel.getMovieResult(it.first, currentPage, it.second)
        }
    }

    private fun renderData(data: List<Search>?) {
        (activity as AppCompatActivity).setSubTitle("")
        data?.let {

            it.map { search ->
                search.background =Const.LIST_OF_GRADIENTS.random()
                search.searchText = mViewModel.currentSearch.value?.first
                mViewModel.saveSearch(search)
            }
            mViewModel.currentResult.addAll(it)
            mAdapter.notifyItemInserted(mAdapter.itemCount)
        }
    }

    private fun onItemClicked(movie: Search, imageView: ImageView) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            imageView,
            imageView.transitionName
        )

        val intent = MovieDetailsActivity.getStartIntent(
            requireContext(),
            movie.omdbID ?: "",
            movie.background ?: 0,
            movie.poster ?: ""
        )
        startActivity(intent, options.toBundle())
    }

    companion object {
        @JvmStatic
        fun newInstance(searchType: Int) = MovieFragment().apply {
            arguments = bundleOf(SEARCH_TYPE to searchType)
        }

        private var SEARCH_TYPE = "SEARCH_TYPE"
    }

}
