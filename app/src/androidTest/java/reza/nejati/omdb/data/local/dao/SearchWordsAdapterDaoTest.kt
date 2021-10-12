package reza.nejati.omdb.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import reza.nejati.omdb.data.repository.OmdbiDatabase
import reza.nejati.omdb.data.model.response.Search
import reza.nejati.omdb.data.model.search.SearchWords
import reza.nejati.omdb.def.Const
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableContainingInOrder
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

@RunWith(AndroidJUnit4::class)
class SearchWordsAdapterDaoTest {

    private lateinit var mDatabase: OmdbiDatabase

    @Before
    fun init() {
        mDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            OmdbiDatabase::class.java
        ).build()
        mDatabase.clearAllTables()

    }

    @Test
    @Throws(InterruptedException::class)
    fun test_save_search_words() = runBlocking {
        val movies = listOf(
            SearchWords(id=1, searchedWords = "Avatar",searchType = Const.SERIES),
            SearchWords(id=2, searchedWords ="King",searchType=Const.MOVIE)
        )

        movies.map { mDatabase.searchWordsDao().saveSearchWords(it) }


        val dbSearchWords = mDatabase.searchWordsDao().getAllSearchWords().first().reversed()

        assertThat(dbSearchWords, equalTo(movies))
    }

    @Test
    @Throws(InterruptedException::class)
    fun get_search_result_by_type_title() = runBlocking {

        val movies = listOf(
            Search(id=1,title="Beta", year="2016", omdbID="tt4244162", type="movie", poster="https://m.media-amazon.com/images/M/MV5BODdlMjU0MDYtMWQ1NC00YjFjLTgxMDQtNDYxNTg2ZjJjZDFiXkEyXkFqcGdeQXVyMTU2NTcxNDg@._V1_SX300.jpg", background=1, searchText="Beta"),
            Search(id=2, title="Avatar", year="2018", omdbID="tt12300", type="series", poster="https://m.media-amazon.com/images/M/wer5BOwerewr0MDYtMWQ1NC00YjFjLTgxMDQtNDYxNTg2ZjJjsdfsdfsXkFqcGdeQXyMTU2NTcxNDg@._V1_SX300.jpg", background=50, searchText="Avatar")
        )
        movies.map {
            mDatabase.searchDao().saveSearch(it)
        }

        var movie = mDatabase.searchDao().getSearchResult(Const.MOVIE,"Beta").first()
        assertThat(movie,  IsIterableContainingInOrder.contains(movies[0]))

        movie =  mDatabase.searchDao().getSearchResult(Const.SERIES,"Avatar").first()
        assertThat(movie,  IsIterableContainingInOrder.contains(movies[1]))
    }

    @After
    fun cleanup() {
        mDatabase.close()
    }


}
