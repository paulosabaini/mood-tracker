package org.sabaini.moodtracker.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MoodTrackerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mood: Mood)

    @Query("select * from mood where date = :date")
    fun getByDate(date: Long): LiveData<Mood>

    @Update
    fun update(mood: Mood)

    @Query("select * from mood order by id")
    fun getAllMoods(): List<Mood>

    @Query("select mood, count(mood) as quantity, count(mood) * 100.0 / (select count(*) from mood where date >= :begin and date <= :end) as percent from mood where date >= :begin and date <= :end group by mood order by count(mood) desc")
    fun periodStatistics(begin: Long, end: Long): List<Statistics>

    @Query("select mood, count(mood) as quantity, count(mood) * 100.0 / (select count(*) from mood ) as percent from mood group by mood order by count(mood) desc")
    fun allTimeStatistics(): List<Statistics>
}