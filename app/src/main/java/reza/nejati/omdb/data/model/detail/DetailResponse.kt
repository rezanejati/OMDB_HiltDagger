package reza.nejati.omdb.data.model.detail

import com.squareup.moshi.Json

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */


/**
 * Response of detail movie
 *
 * @property ratings
 * @property title
 * @property year
 * @property rated
 * @property released
 * @property runtime
 * @property genre
 * @property director
 * @property writer
 * @property actors
 * @property plot
 * @property language
 * @property country
 * @property awards
 * @property poster
 * @property metascore
 * @property imdbRating
 * @property imdbVotes
 * @property imdbId
 * @property type
 * @property totalSeasons
 * @property response
 * @property error
 */
data class DetailResponse
    (
    @Json(name = "Ratings")
    var ratings: List<Rating>? = null,
    @Json(name = "Title")
    var title: String? = null,
    @Json(name = "Year")
    var year: String? = null,
    @Json(name = "Rated")
    var rated: String? = null,
    @Json(name = "Released")
    var released: String? = null,
    @Json(name = "Runtime")
    var runtime: String? = null,
    @Json(name = "Genre")
    var genre: String? = null,
    @Json(name = "Director")
    var director: String? = null,
    @Json(name = "Writer")
    var writer: String? = null,
    @Json(name = "Actors")
    var actors: String? = null,
    @Json(name = "Plot")
    var plot: String? = null,
    @Json(name = "Language")
    var language: String? = null,
    @Json(name = "Country")
    var country: String? = null,
    @Json(name = "Awards")
    var awards: String? = null,
    @Json(name = "Poster")
    var poster: String? = null,
    @Json(name = "Metascore")
    var metascore: String? = null,
    @Json(name = "imdbRating")
    var imdbRating: String? = null,
    @Json(name = "imdbVotes")
    var imdbVotes: String? = null,
    @Json(name = "imdbID")
    var imdbId: String? = null,
    @Json(name = "Type")
    var type: String? = null,
    @Json(name = "totalSeasons")
    var totalSeasons: String? = "",
    @Json(name = "Response")
    var response: String? = null,
    @Json(name = "Error")
    var error: String? = null
){
    data class Rating(
        @Json(name = "Source")
        var source: String? = null,
        @Json(name = "Value")
        var value: String? = null,
    )
}