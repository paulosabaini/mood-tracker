package org.sabaini.moodtracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.sabaini.moodtracker.entities.Mood

@Entity
data class DatabaseMood(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val date: Long,
    val mood: String
)

fun List<DatabaseMood>.asEntitieMood(): List<Mood> {
    return map {
        Mood(
            id = it.id,
            date = it.date,
            mood = it.mood
        )
    }
}