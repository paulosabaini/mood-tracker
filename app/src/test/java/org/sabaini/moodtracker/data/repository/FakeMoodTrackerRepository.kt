package org.sabaini.moodtracker.data.repository

import org.sabaini.moodtracker.domain.model.Mood
import org.sabaini.moodtracker.domain.model.Statistics
import org.sabaini.moodtracker.domain.repository.MoodTrackerRepository
import java.time.LocalDate

class FakeMoodTrackerRepository() : MoodTrackerRepository {

    private val moods = mutableListOf<Mood>()
    private val statistics = mutableListOf<Statistics>()

    override suspend fun getMoods(): List<Mood> {
        return moods
    }

    override suspend fun insertMood(date: LocalDate, mood: CharSequence) {
        val id = (moods.size + 1).toLong()
        val newMood = Mood(id, date.toEpochDay(), mood.toString())
        moods.add(newMood)
    }

    override suspend fun updateMood(mood: Mood) {
        val newMood = moods.find { it.id == mood.id }
        moods.remove(newMood)
        moods.add(mood)
    }

    override suspend fun getStatistics(begin: Long?, end: Long?): List<Statistics> {
        moods.forEach { mood ->
            val stat = statistics.find { statistic -> mood.mood == statistic.mood }
            if (stat == null) {
                val newStat = Statistics(mood.mood, 1, 100f)
                statistics.add(newStat)
            } else {
                val upStat = Statistics(
                    mood = stat.mood,
                    quantity = stat.quantity!!.plus(1),
                    percent = stat.percent!! / 2,
                )
                statistics.remove(stat)
                statistics.add(upStat)
            }
        }
        return statistics
    }
}
