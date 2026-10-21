package com.wongpinter.ketawa.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wongpinter.ketawa.data.local.dao.JokeDao
import com.wongpinter.ketawa.data.local.entity.JokeEntity

@Database(
    entities = [JokeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class KetawaDatabase : RoomDatabase() {
    abstract val jokeDao: JokeDao
}
