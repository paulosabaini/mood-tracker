package org.sabaini.moodtracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Mood(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val date: Long,
    val mood: String
)