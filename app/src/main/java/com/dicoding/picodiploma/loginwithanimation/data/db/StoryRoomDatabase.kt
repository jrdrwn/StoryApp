package com.dicoding.picodiploma.loginwithanimation.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Story::class], version = 1)
abstract class StoryRoomDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao

    companion object {
        @Volatile
        private var INSTANCE: StoryRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryRoomDatabase {
            if (INSTANCE == null) {
                synchronized(StoryRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        StoryRoomDatabase::class.java, "story_database"
                    )
                        .build()
                }
            }
            return INSTANCE as StoryRoomDatabase
        }
    }


}