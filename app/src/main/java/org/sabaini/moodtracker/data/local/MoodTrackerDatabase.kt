package org.sabaini.moodtracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseMood::class], version = 1, exportSchema = false)
abstract class MoodTrackerDatabase : RoomDatabase() {

    abstract fun moodTrackerDao(): MoodTrackerDao
}