package org.sabaini.moodtracker.repositories

import org.sabaini.moodtracker.data.local.DatabaseMood
import org.sabaini.moodtracker.data.local.DatabaseStatistics
import org.sabaini.moodtracker.entities.Mood
import org.sabaini.moodtracker.entities.Statistics
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
        moods.forEach { mood ->
            val stat = statistics.find { statistic -> mood.mood == statistic.mood }
            if (stat == null) {
                val stat_ = Statistics(mood.mood, 1, 100f)
                statistics.add(stat_)
            } else {
                val upStat = Statistics(
                    mood = stat.mood,
                    quantity = stat.quantity!!.plus(1),
                    percent = stat.percent!! / 2
                )
                statistics.remove(stat)
                statistics.add(upStat)
            }
        }
        return statistics
    }
}