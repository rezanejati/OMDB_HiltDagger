package reza.nejati.omdb.data.remote

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

sealed class Resource<T> {
    class Success<T>(val data: T?) : Resource<T>()
    class Failed<T>(val message: String) : Resource<T>()
}
