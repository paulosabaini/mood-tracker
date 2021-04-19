package org.sabaini.moodtracker.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sabaini.moodtracker.data.local.*
import org.sabaini.moodtracker.entities.Mood
import org.sabaini.moodtracker.entities.Statistics
import org.sabaini.moodtracker.entities.asDatabaseMood
import java.lang.Exception
import java.time.LocalDate
import javax.inject.Inject

class MoodTrackerRepositoryImpl @Inject constructor(private val moodTrackerDao: MoodTrackerDao) :
    MoodTrackerRepository {

    override suspend fun getMoods(): List<Mood>? {
        var databaseMoods: List<DatabaseMood>? = null
        withContext(Dispatchers.IO) {
            try {
                databaseMoods = moodTrackerDao.getAllMoods()
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
        return databaseMoods?.asEntitieMood()
    }

    override suspend fun insertMood(date: LocalDate, mood: CharSequence) {
        withContext(Dispatchers.IO) {
            try {
                val newMood = DatabaseMood(null, date.toEpochDay(), mood as String)
                moodTrackerDao.insert(newMood)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

    override suspend fun updateMood(mood: Mood) {
        withContext(Dispatchers.IO) {
            try {
                moodTrackerDao.update(mood.asDatabaseMood())
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

    override suspend fun getStatistics(begin: Long?, end: Long?): List<Statistics>? {
        var statistics: List<DatabaseStatistics>? = null
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
        return statistics?.asEntitieStatistic()
    }
}