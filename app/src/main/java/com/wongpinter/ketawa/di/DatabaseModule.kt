package com.wongpinter.ketawa.di

import android.content.Context
import androidx.room.Room
import com.wongpinter.ketawa.data.local.KetawaDatabase
import com.wongpinter.ketawa.data.local.dao.JokeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KetawaDatabase {
        return Room.databaseBuilder(
            context,
            KetawaDatabase::class.java,
            "ketawa_offline.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideJokeDao(database: KetawaDatabase): JokeDao {
        return database.jokeDao
    }
}
