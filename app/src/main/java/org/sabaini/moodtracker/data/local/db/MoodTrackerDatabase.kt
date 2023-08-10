package org.sabaini.moodtracker.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.sabaini.moodtracker.data.local.dao.MoodDao
import org.sabaini.moodtracker.data.local.model.DatabaseMood

private const val DATABASE_VERSION = 1

@Database(entities = [DatabaseMood::class], version = DATABASE_VERSION)
abstract class MoodTrackerDatabase : RoomDatabase() {

    abstract fun moodDao(): MoodDao
}
