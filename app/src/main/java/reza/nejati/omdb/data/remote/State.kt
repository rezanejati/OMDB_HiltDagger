package reza.nejati.omdb.data.remote

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 * State Management for UI & Data.
 */
sealed class State<T> {
    class Loading<T> : State<T>()

    data class Success<T>(val data: T) : State<T>()

    data class Error<T>(val message: String) : State<T>()

    fun isLoading(): Boolean = this is Loading

    fun isSuccessful(): Boolean = this is Success

    fun isFailed(): Boolean = this is Error

    companion object {

        fun <T> loading() = Loading<T>()

        private fun <T> success(data: T) =
            Success(data)

        private fun <T> error(message: String) =
            Error<T>(message)

        fun <T> fromResource(resource: Resource<T>): State<T?> = when (resource) {
            is Resource.Success -> success(resource.data)
            is Resource.Failed -> error(resource.message)
        }
    }
}
