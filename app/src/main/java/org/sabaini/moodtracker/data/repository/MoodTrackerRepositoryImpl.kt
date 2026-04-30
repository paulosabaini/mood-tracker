package org.sabaini.moodtracker.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.sabaini.moodtracker.data.local.dao.MoodDao
import org.sabaini.moodtracker.data.local.model.MoodEntity
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

    override fun getMoods(): Flow<List<Mood>?> {
        return moodDao.getAllMoods()
            .map { it.toDomainModel() }
            .catch { e ->
                Log.d("Exception", e.toString())
                emit(emptyList())
            }
    }

    override suspend fun insertMood(date: LocalDate, mood: CharSequence) {
        withContext(Dispatchers.IO) {
            try {
                val newMood = MoodEntity(null, date.toEpochDay(), mood as String)
                moodDao.saveMood(newMood)
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

    override suspend fun updateMood(mood: Mood) {
        withContext(Dispatchers.IO) {
            try {
                moodDao.saveMood(mood.toEntityModel())
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

    override fun getStatistics(begin: Long?, end: Long?): Flow<List<Statistics>?> {
        val statisticsFlow = if (begin == null && end == null) {
            moodDao.allTimeStatistics()
        } else {
            moodDao.periodStatistics(begin!!, end!!)
        }

        return statisticsFlow
            .map { it.toStatisticsDomainModel() }
            .catch { e ->
                Log.d("Exception", e.toString())
                emit(emptyList())
            }
    }

    override suspend fun clearAllData() {
        withContext(Dispatchers.IO) {
            try {
                moodDao.deleteAllMoods()
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }
}