package reza.nejati.omdb.data.remote

import androidx.annotation.MainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 * Call Api
 *
 * @param REQUEST
 */
@ExperimentalCoroutinesApi
abstract class CallApi<REQUEST> {

    fun asFlow() = flow<Resource<REQUEST>> {
        val apiResponse = fetchFromRemote()
        val remotePosts = apiResponse.body()
        emit(Resource.Success(remotePosts))

    }.catch { e ->
        e.printStackTrace()
        emit(Resource.Failed(e.message ?: ""))
    }

    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>
}
