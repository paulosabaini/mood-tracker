package org.sabaini.moodtracker.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sabaini.moodtracker.data.local.dao.MoodDao
import org.sabaini.moodtracker.data.local.model.DatabaseMood
import org.sabaini.moodtracker.data.local.model.DatabaseStatistics
import org.sabaini.moodtracker.data.mapper.toDomainModel
import org.sabaini.moodtracker.data.mapper.toStatisticsDomainModel
import org.sabaini.moodtracker.data.mapper.toEntityModel
import org.sabaini.moodtracker.domain.model.Mood
import org.sabaini.moodtracker.domain.model.Statistics
import org.sabaini.moodtracker.domain.repository.MoodTrackerRepository
import java.lang.Exception
import java.time.LocalDate
import javax.inject.Inject

class MoodTrackerRepositoryImpl @Inject constructor(private val moodDao: MoodDao) :
    MoodTrackerRepository {

    override suspend fun getMoods(): List<Mood>? {
        var databaseMoods: List<DatabaseMood>? = null
        withContext(Dispatchers.IO) {
            try {
                databaseMoods = moodDao.getAllMoods()
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
        return databaseMoods?.toDomainModel()
    }

    override suspend fun insertMood(date: LocalDate, mood: CharSequence) {
        withContext(Dispatchers.IO) {
            try {
                val newMood = DatabaseMood(null, date.toEpochDay(), mood as String)
                moodDao.insert(newMood)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

    override suspend fun updateMood(mood: Mood) {
        withContext(Dispatchers.IO) {
            try {
                moodDao.update(mood.toEntityModel())
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
                    statistics = moodDao.allTimeStatistics()
                } else {
                    statistics = moodDao.periodStatistics(begin!!, end!!)
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
        return statistics?.toStatisticsDomainModel()
    }
}