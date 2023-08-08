package org.sabaini.moodtracker.data.local.model

import androidx.room.ColumnInfo

data class DatabaseStatistics(
    @ColumnInfo(name = "mood") val mood: String?,
    @ColumnInfo(name = "quantity") val quantity: Int?,
    @ColumnInfo(name = "percent") val percent: Float?
)
