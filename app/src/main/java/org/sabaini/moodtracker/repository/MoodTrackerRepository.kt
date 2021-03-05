package org.sabaini.moodtracker.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sabaini.moodtracker.db.Mood
import org.sabaini.moodtracker.db.MoodTrackerDb
import org.sabaini.moodtracker.db.Statistics
import java.lang.Exception
import java.time.LocalDate

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
                val newMood = Mood(null, date.toEpochDay(), mood as String)
                database.moodTrackerDao.insert(newMood)
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

    suspend fun getStatistics(begin: Long?, end: Long?): List<Statistics>? {
        var statistics: List<Statistics>? = null
        withContext(Dispatchers.IO) {
            try {
                if (begin == null && end == null) {
                    statistics = database.moodTrackerDao.allTimeStatistics()
                } else {
                    statistics = database.moodTrackerDao.periodStatistics(begin!!, end!!)
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
        return statistics
    }
}