package org.sabaini.moodtracker.data.local

import androidx.room.ColumnInfo
import org.sabaini.moodtracker.entities.Statistics

data class DatabaseStatistics(
    @ColumnInfo(name = "mood") val mood: String?,
    @ColumnInfo(name = "quantity") val quantity: Int?,
    @ColumnInfo(name = "percent") val percent: Float?
)

fun List<DatabaseStatistics>.asEntitieStatistic(): List<Statistics> {
    return map {
        Statistics(
            mood = it.mood,
            quantity = it.quantity,
            percent = it.percent
        )
    }
}