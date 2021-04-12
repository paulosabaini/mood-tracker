package org.sabaini.moodtracker.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Mood::class], version = 1, exportSchema = false)
abstract class MoodTrackerDb : RoomDatabase() {
    abstract fun moodTrackerDao(): MoodTrackerDao
}