package org.sabaini.moodtracker.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.sabaini.moodtracker.data.local.db.MoodTrackerDatabase
import org.sabaini.moodtracker.data.local.model.DatabaseMood
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class MoodDaoTest {
    private lateinit var database: MoodTrackerDatabase
    private lateinit var moodDao: MoodDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MoodTrackerDatabase::class.java,
        ).build()
        moodDao = database.moodDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetAllMoods() = runBlocking {
        val now = LocalDate.now().toEpochDay()
        val mood1 = DatabaseMood(1, now, "emoji")
        val mood2 = DatabaseMood(2, now, "emoji")
        moodDao.insert(mood1)
        moodDao.insert(mood2)
        assertThat(moodDao.getAllMoods().size).isEqualTo(2)
    }

    @Test
    fun testInsertMood() = runBlocking {
        val now = LocalDate.now().toEpochDay()
        val mood = DatabaseMood(1, now, "emoji")
        moodDao.insert(mood)
        assertThat(moodDao.getAllMoods()[0]).isEqualTo(mood)
    }

    @Test
    fun testUpdateMood() = runBlocking {
        val now = LocalDate.now().toEpochDay()
        val mood = DatabaseMood(1, now, "emoji")
        moodDao.insert(mood)
        moodDao.update(mood.copy(mood = "mood2"))
        assertThat(moodDao.getAllMoods()[0]).isNotEqualTo(mood)
    }

    @Test
    fun testPeriodStatistics() = runBlocking {
        val today = LocalDate.now().toEpochDay()
        val yesterday = LocalDate.now().minusDays(1).toEpochDay()
        val mood1 = DatabaseMood(1, yesterday, "emoji")
        val mood2 = DatabaseMood(2, today, "emoji")
        moodDao.insert(mood1)
        moodDao.insert(mood2)
        val stats = moodDao.periodStatistics(today, today)
        assertThat(stats[0].mood).isEqualTo(mood2.mood)
        assertThat(stats[0].quantity).isEqualTo(1)
        assertThat(stats[0].percent).isEqualTo(100f)
    }

    @Test
    fun testAllTimeStatistics() = runBlocking {
        val today = LocalDate.now().toEpochDay()
        val yesterday = LocalDate.now().minusDays(1).toEpochDay()
        val mood1 = DatabaseMood(1, yesterday, "emoji")
        val mood2 = DatabaseMood(2, today, "emoji")
        moodDao.insert(mood1)
        moodDao.insert(mood2)
        val stats = moodDao.allTimeStatistics()
        assertThat(stats[0].mood).isEqualTo(mood1.mood)
        assertThat(stats[0].quantity).isEqualTo(2)
        assertThat(stats[0].percent).isEqualTo(100f)
    }
}