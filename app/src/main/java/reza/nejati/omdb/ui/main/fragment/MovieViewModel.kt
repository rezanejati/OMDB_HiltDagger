package reza.nejati.omdb.ui.main.fragment

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import reza.nejati.omdb.data.local.DataBaseRepository
import reza.nejati.omdb.data.model.response.Search
import reza.nejati.omdb.data.remote.State
import reza.nejati.omdb.data.repository.OmdbiRepository
import reza.nejati.omdb.def.SearchType
import reza.nejati.omdb.utils.getSearchType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import reza.nejati.omdb.data.model.response.SearchResponse

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */


/**
 * ViewModel for MovieFragment
 *
 * @property omdbiRepository
 * @property database
 * @property searchType
 */

@ExperimentalCoroutinesApi
class MovieViewModel @AssistedInject constructor(
    private val omdbiRepository: OmdbiRepository, private val database: DataBaseRepository,
    @Assisted val searchType: Int
) : ViewModel() {

    @AssistedFactory
    interface SearchIdMovieViewModelFactory {
        fun create(searchType: Int): MovieViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: SearchIdMovieViewModelFactory,
            searchType: Int,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(searchType) as T
            }
        }
    }


    private val _apiResponse = MutableLiveData<State<SearchResponse?>>()
    val apiResponse: LiveData<State<SearchResponse?>> = _apiResponse

    private val _currentSearch = MutableLiveData<Pair<String, @SearchType Int>>()
    val currentSearch: LiveData<Pair<String, Int>> = _currentSearch

    private val _searchResultFromDb = MutableLiveData<List<Search>>()
    val searchResultFromDb: LiveData<List<Search>> = _searchResultFromDb

    var currentResult = ArrayList<Search>()

    private val _waitingForSearch = MutableLiveData(true)
    val waitingForSearch: LiveData<Boolean> = _waitingForSearch


    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading


    private val _totalItem = MutableLiveData<Int>()
    val totalItem: LiveData<Int> = _totalItem

    fun setTotalItem(int: Int) {
        _totalItem.postValue(int)

    }

    fun setLoadingStatus(int: Boolean) {
        _isLoading.postValue(int)

    }

    fun setWaitingForSearch(int: Boolean) {
        _waitingForSearch.postValue(int)

    }

    fun saveSearch(search: Search) {
        viewModelScope.launch {
            database.insertSearchResult(search)
        }
    }

    fun submitSearch(search: Pair<String, @SearchType Int>) {
        _currentSearch.postValue(search)
    }

    fun getAllSearch() {
        viewModelScope.launch {
            database.getLastSearchWord(searchType.getSearchType())?.let { lastSearch ->
                database.getSearchResult(
                    searchType.getSearchType(),
                    lastSearch.searchedWords
                ).let {
                    _searchResultFromDb.value = it.first().distinctBy { Pair(it.title, it.title) }
                }
            }


        }

    }


    fun getMovieResult(query: String?, page: Int, searchType: Int?) {
        viewModelScope.launch {
            omdbiRepository.getResultByWord(query, searchType?.getSearchType(), page)
                .onStart {
                    _apiResponse.value = State.loading()
                }
                .map { resource ->
                    State.fromResource(resource)
                }
                .collect { state ->
                    _apiResponse.value = state
                }
        }
    }


}
