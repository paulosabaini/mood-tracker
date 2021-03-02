package org.sabaini.moodtracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Mood::class], version = 1)
abstract class MoodTrackerDb : RoomDatabase() {
    abstract val moodTrackerDao: MoodTrackerDao
}

private lateinit var INSTANCE: MoodTrackerDb

fun getDatabase(context: Context): MoodTrackerDb {
    synchronized(MoodTrackerDb::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MoodTrackerDb::class.java,
                "moods"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}