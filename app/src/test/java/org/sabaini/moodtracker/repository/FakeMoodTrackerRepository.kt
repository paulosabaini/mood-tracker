package org.sabaini.moodtracker.repository

import org.sabaini.moodtracker.db.Mood
import org.sabaini.moodtracker.db.Statistics
import java.time.LocalDate

class FakeMoodTrackerRepository() : MoodTrackerRepository {

    private val moods = mutableListOf<Mood>()
    private val statistics = mutableListOf<Statistics>()

    override suspend fun getMoods(): List<Mood>? {
        return moods
    }

    override suspend fun insertMood(date: LocalDate, mood: CharSequence) {
        val id = (moods.size + 1).toLong()
        val mood = Mood(id, date.toEpochDay(), mood.toString())
        moods.add(mood)
    }

    override suspend fun updateMood(mood: Mood) {
        val mood_ = moods.find { it.id == mood.id }
        moods.remove(mood_)
        moods.add(mood)
    }

    override suspend fun getStatistics(begin: Long?, end: Long?): List<Statistics>? {
        if (moods.size == 1) {
            val stat = Statistics(moods[0].mood, moods.size, moods.size * 100f)
            statistics.add(stat)
        }
        return statistics
    }
}