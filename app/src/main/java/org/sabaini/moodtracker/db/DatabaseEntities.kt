package org.sabaini.moodtracker.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Mood(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val date: Long,
    val mood: String
)

data class Statistics(
    @ColumnInfo(name = "mood") val mood: String?,
    @ColumnInfo(name = "quantity") val quantity: Int?,
    @ColumnInfo(name = "percent") val percent: Float?
)