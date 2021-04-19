package org.sabaini.moodtracker.entities

import org.sabaini.moodtracker.data.local.DatabaseMood

class Mood(
    val id: Long?,
    val date: Long,
    val mood: String
)

fun Mood.asDatabaseMood(): DatabaseMood {
    return DatabaseMood(
        id = this.id,
        date = this.date,
        mood = this.mood
    )
}