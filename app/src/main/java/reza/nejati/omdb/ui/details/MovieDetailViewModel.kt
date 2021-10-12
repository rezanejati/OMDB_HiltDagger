package reza.nejati.omdb.ui.details

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import reza.nejati.omdb.data.model.detail.DetailResponse
import reza.nejati.omdb.data.remote.State
import reza.nejati.omdb.data.repository.OmdbiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 * Viewmodel for MovieDetailsActivity
 *
 * @property omdbiRepository
 * @property movieId
 */
@ExperimentalCoroutinesApi
class MovieDetailViewModel @AssistedInject constructor(
    private val omdbiRepository: OmdbiRepository,
    @Assisted val movieId: String
) : ViewModel() {

    private val _apiResponse = MutableLiveData<State<DetailResponse?>>()
    val apiResponse: LiveData<State<DetailResponse?>> = _apiResponse

    private val _movieDetails = MutableLiveData<DetailResponse>()
    val movieDetails: LiveData<DetailResponse?> = _movieDetails

    @AssistedFactory
    interface MovieDetailViewModelFactory {
        fun create(movieId: String): MovieDetailViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: MovieDetailViewModelFactory,
            movieId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(movieId) as T
            }
        }
    }

    fun setDetailMovie(detail: DetailResponse?) {
        detail?.let {
            _movieDetails.postValue(it)
        }

    }

    fun getMovieDetail() {
        viewModelScope.launch {
            omdbiRepository.getDetailById(movieId)
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
