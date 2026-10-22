package com.wongpinter.ketawa.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wongpinter.ketawa.data.local.entity.JokeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDao {
    @Query("SELECT * FROM jokes ORDER BY downloadedAt DESC")
    fun getAllJokes(): Flow<List<JokeEntity>>

    @Query("SELECT * FROM jokes ORDER BY downloadedAt DESC")
    suspend fun getAllJokesList(): List<JokeEntity>

    @Query("SELECT * FROM jokes WHERE id = :id LIMIT 1")
    fun getJokeById(id: String): Flow<JokeEntity?>

    @Query("SELECT * FROM jokes WHERE id = :id LIMIT 1")
    suspend fun getJokeByIdOnce(id: String): JokeEntity?

    @Query("SELECT * FROM jokes WHERE categoryId = :categoryId ORDER BY downloadedAt DESC")
    fun getJokesByCategory(categoryId: String): Flow<List<JokeEntity>>

    @Query("SELECT * FROM jokes WHERE categoryId = :categoryId ORDER BY downloadedAt DESC")
    suspend fun getJokesByCategoryList(categoryId: String): List<JokeEntity>

    @Query("SELECT * FROM jokes WHERE isBookmarked = 1 ORDER BY downloadedAt DESC")
    fun getBookmarkedJokes(): Flow<List<JokeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJoke(joke: JokeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJokes(jokes: List<JokeEntity>)

    @Query("UPDATE jokes SET isBookmarked = :bookmarked WHERE id = :id")
    suspend fun setBookmark(id: String, bookmarked: Boolean)

    @Query("SELECT COUNT(*) FROM jokes")
    fun getJokeCount(): Flow<Int>

    @Query("DELETE FROM jokes")
    suspend fun clearAll()
}
