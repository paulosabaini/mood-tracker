package org.sabaini.moodtracker.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseMood(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val date: Long,
    val mood: String
)
