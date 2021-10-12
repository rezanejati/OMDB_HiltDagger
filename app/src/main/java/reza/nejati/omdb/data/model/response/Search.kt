package reza.nejati.omdb.data.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import reza.nejati.omdb.data.model.response.Search.Companion.TABLE_NAME

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 * Entity for the result of movie search
 *
 * @property id
 * @property title
 * @property year
 * @property omdbID
 * @property type
 * @property poster
 * @property background
 * @property searchText
 */
@Entity(tableName = TABLE_NAME)
data class Search (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @Json(name = "Title")
    var title: String? = null,

    @Json(name = "Year")
    var year: String? = null,

    @Json(name = "imdbID")
    var omdbID: String? = null,

    @Json(name = "Type")
    var type: String? = null,

    @Json(name = "Poster")
    var poster: String? = null,

    @Json(name = "Background")
    var background: Int? = null,

    @Json(name = "SearchText")
    var searchText: String? = null

)
{
    companion object {
        const val TABLE_NAME = "movies"
    }
}
