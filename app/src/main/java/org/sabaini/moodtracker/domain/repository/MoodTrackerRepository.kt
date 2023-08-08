package org.sabaini.moodtracker.domain.repository

import org.sabaini.moodtracker.domain.model.Mood
import org.sabaini.moodtracker.domain.model.Statistics
import java.time.LocalDate

interface MoodTrackerRepository {
    suspend fun getMoods(): List<Mood>?
    suspend fun insertMood(date: LocalDate, mood: CharSequence)
    suspend fun updateMood(mood: Mood)
    suspend fun getStatistics(begin: Long?, end: Long?): List<Statistics>?
}