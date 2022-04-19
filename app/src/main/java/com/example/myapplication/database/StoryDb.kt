package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.api.ListStoryItem
import com.example.myapplication.data.RemoteKeys
import com.example.myapplication.data.RemoteKeysDao
import com.example.myapplication.repository.StoryDao

@Database(
    entities = [ListStoryItem::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDb : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDb? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDb {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDb::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}