package org.sabaini.moodtracker.domain.repository

import kotlinx.coroutines.flow.Flow
import org.sabaini.moodtracker.domain.model.Mood
import org.sabaini.moodtracker.domain.model.Statistics
import java.time.LocalDate

interface MoodTrackerRepository {
    fun getMoods(): Flow<List<Mood>?>
    suspend fun insertMood(date: LocalDate, mood: CharSequence)
    suspend fun updateMood(mood: Mood)
    fun getStatistics(begin: Long?, end: Long?): Flow<List<Statistics>?>
    suspend fun clearAllData()
}
