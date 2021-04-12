package org.sabaini.moodtracker.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sabaini.moodtracker.db.Mood
import org.sabaini.moodtracker.db.MoodTrackerDao
import org.sabaini.moodtracker.db.Statistics
import java.lang.Exception
import java.time.LocalDate
import javax.inject.Inject

class MoodTrackerRepository @Inject constructor(private val moodTrackerDao: MoodTrackerDao) {

    suspend fun getMoods(): List<Mood>? {
        var moods: List<Mood>? = null
        withContext(Dispatchers.IO) {
            try {
                moods = moodTrackerDao.getAllMoods()
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
                moodTrackerDao.insert(newMood)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

    suspend fun updateMood(mood: Mood) {
        withContext(Dispatchers.IO) {
            try {
                moodTrackerDao.update(mood)
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
                    statistics = moodTrackerDao.allTimeStatistics()
                } else {
                    statistics = moodTrackerDao.periodStatistics(begin!!, end!!)
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
        return statistics
    }
}