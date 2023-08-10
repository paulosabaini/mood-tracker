package org.sabaini.moodtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.sabaini.moodtracker.data.local.model.DatabaseMood
import org.sabaini.moodtracker.data.local.model.DatabaseStatistics

@Dao
interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(databaseMood: DatabaseMood)

    @Update
    fun update(databaseMood: DatabaseMood)

    @Query("select * from databasemood order by id")
    fun getAllMoods(): List<DatabaseMood>

    @Query("select mood, count(mood) as quantity, count(mood) * 100.0 / (select count(*) from databasemood where date >= :begin and date <= :end) as percent from databasemood where date >= :begin and date <= :end group by mood order by count(mood) desc")
    fun periodStatistics(begin: Long, end: Long): List<DatabaseStatistics>

    @Query("select mood, count(mood) as quantity, count(mood) * 100.0 / (select count(*) from databasemood ) as percent from databasemood group by mood order by count(mood) desc")
    fun allTimeStatistics(): List<DatabaseStatistics>
}
