package org.sabaini.moodtracker.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.sql.Date

@Dao
interface MoodTrackerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mood: Mood)

    @Query("select * from mood where date = :date")
    fun getByDate(date: Long): LiveData<Mood>

    @Update()
    fun update(mood: Mood)

    @Query("select * from mood order by id")
    fun getAllMoods(): List<Mood>
}