package reza.nejati.omdb.data.local

import androidx.annotation.WorkerThread
import reza.nejati.omdb.data.local.dao.SearchResultDao
import reza.nejati.omdb.data.local.dao.SearchWordsDao
import reza.nejati.omdb.data.model.response.Search
import reza.nejati.omdb.data.model.search.SearchWords
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 * The Repository for fetching data from database
 *
 * @property searchWordsDao
 * @property searchResultDao
 */
class DataBaseRepository
@Inject constructor(
    private val searchWordsDao: SearchWordsDao,
    private val searchResultDao: SearchResultDao
) {

    /**
     * Get all search words as live data
     */

    val allWords: Flow<List<SearchWords>> = searchWordsDao.getAllSearchWords()

    /**
     * Save search word
     * @param search
     */
    @WorkerThread
    suspend fun saveSearchWords(word: SearchWords) = withContext(Dispatchers.IO) {
        searchWordsDao.saveSearchWords(word)
    }

    /**
     * Get search words by search type (series or movie)
     * @param searchType
     */

    @WorkerThread
    suspend fun getLastSearchWord(searchType: String) = withContext(Dispatchers.IO) {
        searchWordsDao.getLastSearchWord(searchType)
    }

    /**
     * Save result of movies
     * @param search
     */

    @WorkerThread
    suspend fun insertSearchResult(search: Search) = withContext(Dispatchers.IO) {
        searchResultDao.saveSearch(search)
    }


    /**
     * Get last results of search by search type (series or movie) and search text
     *
     * @param searchType
     * @param searchText
     */
    @WorkerThread
    suspend fun getSearchResult(searchType: String, searchText: String) =
        withContext(Dispatchers.IO) {
            searchResultDao.getSearchResult(searchType, searchText)
        }


}
