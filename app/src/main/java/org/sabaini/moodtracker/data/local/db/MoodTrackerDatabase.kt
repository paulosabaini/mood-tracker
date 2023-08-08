package org.sabaini.moodtracker.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.sabaini.moodtracker.data.local.model.DatabaseMood
import org.sabaini.moodtracker.data.local.dao.MoodTrackerDao

@Database(entities = [DatabaseMood::class], version = 1, exportSchema = false)
abstract class MoodTrackerDatabase : RoomDatabase() {

    abstract fun moodTrackerDao(): MoodTrackerDao
}