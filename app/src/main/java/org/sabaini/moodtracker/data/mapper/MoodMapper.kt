package org.sabaini.moodtracker.data.mapper

import org.sabaini.moodtracker.data.local.model.MoodEntity
import org.sabaini.moodtracker.data.local.model.DatabaseStatistics
import org.sabaini.moodtracker.domain.model.Mood
import org.sabaini.moodtracker.domain.model.Statistics

fun List<MoodEntity>.toDomainModel(): List<Mood> {
    return map {
        Mood(
            id = it.id,
            date = it.date,
            mood = it.mood,
        )
    }
}

fun List<DatabaseStatistics>.toStatisticsDomainModel(): List<Statistics> {
    return map {
        Statistics(
            mood = it.mood,
            quantity = it.quantity,
            percent = it.percent,
        )
    }
}

fun Mood.toEntityModel(): MoodEntity {
    return MoodEntity(
        id = this.id,
        date = this.date,
        mood = this.mood,
    )
}
