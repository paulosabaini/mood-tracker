package org.sabaini.moodtracker.repositories

import org.sabaini.moodtracker.entities.Mood
import org.sabaini.moodtracker.entities.Statistics
import java.time.LocalDate

interface MoodTrackerRepository {
    suspend fun getMoods(): List<Mood>?
    suspend fun insertMood(date: LocalDate, mood: CharSequence)
    suspend fun updateMood(mood: Mood)
    suspend fun getStatistics(begin: Long?, end: Long?): List<Statistics>?
}