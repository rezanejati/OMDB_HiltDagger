package reza.nejati.omdb.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import reza.nejati.omdb.data.model.search.SearchWords
import kotlinx.coroutines.flow.Flow


/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 * Data Access Object (DAO) for [reza.nejati.omdb.data.local.OmdbiDatabase]
 */
@Dao
interface SearchWordsDao {

    @Insert
    fun saveSearchWords(search: SearchWords)

    @Query(value = "SELECT * from ${SearchWords.TABLE_NAME} ORDER BY id DESC")
    fun getAllSearchWords(): Flow<List<SearchWords>>


    @Query(value = "SELECT * FROM ${SearchWords.TABLE_NAME} WHERE SearchType = :searchType  ORDER BY ID DESC LIMIT 1")
    fun getLastSearchWord(searchType: String): SearchWords?

}
