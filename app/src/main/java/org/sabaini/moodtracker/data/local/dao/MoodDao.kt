package org.sabaini.moodtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.sabaini.moodtracker.data.local.model.MoodEntity
import org.sabaini.moodtracker.data.local.model.DatabaseStatistics

@Dao
interface MoodDao {
    @Upsert
    fun saveMood(moodEntity: MoodEntity)

    @Query("select * from mood order by id")
    fun getAllMoods(): Flow<List<MoodEntity>>

    @Query("select mood, count(mood) as quantity, count(mood) * 100.0 / (select count(*) from mood where date >= :begin and date <= :end) as percent from mood where date >= :begin and date <= :end group by mood order by count(mood) desc")
    fun periodStatistics(begin: Long, end: Long): Flow<List<DatabaseStatistics>>

    @Query("select mood, count(mood) as quantity, count(mood) * 100.0 / (select count(*) from mood ) as percent from mood group by mood order by count(mood) desc")
    fun allTimeStatistics(): Flow<List<DatabaseStatistics>>

    @Query("DELETE FROM mood")
    fun deleteAllMoods()
}
