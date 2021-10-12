

package reza.nejati.omdb.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import reza.nejati.omdb.def.Const
import reza.nejati.omdb.data.local.dao.SearchWordsDao
import reza.nejati.omdb.data.local.dao.SearchResultDao
import reza.nejati.omdb.data.model.search.SearchWords
import reza.nejati.omdb.data.model.response.Search

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 * TODO
 * entities
 * SearchWords => It provides DAO for the search words
 * *Search => It provides DAO for the movie result
 */
@Database(
    entities = [SearchWords::class, Search::class],
    version = Const.DATABASE_VERSION
)

abstract class OmdbiDatabase : RoomDatabase() {
    /**
     * DAO for the search words
     * @return
     */
    abstract fun searchWordsDao() : SearchWordsDao


    /**
     * It provides DAO for the movie result
     * @return
     */
    abstract fun searchDao() : SearchResultDao

}
