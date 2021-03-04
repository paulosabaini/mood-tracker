package org.sabaini.moodtracker.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sabaini.moodtracker.db.Mood
import org.sabaini.moodtracker.db.MoodTrackerDb
import java.lang.Exception
import java.sql.Date
import java.time.LocalDate
import java.time.temporal.Temporal
import java.time.temporal.TemporalField

class MoodTrackerRepository(private val database: MoodTrackerDb) {

    suspend fun getMoods(): List<Mood>? {
        var moods: List<Mood>? = null
        withContext(Dispatchers.IO) {
            try {
                moods = database.moodTrackerDao.getAllMoods()
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
        return moods
    }

    suspend fun insertMood(date: LocalDate, mood: CharSequence) {
        withContext(Dispatchers.IO) {
            try {
                val mood = Mood(null, date.toEpochDay(), mood as String)
                database.moodTrackerDao.insert(mood)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

    suspend fun updateMood(mood: Mood) {
        withContext(Dispatchers.IO) {
            try {
                database.moodTrackerDao.update(mood)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }
}