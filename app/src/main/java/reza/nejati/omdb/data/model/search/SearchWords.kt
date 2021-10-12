package reza.nejati.omdb.data.model.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import reza.nejati.omdb.data.model.search.SearchWords.Companion.TABLE_NAME

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 *  * Entity for the search words
 *
 * @property id
 * @property searchedWords
 * @property searchType
 */

@Entity(tableName = TABLE_NAME)
data class SearchWords(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "SearchWord")
    var searchedWords: String = "",

    @ColumnInfo(name = "SearchType")
    var searchType: String = ""

) {
    companion object {
        const val TABLE_NAME = "search_words"
    }
}
