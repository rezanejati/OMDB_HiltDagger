package reza.nejati.omdb.data.repository

import reza.nejati.omdb.BuildConfig
import reza.nejati.omdb.data.remote.api.OmdbiService
import reza.nejati.omdb.data.model.detail.DetailResponse
import reza.nejati.omdb.data.remote.CallApi
import reza.nejati.omdb.data.remote.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import reza.nejati.omdb.data.model.response.SearchResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 * The Repository for fetching data from remote
 *
 */
interface OmdbiRepository {
    /**
     * fetch movies from Api
     * @param searchName
     * @param type
     * @param page
     * @return
     */
    fun getResultByWord(searchName: String?, type: String?, page: Int?): Flow<Resource<SearchResponse>>

    /**
     * fetch detail movies from Api
     * @param movieId
     * @return
     */
    fun getDetailById( movieId: String): Flow<Resource<DetailResponse>>
}

/**
 * Get Apis
 *
 * @property omdbiService
 */
@ExperimentalCoroutinesApi
class DefaultOmdbiRepository @Inject constructor(
    private val omdbiService: OmdbiService
) : OmdbiRepository {

    /**
     * fetch movies from Api
     * @param searchName
     * @param type
     * @param page
     * @return
     */
    override fun getResultByWord(
        searchName: String?, type: String?, page: Int?): Flow<Resource<SearchResponse>> {
        return object : CallApi<SearchResponse>() {

            override suspend fun fetchFromRemote(): Response<SearchResponse> =
                omdbiService.getPosts(
                    searchName,
                    BuildConfig.OMDB_KEY,
                    type,
                    page
                )


        }.asFlow()
    }


    /**
     * fetch detail movies from Api
     * @param movieId
     * @return
     */
    override fun getDetailById(
        movieId: String
    ): Flow<Resource<DetailResponse>> {
        return object : CallApi<DetailResponse>() {

            override suspend fun fetchFromRemote(): Response<DetailResponse> =
                omdbiService.getDetailPosts(
                    BuildConfig.OMDB_KEY,
                    movieId
                )

        }.asFlow()
    }

}
