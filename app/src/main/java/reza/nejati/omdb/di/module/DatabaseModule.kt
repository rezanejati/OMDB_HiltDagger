package reza.nejati.omdb.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import reza.nejati.omdb.data.repository.OmdbiDatabase
import reza.nejati.omdb.def.Const
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    /**
     * Init DataBase Repository
     *
     * @param repository
     * @return
     */
    @Singleton
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        OmdbiDatabase::class.java, Const.DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideWordsDatabase(db: OmdbiDatabase) = db.searchWordsDao()

    @Singleton
    @Provides
    fun provideSearch(db: OmdbiDatabase) = db.searchDao()

}
